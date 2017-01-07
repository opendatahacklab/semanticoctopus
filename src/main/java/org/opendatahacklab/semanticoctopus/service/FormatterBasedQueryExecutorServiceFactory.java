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
