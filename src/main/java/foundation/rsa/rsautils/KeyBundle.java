package foundation.rsa.rsautils;

/**
 * Created by gioacchino on 16/05/17.
 */
public class KeyBundle {

    private PublicKey publickey;
    private PrivateKey privatekey;

    public PublicKey getPublickey() {
        return publickey;
    }

    void setPublickey(PublicKey publickey) {
        this.publickey = publickey;
    }

    public PrivateKey getPrivatekey() {
        return privatekey;
    }

    void setPrivatekey(PrivateKey privatekey) {
        this.privatekey = privatekey;
    }
}
