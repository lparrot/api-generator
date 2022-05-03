package fr.lauparr.apigenerator.modules.account;

import fr.lauparr.apigenerator.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import java.io.Serializable;

@Controller
@ViewScoped
public class ProfileBean implements Serializable {

	@Autowired private ProfileSrv profileSrv;

	@Getter @Setter private User utilisateur;

	@Getter @Setter private byte[] avatar;

	@PostConstruct
	public void postConstruct() {
		this.utilisateur = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		this.avatar = this.utilisateur.getAvatar();
	}

	public void save() {
		this.utilisateur.setAvatar(avatar);
		this.utilisateur = profileSrv.save(this.utilisateur);
		Messages.addGlobalInfo("Les informations ont bien été enregistrées.");
	}

	public void uploadAvatar(FileUploadEvent event) {
		avatar = event.getFile().getContent();
	}

}
