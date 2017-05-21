package domain.rsa;

import domain.rsa.projectcustomrsa.utils.KeyBundle;
import domain.rsa.projectcustomrsa.utils.PrivateKey;
import domain.rsa.projectcustomrsa.utils.PublicKey;

import java.math.BigInteger;

/**
 * Created by gioacchino on 16/05/17.
 */
public interface IRSAChiper {

    KeyBundle getKeys(int pfactorlength, int qfactorlength);

    BigInteger encryptBlock(BigInteger plainmessage, PublicKey key);

    BigInteger dencryptBlock(BigInteger chipertex, PrivateKey key);

    KeyBundle attackRSA(PublicKey publicKey);


}
