import foundation.rsa.rsautils.CustomRSAChiper;
import foundation.rsa.rsautils.KeyBundle;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by gioacchino on 16/05/17.
 */
public class App {

    public static void main(String[] args) throws NoSuchAlgorithmException {


        // generazione chiave DES
        KeyGenerator kg = KeyGenerator.getInstance("DES");

        SecretKey key = kg.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        System.out.println(Arrays.toString(key.getEncoded()));

        byte[] data = key.getEncoded();
        BigInteger messageint = new BigInteger(key.getEncoded());

        System.out.println("Lunghezza chiave DES: " + messageint.bitLength());
        System.out.println(" chiave DES: " + messageint.toString(10));

        SecretKey key2 = new SecretKeySpec(data, 0, data.length, "DES");
        System.out.println(Arrays.toString(key2.getEncoded()));

        // generazione chiave des


        String msg = "qw";


        CustomRSAChiper c = new CustomRSAChiper();

        KeyBundle kb = c.getKeys(23, 7);

        //System.out.println(c.encryptMessage(msg, kb.getPublickey()));

        System.out.println("N:" + kb.getPublickey().getN().toString(10));


        System.out.println("Numero bit chiave: " + kb.getPublickey().getE().bitLength());

        System.out.println("Messaggio: " + msg );

        byte[] msg_byte = msg.getBytes();

        System.out.println("Messaggio bytes: " + Arrays.toString(msg_byte) );

        //messageint = new BigInteger(msg_byte);


        //System.out.println(messageint.toString(2));


        //BigInteger criptato = c.encryptBlock(messageint, kb.getPublickey() );
        BigInteger criptato = c.encryptBlock(messageint, kb.getPublickey() );

        System.out.println("Testo chiaro: " +  messageint.toString(2));

        System.out.println("Testo criptato: " +  criptato.toString(2));


        BigInteger decriptato = c.dencryptBlock(criptato, kb.getPrivatekey());

        //System.out.println(decriptato.toString(2));

        System.out.println("Chiave pubblica E: " + kb.getPublickey().getE().toString(2) );
        System.out.println("Chiave pubblica N: " + kb.getPublickey().getN().toString(2) );


        //String str = new String(decriptato.toByteArray());

        System.out.println("Testo decriptato: " +  decriptato.toString(2));
















    }
}
