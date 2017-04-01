/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * An Async Aggregation Engine is a query engine but which provide an additional
 * method update and can be in three states: READY the query engine is running,
 * this is the solely state in which the query functionality is available,
 * UPDATING the engine is preparing the knowledge base, ERROR some error occurred
 * during the last update. The ERROR may be fixed with a subsequent update
 * 
 * @author Cristiano Longo
 *
 */
public class AsyncAggregationEngine implements QueryEngine {

	/**
	 * 
	 */
	public AsyncAggregationEngine(final List<URL> ontologyURLs) {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#write(java.io
	 * .OutputStream, java.lang.String)
	 */
	@Override
	public void write(OutputStream out, String baseUri) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#execQuery(
	 * java.lang.String)
	 */
	@Override
	public ResultSet execQuery(String query) throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
