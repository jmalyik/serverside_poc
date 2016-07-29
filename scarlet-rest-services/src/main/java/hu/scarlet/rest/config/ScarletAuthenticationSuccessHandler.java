package hu.scarlet.rest.config;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 
 * @author jmalyik
 *
 */
public class ScarletAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static Logger logger = LoggerFactory.getLogger(ScarletAuthenticationSuccessHandler.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities()); 
		logger.debug("Principal: {}, current roles: {}", authentication.getPrincipal(), roles);
       if (roles.contains("ROLE_COMPANY_USER")) {
    	   // go to company user page
           response.sendRedirect("/company");
       }else if(roles.contains("ROLE_ADMIN")){
    	   // go to admin page
    	   response.sendRedirect("/admin");
       }else {
    	   // go to normal user page
    	   response.sendRedirect("/user");
       }
	}

}
