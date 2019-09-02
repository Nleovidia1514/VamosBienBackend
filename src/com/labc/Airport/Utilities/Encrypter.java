package com.labc.Airport.Utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

public class Encrypter {
	private static Encrypter instance = new Encrypter();
	
	public String encrypt(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] hashbyte = md.digest(password.getBytes());
			String hash = DatatypeConverter.printHexBinary(hashbyte);
			return hash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Encrypter getInstance() {
		return instance;
	}

}
