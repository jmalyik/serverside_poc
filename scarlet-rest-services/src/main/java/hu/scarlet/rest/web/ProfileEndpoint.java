package hu.scarlet.rest.web;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hu.scarlet.rest.config.SecurityConfig;
import hu.scarlet.rest.security.UserContext;
import hu.scarlet.rest.security.token.JwtAuthenticationToken;


/**
 * End-point for retrieving logged-in user details.
 * 
 * @author vladimir.stankovic
 *
 *         Aug 4, 2016
 */
@RestController("profileEndpoint")
public class ProfileEndpoint {
	@RequestMapping(value = SecurityConfig.PROFILE_ENTRY_POINT, method = RequestMethod.GET)
	public @ResponseBody UserContext get(JwtAuthenticationToken token) throws Exception {
		if (token != null) {
			return (UserContext) token.getPrincipal();
		} else {
			throw new Exception("Unable to read JWT token");
		}
	}
}
