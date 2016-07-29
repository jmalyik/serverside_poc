package hu.scarlet.pers.ex;

/**
 * 
 * @author jmalyik
 *
 */
public class UserNotFoundException extends RuntimeException {

	private final String emailAddress; 
	
	public UserNotFoundException(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	private static final long serialVersionUID = 7518780368178500084L;

}
