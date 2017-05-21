package foundation.rsa.rsautils;

import foundation.rsa.IRSAChiper;
import foundation.rsa.rsautils.KeyBundle;
import foundation.rsa.rsautils.PrivateKey;
import foundation.rsa.rsautils.PublicKey;

import java.math.BigInteger;
import java.util.*;

/**
 * Created by gioacchino on 16/05/17.
 */
public class CustomRSAChiper implements IRSAChiper {

    public final static short FIRST_INTEGER_BIGGER_THAN_SECOND = 1;
    public final static short FIRST_INTEGER_EQUAL_TO_SECOND = 0;

    /**
     * Generazione delle chiavi RSA
     *
     * @param pfactorlength Lunghezza in bit del fattore p
     * @param qfactorlength Lunghezza in bit del fattore q
     * @return Bundle delle chiavi pubbliche e private
     */
    public KeyBundle getKeys(int pfactorlength, int qfactorlength) {


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

            p = BigInteger.probablePrime(pfactorlength, rnd);
            q = BigInteger.probablePrime(qfactorlength, rnd);

        } while (p.equals(q));

        // privateKey.setP(p);

        //System.out.println(p.bitLength());
        //System.out.println(q.bitLength());


        nproduct = p.multiply(q);
        //System.out.println(nproduct.bitLength());
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        //System.out.println(phi.bitLength());

        /*
        System.out.println("q: " + q);
        System.out.println("p: " + p);
        System.out.println("n: " + nproduct);
        System.out.println("phi: " + phi);
        */

        // genera la chiave pubblica
        do {

            e = new BigInteger(phi.bitLength(), rnd).add(BigInteger.ONE);

        } while (e.compareTo(phi) == CustomRSAChiper.FIRST_INTEGER_BIGGER_THAN_SECOND || !e.gcd(phi).equals(BigInteger.ONE));


        //System.out.println("chiave pubblica: " + e);

        // genera la chiave privata d*e = 1 (mod phi)
        d = e.modInverse(phi);

        //System.out.println("chiave privata: " + d);


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

    public BigInteger encryptBlock(BigInteger plainmessage, PublicKey key) {

        return plainmessage.modPow(key.getE(), key.getN());
    }

    public BigInteger dencryptBlock(BigInteger chipertex, PrivateKey key) {

        return chipertex.modPow(key.getD(), key.getN());
    }

    public String encryptMessage(String plaintext, PublicKey key) {

        int blocksize = 4;
        byte[] msg_bytes = plaintext.getBytes();

        List<String> tmp = new ArrayList<>();

        //short tmpindex;
        BigInteger tmp_integer;

        for (short i = 0; i < msg_bytes.length; i += (blocksize+1)) {

            tmp_integer = new BigInteger(Arrays.copyOfRange(msg_bytes, i, i+blocksize));

            tmp.add(this.encryptBlock(tmp_integer, key).toString(2));

        }



        return tmp.toString();
    }

    public String decryptMessage(String chipertext, PrivateKey key) {



        return null;
    }

}