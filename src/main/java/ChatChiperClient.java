import domain.rsa.projectcustomrsa.CustomRSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;
import domain.rsa.projectcustomrsa.utils.PublicKey;
import foundation.socket.SocketClient;
import org.apache.log4j.BasicConfigurator;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by gioacchino on 20/05/17.
 */
public class ChatChiperClient {

    public static void main(String[] args) {

        // configurazione del logger
        BasicConfigurator.configure();

        SocketClient sc = new SocketClient("localhost", 8080);

        //KeyBundle kb = new KeyBundle(); // todo leggere da file

        // leggi la chiave pubblica del server dal file
        KeyBundle kb = readKeyFromFile();

        // generazione chiave DES
        SecretKey deskey = generateDESKey();

        try {

            sc.init();
            startClientLoop(sc, kb, deskey);
            sc.dismissClient();


        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }

    private static SecretKey generateDESKey() {

        final String DES_ID = "DES";

        KeyGenerator kg;
        SecretKey key = null;
        try {
            kg = KeyGenerator.getInstance(DES_ID);
            key = kg.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return key;
    }

    private static KeyBundle readKeyFromFile() {

        KeyBundle keyBundle = new KeyBundle();
        PublicKey publicKey = new PublicKey();
        FileReader fileReader;
        BufferedReader bufferedReader;


        String line = "";

        // scrivi la chiave pubblica su un file "public.key"
        try {

            fileReader = new FileReader("public.key");

            bufferedReader = new BufferedReader(fileReader);

            // lettura di N
            line = bufferedReader.readLine();
            publicKey.setN(new BigInteger(line, 2));

            // lettura di E
            line = bufferedReader.readLine();
            publicKey.setE(new BigInteger(line, 2));

            keyBundle.setPublickey(publicKey);


            bufferedReader.close(); // chiusura del file in lettura

        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyBundle;
    }

    private static void startClientLoop(SocketClient socketClient, KeyBundle keyBundle, SecretKey deskey) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        String outgoingmessage = "";
        String incomingmessage = "";

        Cipher cipher = Cipher.getInstance("DES");;
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] textEncrypted; // conterrà il testo criptato


        String encrypteddes = encryptDESKey(keyBundle, deskey);

        // invia chiave criptata (con handshaking)
        socketClient.sendMessage("KEY " + encrypteddes);
        incomingmessage = socketClient.receiveMessage();
        System.out.println("Server>" + incomingmessage);


        do {

            System.out.print("Me>");
            outgoingmessage = socketClient.getConsoleReader().readLine();


            textEncrypted = cipher.doFinal(outgoingmessage.getBytes());

            socketClient.sendMessage(new String(textEncrypted, StandardCharsets.UTF_8));

            incomingmessage = socketClient.receiveMessage();

            // todo decriptare con des

            System.out.println("Server>" + incomingmessage);

        }
        while (!outgoingmessage.equals("/end"));

    }

    private static String encryptDESKey(KeyBundle key, SecretKey deskey) {

        byte[] data = deskey.getEncoded();
        BigInteger messageint = new BigInteger(deskey.getEncoded());
        BigInteger desencryptedkey = CustomRSAChiper.getInstance().encryptBlock(messageint, key.getPublickey());

        return desencryptedkey.toString(2);

    }
}
