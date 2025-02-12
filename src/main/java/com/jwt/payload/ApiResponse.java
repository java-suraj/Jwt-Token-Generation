package com.jwt.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@Builder
public class ApiResponse {
	private Boolean success;
	private String message;
	private Integer statusCode;
	private Object data;
	private List<String> errors;

}