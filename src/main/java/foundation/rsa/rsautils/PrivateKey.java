package foundation.rsa.rsautils;

import java.math.BigInteger;

/**
 * Created by gioacchino on 16/05/17.
 */
public class PrivateKey {

    public final static int BASE = 2;

    private BigInteger dvalue;
    private BigInteger pvalue;
    private BigInteger qvalue;
    private BigInteger nvalue;

    public BigInteger getD() {
        return dvalue;
    }

    void setD(BigInteger dvalue) {
        this.dvalue = dvalue;
    }

    public BigInteger getP() {
        return pvalue;
    }

    void setP(BigInteger pvalue) {
        this.pvalue = pvalue;
    }

    public BigInteger getQ() {
        return qvalue;
    }

    void setQ(BigInteger qvalue) {
        this.qvalue = qvalue;
    }

    public BigInteger getN() {
        return nvalue;
    }

    public void setN(BigInteger nvalue) {
        this.nvalue = nvalue;
    }
}
