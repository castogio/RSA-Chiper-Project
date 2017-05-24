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

    public void setPublickey(PublicKey publickey) {
        this.publickey = publickey;
    }

    public PrivateKey getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(PrivateKey privatekey) {
        this.privatekey = privatekey;
    }
}
