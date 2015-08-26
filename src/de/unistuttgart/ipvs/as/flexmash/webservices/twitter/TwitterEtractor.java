package de.unistuttgart.ipvs.as.flexmash.webservices.twitter;

import javax.jws.*;


@WebService(name = "TwitterExtractor")
public class TwitterEtractor {

	@WebMethod(operationName = "extract")
	@WebResult(name = "key")
	public String extract(@WebParam(name = "key") String key) {
		return "";
	}
}
