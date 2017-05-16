package foundation.rsa;

import foundation.rsa.rsautils.KeyBundle;

import java.math.BigInteger;

/**
 * Created by gioacchino on 16/05/17.
 */
public interface IRSAChiper {

    KeyBundle getKeys(int factorlength);

    // BigInteger encrypt(BigInteger )

}
