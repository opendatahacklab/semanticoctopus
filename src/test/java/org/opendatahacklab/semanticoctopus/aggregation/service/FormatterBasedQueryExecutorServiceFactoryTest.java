package org.opendatahacklab.semanticoctopus.aggregation.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatter;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatterProvider;
import org.opendatahacklab.semanticoctopus.service.FormatterBasedQueryExecutorService;
import org.opendatahacklab.semanticoctopus.service.FormatterBasedQueryExecutorServiceFactory;
import org.opendatahacklab.semanticoctopus.service.QueryExecutorService;
import org.opendatahacklab.semanticoctopus.service.QueryExecutorServiceFactory;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Test class for {@link FormatterBasedQueryExecutorServiceFactory}
 *
 * @author OOL
 */
public class FormatterBasedQueryExecutorServiceFactoryTest {

	// Testbed
	private static final ResultSet RESULT_SET = mock(ResultSet.class);
	private static final String MIME_TYPE = "text/plain";
	private static final String FORMATTED_RESULT = "?s ?p ?o";

	/**
	 * @param aggregationEngine
	 * @param formatterProvider
	 *
	 * @return
	 */
	private QueryExecutorServiceFactory createTestSubject(final AggregationEngine aggregationEngine,
					final ResultSetFormatterProvider formatterProvider) {
		return new FormatterBasedQueryExecutorServiceFactory(aggregationEngine, formatterProvider);
	}

	/**
	 * @return
	 */
	private AggregationEngine createAggregationEngine() {
		final AggregationEngine aggregator = mock(AggregationEngine.class);
		// when(aggregator.execQuery(anyString())).thenReturn(RESULT_SET);

		return aggregator;
	}

	/**
	 * @param givesError
	 *
	 * @return
	 * @throws IllegalMimeTypeException
	 */
	private ResultSetFormatterProvider createFormatterProvider(final boolean givesError)
					throws IllegalMimeTypeException {
		final ResultSetFormatter resultSetFormatter = mock(ResultSetFormatter.class);
		final ResultSetFormatterProvider formatterProvider = mock(ResultSetFormatterProvider.class);

		if (givesError)
			when(formatterProvider.getFormatter(MIME_TYPE)).thenThrow(new IllegalMimeTypeException(MIME_TYPE));
		else
			when(formatterProvider.getFormatter(MIME_TYPE)).thenReturn(resultSetFormatter);

		return formatterProvider;
	}

	// /**
	// * @return
	// */
	// private ResultSetFormatter createFormatter() {
	// final ResultSetFormatter resultSetFormatter = mock(ResultSetFormatter.class);
	// when(resultSetFormatter.getMimeType()).thenReturn(MIME_TYPE);
	// when(resultSetFormatter.format(RESULT_SET)).thenReturn(FORMATTED_RESULT);
	//
	// return resultSetFormatter;
	// }

	/**
	 * @throws IllegalMimeTypeException
	 */
	@Test
	public void shouldCreateService() throws IllegalMimeTypeException {
		final AggregationEngine aggregator = createAggregationEngine();
		final ResultSetFormatterProvider formatterProvider = createFormatterProvider(false);

		final QueryExecutorServiceFactory testSubject = createTestSubject(aggregator, formatterProvider);
		final QueryExecutorService service = testSubject.createService(MIME_TYPE);

		assertTrue(service instanceof FormatterBasedQueryExecutorService);
	}

	/**
	 * @throws IllegalMimeTypeException
	 */
	@Test
	public void shouldRaiseException() throws IllegalMimeTypeException {
		final AggregationEngine aggregator = createAggregationEngine();
		final ResultSetFormatterProvider formatterProvider = createFormatterProvider(true);

		final QueryExecutorServiceFactory testSubject = createTestSubject(aggregator, formatterProvider);

		try {
			final QueryExecutorService service = testSubject.createService(MIME_TYPE);
			fail("An exception should have been raised");
		} catch (final IllegalMimeTypeException e) {
			// All ok: expected!
		}
	}
}
