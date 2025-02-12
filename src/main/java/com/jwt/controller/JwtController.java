package com.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.payload.ApiResponse;
import com.jwt.payload.Audience;
import com.jwt.payload.JWTTokenPayload;
import com.jwt.payload.Role;
import com.jwt.service.JWTTokenService;

import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/jwt")
@Slf4j
public class JwtController {

	@Autowired
	private JWTTokenService jwtTokenService;

//	@Autowired
//	private CacheService cacheService;

	@PostMapping("/generateToken")
	public ApiResponse generateToken(@Valid @RequestBody JWTTokenPayload payload,
			@ApiParam(value = "Audience", allowableValues = "ADMIN, USER, MODERATOR, GUEST") @RequestParam Audience audience,
			@ApiParam(value = "Role", allowableValues = "ADMIN, USER, GUEST") @RequestParam Role role) {

		return jwtTokenService.generateToken(payload, audience, role);
	}

	@GetMapping("/hello")
	public String hello() {
		return "This is a JWT secured page";
	}

	@PostMapping("/validateToken")
	public ApiResponse validateJWTToken(@RequestParam String token) {
		return jwtTokenService.validateToken(token);
	}

//	@PostMapping("/getAllCacheToken/{uname}")
//	public ApiResponse getAllCacheToken(@PathVariable String uname) {
//		return jwtTokenService.getCacheUserData(uname);
//	}

}
