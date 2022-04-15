package fr.lauparr.apigenerator.modules.security;

import fr.lauparr.apigenerator.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired @Lazy private PasswordEncoder passwordEncoder;
	@Autowired private CustomUserDetailsService userDetailsService;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		User user = (User) userDetails;

		String plainPassword = authentication.getCredentials().toString();

		if (!this.passwordEncoder.matches(plainPassword, user.getPassword())) {
			throw new BadCredentialsException("Nom d'utilisateur ou mot de passe incorrect");
		}
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		User user = (User) this.userDetailsService.loadUserByUsername(username);

		if (user == null) {
			throw new AuthenticationServiceException("Nom d'utilisateur ou mot de passe incorrect");
		}

		return user;
	}
}