public interface IRSACipher {

    KeyBundle getWienerAttackableKeys(int factorlength);

    BigInteger encryptBlock(BigInteger plainmessage, PublicKey key);

    BigInteger decryptBlock(BigInteger chipertex, PrivateKey key);

    KeyBundle attackWiener(PublicKey publicKey);

    boolean isWienerAttackable(PrivateKey privateKey);

}
