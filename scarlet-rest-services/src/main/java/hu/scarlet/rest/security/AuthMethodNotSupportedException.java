package hu.scarlet.rest.security;

import org.springframework.security.authentication.AuthenticationServiceException;

public class AuthMethodNotSupportedException extends AuthenticationServiceException {
	private static final long serialVersionUID = 3715043083010314496L;

	public AuthMethodNotSupportedException(String msg) {
		super(msg);
	}
}
