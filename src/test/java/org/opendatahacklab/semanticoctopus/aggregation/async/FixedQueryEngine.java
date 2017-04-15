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
 * This file is part of Semantic Octopus.
 * 
 * Copyright 2017 Cristiano Longo, Antonio Pisasale
 *
 * Semantic Octopus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Semantic Octopus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class FixedQueryEngine implements QueryEngine {
	private final String expectedQuery;
	private final ResultSet resultSet;
	
	private boolean called;
	private boolean disposed;

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
		if (disposed)
			throw new IllegalStateException();
		assertEquals(expectedQuery, query);
		assertFalse("Multiple calls to execQuery", called);
		called=false;
		return resultSet;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#dispose()
	 */
	@Override
	public void dispose() {
		disposed=true;
	}
	
	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#isDisposed()
	 */
	@Override
	public boolean isDisposed(){
		return disposed;
	}

}
