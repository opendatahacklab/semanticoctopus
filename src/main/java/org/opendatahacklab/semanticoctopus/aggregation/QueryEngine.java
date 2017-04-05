package org.opendatahacklab.semanticoctopus.aggregation;

import java.io.OutputStream;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * An engine for executing queries against an underlying knowledge base
 * 
 * @author Cristiano Longo, OOL
 */
public interface QueryEngine {

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
	
	/**
	 * Free all resources
	 */
	void dispose();
}