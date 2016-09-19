package de.unistuttgart.ipvs.as.flexmash.webservices.operations;

import javax.jws.*;

@WebService(name = "Filter")
/**
 * web service that joins two data sources
 */
public class Filter {

	@WebMethod(operationName = "filter")
	@WebResult(name = "key")
	/**
	 * joins two data sources
	 * 
	 * @param key
	 * 			data to be filtered
	 * @param criteria
	 * 			the filter criteria
	 * @return the filtered data
	 */
	public String filter(@WebParam(name = "key") String key1, @WebParam(name = "criteria") String criteria) {

		return null;
	}
}
