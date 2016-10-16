package hu.scarlet.rest.security.auth.ajax;

/**
 * Model intended to be used for AJAX based authentication.
 * 
 * @author vladimir.stankovic
 *
 *         Aug 3, 2016
 */

public class SignUpRequest {
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String password;
	private boolean company = false;

	public SignUpRequest() {
	}

	public SignUpRequest(String emailAddress, String password, boolean company) {
		this.emailAddress = emailAddress;
		this.password = password;
		this.company = company;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public boolean isCompany() {
		return company;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
