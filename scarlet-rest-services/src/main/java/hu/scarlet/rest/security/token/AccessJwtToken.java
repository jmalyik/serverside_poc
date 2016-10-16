package hu.scarlet.rest.security.token;

import io.jsonwebtoken.Claims;

public class AccessJwtToken implements JwtToken {
	private final String rawToken;

	private Claims claims;

	protected AccessJwtToken(final String token, Claims claims) {
		this.rawToken = token;
		this.claims = claims;
	}

	public String getToken() {
		return this.rawToken;
	}

	public Claims getClaims() {
		return claims;
	}

}
