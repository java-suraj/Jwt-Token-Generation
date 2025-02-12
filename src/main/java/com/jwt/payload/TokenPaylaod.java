package com.jwt.payload;

import lombok.Data;

@Data
public class TokenPaylaod {

	private String tokenType;
	private String accessToken;
}
