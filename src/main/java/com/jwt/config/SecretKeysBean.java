package com.jwt.config;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class SecretKeysBean {

	private SaltGenerator saltGenerator = null;

	SecretKeysBean(SaltGenerator saltGenerator) {
		this.saltGenerator = saltGenerator;
	}

	@PostConstruct
	void generateEncryptProperties() {
		saltGenerator.generateEncryptProperties();
	}
}
