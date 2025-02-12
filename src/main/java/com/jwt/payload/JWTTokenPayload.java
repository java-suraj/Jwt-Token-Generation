package com.jwt.payload;

import java.util.Date;
import java.util.Map;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class JWTTokenPayload {

	private Long expiredInHours;

	private Date issuedAt;

	private String issuer;

	private String subject;

	@NotEmpty(message = "Username (uname) cannot be empty")
	private String userName;

	private Map<String, Object> customClaim;
}
