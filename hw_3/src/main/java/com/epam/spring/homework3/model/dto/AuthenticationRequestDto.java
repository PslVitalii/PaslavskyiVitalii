package com.epam.spring.homework3.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class AuthenticationRequestDto {

	@Email
	@NotBlank
	private String email;

	@NotBlank
	private String password;
}
