package fr.lauparr.apigenerator;

import fr.lauparr.apigenerator.dao.UserRepository;
import fr.lauparr.apigenerator.entities.Profile;
import fr.lauparr.apigenerator.entities.Role;
import fr.lauparr.apigenerator.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class DatabaseInitializer implements CommandLineRunner {

	@Autowired private UserRepository userRepository;
	@Autowired private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		if (userRepository.count() > 0) {
			return;
		}

		Profile profilAdministrateur = Profile.builder()
			.label("Administrateur")
			.role(Role.ROLE_ADMIN)
			.build();

		Profile profilInvite = Profile.builder()
			.label("Invité")
			.role(Role.ROLE_USER)
			.build();

		User root = User.builder()
			.username("root")
			.firstname("Laurent")
			.lastname("Parrot")
			.password(passwordEncoder.encode("123"))
			.profile(profilAdministrateur)
			.build();

		User user = User.builder()
			.username("user")
			.firstname("Laurent")
			.lastname("Parrot")
			.password(passwordEncoder.encode("123"))
			.profile(profilInvite)
			.build();

		userRepository.saveAll(Arrays.asList(root, user));
	}
}
