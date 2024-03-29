package com.nvn.demo.restfulservices.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AccountNotFoundException(String message){
		super(message);
	}
}
