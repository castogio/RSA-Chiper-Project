package foundation.rsa.rsautils;

/**
 * Created by gioacchino on 16/05/17.
 */
public class KeyBundle {

    private BinaryKey publickey;
    private BinaryKey privatekey;

    public KeyBundle() {
    }

    public BinaryKey getPublickey() {
        return publickey;
    }

    public BinaryKey getPrivatekey() {
        return privatekey;
    }


}
