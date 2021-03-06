package domain.rsa.projectcustomrsa.utils;

import java.math.BigInteger;

/**
 * Created by gioacchino on 16/05/17.
 */
public class PublicKey {

    public final static int BASE = 2;

    private BigInteger nvalue;
    private BigInteger evalue;

    public BigInteger getN() {
        return nvalue;
    }

    public void setN(BigInteger nvalue) {
        this.nvalue = nvalue;
    }

    public BigInteger getE() {
        return evalue;
    }

    public void setE(BigInteger evalue) {
        this.evalue = evalue;
    }

    public String toString(int radix) {
        return "PUBLIC KEY\n" +
                "n = " + nvalue.toString(radix) +
                ",\ne = " + evalue.toString(radix);
    }
}
