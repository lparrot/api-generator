package fr.lauparr.apigenerator.utils;

import javax.servlet.http.HttpServletRequest;

public abstract class UtilsRequest {

	public static String getRemoteAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress != null) {
			// cares only about the first IP if there is a list
			ipAddress = ipAddress.replaceFirst(",.*", "");
		} else {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

}
