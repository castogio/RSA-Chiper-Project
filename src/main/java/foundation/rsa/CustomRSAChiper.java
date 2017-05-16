package foundation.rsa;

import foundation.rsa.rsautils.KeyBundle;

import java.math.BigInteger;
import java.util.Date;
import java.util.Random;

/**
 * Created by gioacchino on 16/05/17.
 */
public class CustomRSAChiper implements IRSAChiper {

    public KeyBundle getKeys(int factorlength) {

        BigInteger p;
        BigInteger q;

        Long seed = new Date().getTime();
        Random rnd = new Random();

        BigInteger nproduct;
        BigInteger phi;

        BigInteger publickey; // chiave pubblica
        BigInteger privatekey; // chiave privata


        do {

            p = BigInteger.probablePrime(factorlength, rnd);
            q = BigInteger.probablePrime(factorlength, rnd);

        } while (p.equals(q));

        nproduct = p.multiply(q);
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));


        System.out.println(q);
        System.out.println(p);
        System.out.println(phi);

        final int BIGGER_THAN_PHI = 1;
        final int LESSER_THAN_ONE = -1;


        // genera la chiave pubblica
        do {

            publickey = new BigInteger(phi.bitLength(), rnd);


        } while (publickey.compareTo(phi) == BIGGER_THAN_PHI || publickey.compareTo(BigInteger.ONE) == LESSER_THAN_ONE);

        // genera la chiave privata

        System.out.println("chiave pubblica" + publickey);


        boolean foundinverse = false;

        privatekey = BigInteger.ONE;

        do {

            //System.out.println("Chiave privata " + privatekey);
            //System.out.println("modulo " + privatekey.multiply(publickey).mod(phi));

            if (privatekey.multiply(publickey).mod(phi).equals(BigInteger.ONE)) {
                //System.out.println(privatekey);
                foundinverse = true;
            }
            else {
                privatekey = privatekey.add(BigInteger.ONE);
            }


        }
        while (!foundinverse);

        System.out.println(privatekey);

        /*
        do {

            privatekey = new BigInteger(phi.bitLength(), rnd);

            modulus = privatekey.multiply(publickey).mod(phi);

            System.out.println("Tentativo chiave privata");
            System.out.println(modulus);
            System.out.println(privatekey);

        } while (!(modulus.equals(BigInteger.ONE)));
        */







        return null;
    }
}
