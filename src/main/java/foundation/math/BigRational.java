package foundation.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gioacchino on 22/05/17.
 */
public class BigRational {

    private BigInteger numerator;
    private BigInteger denominator;


    public static BigRational recomposeConvergent(List<BigInteger> expansion) {

        List<BigInteger> tmplist = new ArrayList<>();
        for (BigInteger term : expansion) {
            tmplist.add(term.add(BigInteger.ZERO));
        }

        BigRational result = new BigRational(tmplist.remove(tmplist.size()-1), BigInteger.ONE); // a_n / 1

        while (!tmplist.isEmpty()) {

            // trova il reciproco e somma a_{n-1}
            result = result.getReciprocal().sum(tmplist.remove(tmplist.size()-1));

        }

        return result;

    }

    public BigRational(BigInteger numerator, BigInteger denominator) {
        this.setNumerator(numerator);
        this.setDenominator(denominator);
    }

    private BigRational() {
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public void setNumerator(BigInteger numerator) {
        this.numerator = numerator;

        this.semplify();
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public void setDenominator(BigInteger denominator) throws ArithmeticException {

        if (denominator.equals(BigInteger.ZERO))
            throw new ArithmeticException("Denominator cannot be 0");

        this.denominator = denominator;

        this.semplify();

    }

    private void semplify() {

        if (this.numerator != null && this.denominator != null) {

            BigInteger gcd = this.numerator.gcd(denominator);

            if (!gcd.equals(BigInteger.ONE)) {
                this.numerator = this.numerator.divide(gcd);
                this.denominator = denominator.divide(gcd);
            }

        }
    }

    public BigRational getReciprocal() throws ArithmeticException {

        BigRational reciprocal;

        if (this.isZero())
            throw new ArithmeticException("Reciprocal of 0 does not exist");

        reciprocal = new BigRational();

        reciprocal.setNumerator(this.denominator);
        reciprocal.setDenominator(this.numerator);

        return reciprocal;

    }

    public BigRational sum(BigInteger val) {

        BigRational sum = new BigRational();

        sum.setNumerator(val.multiply(this.denominator).add(this.numerator));
        sum.setDenominator(this.denominator);

        return sum;
    }

    public BigRational copy() {
        return new BigRational(this.numerator, this.denominator);
    }

    public List<RationalExpansionPair> getContinousFractionExpansion() {

        List<RationalExpansionPair> expansion = new ArrayList<>();
        RationalExpansionPair steppair;

        BigRational tmpfraction = this.copy();

        do {

            steppair = tmpfraction.splitIntegerPlusRational();
            expansion.add(steppair);
            if (!steppair.getRationalPart().isZero())
            tmpfraction = steppair.getRationalPart().getReciprocal();

        } while (!steppair.getRationalPart().isZero());

        return expansion;

    }

    public RationalExpansionPair splitIntegerPlusRational() throws ArithmeticException {

        BigRational rationalpart = new BigRational();
        BigInteger integerpart = this.getNumerator().divide(this.getDenominator());

        rationalpart.setNumerator(this.numerator.subtract(integerpart.multiply(this.denominator)));
        rationalpart.setDenominator(this.denominator);


        return new RationalExpansionPair(integerpart, rationalpart);
    }


    public List<BigInteger> getListIntegersContinuedFraction() {

        List<BigInteger> integers = new ArrayList<>();
        RationalExpansionPair steppair;

        BigRational tmpfraction = this.copy();

        do {

            steppair = tmpfraction.splitIntegerPlusRational();
            integers.add(steppair.getIntegerPart());
            if (!steppair.getRationalPart().isZero())
                tmpfraction = steppair.getRationalPart().getReciprocal();

        } while (!steppair.getRationalPart().isZero());

        return integers;
    }

    public boolean isZero() {

        return this.numerator.equals(BigInteger.ZERO);

    }

    public String toString(int radix) {
        return this.numerator.toString(radix) + " / " + this.denominator.toString(radix);
    }

    public class RationalExpansionPair {

        private BigInteger integerpart;
        private BigRational rationalpart;

        public RationalExpansionPair(BigInteger integerpart, BigRational rationalpart) {
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

        public BigRational getRationalPart() {
            return rationalpart;
        }

        public void setRationalPart(BigRational rationalpart) {
            this.rationalpart = rationalpart;
        }

        public String toString(int radix) {
            return this.integerpart.toString(radix) + " + " + this.rationalpart.toString(radix);
        }
    }
}
