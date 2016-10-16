package hu.scarlet.rest.security.auth.ajax;


import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import hu.scarlet.rest.config.RestMessages;
import hu.scarlet.rest.security.AuthMethodNotSupportedException;
import hu.scarlet.rest.util.WebUtil;

/**
 * AjaxLoginProcessingFilter
 * 
 * @author vladimir.stankovic
 *
 *         Aug 3, 2016
 */
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
	private static Logger logger = LoggerFactory.getLogger(AjaxLoginProcessingFilter.class);

	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private RestMessages restMessages;

	public AjaxLoginProcessingFilter(String defaultProcessUrl, AuthenticationSuccessHandler successHandler,
			AuthenticationFailureHandler failureHandler) {
		super(defaultProcessUrl);
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		Locale locale = LocaleContextHolder.getLocale();
		if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Authentication method not supported. Request method: " + request.getMethod());
			}
			throw new AuthMethodNotSupportedException(restMessages.getBundle().getMessage("exceptions.authMethodNotSupported", null, locale ));
		}
		
		LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);

		logger.debug("Trying to login: {}", loginRequest);

		if (StringUtils.isBlank(loginRequest.getEmailAddress()) || StringUtils.isBlank(loginRequest.getPassword())) {
			throw new AuthenticationServiceException(
					restMessages.getBundle().getMessage("exceptions.emailAddressOrPasswordNotProvided", null, locale));
		}
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				loginRequest.getEmailAddress(),
				loginRequest.getPassword());

		return this.getAuthenticationManager().authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		successHandler.onAuthenticationSuccess(request, response, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		failureHandler.onAuthenticationFailure(request, response, failed);
	}
}
