import domain.rsa.projectcustomrsa.CustomRSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;
import foundation.socket.SocketServer;
import org.apache.log4j.BasicConfigurator;

import javax.crypto.SecretKey;
import java.io.*;
import java.math.BigInteger;

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
        SecretKey deskey = null;


        do {

            incomingmessage = ss.receiveMessage();


            if (incomingmessage.substring(0,3).equals("KEY")) {

                BigInteger key = null;
                CustomRSAChiper rsaChiper = CustomRSAChiper.getInstance();
                System.out.println(incomingmessage.substring(0,3));
                ss.sendMessage("KEY RECEIVED");

                // Decripta la chiave ricevuta con RSA
                key = new BigInteger(incomingmessage.substring(5,incomingmessage.length()), 2);
                key = rsaChiper.decryptBlock(key, keyBundle.getPrivatekey());






            }
            else if (incomingmessage.equals("/end"))
                endconversation = true;
            else{
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
