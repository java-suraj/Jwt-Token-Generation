package com.jwt.config;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jwt.payload.EncryptPropertiesDTO;

@Configuration
public class SaltGenerator {

	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int DEFAULT_LENGTH = 32; // Length for the secret key, salt, and JWT
	private static final int DEFAULT_PASSWORD_LENGTH = 22; // Length for the encryption password
	private static final int DEFAULT_IVP_LENGTH = 16; // Length for the Initialization Vector (IVP)

	public static String generateRandomString(int length) {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			int index = random.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}

		return sb.toString();
	}

	@Bean
	public EncryptPropertiesDTO generateEncryptProperties() {
		EncryptPropertiesDTO dto = new EncryptPropertiesDTO();
		dto.setSecretKey(generateRandomString(DEFAULT_LENGTH));
		dto.setSalt(generateRandomString(DEFAULT_LENGTH));
		dto.setJwt(generateRandomString(DEFAULT_LENGTH));
		dto.setIvp(generateRandomString(DEFAULT_IVP_LENGTH));
		dto.setPassword(generateRandomString(DEFAULT_PASSWORD_LENGTH));
		return dto;
	}
}
