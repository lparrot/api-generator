package fr.lauparr.apigenerator.modules.security;

import fr.lauparr.apigenerator.entities.User;
import fr.lauparr.apigenerator.utils.UtilsRequest;
import org.omnifaces.util.Faces;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	public CustomAuthenticationSuccessHandler() {
		super();
		setUseReferer(true);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		User principal = (User) authentication.getPrincipal();
		System.out.println("Connexion de l'utilisateur " + principal.getUsername());
		Faces.setSessionAttribute("ip_address", UtilsRequest.getRemoteAddress(request));

		SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);

		if (savedRequest == null) {
			Faces.redirect("/index.xhtml");
			return;
		}
		super.onAuthenticationSuccess(request, response, authentication);
	}

}