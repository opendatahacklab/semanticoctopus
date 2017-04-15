package org.opendatahacklab.semanticoctopus.aggregation.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatter;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatterProvider;
import org.opendatahacklab.semanticoctopus.service.FormatterBasedQueryExecutorService;
import org.opendatahacklab.semanticoctopus.service.FormatterBasedQueryExecutorServiceFactory;
import org.opendatahacklab.semanticoctopus.service.QueryExecutorService;
import org.opendatahacklab.semanticoctopus.service.QueryExecutorServiceFactory;

/**
 * Test class for {@link FormatterBasedQueryExecutorServiceFactory}
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
public class FormatterBasedQueryExecutorServiceFactoryTest {

	// Testbed
	private static final String MIME_TYPE = "text/plain";

	/**
	 * @param aggregationEngine
	 * @param formatterProvider
	 *
	 * @return
	 */
	private QueryExecutorServiceFactory createTestSubject(final QueryEngine aggregationEngine,
					final ResultSetFormatterProvider formatterProvider) {
		return new FormatterBasedQueryExecutorServiceFactory(aggregationEngine, formatterProvider);
	}

	/**
	 * @return
	 */
	private QueryEngine createAggregationEngine() {
		final QueryEngine aggregator = mock(QueryEngine.class);
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
		final QueryEngine aggregator = createAggregationEngine();
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
		final QueryEngine aggregator = createAggregationEngine();
		final ResultSetFormatterProvider formatterProvider = createFormatterProvider(true);

		final QueryExecutorServiceFactory testSubject = createTestSubject(aggregator, formatterProvider);

		try {
			testSubject.createService(MIME_TYPE);
			fail("An exception should have been raised");
		} catch (final IllegalMimeTypeException e) {
			// All ok: expected!
		}
	}
}
