import foundation.socket.SocketClient;

import java.io.IOException;

/**
 * Created by gioacchino on 20/05/17.
 */
public class ChatChiperClient {

    public static void main(String[] args) {

        SocketClient sc = new SocketClient("localhost", 8080);
        sc.init();

        try {

            String inputline = "";
            String chatline = "";

            do {

                System.out.print("Input: ");
                inputline = sc.getConsoleReader().readLine();
                sc.getDataOutputStream().writeUTF(inputline);
                sc.getDataOutputStream().flush();

                //chatline = this.dataInputStream.readUTF();
                System.out.println("Server --> " + chatline);

            }
            while (!inputline.equals("/end"));

            //this.dismissClient();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
