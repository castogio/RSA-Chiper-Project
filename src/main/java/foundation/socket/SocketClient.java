package foundation.socket;

import java.io.*;
import java.net.Socket;

/**
 * Created by gioacchino on 20/05/17.
 */
public class SocketClient {

    private String hostaddress = "";
    private int serviceport = 0;
    private Socket socket = null;
    private BufferedReader consoleReader = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;

    public SocketClient(String hostaddress, int serviceport) {
        this.hostaddress = hostaddress;
        this.serviceport = serviceport;
    }

    public void init() {

        String inputline = "";
        String chatline = "";

        try {
            this.socket = new Socket(this.hostaddress, this.serviceport);
            this.startClient();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dismissClient() throws IOException {
        if (this.socket != null)
            this.socket.close();
        if (this.dataOutputStream != null)
            this.dataOutputStream.close();
        if (this.consoleReader != null)
            this.consoleReader.close();
    }

    private void startClient() throws IOException {
        this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        this.dataInputStream = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
    }

    public String getHostaddress() {
        return hostaddress;
    }

    public void setHostaddress(String hostaddress) {
        this.hostaddress = hostaddress;
    }

    public int getServiceport() {
        return serviceport;
    }

    public void setServiceport(int serviceport) {
        this.serviceport = serviceport;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
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

        this.dataOutputStream.writeUTF(inputline);
        this.dataOutputStream.flush();
    }

    public String receiveMessage() throws IOException {

        return this.dataInputStream.readUTF();
    }
}
