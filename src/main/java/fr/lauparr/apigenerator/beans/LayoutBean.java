package fr.lauparr.apigenerator.beans;

import fr.lauparr.apigenerator.services.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.faces.view.ViewScoped;

@Controller
@ViewScoped
public class LayoutBean {

	@Autowired private LayoutService layoutService;

}
