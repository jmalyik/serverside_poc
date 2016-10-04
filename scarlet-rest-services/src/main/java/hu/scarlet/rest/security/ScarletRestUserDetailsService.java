package hu.scarlet.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hu.scarlet.pers.ex.UserNotFoundException;
import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;

/**
 * 
 * @author jmalyik
 * TODO: check whether the fix true valued user flags should be configurable or not
 */
@Service
public class ScarletRestUserDetailsService implements UserDetailsService {
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
		User u = userService.getUserByEmailAddress(emailAddress);
		if(u != null){
			String role = u.isCompany() ? User.ROLE_COMPANY_USER : User.ROLE_USER;
			return new org.springframework.security.core.userdetails.User(u.getEmailAddress(), 
					u.getPassword(), 
					u.isEnabled(), 
					true, // accountNonExpired
					true, // credentialsNonExpired, 
					true, // accountNonLocked, 
					AuthorityUtils.createAuthorityList(role));
		}else{
			throw new UserNotFoundException(emailAddress);
		}
	}
}
