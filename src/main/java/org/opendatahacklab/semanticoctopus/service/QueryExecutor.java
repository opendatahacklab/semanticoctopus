package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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

	/**
	 * HTTP status code representing a successful response
	 */
	public static final int SUCCESSFUL_STATUS_CODE = Response.Status.OK.getStatusCode();

	/**
	 * HTTP status code representing an illegal response format requested
	 */
	public static final int INVALID_FORMAT_STATUS_CODE = Response.Status.NOT_ACCEPTABLE.getStatusCode();

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
		final Response response = Response.status(INVALID_FORMAT_STATUS_CODE)
						// .entity(Entity.text(ACCEPTED_FORMATS_MESSAGE + mimeType))
						.build();

		return response;
	}
}
