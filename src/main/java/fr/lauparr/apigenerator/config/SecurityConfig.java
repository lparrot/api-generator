package fr.lauparr.apigenerator.config;

import fr.lauparr.apigenerator.modules.security.CustomAuthenticationProvider;
import fr.lauparr.apigenerator.modules.security.CustomAuthenticationSuccessHandler;
import fr.lauparr.apigenerator.modules.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.time.Duration;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${info.app.security.rememberme-token}")
	private String securityRememberMeToken;
	@Value("${info.app.security.rememberme-token-validity}")
	private Duration securityRememberMeTokenValidity;

	public static final String LOGIN_PAGE = "/login.xhtml";
	public static final String INDEX_PAGE = "/index.xhtml";
	public static final String SPRING_SECURITY_CHECK_USER = "/signin";
	public static final String SPRING_SECURITY_LOGOUT_USER = "/signout";
	public static final String SPRING_SECURITY_SWITCH_USER = "/signin/impersonate";
	public static final String SPRING_SECURITY_EXIT_USER = "/signout/impersonate";

	@Autowired private CustomUserDetailsService userDetailsService;
	@Autowired private CustomAuthenticationProvider authenticationProvider;
	@Autowired private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());

		http.authorizeRequests().antMatchers(LOGIN_PAGE).authenticated();

		http.authorizeRequests().antMatchers("/resources/**").permitAll();
		http.authorizeRequests().antMatchers("/javax.faces.resources/**").permitAll();
		http.authorizeRequests().antMatchers("/register.xhtml**").permitAll();
		http.authorizeRequests().antMatchers(SPRING_SECURITY_CHECK_USER).permitAll();
		http.authorizeRequests().antMatchers(SPRING_SECURITY_LOGOUT_USER).permitAll();
		http.authorizeRequests().antMatchers(SPRING_SECURITY_SWITCH_USER).permitAll();
		http.authorizeRequests().antMatchers(SPRING_SECURITY_EXIT_USER).permitAll();

		http.authorizeRequests().antMatchers("/profile.xhtml").authenticated();

		http.formLogin()
			.loginPage(LOGIN_PAGE)
			.loginProcessingUrl(SPRING_SECURITY_CHECK_USER)
			.defaultSuccessUrl(INDEX_PAGE)
			.successHandler(this.authenticationSuccessHandler)
			.usernameParameter("form_email")
			.passwordParameter("form_password")
			.permitAll()
			.failureUrl(LOGIN_PAGE + "?code=erreur");

		http.logout()
			.invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutUrl(SPRING_SECURITY_LOGOUT_USER)
			.logoutSuccessUrl(LOGIN_PAGE + "?code=deconnexion")
			.deleteCookies("JSESSIONID")
			.permitAll();

		http.rememberMe()
			.rememberMeParameter("_spring_security_remember_me_input")
			.key(securityRememberMeToken)
			.tokenValiditySeconds(((Long) securityRememberMeTokenValidity.getSeconds()).intValue())
			.useSecureCookie(true)
			.userDetailsService(userDetailsService);

		http.sessionManagement()
			.maximumSessions(1)
			.sessionRegistry(this.sessionRegistry())
			.expiredUrl(LOGIN_PAGE + "?code=session_expiree");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(this.authenticationProvider);
	}

	@Bean
	public SwitchUserFilter switchUserFilter() {
		SwitchUserFilter switchUserFilter = new SwitchUserFilter();
		switchUserFilter.setUserDetailsService(this.userDetailsService);
		switchUserFilter.setTargetUrl(LOGIN_PAGE);
		return switchUserFilter;
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
}
