package fr.lauparr.apigenerator.modules.account;

import fr.lauparr.apigenerator.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Controller
@ViewScoped
public class ProfileBean implements Serializable {

	@Getter @Setter private User utilisateur;

	@PostConstruct
	public void postConstruct() {
		this.utilisateur = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
