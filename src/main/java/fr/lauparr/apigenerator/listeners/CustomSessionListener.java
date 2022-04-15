package fr.lauparr.apigenerator.listeners;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Component
@WebListener
public class CustomSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		//		event.getSession().setMaxInactiveInterval(1);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		SecurityContextImpl securityContext = (SecurityContextImpl) event.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

		if (securityContext != null) {
			// Si l'utilisateur était connecté
		}
	}
}
