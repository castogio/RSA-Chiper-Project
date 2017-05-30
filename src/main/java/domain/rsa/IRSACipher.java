package domain.rsa;

import domain.rsa.projectcustomrsa.utils.KeyBundle;
import domain.rsa.projectcustomrsa.utils.PrivateKey;
import domain.rsa.projectcustomrsa.utils.PublicKey;

import java.math.BigInteger;

/**
 * Created by gioacchino on 16/05/17.
 */
public interface IRSACipher {

    /**
     * Genera casualmente delle chiari RSA attaccabili con il Teorema di Wiener
     * @param factorlength Lunghezza dei fattori p e q in numero di bit
     * @return Contenitore delle chiavi generate (una pubblica e una privata)
     */
    KeyBundle getWienerAttackableKeys(int factorlength);

    /**
     * Encrypt di un messaggio
     * @param plainmessage Rappresentazione come intero del messaggio chiaro
     * @param key Chiave pubblica di criptazione
     * @return
     */
    BigInteger encryptBlock(BigInteger plainmessage, PublicKey key);

    /**
     * Decrypt di un messaggio
     * @param chipertex Rappresentazione come intero del messaggio cifrato
     * @param key Chiave privata di decriptazione
     * @return
     */
    BigInteger decryptBlock(BigInteger chipertex, PrivateKey key);

    /**
     * Attuazione dell'attacco ad RSA mediante il teorema di Wiener
     * @param publicKey Chiave pubblica su cui tentare l'attacco
     * @return
     */
    KeyBundle attackWiener(PublicKey publicKey);

    /**
     * Controllo di debolezza sulla chiave privata
     * @param privateKey Chiave privata da testare
     * @return true se attaccabile
     */
    boolean isWienerAttackable(PrivateKey privateKey);


}
