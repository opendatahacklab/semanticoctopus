/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.OutputStream;

import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * A query engine which expects a specified query, just one time, and provide 
 * a fixed result set.
 * 
 * @author Cristiano Longo
 *
 */
public class FixedQueryEngine implements QueryEngine {
	private final String expectedQuery;
	private final ResultSet resultSet;
	
	private boolean called;

	/**
	 * 
	 */
	/**
	 * @param expectedQuery the query which is expected
	 * @param resultSet the one which will be returned as query reply
	 */
	public FixedQueryEngine(final String expectedQuery, final ResultSet resultSet) {
		this.expectedQuery=expectedQuery;
		this.resultSet=resultSet;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#write(java.io.OutputStream, java.lang.String)
	 */
	@Override
	public void write(OutputStream out, String baseUri) {
		fail("Unexpected method call");
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#execQuery(java.lang.String)
	 */
	@Override
	public ResultSet execQuery(String query) throws QueryParseException {
		assertEquals(expectedQuery, query);
		assertFalse("Multiple calls to execQuery", called);
		called=false;
		return resultSet;
	}

}
