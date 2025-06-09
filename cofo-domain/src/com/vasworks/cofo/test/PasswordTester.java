package com.vasworks.cofo.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PasswordTester {
	
	public static final Log LOG = LogFactory.getLog(PasswordTester.class);
	
	public static void main(String[] args) {
		PasswordTester tester = new PasswordTester();
		
		try {
			byte[] salt = tester.getSalt();
			
			File saltFile = new File("/home/odada/Documents/saltfile.dat");
			FileOutputStream fos = new FileOutputStream(saltFile);
			fos.write(salt);
			fos.close();
			LOG.info("Password: " + tester.hashPassword("passw0rd123", salt));
		} catch (NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String hashPassword(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {
			// Create MessageDigest instance for SHA-256
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			// Add password bytes to digest
			md.update(salt);
			// Get the hash's bytes
			byte[] bytes = md.digest(passwordToHash.getBytes());
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e);
		}
		return generatedPassword;
	}

	// Add salt
	private byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
		// Always use a SecureRandom generator
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		// Create array for salt
		byte[] salt = new byte[16];
		// Get a random salt
		sr.nextBytes(salt);
		// return salt
		return salt;
	}
}
