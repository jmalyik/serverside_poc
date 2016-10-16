package hu.scarlet.rest.security.auth.ajax;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import hu.scarlet.rest.security.UserContext;
import hu.scarlet.rest.security.token.JwtToken;
import hu.scarlet.rest.security.token.JwtTokenFactory;

/**
 * AjaxAwareAuthenticationSuccessHandler
 * 
 * @author vladimir.stankovic
 *
 *         Aug 3, 2016
 */
@Component
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final Logger logger = LoggerFactory.getLogger(AjaxAwareAuthenticationSuccessHandler.class);

	private final ObjectMapper mapper = new ObjectMapper();
	private final JwtTokenFactory tokenFactory;

	@Autowired
	public AjaxAwareAuthenticationSuccessHandler(final JwtTokenFactory tokenFactory) {
		this.tokenFactory = tokenFactory;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserContext userContext = (UserContext) authentication.getPrincipal();
		logger.debug("Authentication was successful. Logged in user: {}", userContext.getUsername());

		JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
		JwtToken refreshToken = tokenFactory.createRefreshToken(userContext);

		Map<String, String> tokenMap = new HashMap<String, String>();
		tokenMap.put("token", accessToken.getToken());
		tokenMap.put("refreshToken", refreshToken.getToken());

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		mapper.writeValue(response.getWriter(), tokenMap);

		clearAuthenticationAttributes(request);
	}

	/**
	 * Removes temporary authentication-related data which may have been stored
	 * in the session during the authentication process..
	 * 
	 */
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return;
		}

		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
