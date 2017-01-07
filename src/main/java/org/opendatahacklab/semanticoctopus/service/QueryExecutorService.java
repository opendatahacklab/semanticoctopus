package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.core.Response;

import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;

import com.hp.hpl.jena.query.QueryParseException;

/**
 * A service for SPARQL query execution by an {@link QueryEngine}
 *
 * @author OOL
 */
public interface QueryExecutorService {

	/**
	 * Key of reponse header for content type
	 */
	public static final String CONTENT_TYPE_HEADER_KEY = "Content-type";

	/**
	 * Key of reponse header for allow origin header, to avoid cross origin request blocks
	 */
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER_KEY = "Access-Control-Allow-Origin";

	/**
	 * Executes a certain SPARQL SELECT query
	 *
	 * @param query
	 *
	 * @return An HTTP {@link Response} containing the result of query
	 * @throws IllegalMimeTypeException 
	 * @throws QueryParseException 
	 */
	Response execQuery(final String query) throws QueryParseException, IllegalMimeTypeException;
}
