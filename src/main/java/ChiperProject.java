import domain.rsa.projectcustomrsa.RSAChiper;
import domain.rsa.projectcustomrsa.utils.KeyBundle;

import java.math.BigInteger;

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
        factorbitlenght = 80;
        keychain = chiper.getVulnerableKeys(factorbitlenght);

        // inizializzazione del messaggio da criptare
        messaggiochiaro = "Ciao Bob!";
        msgbytes = new BigInteger(messaggiochiaro.getBytes());

        // criptazione del messaggio mediante la chiave pubblica
        msgcriptato = chiper.encryptBlock(msgbytes, keychain.getPublicKey());


        /* ------ SUPPONIAMO CHE IL MESSAGGIO CRIPTATO SIA STATO TRASMESSO IN RETE A BOB  ------ */


        // decriptazione del messaggio mediante la chiave privata
        msgdecriptato = chiper.decryptBlock(msgcriptato, keychain.getPrivatekey());
        messaggioricevuto = new String(msgdecriptato.toByteArray());

        // confrontiamo i messaggi criptato e decriptato
        System.out.println("Testo chiaro originale: " +  messaggiochiaro);
        System.out.println("Testo decriptato: " +  messaggioricevuto);


        /* ------ EVELINE È A CONOSCENZA DELLA CHIAVE PUBBLICA  ------ */

        crackerkeychain = chiper.attackRSA(keychain.getPublicKey());

        // decriptazione del messaggio mediante la chiave privata
        msgdecriptatocracker = chiper.decryptBlock(msgcriptato, crackerkeychain.getPrivatekey());
        messaggiochiarocracker = new String(msgdecriptatocracker.toByteArray());
        System.out.println("Testo decriptato dal cracker: " +  messaggiochiarocracker);














    }
}
