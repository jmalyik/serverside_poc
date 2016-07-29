package hu.scarlet.pers.model;

import java.util.List;

/**
 * 
 * @author jmalyik
 *
 */
public interface UserService {
	List<? extends User> findAll();

	User createNew();

	User save(User user);
	
	void deleteUserById(String id);
	
	void deleteUserByEmailAddress(String emailAddress);
	
	User update(User user);

	User getUserByEmailAddress(String emailAddress);
}
