package org.opendatahacklab.semanticoctopus.aggregation;

import java.io.*;

import com.hp.hpl.jena.query.*;

/**
 * An engine for executing queries against aggregated ontologies
 * 
 * @author Cristiano Longo, OOL
 */
public interface AggregationEngine {

	/**
	 * Write the ontology on the specified stream
	 * 
	 * @param out
	 * @param baseUri
	 */
	void write(OutputStream out, String baseUri);

	/**
	 * Perform a query against the aggregated ontology
	 * 
	 * @param String
	 *            query
	 * @return
	 * @throws QueryParseException
	 */
	ResultSet execQuery(String query) throws QueryParseException;
}