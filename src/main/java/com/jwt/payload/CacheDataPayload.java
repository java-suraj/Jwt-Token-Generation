package com.jwt.payload;

import lombok.Data;

@Data
public class CacheDataPayload {

	private String userName;

	private String token;

	private String audience;

	private String role;

	private String validTill;

	private Boolean isValid;

}
