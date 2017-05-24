package domain.rsa.projectcustomrsa;

import domain.rsa.IRSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;
import domain.rsa.projectcustomrsa.utils.PrivateKey;
import domain.rsa.projectcustomrsa.utils.PublicKey;
import foundation.math.BigRational;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by gioacchino on 16/05/17.
 */
public class CustomRSAChiper implements IRSAChiper {

    public final static short FIRST_INTEGER_BIGGER_THAN_SECOND = 1;
    public final static short FIRST_INTEGER_EQUAL_TO_SECOND = 0;


    private static CustomRSAChiper ourInstance = new CustomRSAChiper();

    public static CustomRSAChiper getInstance() {
        return ourInstance;
    }

    private CustomRSAChiper() {
    }

    /**
     * Generazione delle chiavi RSA
     *
     * @param factorlength Lunghezza in bit dei fattori p e q
     * @return Bundle delle chiavi pubbliche e private
     */
    public KeyBundle getVulnerableKeys(int factorlength) {


        PrivateKey privateKey = new PrivateKey();
        PublicKey publicKey = new PublicKey();
        KeyBundle kb = new KeyBundle();

        BigInteger p;
        BigInteger q;

        Long seed = new Date().getTime(); // seme pseudo-random
        Random rnd = new Random(seed); // generatore pseudo-random

        BigInteger nproduct; // n = p * q
        BigInteger phi;

        BigInteger e; // chiave pubblica
        BigInteger d; // chiave privata


        do {

            // nota che le lunghezza in bit di p può essere al più bitlenght(q) + 1
            // poiché deve valere q < p < 2q
            p = BigInteger.probablePrime(factorlength, rnd);
            q = BigInteger.probablePrime(factorlength, rnd);

        } while (!(p.compareTo(q) == CustomRSAChiper.FIRST_INTEGER_BIGGER_THAN_SECOND && // p > q
                q.multiply(BigInteger.valueOf(2)).compareTo(p) == CustomRSAChiper.FIRST_INTEGER_BIGGER_THAN_SECOND)); // 2q > p

        System.out.println("p: " + p.toString(10));
        System.out.println("q: " + q.toString(10));
        System.out.println("2q: " + q.multiply(BigInteger.valueOf(2)).toString(10));
        // n = pq
        nproduct = p.multiply(q);

        // phi = (p-1)(q-1)
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        System.out.println("Phi: " + phi.toString(10));



        // upper bound dell'esponente privato che rispetti il teorema di Wiener
        // d < 1/3 * (n)^{1/4}
        int maxnumbit = this.getSquareRoot(this.getSquareRoot(nproduct)).divide(BigInteger.valueOf(3)).bitLength() - 1;

        do {

            d = new BigInteger(maxnumbit, rnd);

        } while (d.compareTo(phi) == CustomRSAChiper.FIRST_INTEGER_BIGGER_THAN_SECOND || !d.gcd(phi).equals(BigInteger.ONE));


        // de = 1 mod phi
        e = d.modInverse(phi);


        // creazione bundle finali
        privateKey.setQ(q);
        privateKey.setD(d);
        privateKey.setP(p);
        privateKey.setN(nproduct);

        publicKey.setN(nproduct);
        publicKey.setE(e);

        // bundle di chiavi
        kb.setPrivatekey(privateKey);
        kb.setPublickey(publicKey);

        return kb;
    }

    @Override
    public BigInteger encryptBlock(BigInteger plainmessage, PublicKey key) {

        return plainmessage.modPow(key.getE(), key.getN());
    }

    @Override
    public BigInteger decryptBlock(BigInteger chipertex, PrivateKey key) {

        return chipertex.modPow(key.getD(), key.getN());
    }

    @Override
    public KeyBundle attackRSA(PublicKey publicKey) {

        boolean foundkeys = false;
        BigRational tmpconvergent = null;

        BigRational candidatephi = null;
        BigInteger candidatenumerator = null;

        // ottinei l'espansione in continued fraction di e/N
        List<BigInteger> convergents = (new BigRational(publicKey.getE(), publicKey.getN())).getListIntegersContinuedFraction();
        System.out.println(convergents.toString());

        //System.out.println(BigRational.recomposeConvergent(convergents).toString(10));


        for (int i = 1; !foundkeys && i < convergents.size(); i++) {

            tmpconvergent = BigRational.recomposeConvergent(convergents.subList(0, i+1));


            candidatenumerator = publicKey.getE().multiply(tmpconvergent.getDenominator()).subtract(BigInteger.ONE);

            candidatephi = new BigRational(candidatenumerator, tmpconvergent.getNumerator());

            if (candidatephi.getDenominator().equals(BigInteger.ONE)) { // controllo interezza numeratore


                System.out.println("Ne ho trovato uno!");

                this.solveFactorizationEquation(candidatephi, publicKey.getN());


            }





        }


        return null;
    }

    private BigInteger getSquareRoot(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = n.shiftRight(5).add(BigInteger.valueOf(8));
        while (b.compareTo(a) >= 0) {
            BigInteger mid = a.add(b).shiftRight(1);
            if (mid.multiply(mid).compareTo(n) > 0) {
                b = mid.subtract(BigInteger.ONE);
            } else {
                a = mid.add(BigInteger.ONE);
            }
        }
        return a.subtract(BigInteger.ONE);
    }

    private BigInteger solveFactorizationEquation(BigRational candidatephi, BigInteger nproduct) {

        BigInteger four = BigInteger.valueOf(4);
        BigInteger two = BigInteger.valueOf(2);

        // results
        BigRational x1 = null;
        BigRational x2 = null;

        // x^2 - (n+1-C)x + n

        BigInteger xcoefficient = nproduct.add(BigInteger.ONE).subtract(candidatephi.getNumerator());

        // -b + sqrt(b^2 -4ac)
        BigInteger tmpnumerator  = xcoefficient.add(this.getSquareRoot(xcoefficient.pow(2).subtract(nproduct.multiply(four))));
        x1 = new BigRational(tmpnumerator, two);



        if (x1.getDenominator().equals(BigInteger.ONE)) {

            tmpnumerator  = xcoefficient.subtract(this.getSquareRoot(xcoefficient.pow(2).subtract(nproduct.multiply(four))));
            x2 = new BigRational(tmpnumerator, two);
            System.out.println(x1.toString(10));
            System.out.println(x2.toString(10));

        }

        return x1.getNumerator();


    }


}
