package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Query executor API.<br>
 * It uses an instance of {@link QueryExecutorService} to generate a response, containing the result of SELECT query or
 * an error message, according to the latter and requested output format.
 * 
 * @author OOL
 */
@Path("endpoint")
public class QueryExecutor {

	// Service for real query execution
	private final QueryExecutorService queryExecutorService;

	/**
	 * Constructs a {@link QueryExecutor} with specified parameters (only for test use)
	 *
	 * @param queryExecutorService
	 *            Service for real query execution
	 */
	QueryExecutor(final QueryExecutorService queryExecutorService) {
		this.queryExecutorService = queryExecutorService;
	}

	/**
	 * @param query
	 * @param defaultGraphUri
	 * @param namedGraphUri
	 *
	 * @return
	 */
	@GET
	@Path("execQuery")
	public Response executeQueryViaGET(@HeaderParam("Accept") final String acceptedFormat,
			@QueryParam("query") final String query,
			@QueryParam("default-graph-uri") final String defaultGraphUri,
			@QueryParam("named-graph-uri") final String namedGraphUri) {
		throw new RuntimeException("Unimplemented yet!");

		// final Response response = Response.ok("GET: " + query).build();
		// final MultivaluedMap<String, Object> headers = response.getHeaders();
		// headers.putSingle("Content-type", acceptedFormat);
	}

	/**
	 * @param request
	 * @param query
	 *
	 * @return
	 */
	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Path("execQuery")
	public Response executeUrlEncodedQueryViaPOST(@HeaderParam("Accept") final String acceptedFormat,
			final String query) {
		throw new RuntimeException("Unimplemented yet!");
	}

	/**
	 * @param request
	 * @param query
	 * @param defaultGraphUri
	 * @param namedGraphUri
	 *
	 * @return
	 */
	@POST
	@Consumes("application/sparql-query")
	@Path("execQuery")
	public Response executeQueryViaPOST(@HeaderParam("Accept") final String acceptedFormat,
			final String query,
			@QueryParam("default-graph-uri") final String defaultGraphUri,
			@QueryParam("named-graph-uri") final String namedGraphUri) {
		throw new RuntimeException("Unimplemented yet!");
	}
}
