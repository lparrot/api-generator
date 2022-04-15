package fr.lauparr.apigenerator.modules.account;

import fr.lauparr.apigenerator.dao.UserRepository;
import fr.lauparr.apigenerator.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileSrv {

	@Autowired private UserRepository userRepository;

	public User save(User user) {
		return userRepository.save(user);
	}

}
