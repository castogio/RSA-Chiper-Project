package domain.rsa.projectcustomrsa.utils;

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

    public void setD(BigInteger dvalue) {
        this.dvalue = dvalue;
    }

    public BigInteger getP() {
        return pvalue;
    }

    public void setP(BigInteger pvalue) {
        this.pvalue = pvalue;
    }

    public BigInteger getQ() {
        return qvalue;
    }

    public void setQ(BigInteger qvalue) {
        this.qvalue = qvalue;
    }

    public BigInteger getN() {
        return nvalue;
    }

    public void setN(BigInteger nvalue) {
        this.nvalue = nvalue;
    }
}
