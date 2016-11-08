package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.core.Response;

import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;

/**
 * A factory of {@link QueryExecutorService}s
 * 
 * @author OOL
 */
public interface QueryExecutorServiceFactory {

	/**
	 * Creates a {@link QueryExecutorService} returning {@link Response}s formatted as per specified mime-type. Accepted
	 * mime-types are: <code>text/csv</code>, <code>text/tab-separated-values<code></code>,
	 * <code>application/sparql-results+json</code>,<code>application/json</code>,
	 * <code>application/sparql-results+xml</code>,<code>application/xml</code>
	 * 
	 * @param mimeType
	 * 
	 * @return
	 * 
	 * @throws IllegalMimeTypeException
	 *             when mime-type is not accepted in the context of SPARQL SELECT queries
	 */
	QueryExecutorService createService(String mimeType) throws IllegalMimeTypeException;
}