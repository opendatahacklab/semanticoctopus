package org.opendatahacklab.semanticoctopus.service;

import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatter;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatterProvider;

/**
 * An implementation of {@link QueryExecutorServiceFactory} based on an instance of {@link QueryEngine} which
 * generates {@link FormatterBasedQueryExecutorService}s
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
public class FormatterBasedQueryExecutorServiceFactory implements QueryExecutorServiceFactory {

	// Engine for query executions
	private final QueryEngine aggregationEngine;

	// Provider of formatters
	private final ResultSetFormatterProvider formatterProvider;

	/**
	 * Constructs an {@link FormatterBasedQueryExecutorServiceFactory} which builds {@link QueryExecutorService}s using
	 * specified engine and provider of formatters
	 *
	 * @param aggregationEngine
	 * @param formatterProvider
	 */
	public FormatterBasedQueryExecutorServiceFactory(final QueryEngine aggregationEngine,
					final ResultSetFormatterProvider formatterProvider) {
		this.aggregationEngine = aggregationEngine;
		this.formatterProvider = formatterProvider;
	}

	/* (non-Javadoc)
	 *
	 * @see org.opendatahacklab.semanticoctopus.service.QueryExecutorServiceFactory#createService(java.lang.String) */
	@Override
	public QueryExecutorService createService(final String mimeType) throws IllegalMimeTypeException {
		final ResultSetFormatter formatter = formatterProvider.getFormatter(mimeType);
		final FormatterBasedQueryExecutorService service = new FormatterBasedQueryExecutorService(aggregationEngine, formatter);

		return service;
	}
}
