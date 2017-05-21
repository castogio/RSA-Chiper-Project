package foundation.socket;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gioacchino on 20/05/17.
 */
public class SocketServer {

    final static Logger logger = Logger.getLogger(ServerSocket.class);

    private int port = 0;
    private Socket socket = null;
    private DataInputStream dataInputStream = null;
    private BufferedReader consoleReader = null;
    private DataOutputStream dataOutputStream = null;

    public SocketServer(int port) {
        this.port = port;
    }

    public void init() {

        String chatline = "";
        String inputline = "";

        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            logger.info("Server in ascolto sulla porta: " + this.getPort());
            logger.info("Il Server rimane in attesa di una connessione");
            this.socket = serverSocket.accept();
            this.openServer();
            this.openResponseChat();
            logger.info("Il Server è pronto al loop di conversazione");


        } catch (EOFException e) { // se il peer chiude inaspettatamente la connessione
            try {
                this.dismissServer();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void openResponseChat() throws IOException {

        this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());

    }

    public void dismissServer() throws IOException {
        if (this.socket != null)
            this.socket.close();
        if (this.dataInputStream != null)
            this.dataInputStream.close();

        logger.info("Esecuzione del server terminata correttamente");
    }

    private void openServer() throws IOException {

        this.dataInputStream = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public void setDataInputStream(DataInputStream dataInputStream) {
        this.dataInputStream = dataInputStream;
    }

    public BufferedReader getConsoleReader() {
        return consoleReader;
    }

    public void setConsoleReader(BufferedReader consoleReader) {
        this.consoleReader = consoleReader;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void setDataOutputStream(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
    }

    public void sendMessage(String inputline) throws IOException {

        this.getDataOutputStream().writeUTF(inputline);
        this.getDataOutputStream().flush();
    }

    public String receiveMessage() throws IOException {

        return this.getDataInputStream().readUTF();
    }
}
