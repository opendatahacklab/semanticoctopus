package org.opendatahacklab.semanticoctopus.service;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatterProvider;

/**
 * An implementation of {@link QueryExecutorServiceFactory} based on an instance of {@link AggregationEngine}
 * 
 * @author OOL
 */
public class AggregatorBasedQueryExecutorServiceFactory implements QueryExecutorServiceFactory {

	// Engine for query executions
	private final AggregationEngine aggregationEngine;

	// Provider of formatters
	private final ResultSetFormatterProvider formatterProvider;

	/**
	 * Constructs an {@link AggregatorBasedQueryExecutorServiceFactory} which builds {@link QueryExecutorService}s using
	 * specified engine and provider of formatters
	 * 
	 * @param aggregationEngine
	 * @param formatterProvider
	 */
	public AggregatorBasedQueryExecutorServiceFactory(final AggregationEngine aggregationEngine,
			final ResultSetFormatterProvider formatterProvider) {
		this.aggregationEngine = aggregationEngine;
		this.formatterProvider = formatterProvider;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.service.QueryExecutorServiceFactory#createService(java.lang.String) */
	@Override
	public QueryExecutorService createService(final String mimeType) throws IllegalMimeTypeException {
		throw new RuntimeException("Unimplemented yet!");
	}
}
