package org.opendatahacklab.semanticoctopus.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.formatters.IllegalRequestBodyException;

import com.hp.hpl.jena.query.QueryParseException;

/**
 * Query executor API.<br>
 * It uses an instance of {@link QueryExecutorServiceFactory} to generate a response, containing the result of SELECT
 * query or an error message, according to the latter and requested output format.
 *
 * @author OOL, Cristiano Longo
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

	/**
	 * HTTP status code for the case of a query syntax error
	 */
	public static final int SYNTAX_ERROR_STATUS_CODE = Response.Status.BAD_REQUEST.getStatusCode();

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
			@QueryParam("query") final String query, @QueryParam("default-graph-uri") final String defaultGraphUri,
			@QueryParam("named-graph-uri") final String namedGraphUri) {
		logRequest("GET", acceptedFormat, query, defaultGraphUri, namedGraphUri);

		return executeQuery(acceptedFormat, query);
	}

	/**
	 * Helper method to log a query request
	 * 
	 * @param method
	 * @param acceptedFormat
	 * @param query
	 * @param defaultGraph
	 * @param namedGraph
	 */
	private void logRequest(final String method, final String acceptedFormat, final String query,
			final String defaultGraph, final String namedGraph) {
		System.out.println("Requested execution of query: \n" + query);
		System.out.println("Method: " + method);
		System.out.println("Accepted format: " + acceptedFormat);
		System.out.println("Default graph: " + defaultGraph);
		System.out.println("Named Graph: " + namedGraph);
	}

	/**
	 * @param request
	 * @param body
	 *
	 * @return
	 * @throws IllegalRequestBodyException
	 * @throws UnsupportedEncodingException
	 */
	@POST
	@Consumes(URL_ENCODED_MEDIA_TYPE)
	public Response executeUrlEncodedQueryViaPOST(@HeaderParam("Accept") final String acceptedFormat, final String body)
			throws UnsupportedEncodingException, IllegalRequestBodyException {
		// URL-encoded, ampersand-separated query parameters.
		logRequest("POST URLENCODED", acceptedFormat, body, "NA", "NA");

		final Map<String, String> queryParameters = extractParameters(body);
		final String query = queryParameters.get("query");
		if (query == null)
			throw new IllegalRequestBodyException("Missing mandatory query parameter in the request body");

		System.out.println("Performing query " + query);

		return executeQuery(acceptedFormat, query);
	}

	/**
	 * Extract a map key -> value pairs from a URLEncoded query body
	 * 
	 * @param body
	 * @return
	 * @throws IllegalRequestBodyException
	 * @throws UnsupportedEncodingException
	 */
	private Map<String, String> extractParameters(final String body)
			throws IllegalRequestBodyException, UnsupportedEncodingException {
		final Map<String, String> result = new TreeMap<String, String>();
		for (final String piece : body.split("&")) {
			final String[] pair = piece.split("=");
			if (pair.length != 2)
				throw new IllegalRequestBodyException("Error parsing in query body " + piece);
			result.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
		}

		return result;
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
	public Response executeQueryViaPOST(@HeaderParam("Accept") final String acceptedFormat, final String query,
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
			e.printStackTrace();
			return createErrorResponse(INVALID_FORMAT_STATUS_CODE);
		} catch (final QueryParseException e) {
			e.printStackTrace();
			return createErrorResponse(SYNTAX_ERROR_STATUS_CODE);
		}
	}

	/**
	 * Create a response for a failure
	 * 
	 * @param statusCode
	 * @return
	 */
	private Response createErrorResponse(final int statusCode) {
		return Response.status(statusCode)
				.header(QueryExecutorService.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER_KEY, "*")
				.build();
	}
}
