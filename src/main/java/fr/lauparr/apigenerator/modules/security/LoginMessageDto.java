package fr.lauparr.apigenerator.modules.security;

import lombok.Data;

@Data
public class LoginMessageDto {

	private String severity;
	private String message;

	public LoginMessageDto(String severity, String message) {
		this.severity = severity;
		this.message = message;
	}
}
