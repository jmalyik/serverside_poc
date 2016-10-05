package hu.scarlet.rest.security;

import org.springframework.security.core.AuthenticationException;

import hu.scarlet.rest.security.token.JwtToken;

public class JwtExpiredTokenException extends AuthenticationException {
	private static final long serialVersionUID = 5759563783327224864L;

	private JwtToken token;

	public JwtExpiredTokenException(String msg) {
		super(msg);
	}

	public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
		super(msg, t);
		this.token = token;
	}

	public String token() {
		return this.token.getToken();
	}

}
