package com.petgang.commons.security;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

public class MD5 {

	public static String encode(String content) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		digest.update(content.getBytes());
		byte[] encoded = digest.digest();
		return Hex.encodeHexString(encoded);
	}
}
