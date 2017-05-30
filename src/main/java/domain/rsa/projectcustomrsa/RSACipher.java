package domain.rsa.projectcustomrsa;

import domain.rsa.IRSACipher;
import domain.rsa.projectcustomrsa.utils.KeyBundle;
import domain.rsa.projectcustomrsa.utils.PrivateKey;
import domain.rsa.projectcustomrsa.utils.PublicKey;
import foundation.math.BigRational;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by gioacchino on 16/05/17.
 */
public class RSACipher implements IRSACipher {

    private final static short FIRST_INTEGER_BIGGER_THAN_SECOND = 1;

    // start implementazone singleton
    private static RSACipher ourInstance = new RSACipher();
    public static RSACipher getInstance() {
        return ourInstance;
    }
    private RSACipher() {}
    // end implementazone singleton

    /**
     * Generazione delle chiavi RSA
     *
     * @param factorlength Lunghezza in bit dei fattori p e q
     * @return Bundle delle chiavi pubbliche e private
     */
    public KeyBundle getWienerAttackableKeys(int factorlength) {


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

        } while (!(p.compareTo(q) == RSACipher.FIRST_INTEGER_BIGGER_THAN_SECOND && // p > q
                q.multiply(BigInteger.valueOf(2)).compareTo(p) == RSACipher.FIRST_INTEGER_BIGGER_THAN_SECOND)); // 2q > p

        // n = pq
        nproduct = p.multiply(q);

        // phi = (p-1)(q-1)
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // upper bound dell'esponente privato che rispetti il teorema di Wiener
        // d < 1/3 * (n)^{1/4}
        int maxnumbit = this.getSquareRoot(this.getSquareRoot(nproduct)).divide(BigInteger.valueOf(3)).bitLength() - 1;

        do {

            d = new BigInteger(maxnumbit, rnd);

        } while (d.compareTo(phi) == RSACipher.FIRST_INTEGER_BIGGER_THAN_SECOND || !d.gcd(phi).equals(BigInteger.ONE));


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
        kb.setPrivateKey(privateKey);
        kb.setPublicKey(publicKey);

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
    public KeyBundle attackWiener(PublicKey publicKey) {

        KeyBundle returnKeys = null;

        boolean hasfound = false;

        PrivateKey privatefoundkeys = null;
        BigRational tmpconvergent = null;

        BigRational candidatephi = null;
        BigInteger candidatenumerator = null;

        // ottinei l'espansione in continued fraction di e/N
        List<BigInteger> convergents = (new BigRational(publicKey.getE(), publicKey.getN())).getListIntegersContinuedFraction();


        for (int i = 1; !hasfound && i < convergents.size(); i++) {

            tmpconvergent = BigRational.recomposeConvergent(convergents.subList(0, i+1));


            candidatenumerator = publicKey.getE().multiply(tmpconvergent.getDenominator()).subtract(BigInteger.ONE);

            candidatephi = new BigRational(candidatenumerator, tmpconvergent.getNumerator());

            if (candidatephi.getDenominator().equals(BigInteger.ONE)) { // controllo interezza numeratore

                privatefoundkeys = this.solveFactorizationEquation(candidatephi, publicKey.getN());

                if (privatefoundkeys != null)
                    hasfound = true;

            }

        }

        if (privatefoundkeys != null) { // se abbiamo trovato p e q possiamo calcolare d

            privatefoundkeys.setD(publicKey.getE().modInverse(candidatephi.getNumerator()));

            returnKeys = new KeyBundle();
            returnKeys.setPrivateKey(privatefoundkeys);
            returnKeys.setPublicKey(publicKey);

        }


        return returnKeys;
    }

    @Override
    public boolean isWienerAttackable(PrivateKey privateKey) {

        boolean attackable = false;
        int compared;

        // d < 1/3 * {n}^{1/4}
        compared = privateKey.getD().compareTo(this.getSquareRoot(this.getSquareRoot(privateKey.getN()).divide(BigInteger.valueOf(3))));

        if (compared == -1) {
            attackable = true;
        }
        return attackable;
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

    private PrivateKey solveFactorizationEquation(BigRational candidatephi, BigInteger nproduct) {

        PrivateKey privateKey = null;

        BigInteger four = BigInteger.valueOf(4);
        BigInteger two = BigInteger.valueOf(2);

        BigInteger sqrtDelta;
        BigInteger delta;
        BigInteger tmpnumerator;

        // results
        BigRational x1 = null;
        BigRational x2 = null;


        // x^2 - (n+1-C)x + n

        // (n+1-C)
        BigInteger xcoefficient = nproduct.add(BigInteger.ONE).subtract(candidatephi.getNumerator());

        // sqrt(b^2 -4ac)
        // delta = b^2 -4ac
        delta = xcoefficient.pow(2).subtract(nproduct.multiply(four));
        sqrtDelta = this.getSquareRoot(delta);

        if (sqrtDelta.pow(2).equals(delta)) { // ciò è possibile <--> delta è un quadrato perfetto

            // -b + sqrt(b^2 -4ac)
            tmpnumerator  = xcoefficient.add(sqrtDelta);
            x1 = new BigRational(tmpnumerator, two);


            if (x1.getDenominator().equals(BigInteger.ONE)) {

                tmpnumerator  = xcoefficient.subtract(sqrtDelta);
                x2 = new BigRational(tmpnumerator, two);

                if (x2.getDenominator().equals(BigInteger.ONE)) {// se sono entrambi interi
                    privateKey = new PrivateKey();

                    // restituisci tutta la chiave privata
                    privateKey.setP(x1.getNumerator()); // p
                    privateKey.setQ(x2.getNumerator()); // q
                    privateKey.setN(nproduct);



                }

            }

        }

        return privateKey;


    }


}
