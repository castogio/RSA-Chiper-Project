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

        // n = pq
        nproduct = p.multiply(q);

        // phi = (p-1)(q-1)
        phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));


        // genera la chiave pubblica
        do {

            e = new BigInteger(phi.bitLength(), rnd).add(BigInteger.ONE);

        } while (e.compareTo(phi) == CustomRSAChiper.FIRST_INTEGER_BIGGER_THAN_SECOND || !e.gcd(phi).equals(BigInteger.ONE));


        // de = 1 mod phi
        d = e.modInverse(phi);


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


}
