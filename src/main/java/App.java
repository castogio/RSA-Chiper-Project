import foundation.rsa.rsautils.CustomRSAChiper;
import foundation.rsa.rsautils.KeyBundle;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by gioacchino on 16/05/17.
 */
public class App {

    public static void main(String[] args) {

        String msg = "Il mio amore per te è resistente come questo messaggio, Miki - 16/05/2017";


        CustomRSAChiper c = new CustomRSAChiper();

        KeyBundle kb = c.getKeys(16, 17);

        System.out.println(c.encryptMessage(msg, kb.getPublickey()));

        /*
        System.out.println("Numero bit chiave: " + kb.getPublickey().getE().bitLength());

        System.out.println("Messaggio: " + msg );

        byte[] msg_byte = msg.getBytes();

        System.out.println("Messaggio bytes: " + Arrays.toString(msg_byte) );

        BigInteger messageint = new BigInteger(msg_byte);


        //System.out.println(messageint.toString(2));


        BigInteger criptato = c.encryptBlock(messageint, kb.getPublickey() );


        System.out.println("Testo criptato: " +  criptato.toString(2));


        BigInteger decriptato = c.dencryptBlock(criptato, kb.getPrivatekey());

        //System.out.println(decriptato.toString(2));

        System.out.println("Chiave pubblica E: " + kb.getPublickey().getE().toString(2) );
        System.out.println("Chiave pubblica N: " + kb.getPublickey().getN().toString(2) );


        String str = new String(decriptato.toByteArray());

        System.out.println(str);
        */













    }
}
