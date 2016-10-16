package hu.scarlet.pers.mongo.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import hu.scarlet.pers.model.User;

/**
 * 
 * @author jmalyik
 *
 */
@Document(collection="Users")
public class MongoUser implements User {
	@Id
	private String id;
	private String firstName;
	private String lastName;
	@Indexed(unique=true)
	private String emailAddress;
	private String password;
	private boolean enabled;
	private boolean deleted;
	private boolean company;
	private boolean locked;
	private List<String> roles;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String getEmailAddress() {
		return emailAddress;
	}

	@Override
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;	
	}

	@Override
	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Override
	public boolean isCompany() {
		return company;
	}

	@Override
	public void setCompany(boolean company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "MongoUser [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailAddress="
				+ emailAddress + ", password=" + password + ", enabled=" + enabled + ", deleted=" + deleted
				+ ", company=" + company + "]";
	}

	@Override
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
