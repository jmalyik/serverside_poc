package hu.scarlet.rest.web;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import hu.scarlet.rest.security.UserContext;
import hu.scarlet.rest.security.token.JwtAuthenticationToken;


/**
 * End-point for retrieving logged-in user details.
 * 
 * @author vladimir.stankovic
 *
 *         Aug 4, 2016
 */
@RestController
public class ProfileEndpoint {
	@RequestMapping(value = "/api/me", method = RequestMethod.GET)
	public @ResponseBody UserContext get(JwtAuthenticationToken token) {
		return (UserContext) token.getPrincipal();
	}
}
