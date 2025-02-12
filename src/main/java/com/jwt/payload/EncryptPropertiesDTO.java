package com.jwt.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EncryptPropertiesDTO {

	private String secretKey;
	private String salt;
	private String jwt;
	private String ivp;
	private String password;
}
