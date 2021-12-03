package com.wwyl.util;

import java.util.Random;

/**
 * @author Fyun Li
 */
public final class RandomGenerator {

	private RandomGenerator() {

	}

	public static String randomPassword() {
		return randomString(6, true);
	}

	public static String randomString(int length) {
		return randomString(length, true);
	}

	public static String randomString(int length, boolean includeNumbers) {
		StringBuffer b = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			if (includeNumbers) {
				b.append(randomCharacter());
			} else {
				b.append(randomAlpha());
			}
		}
		return b.toString();
	}

	public static String randomNumber(int length) {
		StringBuffer b = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			b.append(randomDigit());
		}
		return b.toString();
	}

	public static char randomCharacter() {
		int i = new Random().nextInt(3);
		if (i < 2) {
			return randomAlpha();
		} else {
			return randomDigit();
		}
	}

	public static char randomAlpha() {
		int i = new Random().nextInt(52);
		if (i > 25) {
			return (char) ((97 + i) - 26);
		} else {
			return (char) (65 + i);
		}
	}

	public static char randomDigit() {
		int i = new Random().nextInt(10);
		return (char) (48 + i);
	}
}
