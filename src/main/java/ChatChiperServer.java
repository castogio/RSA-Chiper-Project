import domain.rsa.projectcustomrsa.CustomRSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;
import foundation.socket.SocketServer;
import org.apache.log4j.BasicConfigurator;

import java.io.*;

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



        /*
        KeyGenerator keyGenerator;
        SecretKey deskey;
        CustomRSAChiper chiper;
        KeyBundle rsakeybundle;
        BigInteger deskeyencrypted;

        try {
            keyGenerator = KeyGenerator.getInstance("DES");
            deskey = keyGenerator.generateKey();
            chiper = new CustomRSAChiper();
            rsakeybundle = chiper.getKeys(60, 30);

            deskeyencrypted = chiper.encryptBlock(new BigInteger(deskey.getEncoded()), rsakeybundle.getPublickey());
            System.out.println("Testo chiaro: " +  new BigInteger(deskey.getEncoded()).toString(2));
            System.out.println("Testo des chiave criptata: " +  deskeyencrypted.toString(2));
            BigInteger decriptato = chiper.dencryptBlock(deskeyencrypted, rsakeybundle.getPrivatekey());
            System.out.println("Testo decriptato: " +  decriptato.toString(2));

            PrintWriter writer = new PrintWriter("", "UTF-8");
            writer.println("The first line");
            writer.println("The second line");
            writer.close();

        } catch (NoSuchAlgorithmException | FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        */


        // avvio dell'ascolto
        try {

            ss.init();
            startChatLoop(ss, rsakeybundle); // TODO USARE LE CHIAVI
            ss.dismissServer();

        } catch (IOException e) {
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

    private static void startChatLoop(SocketServer ss, KeyBundle keyBundle) throws IOException {

        String incomingmessage = "";
        boolean endconversation = false;
        String outgoingmessage = "";

        do {

            incomingmessage = ss.receiveMessage();

            if (incomingmessage.equals("/end"))
                endconversation = true;
            else {
                // TODO gestici logica
                System.out.println("Peer>" + incomingmessage);
                System.out.print("Me>");
                outgoingmessage = ss.getConsoleReader().readLine();
                ss.sendMessage(outgoingmessage);

            }


        }
        while (!endconversation);

    }
}
