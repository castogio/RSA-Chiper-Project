import domain.rsa.projectcustomrsa.utils.KeyBundle;
import domain.rsa.projectcustomrsa.utils.PublicKey;
import foundation.socket.SocketServer;
import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.math.BigInteger;

/**
 * Created by gioacchino on 20/05/17.
 */
public class ChatChiperMaliciousServer {

    public static void main(String[] args) {

        // configurazione del logger
        BasicConfigurator.configure();

        // creazione del server socket di ascolto
        SocketServer ss = new SocketServer(8080);


        // retrieve della chiave privata a partire dalla chiave pubblica
        KeyBundle rsakeybundle = hackRSAKey();

        // TODO applicare Wiener


        /*
        // avvio dell'ascolto
        try {

            ss.init();
            startChatLoop(ss, rsakeybundle);
            ss.dismissServer();

        } catch (IOException e) {
            e.printStackTrace();
        }
        */


    }

    private static KeyBundle hackRSAKey() {

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
