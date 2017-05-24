import domain.rsa.projectcustomrsa.RSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;

import javax.crypto.*;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gioacchino on 16/05/17.
 */
public class App {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {


        // generazione chiave DES
        /*
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

        */
        // test criptazione messaggio des

        String message = "Ciao";

        BigInteger messageint = new BigInteger(message.getBytes());

        RSAChiper c = RSAChiper.getInstance();

        KeyBundle kb = c.getWienerAttackableKeys(60);

        System.out.println("N:" + kb.getPublicKey().getN().toString(10));


        System.out.println("Numero bit chiave: " + kb.getPublicKey().getE().bitLength());


        BigInteger deschiavecriptata = c.encryptBlock(messageint, kb.getPublicKey() );

        System.out.println("Testo chiaro: " +  messageint.toString(2));

        System.out.println("Testo deschiave criptata: " +  deschiavecriptata.toString(2));


        BigInteger decriptato = c.decryptBlock(deschiavecriptata, kb.getPrivateKey());

        //System.out.println(decriptato.toString(2));
        String decr = new String(decriptato.toByteArray());

        System.out.println("Chiave pubblica E: " + kb.getPublicKey().getE().toString(2) );
        System.out.println("Chiave pubblica N: " + kb.getPublicKey().getN().toString(2) );

        //System.out.println("");



        System.out.println("Testo decriptato: " +  decr);

        // controllo attacco di Wiener
        BigInteger d = kb.getPrivateKey().getD();

        BigInteger n = kb.getPublicKey().getN();

        System.out.println("N: " + n.toString(10));
        System.out.println(" 1/3  Radice quarta: " + sqrt(sqrt(n)).divide(BigInteger.valueOf(3)).toString(10));
        System.out.println(" d: " + d.toString(10));

        System.out.println(d.compareTo(sqrt(sqrt(n)).divide(BigInteger.valueOf(3))));

        // ATTACCO DI WIENER

        c.attackWiener(kb.getPublicKey());

















    }

    static BigInteger sqrt(BigInteger n) {
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
