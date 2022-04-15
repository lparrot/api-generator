package fr.lauparr.apigenerator.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.primefaces.PrimeFaces;

import javax.faces.application.FacesMessage;

public abstract class UtilsPrimefaces {

	public static void sendGrowlMessage(FacesMessage.Severity severity, String text) {
		String message = JsonNodeFactory.instance.objectNode()
			.put("severity", severity.toString().toLowerCase())
			.put("detail", text)
			.toString();

		PrimeFaces.current().executeScript(String.format("PF('growl').renderMessage(%s)", message));
	}

}
