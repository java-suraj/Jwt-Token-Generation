package com.jwt.config;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.payload.ApiResponse;
import com.jwt.payload.Audience;
import com.jwt.payload.EncryptPropertiesDTO;
import com.jwt.payload.JWTTokenPayload;
import com.jwt.payload.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtTokenProvider {

	private static final Integer JWT_EXPIRATIONIN_MS = 86400000; // 24 hours in milliseconds

	@Autowired
	private EncryptPropertiesDTO secretKeys; // Secret key for JWT signing

	private static final String TOKEN_VALIDATION_ERROR_MSG = "Token validation failed";

//	@Autowired
//	private CacheService cacheService;
//
//	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Generates a JWT token with provided payload and audience/role
	 */
	public String generateToken(JWTTokenPayload jwtTokenPayload, Audience audience, Role role) {
		log.info("Generating token for audience: {} and role: {}", audience, role);

		Long expiredInHours = jwtTokenPayload.getExpiredInHours();
		String issuer = jwtTokenPayload.getIssuer() != null ? jwtTokenPayload.getIssuer() : "JWTDemoIssuer";
		String subject = jwtTokenPayload.getSubject() != null ? jwtTokenPayload.getSubject() : "JWTDemoSubject";
		Date issuedAt = jwtTokenPayload.getIssuedAt() != null ? jwtTokenPayload.getIssuedAt() : new Date();
		Date expiration = (expiredInHours != null && expiredInHours > 0)
				? new Date(issuedAt.getTime() + (expiredInHours * 60 * 60 * 1000)) // Expiration in hours
				: new Date(issuedAt.getTime() + JWT_EXPIRATIONIN_MS); // Default expiration is 24 hours

		String uname = jwtTokenPayload.getUserName();
		Map<String, Object> customClaim = jwtTokenPayload.getCustomClaim();

		log.info("Generating token.");

		//@formatter:off
		String jwtToken  =  Jwts.builder()
            .claim("sub", subject) 
            .claim("iat", issuedAt.getTime() / 1000) 
            .setExpiration(expiration)
            .setIssuer(issuer)
            .claim("uname", uname)
            .claim("audience", audience.getAudienceName())
            .claim("role", role.getRoleName())
            .addClaims(customClaim)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256) 
            .compact(); 
		//@formatter:on

		// Store the token in cache
//		CacheDataPayload cacheDataPayload = new CacheDataPayload();
//		cacheDataPayload.setUserName(uname);
//		cacheDataPayload.setToken(jwtToken);
//		cacheDataPayload.setAudience(audience.getAudienceName());
//		cacheDataPayload.setRole(role.getRoleName());
//		cacheDataPayload.setValidTill(expiration.toString());
//		cacheDataPayload.setIsValid(true);
//		try {
//			cacheService.put(GlobalCacheConstant.JWT_TOKEN_CACHE, uname,
//					objectMapper.writeValueAsString(cacheDataPayload));
//		} catch (JsonProcessingException e) {
//			log.error("Error while storing token in cache: {}", e.getMessage());
//		}
		return jwtToken;

	}

	/**
	 * Get a specific claim from the JWT token
	 */
	public String getClaimFromJWT(String token, String claimType) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
		return claims.get(claimType, String.class);
	}

	/**
	 * Returns the signing key used for JWT token generation
	 */
	private Key getSigningKey() {
		log.debug("Secret key: {}", secretKeys.getSecretKey());
		String secret = secretKeys.getSecretKey();
		byte[] secretBytes = secret.getBytes();
		return Keys.hmacShaKeyFor(secretBytes); // Create HMAC key from the secret
	}

	/**
	 * Extracts the username from the JWT token
	 */
	public String getUserNameFromJWT(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
		return claims.get("uname", String.class);
	}

	/**
	 * Validates the JWT token and returns a response with status
	 */
	public ApiResponse validateJWTToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token); // Validate token
			return new ApiResponse(true, "Token validation success", HttpStatus.SC_OK, null, Collections.emptyList());
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token: {}", ex.getMessage());
			return new ApiResponse(false, TOKEN_VALIDATION_ERROR_MSG, HttpStatus.SC_UNAUTHORIZED, null,
					Collections.singletonList("Token expired"));
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token: {}", ex.getMessage());
			return new ApiResponse(false, TOKEN_VALIDATION_ERROR_MSG, HttpStatus.SC_UNAUTHORIZED, null,
					Collections.singletonList("Token unsupported"));
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token: {}", ex.getMessage());
			return new ApiResponse(false, TOKEN_VALIDATION_ERROR_MSG, HttpStatus.SC_UNAUTHORIZED, null,
					Collections.singletonList("Token malformed"));
		} catch (SignatureException ex) {
			log.error("JWT signature does not match: {}", ex.getMessage());
			return new ApiResponse(false, TOKEN_VALIDATION_ERROR_MSG, HttpStatus.SC_UNAUTHORIZED, null,
					Collections.singletonList("Token signature mismatch"));
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty: {}", ex.getMessage());
			return new ApiResponse(false, TOKEN_VALIDATION_ERROR_MSG, HttpStatus.SC_UNAUTHORIZED, null,
					Collections.singletonList("Token claims empty"));
		}
	}

	/**
	 * Validates the token and returns true if valid, false otherwise
	 */
	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken); // Validate token
			return true;
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token: {}", ex.getMessage());
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token: {}", ex.getMessage());
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token: {}", ex.getMessage());
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty: {}", ex.getMessage());
		} catch (SignatureException ex) {
			log.error("JWT signature does not match: {}", ex.getMessage());
		}
		return false;
	}
}
