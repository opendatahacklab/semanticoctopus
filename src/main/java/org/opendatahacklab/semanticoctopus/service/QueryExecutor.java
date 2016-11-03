package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

/**
 * Query executor API
 *
 * @author OOL
 */
@Path("endpoint")
public class QueryExecutor {

	@POST
	//@Consumes("application/json")
	@Consumes("text/plain")
	@Produces("text/plain")
	@Path("execQuery")
	public String executeQuery(@Context final Request request, final String query) {
		return "Query: " + query;
	}
}
