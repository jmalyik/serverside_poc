package hu.scarlet.rest.security.auth.ajax;

/**
 * Model intended to be used for AJAX based authentication.
 * 
 * @author vladimir.stankovic
 *
 *         Aug 3, 2016
 */

public class LoginRequest {

	private String emailAddress;
	private String password;

	public LoginRequest() {

	}

	public LoginRequest(String emailAddress, String password) {
		this.emailAddress = emailAddress;
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "LoginRequest [emailAddress=" + emailAddress + ", password=" + password.hashCode() + " (hashcode)]";
	}
}
