package hu.scarlet.pers.model;

/**
 * 
 * @author jmalyik
 *
 */
public interface User {
	
	public static final String ROLE_COMPANY_USER = "COMPANY_USER";
	public static final String ROLE_USER = "USER";
	public static final String ROLE_ADMIN = "ADMIN";
	
	public String getId();
	
	public void setId(String id);
	
	public String getFirstName();
	
	public void setFirstName(String firstName);
	
	public String getLastName();
	
	public void setLastName(String lastName);
	
	public String getEmailAddress();
	
	public void setEmailAddress(String emailAddress);
	
	public String getPassword();
	
	public void setPassword(String password);
	
	public boolean isEnabled();
	
	public void setEnabled(boolean enabled);
	
	public boolean isDeleted();
	
	public void setDeleted(boolean deleted);
	
	public boolean isCompany();
	
	public void setCompany(boolean company);
}
