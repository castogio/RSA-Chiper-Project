import domain.rsa.projectcustomrsa.CustomRSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;
import foundation.socket.SocketServer;
import org.apache.log4j.BasicConfigurator;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by gioacchino on 20/05/17.
 */
public class ChatChiperServer {

    public static void main(String[] args) {

        // configurazione del logger
        BasicConfigurator.configure();

        // creazione del server socket di ascolto
        SocketServer ss = new SocketServer(8080);


        // creazione delle chiavi crittografiche RSA
        KeyBundle rsakeybundle = generateRSAKeys();

        // avvio dell'ascolto
        try {

            ss.init();
            startChatLoop(ss, rsakeybundle); // TODO USARE LE CHIAVI
            ss.dismissServer();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }


    }

    private static KeyBundle generateRSAKeys() {

        CustomRSAChiper chiper =  CustomRSAChiper.getInstance();

        KeyBundle keyBundle = chiper.getKeys(60, 30);

        PrintWriter writer = null;

        // scrivi la chiave pubblica su un file "public.key"
        try {
            writer = new PrintWriter(new FileWriter("public.key", false));
            writer.println(keyBundle.getPublickey().getN().toString(2));
            writer.println(keyBundle.getPublickey().getE().toString(2));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return keyBundle;
    }

    private static void startChatLoop(SocketServer ss, KeyBundle keyBundle) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        String incomingmessage = "";
        boolean endconversation = false;
        String outgoingmessage = "";
        SecretKey deskey = null;
        Cipher chiper = Cipher.getInstance("DES");


        do {

            incomingmessage = ss.receiveMessage();


            if (incomingmessage.substring(0,3).equals("KEY")) {

                BigInteger key = null;
                CustomRSAChiper rsaChiper = CustomRSAChiper.getInstance();

                // Decripta la chiave ricevuta con RSA
                key = new BigInteger(incomingmessage.substring(4,incomingmessage.length()), 2);
                key = rsaChiper.decryptBlock(key, keyBundle.getPrivatekey());

                System.out.println(Arrays.toString(key.toByteArray()));

                deskey = new SecretKeySpec(key.toByteArray(), 0, key.toByteArray().length, "DES");
                System.out.println(Arrays.toString(deskey.getEncoded()));

                ss.sendMessage("KEY RECEIVED");

            }
            else if (incomingmessage.equals("/end"))
                endconversation = true;
            else{
                // decripta messaggio con DES
                chiper.init(Cipher.DECRYPT_MODE, deskey);
                incomingmessage = new String( chiper.doFinal(Base64.getDecoder().decode(incomingmessage)));

                // incomingmessage = new String(chiper.doFinal(incomingmessage.getBytes()));

                System.out.println("Peer>" + incomingmessage);

                System.out.print("Me>");


                outgoingmessage = ss.getConsoleReader().readLine();

                // cript
                chiper.init(Cipher.ENCRYPT_MODE, deskey);
                //outgoingmessage = new String(chiper.doFinal(outgoingmessage.getBytes()));
                outgoingmessage = new String( chiper.doFinal(Base64.getDecoder().decode(outgoingmessage)));

                ss.sendMessage(outgoingmessage);

            }


        }
        while (!endconversation);

    }
}
