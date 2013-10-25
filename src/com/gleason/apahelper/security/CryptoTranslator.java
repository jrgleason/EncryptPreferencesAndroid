package com.gleason.apahelper.security;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import android.util.Base64;

public class CryptoTranslator {
	private static SecretKey SEC_KEY;
	public static final int IV_LENGTH = 16;
	private static final String RANDOM_ALGORITHM = "SHA1PRNG";

	/**
	 * @return the sEC_KEY
	 */
	public static SecretKey getSEC_KEY() {
		return SEC_KEY;
	}

	public static String getSEC_KEY_String() {
		return Base64.encodeToString(SEC_KEY.getEncoded(), Base64.DEFAULT);
	}

	/**
	 * @param sEC_KEY
	 *            the sEC_KEY to set
	 */
	public static void setSEC_KEY(SecretKey sEC_KEY) {
		SEC_KEY = sEC_KEY;
	}

	public static void setSEC_KEY_STRING(String sEC_KEY) {
		byte[] key = Base64.decode(sEC_KEY, Base64.DEFAULT);
		SEC_KEY = new SecretKeySpec(key, 0, key.length, "AES");
	}

	public static void generateKey() throws NoSuchAlgorithmException {
		// Generate a 256-bit key
		final int outputKeyLength = 256;
		SecureRandom secureRandom = new SecureRandom();
		// Do *not* seed secureRandom! Automatically seeded from system entropy.
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(outputKeyLength, secureRandom);
		SecretKey key = keyGenerator.generateKey();
		SEC_KEY = key;
	}

	private static byte[] getRawKey() throws Exception {
		if (SEC_KEY == null) {
			generateKey();
		}
		return SEC_KEY.getEncoded();
	}

	/**
	 * 
	 * 
	 * @param clear
	 *            clear text string
	 * @param mode
	 *            this should either be Cipher.ENCRYPT_MODE or
	 *            Cipher.DECRYPT_MODE
	 * @return
	 * @throws Exception
	 */
	private static byte[] translate(byte[] val, int mode, byte[] iv)
			throws Exception {
		if (mode != Cipher.ENCRYPT_MODE && mode != Cipher.DECRYPT_MODE)
			throw new IllegalArgumentException(
					"Encryption invalid. Mode should be either Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE");
		SecretKeySpec skeySpec = new SecretKeySpec(getRawKey(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(mode, skeySpec, ivSpec);
		byte[] encrypted = cipher.doFinal(val);
		return encrypted;
	}

	public static String getIv() throws NoSuchAlgorithmException, NoSuchProviderException{
		return new String(Hex.encodeHex(generateIv()));
	}
	
	public static String encrypt(String clear, String iv) throws Exception {
		byte[] test = translate(clear.getBytes(), Cipher.ENCRYPT_MODE,
				Hex.decodeHex(iv.toCharArray()));
		return new String(Hex.encodeHex(test));
	}

	public static String decrypt(String encrypted, String iv) throws Exception {
		return new String(translate(Hex.decodeHex(encrypted.toCharArray()),
				Cipher.DECRYPT_MODE, Hex.decodeHex(iv.toCharArray())));
	}

	private static byte[] generateIv() throws NoSuchAlgorithmException,
			NoSuchProviderException {
		SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
		byte[] iv = new byte[IV_LENGTH];
		random.nextBytes(iv);
		return iv;
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public String toHex(String hex) {

		StringBuilder sb = new StringBuilder();
		StringBuilder temp = new StringBuilder();

		// 49204c6f7665204a617661 split into two characters 49, 20, 4c...
		for (int i = 0; i < hex.length() - 1; i += 2) {

			// grab the hex in pairs
			String output = hex.substring(i, (i + 2));
			// convert hex to decimal
			int decimal = Integer.parseInt(output, 16);
			// convert the decimal to character
			sb.append((char) decimal);

			temp.append(decimal);
		}
		System.out.println("Decimal : " + temp.toString());

		return sb.toString();
	}
}
