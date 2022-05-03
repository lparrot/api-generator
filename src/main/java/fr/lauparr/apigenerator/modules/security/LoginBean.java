package fr.lauparr.apigenerator.modules.security;

import fr.lauparr.apigenerator.config.SecurityConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletException;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@Controller
@RequestScoped
public class LoginBean {

	@Autowired private SecuritySrv securitySrv;

	@Getter @Setter @NotBlank private String username;

	@Getter @Setter @NotBlank private String password;

	@Getter @Setter private LoginMessageDto message;

	@PostConstruct
	public void postConstruct() {
		String code = Faces.getRequestParameter("code");
		if (StringUtils.isNotBlank(code)) {
			LoginCodeErreurEnum codeErreur = EnumUtils.getEnum(LoginCodeErreurEnum.class, code.toUpperCase());
			if (codeErreur != null) {
				switch (codeErreur) {
					case ERREUR:
						Exception error = Faces.getSessionAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
						if (error != null) {
							message = new LoginMessageDto("error", error.getMessage());
						}
						break;
					case DECONNEXION:
						message = new LoginMessageDto("warn", "Vous avez été déconnecté.");
						break;
					case SESSION_EXPIREE:
						message = new LoginMessageDto("warn", "Session expirée.");
						break;
					default:
						break;
				}
			}
		}
	}

	public String handleLogin() throws ServletException, IOException {
		this.doForward(SecurityConfig.SPRING_SECURITY_CHECK_USER);
		return null;
	}

	public String handleLogout() throws ServletException, IOException {
		this.doForward(SecurityConfig.SPRING_SECURITY_LOGOUT_USER);
		return null;
	}

	private void doForward(String url) throws ServletException, IOException {
		Faces.getRequest().getRequestDispatcher(url).forward(Faces.getRequest(), Faces.getResponse());
		Faces.responseComplete();
	}

}
