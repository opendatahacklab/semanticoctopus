package org.opendatahacklab.semanticoctopus.aggregation.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.opendatahacklab.semanticoctopus.service.QueryExecutorService.CONTENT_TYPE_HEADER_KEY;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatter;
import org.opendatahacklab.semanticoctopus.service.FormatterBasedQueryExecutorService;
import org.opendatahacklab.semanticoctopus.service.QueryExecutorService;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Test class of {@link FormatterBasedQueryExecutionService}
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
public class FormatterBasedQueryExecutorServiceTest {

	// Testbed
	private static final ResultSet RESULT_SET = mock(ResultSet.class);
	private static final String MIME_TYPE = "text/plain";
	private static final String FORMATTED_RESULT = "Formatted result";
	private static final String QUERY = "SELECT ?s ?p ?o WHERE { ?s ?p ?o . } LIMIT 1";

	/**
	 * @param engine
	 * @param formatter
	 * 
	 * @return
	 */
	private QueryExecutorService createTestSubject(final QueryEngine engine, final ResultSetFormatter formatter) {
		return new FormatterBasedQueryExecutorService(engine, formatter);
	}

	/**
	 * @param setToReturn
	 * 
	 * @return
	 */
	private QueryEngine createEngine(final ResultSet setToReturn) {
		final QueryEngine engine = mock(QueryEngine.class);
		when(engine.execQuery(anyString())).thenReturn(setToReturn);

		return engine;
	}

	/**
	 * @param mimeType
	 * @param formattedResult
	 * 
	 * @return
	 */
	private ResultSetFormatter createFormatter(final String mimeType, final String formattedResult) {
		final ResultSetFormatter formatter = mock(ResultSetFormatter.class);
		when(formatter.getMimeType()).thenReturn(mimeType);
		when(formatter.format((ResultSet) any())).thenReturn(formattedResult);

		return formatter;
	}

	/**
	 * @throws IllegalMimeTypeException 
	 * @throws QueryParseException 
	 * 
	 */
	@Test
	public void shouldReturnAResponseWithCorrectContentTypeAndResult() throws QueryParseException, IllegalMimeTypeException {
		final QueryEngine engine = createEngine(RESULT_SET);
		final ResultSetFormatter formatter = createFormatter(MIME_TYPE, FORMATTED_RESULT);

		final QueryExecutorService testSubject = createTestSubject(engine, formatter);

		final Response response = testSubject.execQuery(QUERY);

		assertEquals("Wrong content type", response.getHeaders().get(CONTENT_TYPE_HEADER_KEY).get(0), MIME_TYPE);
		assertEquals("Wrong response body", response.getEntity(), FORMATTED_RESULT);
	}

	// TODO Error handling
}
