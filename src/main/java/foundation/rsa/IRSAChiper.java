package foundation.rsa;

import foundation.rsa.rsautils.KeyBundle;
import foundation.rsa.rsautils.PrivateKey;
import foundation.rsa.rsautils.PublicKey;

import java.math.BigInteger;

/**
 * Created by gioacchino on 16/05/17.
 */
public interface IRSAChiper {

    KeyBundle getKeys(int pfactorlength, int qfactorlength);

    BigInteger encryptBlock(BigInteger plainmessage, PublicKey key);

    BigInteger dencryptBlock(BigInteger chipertex, PrivateKey key);

    String encryptMessage(String plaintext, PublicKey key);

    String decryptMessage(String chipertext, PrivateKey key);


}
