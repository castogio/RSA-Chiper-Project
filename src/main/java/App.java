import domain.rsa.projectcustomrsa.CustomRSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by gioacchino on 16/05/17.
 */
public class App {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {


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

        // test criptazione messaggio des

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);


        cipher.init(Cipher.ENCRYPT_MODE, key);

        //sensitive information
        byte[] text = "No body can see me".getBytes();

        System.out.println("Text [Byte Format] : " + Arrays.toString(text));
        System.out.println("Text : " + new String(text));

        // Encrypt the text
        byte[] textEncrypted = cipher.doFinal(text);

        System.out.println("Text Encryted : " + Arrays.toString(textEncrypted));

        // Initialize the same cipher for decryption
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decrypt the text
        byte[] textDecrypted = cipher.doFinal(textEncrypted);

        System.out.println("Text Decryted : " + new String(textDecrypted));

        // test criptazione messaggio des


        CustomRSAChiper c = CustomRSAChiper.getInstance();

        KeyBundle kb = c.getKeys(60, 30);

        System.out.println("N:" + kb.getPublickey().getN().toString(10));


        System.out.println("Numero bit chiave: " + kb.getPublickey().getE().bitLength());


        BigInteger deschiavecriptata = c.encryptBlock(messageint, kb.getPublickey() );

        System.out.println("Testo chiaro: " +  messageint.toString(2));

        System.out.println("Testo deschiave criptata: " +  deschiavecriptata.toString(2));


        BigInteger decriptato = c.dencryptBlock(deschiavecriptata, kb.getPrivatekey());

        //System.out.println(decriptato.toString(2));

        System.out.println("Chiave pubblica E: " + kb.getPublickey().getE().toString(2) );
        System.out.println("Chiave pubblica N: " + kb.getPublickey().getN().toString(2) );



        System.out.println("Testo decriptato: " +  decriptato.toString(2));
















    }
}
