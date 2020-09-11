package com.utad.david.planfit.Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class UtilsEncryptDecryptAES {

    private static String SAFETY_ALGORITHM = "AES";

    private static final byte[] keyValue = new byte[]{'c', 'o', 'd', 'i', 'n', 'g', 'a', 'f', 'f', 'a', 'i', 'r', 's', 'c', 'o', 'm'};
    private final static String HEX = "0123456789ABCDEF";

    /**
     * Encrypt clear text
     * @param cleartext encrypt
     * @return text encrypt
     * @throws Exception encrypt
     */
    public static String encrypt(String cleartext) throws Exception {
        byte[] rawKey = getRawKey();
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        return toHex(result);
    }

    /**
     * Get raw secret key
     * @return key in byte[]
     */
    private static byte[] getRawKey() {
        SecretKey key = new SecretKeySpec(keyValue, SAFETY_ALGORITHM);
        return key.getEncoded();
    }

    /**
     * Init chiper in encrypt mode whit secretKey
     * @param raw secret key
     * @param clearTextBytes clearText in bytes[]
     * @return encrypt clear text
     * @throws Exception encrypt
     */
    private static byte[] encrypt(byte[] raw, byte[] clearTextBytes) throws Exception {
        SecretKey skeySpec = new SecretKeySpec(raw, SAFETY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(SAFETY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(clearTextBytes);
    }

    /**
     * Get string in hexadecimal format
     * @param textEncrypt in byte[]
     * @return string in hexadecimal
     */
    private static String toHex(byte[] textEncrypt) {
        if (textEncrypt == null)
            return "";
        StringBuffer result = new StringBuffer(2 * textEncrypt.length);
        for (int i = 0; i < textEncrypt.length; i++) {
            appendHex(result, textEncrypt[i]);
        }
        return result.toString();
    }

    /**
     * Append character in byte format in stringBuffer
     * @param result hexadecimal format
     * @param character byte of textEncrypt in byte
     */
    private static void appendHex(StringBuffer result, byte character) {
        result.append(HEX.charAt((character >> 4) & 0x0f)).append(HEX.charAt(character & 0x0f));
    }

    /**
     * Decrypt text encrypted
     * @param encrypted text for decrypt
     * @return clear text
     * @throws Exception decrypt
     */
    public static String decrypt(String encrypted) throws Exception {
        byte[] enc = toByte(encrypted);
        byte[] clearText = decrypt(enc);
        return new String(clearText);
    }

    /**
     * Change format string in hexadecimal to array of bytes
     * @param hexString change format
     * @return array of bytes
     */
    private static byte[] toByte(String hexString) {
        int size = hexString.length() / 2;
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    /**
     * Init chiper in decrypt mode whit secretKey
     * @param encrypted array of bytes
     * @return text encrypted
     * @throws Exception encrypt
     */
    private static byte[] decrypt(byte[] encrypted) throws Exception {
        SecretKey skeySpec = new SecretKeySpec(keyValue, SAFETY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(SAFETY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encrypted);
    }

}
