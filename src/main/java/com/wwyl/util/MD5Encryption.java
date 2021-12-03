package com.wwyl.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Fyun Li
 */
public final class MD5Encryption {

	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// IGNORE
		}
	}

	private static MessageDigest md;

	public static String byte2hex(byte[] b) {
		StringBuffer stringBuffer = new StringBuffer();
		for (int n = 0; n < b.length; n++) {
			stringBuffer.append(byteToHex(b[n]));
		}
		return stringBuffer.toString();
	}

	private static String byteToHex(byte b) {
		String stmp = (java.lang.Integer.toHexString(b & 0XFF));
		return stmp.length() == 1 ? "0" + stmp : stmp;
	}

	public static String encode(String origin) {
		return byte2hex(md.digest(origin.getBytes()));
	}

	private MD5Encryption() {
	}

}

// :~
