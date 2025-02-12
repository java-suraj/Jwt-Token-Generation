package com.jwt.service;

import java.util.Collections;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.config.JwtTokenProvider;
import com.jwt.payload.ApiResponse;
import com.jwt.payload.Audience;
import com.jwt.payload.JWTTokenPayload;
import com.jwt.payload.Role;
import com.jwt.payload.TokenPaylaod;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JWTTokenService {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

//	@Autowired
//	private CacheService cacheService;
//
//	private ObjectMapper objectMapper = new ObjectMapper();

	public ApiResponse generateToken(JWTTokenPayload payload, Audience audience, Role role) {
		try {
			if (payload == null)
				return new ApiResponse(false, "Payload cannot be empty", HttpStatus.SC_BAD_REQUEST, null,
						Collections.emptyList());
			if (audience == null || !Audience.isValidAudience(audience))
				return new ApiResponse(false, "Audience cannot be empty", HttpStatus.SC_BAD_REQUEST, null,
						Collections.emptyList());
			if (role == null || !Role.isValidRole(role))
				return new ApiResponse(false, "Role cannot be empty", HttpStatus.SC_BAD_REQUEST, null,
						Collections.emptyList());
			String token = jwtTokenProvider.generateToken(payload, audience, role);
			if (token == null)
				return new ApiResponse(false, "Error while generating token", HttpStatus.SC_INTERNAL_SERVER_ERROR, null,
						Collections.emptyList());
			TokenPaylaod tokenPaylaod = new TokenPaylaod();
			tokenPaylaod.setAccessToken(token);
			tokenPaylaod.setTokenType("Bearer");

			return new ApiResponse(true, "Token generated successfully", HttpStatus.SC_OK, tokenPaylaod,
					Collections.emptyList());
		} catch (Exception e) {
			log.error("Error while generating token: {}", e.getMessage());
			return new ApiResponse(false, "Error while generating token", HttpStatus.SC_INTERNAL_SERVER_ERROR, null,
					Collections.emptyList());
		}
	}

	public ApiResponse validateToken(String token) {
		try {
			if (token == null || token.isEmpty())
				return new ApiResponse(false, "Token cannot be empty", HttpStatus.SC_BAD_REQUEST, null,
						Collections.emptyList());
			if (token.startsWith("Bearer "))
				token = token.substring(7, token.length());
			return jwtTokenProvider.validateJWTToken(token.trim());
		} catch (Exception e) {
			log.error("Error while validating token: {}", e.getMessage());
			return new ApiResponse(false, "Error while validating token", HttpStatus.SC_INTERNAL_SERVER_ERROR, null,
					Collections.emptyList());
		}
	}

//	public ApiResponse getCacheUserData(String uname) {
//		try {
//			if (uname == null || uname.isEmpty())
//				return new ApiResponse(false, "Username cannot be empty", HttpStatus.SC_BAD_REQUEST, null,
//						Collections.emptyList());
//			String cacheData = cacheService.get(GlobalCacheConstant.JWT_TOKEN_CACHE, uname);
//			if (cacheData == null)
//				return new ApiResponse(false, "No data found for user: " + uname, HttpStatus.SC_NOT_FOUND, null,
//						Collections.emptyList());
//			return new ApiResponse(true, "Data found for user: " + uname, HttpStatus.SC_OK,
//					objectMapper.readValue(cacheData, CacheDataPayload.class), Collections.emptyList());
//		} catch (Exception e) {
//			log.error("Error while fetching cache data: {}", e.getMessage());
//			return new ApiResponse(false, "Error while fetching cache data", HttpStatus.SC_INTERNAL_SERVER_ERROR, null,
//					Collections.emptyList());
//		}
//	}

}
