package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.core.Response;

import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatter;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * An implementation of {@link QueryExecutorService} based on a specifical {@link ResultSetFormatter}
 * 
 * @author OOL
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
public class FormatterBasedQueryExecutorService implements QueryExecutorService {

	// Engine for query execution
	private final QueryEngine aggregationEngine;

	// Formatter of result set after query execution
	private final ResultSetFormatter resultSetFormatter;

	/**
	 * Constucts a {@link FormatterBasedQueryExecutorService} with specified parameters
	 * 
	 * @param aggregationEngine
	 *            Engine for query execution
	 * @param resultSetFormatter
	 *            Formatter of result set after query execution
	 */
	public FormatterBasedQueryExecutorService(final QueryEngine aggregationEngine,
			final ResultSetFormatter resultSetFormatter) {
		this.aggregationEngine = aggregationEngine;
		this.resultSetFormatter = resultSetFormatter;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.service.QueryExecutorService# execQuery(java.lang.String) */
	@Override
	public Response execQuery(final String query) throws QueryParseException, IllegalMimeTypeException {
		final String mimeType = resultSetFormatter.getMimeType();
		final ResultSet resultSet = aggregationEngine.execQuery(query);
		final String result = resultSetFormatter.format(resultSet);

		return Response.ok(result)
				.header(CONTENT_TYPE_HEADER_KEY, mimeType)
				.header(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER_KEY, "*")
				.build();
	}
}
