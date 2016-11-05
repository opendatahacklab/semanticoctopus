package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.core.Response;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;

/**
 * A service for SPARQL query execution by an {@link AggregationEngine}
 *
 * @author OOL
 */
public interface QueryExecutorService {

	/**
	 * Key of reponse header for content type
	 */
	public static final String CONTENT_TYPE_HEADER_KEY = "Content-type";

	/**
	 * Executes a certain SPARQL SELECT query
	 *
	 * @param query
	 *
	 * @return An HTTP {@link Response} containing the result of query
	 */
	Response execQuery(final String query);
}
