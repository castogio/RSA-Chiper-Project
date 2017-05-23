package foundation.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gioacchino on 22/05/17.
 */
public class BigRationalFX {

    private BigInteger numerator;
    private BigInteger denominator;

    public BigRationalFX(BigInteger numerator, BigInteger denominator) {
        this.setNumerator(numerator);
        this.setDenominator(denominator);
    }

    private BigRationalFX() {
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public void setNumerator(BigInteger numerator) {
        this.numerator = numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public void setDenominator(BigInteger denominator) throws ArithmeticException {

        BigInteger gcd;

        if (denominator.equals(BigInteger.ZERO))
            throw new ArithmeticException("Denominator cannot be 0");

        gcd = this.numerator.gcd(denominator);

        if (!gcd.equals(BigInteger.ONE)) {
            this.numerator = this.numerator.divide(gcd);
            this.denominator = denominator.divide(gcd);
        }
        else
            this.denominator = denominator;

    }

    public BigRationalFX getReciprocal() throws ArithmeticException {

        BigRationalFX reciprocal;

        if (this.isZero())
            throw new ArithmeticException("Reciprocal of 0 does not exist");

        reciprocal = new BigRationalFX();

        reciprocal.setNumerator(this.denominator);
        reciprocal.setDenominator(this.numerator);

        return reciprocal;

    }

    public BigRationalFX sum(BigInteger val) {

        BigRationalFX sum = new BigRationalFX();

        sum.setNumerator(val.multiply(this.denominator).add(this.numerator));
        sum.setDenominator(this.denominator);

        return sum;
    }

    public BigRationalFX copy() {
        return new BigRationalFX(this.numerator, this.denominator);
    }

    public List<RationalExpansionPair> getContinousFractionExpansion() {

        List<RationalExpansionPair> expansion = new ArrayList<>();
        RationalExpansionPair steppair;

        BigRationalFX tmpfraction = this.copy();

        do {

            steppair = tmpfraction.splitIntegerPlusRational();
            expansion.add(steppair);
            if (!steppair.getRationalPart().isZero())
            tmpfraction = steppair.getRationalPart().getReciprocal();

        } while (!steppair.getRationalPart().isZero());

        return expansion;

    }

    public RationalExpansionPair splitIntegerPlusRational() throws ArithmeticException {

        BigRationalFX rationalpart = new BigRationalFX();
        BigInteger integerpart = this.getNumerator().divide(this.getDenominator());

        rationalpart.setNumerator(this.numerator.subtract(integerpart.multiply(this.denominator)));
        rationalpart.setDenominator(this.denominator);


        return new RationalExpansionPair(integerpart, rationalpart);
    }


    public List<BigInteger> getListIntegersContinousFraction() {
        List<RationalExpansionPair> pairs = this.getContinousFractionExpansion();
        List<BigInteger> integers = new ArrayList<>();

        for (RationalExpansionPair singlepair : pairs)
            integers.add(singlepair.getIntegerPart());

        return integers;
    }

    // TODO completare
    public BigRationalFX getConvergent(int n) {

        List<BigInteger> listconvergent = this.getListIntegersContinousFraction();


        return new BigRationalFX();

    }

    public boolean isZero() {

        return this.numerator.equals(BigInteger.ZERO);

    }

    public String toString(int radix) {
        return this.numerator.toString(radix) + " / " + this.denominator.toString(radix);
    }

    public class RationalExpansionPair {

        private BigInteger integerpart;
        private BigRationalFX rationalpart;

        public RationalExpansionPair(BigInteger integerpart, BigRationalFX rationalpart) {
            this.integerpart = integerpart;
            this.rationalpart = rationalpart;
        }

        public RationalExpansionPair() {
        }

        public BigInteger getIntegerPart() {
            return integerpart;
        }

        public void setIntegerPart(BigInteger integerpart) {
            this.integerpart = integerpart;
        }

        public BigRationalFX getRationalPart() {
            return rationalpart;
        }

        public void setRationalPart(BigRationalFX rationalpart) {
            this.rationalpart = rationalpart;
        }

        public String toString(int radix) {
            return this.integerpart.toString(radix) + " + " + this.rationalpart.toString(radix);
        }
    }
}
