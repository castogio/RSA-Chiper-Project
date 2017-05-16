package foundation.rsa.rsautils;

import java.math.BigInteger;

/**
 * Created by gioacchino on 16/05/17.
 */
public class BinaryKey {

    public static int BASE = 2;

    private BigInteger key;

    public BinaryKey() {
    }

    public BigInteger getKey() {
        return key;
    }

    public void setKey(BigInteger key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "BinaryKey {" + this.key.toString(BinaryKey.BASE)  + "}";
    }
}
