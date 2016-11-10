package org.opendatahacklab.semanticoctopus.service;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;

import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;

/**
 * Query executor API.<br>
 * It uses an instance of {@link QueryExecutorServiceFactory} to generate a response, containing the result of SELECT
 * query or an error message, according to the latter and requested output format.
 *
 * @author OOL
 */
@Path(QueryExecutor.ENDPOINT_NAME)
public class QueryExecutor {

	// Accepted formats
	private static final String URL_ENCODED_MEDIA_TYPE = "application/x-www-form-urlencoded";
	private static final String SPARQL_QUERY_MEDIA_TYPE = "application/sparql-query";
	private static final List<Variant> ACCEPTED_VARIANTS = Arrays.asList(
			Variant.mediaTypes(MediaType.valueOf(URL_ENCODED_MEDIA_TYPE)).build().get(0),
			Variant.mediaTypes(MediaType.valueOf(SPARQL_QUERY_MEDIA_TYPE)).build().get(0));

	public static final String ENDPOINT_NAME = "sparql";

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
	public Response executeQueryViaGET(@HeaderParam("Accept") final String acceptedFormat,
			@QueryParam("query") final String query,
			@QueryParam("default-graph-uri") final String defaultGraphUri,
			@QueryParam("named-graph-uri") final String namedGraphUri) {
		return executeQuery(acceptedFormat, query);
	}

	/**
	 * @param request
	 * @param query
	 *
	 * @return
	 */
	@POST
	@Consumes(URL_ENCODED_MEDIA_TYPE)
	// @Path("execQuery")
	public Response executeUrlEncodedQueryViaPOST(@HeaderParam("Accept") final String acceptedFormat,
			final String query) {
		return executeQuery(acceptedFormat, query);
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
	@Consumes(SPARQL_QUERY_MEDIA_TYPE)
	// @Path("execQuery")
	public Response executeQueryViaPOST(@HeaderParam("Accept") final String acceptedFormat,
			final String query,
			@QueryParam("default-graph-uri") final String defaultGraphUri,
			@QueryParam("named-graph-uri") final String namedGraphUri) {
		return executeQuery(acceptedFormat, query);
	}

	/**
	 * @param acceptedFormat
	 * @param query
	 * 
	 * @return
	 */
	private Response executeQuery(final String acceptedFormat, final String query) {
		try {
			final QueryExecutorService service = queryExecutorServiceFactory.createService(acceptedFormat);
			final Response response = service.execQuery(query);

			return response;
		} catch (final IllegalMimeTypeException e) {
			return createNotAcceptableResponse(acceptedFormat);
		}
	}

	/**
	 * Creates a {@link Response} for a not-acceptable-format condition
	 * 
	 * @param acceptedFormat
	 *            Requested and unaccepted format
	 * 
	 * @return
	 */
	private Response createNotAcceptableResponse(final String mimeType) {
		final Response response = Response.notAcceptable(ACCEPTED_VARIANTS)
				.entity(Entity.text("Provided format is '" + mimeType + "'"))
				.build();

		return response;
	}
}
