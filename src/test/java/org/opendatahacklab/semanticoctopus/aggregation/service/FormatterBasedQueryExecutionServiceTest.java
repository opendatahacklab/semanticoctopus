package org.opendatahacklab.semanticoctopus.aggregation.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.opendatahacklab.semanticoctopus.service.QueryExecutorService.CONTENT_TYPE_HEADER_KEY;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatter;
import org.opendatahacklab.semanticoctopus.service.FormatterBasedQueryExecutorService;
import org.opendatahacklab.semanticoctopus.service.QueryExecutorService;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Test class of {@link FormatterBasedQueryExecutionService}
 * 
 * @author OOL
 */
public class FormatterBasedQueryExecutionServiceTest {

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
	private QueryExecutorService createTestSubject(final AggregationEngine engine, final ResultSetFormatter formatter) {
		return new FormatterBasedQueryExecutorService(engine, formatter);
	}

	/**
	 * @param setToReturn
	 * 
	 * @return
	 */
	private AggregationEngine createEngine(final ResultSet setToReturn) {
		final AggregationEngine engine = mock(AggregationEngine.class);
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
	 * 
	 */
	@Test
	public void shouldReturnAResponseWithCorrectContentTypeAndResult() {
		final AggregationEngine engine = createEngine(RESULT_SET);
		final ResultSetFormatter formatter = createFormatter(MIME_TYPE, FORMATTED_RESULT);

		final QueryExecutorService testSubject = createTestSubject(engine, formatter);

		final Response response = testSubject.execQuery(QUERY);

		assertEquals("Wrong content type", response.getHeaders().get(CONTENT_TYPE_HEADER_KEY).get(0), MIME_TYPE);
		assertEquals("Wrong response body", response.getEntity(), FORMATTED_RESULT);
	}

	// TODO Error handling
}
