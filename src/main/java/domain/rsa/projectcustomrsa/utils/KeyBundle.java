package domain.rsa.projectcustomrsa.utils;

/**
 * Created by gioacchino on 16/05/17.
 */
public class KeyBundle {

    private PublicKey publickey;
    private PrivateKey privatekey;

    public PublicKey getPublicKey() {
        return publickey;
    }

    public void setPublicKey(PublicKey publickey) {
        this.publickey = publickey;
    }

    public PrivateKey getPrivatekey() {
        return privatekey;
    }

    public void setPrivateKey(PrivateKey privatekey) {
        this.privatekey = privatekey;
    }
}
