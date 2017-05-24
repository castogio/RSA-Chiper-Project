package domain.rsa.projectcustomrsa;

import domain.rsa.IRSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;
import domain.rsa.projectcustomrsa.utils.PrivateKey;
import domain.rsa.projectcustomrsa.utils.PublicKey;

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

            // nota che p e q
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



        // upper bound dell'esponente privato
        // d < 1/3 * (n)^{1/4}
        int maxnumbit = this.getSquareRoot(this.getSquareRoot(nproduct)).divide(BigInteger.valueOf(3)).bitLength() - 1;

        do {

            d = new BigInteger(maxnumbit, rnd);

        } while (d.compareTo(phi) == CustomRSAChiper.FIRST_INTEGER_BIGGER_THAN_SECOND || !d.gcd(phi).equals(BigInteger.ONE));


        // de = 1 mod phi
        e = d.modInverse(phi);







        /*
        do {

            e = new BigInteger(phi.bitLength() , rnd).add(BigInteger.ONE);

        } while (e.compareTo(phi) == CustomRSAChiper.FIRST_INTEGER_BIGGER_THAN_SECOND || !e.gcd(phi).equals(BigInteger.ONE));
        */

        /*
        e = BigInteger.ONE;

        do {

            e = e.add(BigInteger.ONE);

        } while (e.compareTo(phi) == CustomRSAChiper.FIRST_INTEGER_BIGGER_THAN_SECOND || !e.gcd(phi).equals(BigInteger.ONE));
        */



        // de = 1 mod phi
        //d = e.modInverse(phi);




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


}
