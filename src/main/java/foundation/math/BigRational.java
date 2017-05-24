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


    /**
     * Ricostruisce il convergente n-esimo
     * @param expansion Lista degli n termini del convergente
     * @return Numero razionale del convergente
     */
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

    /**
     * Costruisce un numero razionale con numeratore e denominatore
     * @param numerator Intero numeratore
     * @param denominator Intero denominatore
     */
    public BigRational(BigInteger numerator, BigInteger denominator) {
        this.setNumerator(numerator);
        this.setDenominator(denominator);
    }

    /**
     * Costruisce un numero razione vuoto
     */
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

    /**
     * Ottiene il reciproco del razionale
     * @return Razionale reciproco
     * @throws ArithmeticException Lanciato se il razionale è nullo
     */
    public BigRational getReciprocal() throws ArithmeticException {

        BigRational reciprocal;

        if (this.isZero())
            throw new ArithmeticException("Reciprocal of 0 does not exist");

        reciprocal = new BigRational();

        reciprocal.setNumerator(this.denominator);
        reciprocal.setDenominator(this.numerator);

        return reciprocal;

    }

    /**
     * Somma di due razionali
     * @param val Razionale da sommare
     * @return Razionale somma
     */
    public BigRational sum(BigInteger val) {

        BigRational sum = new BigRational();

        sum.setNumerator(val.multiply(this.denominator).add(this.numerator));
        sum.setDenominator(this.denominator);

        return sum;
    }

    /**
     * Effettua una copia del razionale
     * @return Deep copy
     */
    public BigRational copy() {

        return new BigRational(this.numerator, this.denominator);
    }

    /*
    public List<SumIntegerPlusRational> getContinousFractionExpansion() {

        List<SumIntegerPlusRational> expansion = new ArrayList<>();
        SumIntegerPlusRational steppair;

        BigRational tmpfraction = this.copy();

        do {

            steppair = tmpfraction.splitIntegerPlusRational();
            expansion.add(steppair);
            if (!steppair.getRationalPart().isZero())
            tmpfraction = steppair.getRationalPart().getReciprocal();

        } while (!steppair.getRationalPart().isZero());

        return expansion;

    }
    */

    /**
     * Decompone il razionale in un intero sommato ad un razionale
     * @return Somma intero razionale
     * @throws ArithmeticException
     */
    public SumIntegerPlusRational splitIntegerPlusRational() throws ArithmeticException {

        BigRational rationalpart = new BigRational();
        BigInteger integerpart = this.getNumerator().divide(this.getDenominator());

        rationalpart.setNumerator(this.numerator.subtract(integerpart.multiply(this.denominator)));
        rationalpart.setDenominator(this.denominator);


        return new SumIntegerPlusRational(integerpart, rationalpart);
    }

    /**
     * Decomposizione del razionale nella lista di interi che compongono la continued fraction
     * @return Lista degli interi
     */
    public List<BigInteger> getListIntegersContinuedFraction() {

        List<BigInteger> integers = new ArrayList<>();
        SumIntegerPlusRational steppair;

        BigRational tmpfraction = this.copy();

        do {

            steppair = tmpfraction.splitIntegerPlusRational();
            integers.add(steppair.getIntegerPart());
            if (!steppair.getRationalPart().isZero())
                tmpfraction = steppair.getRationalPart().getReciprocal();

        } while (!steppair.getRationalPart().isZero());

        return integers;
    }

    /**
     * Controlla se il numero razionale è nullo
     * @return false se il numeratore è nullo
     */
    public boolean isZero() {

        return this.numerator.equals(BigInteger.ZERO);

    }

    public String toString(int radix) {
        return this.numerator.toString(radix) + " / " + this.denominator.toString(radix);
    }

    /**
     * Semplificazione del numeratore e del denominatore
     */
    private void semplify() {

        if (this.numerator != null && this.denominator != null) {

            BigInteger gcd = this.numerator.gcd(denominator);

            if (!gcd.equals(BigInteger.ONE)) {
                this.numerator = this.numerator.divide(gcd);
                this.denominator = denominator.divide(gcd);
            }

        }
    }


    /**
     * Inner class per la decomposizione in convergenti
     */
    public class SumIntegerPlusRational {

        private BigInteger integerpart;
        private BigRational rationalpart;

        public SumIntegerPlusRational(BigInteger integerpart, BigRational rationalpart) {
            this.integerpart = integerpart;
            this.rationalpart = rationalpart;
        }

        public SumIntegerPlusRational() {
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
