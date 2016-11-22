package de.unistuttgart.ipvs.as.flexmash.webservices.twitter;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "TwitterFilter")
public class TwitterFilter {

	@WebMethod(operationName = "filterData")
	@WebResult(name = "key")
	public String filterData(@WebParam(name = "key") String key, @WebParam(name = "criteria") String criteria) {
		
		System.out.println("TwitterFilter SERVICE REACHED");
		System.out.print("--------------------------------------------------------------------------");
		
		//
		return "";
	}

}
