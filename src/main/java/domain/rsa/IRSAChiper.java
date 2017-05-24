package domain.rsa;

import domain.rsa.projectcustomrsa.utils.KeyBundle;
import domain.rsa.projectcustomrsa.utils.PrivateKey;
import domain.rsa.projectcustomrsa.utils.PublicKey;

import java.math.BigInteger;

/**
 * Created by gioacchino on 16/05/17.
 */
public interface IRSAChiper {

    public KeyBundle getVulnerableKeys(int factorlength);

    BigInteger encryptBlock(BigInteger plainmessage, PublicKey key);

    BigInteger decryptBlock(BigInteger chipertex, PrivateKey key);

    KeyBundle attackRSA(PublicKey publicKey);


}
