import java.math.BigInteger;

/**
 * Created by gioacchino on 31/05/17.
 */
public class FermatFactorization {

    public static void main(String[] args) {

        BigInteger n = new BigInteger("891108673282801", 10);
        //System.out.print(n.bitLength());

        BigInteger a = getSquareRoot(n).add(BigInteger.ONE);

        boolean perfectsquare = false;
        BigInteger b2;
        do {
            b2 = a.pow(2).subtract(n);
            perfectsquare = getSquareRoot(b2).pow(2).equals(b2);

            if (!perfectsquare) {
                a = a.add(BigInteger.ONE);
            }
        } while (!perfectsquare);

        BigInteger b = getSquareRoot(b2);

        BigInteger p = a.subtract(b);
        BigInteger q = a.add(b);

        System.out.println("p = " + p.toString(10));
        System.out.println("q = " + q.toString(10));









    }

    private static BigInteger getSquareRoot(BigInteger n) {
        BigInteger a = BigInteger.ONE;
        BigInteger b = n.shiftRight(5).add(BigInteger.valueOf(8));
        while (b.compareTo(a) >= 0) {
            BigInteger mid = a.add(b).shiftRight(1);
            if (mid.multiply(mid).compareTo(n) > 0) {
                b = mid.subtract(BigInteger.ONE);
            } else {
                a = mid.add(BigInteger.ONE);
            }
        }
        return a.subtract(BigInteger.ONE);
    }

}
