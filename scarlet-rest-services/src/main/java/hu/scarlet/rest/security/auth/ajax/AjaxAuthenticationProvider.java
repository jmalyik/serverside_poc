package hu.scarlet.rest.security.auth.ajax;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;
import hu.scarlet.rest.config.RestMessages;
import hu.scarlet.rest.security.UserContext;

/**
 * 
 * @author vladimir.stankovic
 *
 *         Aug 3, 2016
 */
@Component
public class AjaxAuthenticationProvider implements AuthenticationProvider {

	private final Logger logger = LoggerFactory.getLogger(AjaxAuthenticationProvider.class);

	private final BCryptPasswordEncoder encoder;
	private final UserService userService;
	@Autowired
	private RestMessages restMessages;

	@Autowired
	public AjaxAuthenticationProvider(final UserService userService, final BCryptPasswordEncoder encoder) {
		this.userService = userService;
		this.encoder = encoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");

		String emailAddress = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		Locale locale = LocaleContextHolder.getLocale();

		User user = userService.getUserByEmailAddress(emailAddress);
		if (user == null) {
			logger.warn("USer not found: {}", emailAddress);
			new UsernameNotFoundException(restMessages.getBundle().getMessage("exceptions.userNotFound",
					restMessages.asOArr(emailAddress), locale));
		} else {
			logger.debug("User {} found.", emailAddress);
		}

		if (!encoder.matches(password, user.getPassword())) {
			logger.warn("Username or password not valid");
			throw new BadCredentialsException(
					restMessages.getBundle().getMessage("exceptions.emailAddressOrPasswordNotValid", null, locale));
		} else {
			logger.debug("Password matches.");
		}

		if (user.getRoles() == null){
			logger.warn("User has no roles assigned");
			throw new InsufficientAuthenticationException(
					restMessages.getBundle().getMessage("exceptions.userHasNoRoles", null, locale));
		} else {
			logger.debug("User {} has {} roles", emailAddress, user.getRoles());
		}

		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(authority -> new SimpleGrantedAuthority(authority))
				.collect(Collectors.toList());

		UserContext userContext = UserContext.create(user.getEmailAddress(), authorities);

		return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
