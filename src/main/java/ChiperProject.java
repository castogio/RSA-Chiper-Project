import domain.rsa.projectcustomrsa.RSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * Created by gioacchino on 24/05/17.
 */
public class ChiperProject {

    public static void main(String[] args) {

        RSAChiper chiper; // gestiore di RSA (singleton)
        KeyBundle keychain; // contenitore per chiave pubblica e privata RSA
        KeyBundle crackerkeychain; // contenitore per le chiavi di un eventuale attaccante
        int factorbitlenght; // lunghezza in bit dei fattori p e q per la generazione delle chiavi

        String messaggiochiaro; // messaggio da cifrare
        BigInteger msgbytes;
        String messaggioricevuto; // messaggio decifrato
        String messaggiochiarocracker; // messaggio decifrato dal cracker

        BigInteger msgcriptato; // contenitore per i byte del messaggio criptato
        BigInteger msgdecriptato; // contenitore per i byte del messaggio de-criptato
        BigInteger msgdecriptatocracker; // contenitore per i byte del messaggio de-criptato dal cracker



        // inizializzazione del gestore RSA
        chiper = RSAChiper.getInstance();

        // ottenimento della chiave pubblica e privata (generazione casuale)
        factorbitlenght = 512;
        keychain = chiper.getVulnerableKeys(factorbitlenght);

        // output delle chiavi
        System.out.println("CHIAVI GENERATE");
        System.out.println("----------------------------------");
        System.out.println(keychain.getPublicKey().toString(10) + "\n");
        System.out.println("\n" + keychain.getPrivateKey().toString(10));

        System.out.println("\n----------------------------------");
        // controlla se la chiave privata è attaccabile con Wiener
        System.out.println("Attaccabile con Wiener: " + chiper.isWienerAttackable(keychain.getPrivateKey()));
        System.out.println("----------------------------------");

        // inizializzazione del messaggio da criptare
        System.out.print("\n\nTesto da criptare: ");
        messaggiochiaro = new Scanner(System.in).nextLine();
        msgbytes = new BigInteger(messaggiochiaro.getBytes());

        // criptazione del messaggio mediante la chiave pubblica
        msgcriptato = chiper.encryptBlock(msgbytes, keychain.getPublicKey());


        /* ------ SUPPONIAMO CHE IL MESSAGGIO CRIPTATO SIA STATO TRASMESSO IN RETE A BOB  ------ */


        // decriptazione del messaggio mediante la chiave privata
        msgdecriptato = chiper.decryptBlock(msgcriptato, keychain.getPrivateKey());
        messaggioricevuto = new String(msgdecriptato.toByteArray());

        // confrontiamo i messaggi criptato e decriptato
        System.out.println("\n\nCONFRONTO DEI MESSAGGI");
        System.out.println("----------------------------------");
        System.out.println("Testo chiaro originale: " +  messaggiochiaro);
        System.out.println("Testo decriptato: " +  messaggioricevuto);
        System.out.println("----------------------------------\n\n");


        /* ------ EVELINE È A CONOSCENZA DELLA CHIAVE PUBBLICA  ------ */

        // keychain generato dal cracker
        crackerkeychain = chiper.attackWiener(keychain.getPublicKey());

        System.out.println("CHIAVE PRIVATA GENERATA DAL CRACKER");
        System.out.println("----------------------------------");
        System.out.println(crackerkeychain.getPrivateKey().toString(10));

        // decriptazione del messaggio mediante la chiave privata
        msgdecriptatocracker = chiper.decryptBlock(msgcriptato, crackerkeychain.getPrivateKey());
        messaggiochiarocracker = new String(msgdecriptatocracker.toByteArray());
        System.out.println("----------------------------------");
        System.out.println("Testo decriptato dal cracker: " +  messaggiochiarocracker);














    }
}
