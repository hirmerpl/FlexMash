package de.unistuttgart.ipvs.as.flexmash.webservices.operations;

import javax.jws.*;

@WebService(name = "MergeSQL")
/**
 * web service that joins two data sources
 */
public class MergeSQL {

	@WebMethod(operationName = "merge")
	@WebResult(name = "key")
	/**
	 * joins two data sources
	 * 
	 * @param key1
	 * 			first data source
	 * @param key2
	 * 			second data source
	 * @param criteria
	 * 			the join criteria
	 * @return the joined data
	 */
	public String mergeSQL(@WebParam(name = "key1") String key1, @WebParam(name = "key2") String key2, @WebParam(name = "criteria") String criteria) {
		//TODO
		return null;
	}
}
