package hu.scarlet.pers.mongo.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.scarlet.pers.model.User;
import hu.scarlet.pers.model.UserService;
import hu.scarlet.pers.mongo.model.MongoUser;
import hu.scarlet.pers.mongo.model.MongoUserMapper;
import hu.scarlet.pers.mongo.repositories.UserRepository;

@Service
public class MongoUserService implements UserService {
	
	private static Logger logger = LoggerFactory.getLogger(MongoUserService.class);
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public List<MongoUser> findAll() {
		return userRepository.findAll();
	}

	public User findById(String uuid){
		return userRepository.findOne(uuid);
	}

	@Override
	public User createNew() {
		return new MongoUser();
	}

	@Override
	public User save(User user) {
		logger.debug("Saving {}", user);
		if(user instanceof MongoUser){
			logger.debug("saving directly");
			return userRepository.insert((MongoUser) user);
		}else{
			logger.debug("saving after conversion to MongoUser");
			MongoUser mongoUser = MongoUserMapper.INSTANCE.convertToMongoUser(user);
			return userRepository.insert(mongoUser);
		}
	}

	@Override
	public void deleteUserById(String id) {
		userRepository.delete(id);
	}

	@Override
	public void deleteUserByEmailAddress(String emailAddress) {
		userRepository.deleteByEmailAddress(emailAddress);
	}

	@Override
	public User update(User user) {
		if(user instanceof MongoUser){
			logger.debug("saving directly");
			return userRepository.save((MongoUser) user);
		}else{
			logger.debug("saving after conversion to MongoUser");
			MongoUser mongoUser = MongoUserMapper.INSTANCE.convertToMongoUser(user);
			return userRepository.save(mongoUser);
		}
	}

	@Override
	public User getUserByEmailAddress(String emailAddress) {
		return userRepository.findByEmailAddress(emailAddress);
	}
}
