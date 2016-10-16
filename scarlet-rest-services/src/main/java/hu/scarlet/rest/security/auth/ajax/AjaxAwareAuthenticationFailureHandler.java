package hu.scarlet.rest.security.auth.ajax;


import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import hu.scarlet.rest.config.RestMessages;
import hu.scarlet.rest.security.AuthMethodNotSupportedException;
import hu.scarlet.rest.security.JwtExpiredTokenException;
import hu.scarlet.rest.util.ErrorCode;
import hu.scarlet.rest.util.ErrorResponse;

/**
 * 
 * @author vladimir.stankovic
 *
 *         Aug 3, 2016
 */
@Component
public class AjaxAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(AjaxAwareAuthenticationFailureHandler.class);
	private final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private RestMessages restMessages;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {
		logger.debug("Authentication failed", e);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		Locale locale = LocaleContextHolder.getLocale();
			
		if (e instanceof BadCredentialsException) {
			mapper.writeValue(response.getWriter(), ErrorResponse.of("Invalid username or password",
					ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof JwtExpiredTokenException) {
			mapper.writeValue(response.getWriter(),
					ErrorResponse.of(restMessages.getBundle().getMessage("tokenExpired", null, locale),
							ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
		} else if (e instanceof AuthMethodNotSupportedException) {
			mapper.writeValue(response.getWriter(),
					ErrorResponse.of(e.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
		}

		mapper.writeValue(response.getWriter(),
				ErrorResponse.of(restMessages.getBundle().getMessage("authenticationFailed", null, locale),
						ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
	}
}
