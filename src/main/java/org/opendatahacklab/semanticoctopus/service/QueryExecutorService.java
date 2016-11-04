package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.core.Response;

/**
 * A service for SPARQL query execution
 *
 * @author OOL
 */
public interface QueryExecutorService {

	/**
	 * Executes a certain SPARQL SELECT query
	 *
	 * @param query
	 * @param responseFormat
	 *
	 * @return An HTTP {@link Response} in specified mime-type response format
	 */
	Response execQuery(final String query, final String responseFormat);
}
