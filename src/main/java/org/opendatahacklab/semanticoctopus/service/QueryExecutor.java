package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Query executor API.<br>
 * It uses an instance of {@link QueryExecutorServiceFactory} to generate a response, containing the result of SELECT
 * query or an error message, according to the latter and requested output format.
 *
 * @author OOL
 */
@Path("sparql")
public class QueryExecutor {

	// Factory building services for real query execution
	private final QueryExecutorServiceFactory queryExecutorServiceFactory;

	/**
	 * Constructs a {@link QueryExecutor} with specified parameters
	 *
	 * @param queryExecutorServiceFactory
	 *            Service for real query execution
	 */
	public QueryExecutor(final QueryExecutorServiceFactory queryExecutorServiceFactory) {
		this.queryExecutorServiceFactory = queryExecutorServiceFactory;
	}

	/**
	 * @param query
	 * @param defaultGraphUri
	 * @param namedGraphUri
	 *
	 * @return
	 */
	@GET
	// @Path("execQuery")
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
	// @Path("execQuery")
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
	// @Path("execQuery")
	public Response executeQueryViaPOST(@HeaderParam("Accept") final String acceptedFormat,
			final String query,
			@QueryParam("default-graph-uri") final String defaultGraphUri,
			@QueryParam("named-graph-uri") final String namedGraphUri) {
		throw new RuntimeException("Unimplemented yet!");
	}
}
