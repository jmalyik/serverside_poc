package hu.scarlet.rest.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;
import hu.scarlet.rest.config.SecurityConfig;
import hu.scarlet.rest.security.token.JwtToken;
import hu.scarlet.rest.security.token.JwtTokenFactory;
import hu.scarlet.rest.security.token.RawAccessJwtToken;
import hu.scarlet.rest.security.token.RefreshToken;
import hu.scarlet.rest.security.token.TokenExtractor;
import hu.scarlet.rest.security.verifier.TokenVerifier;

/**
 * RefreshTokenEndpoint
 * 
 * @author vladimir.stankovic
 *
 *         Aug 17, 2016
 */
@RestController
public class RefreshTokenEndpoint {
	@Autowired
	private JwtTokenFactory tokenFactory;
	@Autowired
	private JwtSettings jwtSettings;
	@Autowired
	private UserService userService;
	@Autowired
	private TokenVerifier tokenVerifier;
	@Autowired
	@Qualifier("jwtHeaderTokenExtractor")
	private TokenExtractor tokenExtractor;

	@RequestMapping(value = "/api/auth/token", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody JwtToken refreshToken(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String tokenPayload = tokenExtractor.extract(request.getHeader(SecurityConfig.JWT_TOKEN_HEADER_PARAM));

		RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenPayload);
		RefreshToken refreshToken = RefreshToken.create(rawToken, jwtSettings.getTokenSigningKey())
				.orElseThrow(() -> new InvalidJwtToken());

		String jti = refreshToken.getJti();
		if (!tokenVerifier.verify(jti)) {
			throw new InvalidJwtToken();
		}

		String subject = refreshToken.getSubject();
		User user = userService.getUserByEmailAddress(subject);
		if (user == null) {
			new UsernameNotFoundException("User not found: " + subject);
		}

		if (user.getRoles() == null) {
			throw new InsufficientAuthenticationException("User has no roles assigned");
		}
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(authority -> new SimpleGrantedAuthority(authority))
				.collect(Collectors.toList());

		UserContext userContext = UserContext.create(user.getEmailAddress(), authorities);

		return tokenFactory.createAccessJwtToken(userContext);
	}
}
