package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatter;

import com.hp.hpl.jena.query.ResultSet;

/**
 * An implementation of {@link QueryExecutorService} based on a specifical {@link ResultSetFormatter}
 * 
 * @author OOL
 */
public class FormatterBasedQueryExecutorService implements QueryExecutorService {

	// Engine for query execution
	private final AggregationEngine aggregationEngine;

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
	public FormatterBasedQueryExecutorService(final AggregationEngine aggregationEngine,
			final ResultSetFormatter resultSetFormatter) {
		this.aggregationEngine = aggregationEngine;
		this.resultSetFormatter = resultSetFormatter;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.service.QueryExecutorService#execQuery(java.lang.String) */
	@Override
	public Response execQuery(final String query) {
		final ResultSet resultSet = aggregationEngine.execQuery(query);
		final String result = resultSetFormatter.format(resultSet);
		final String mimeType = resultSetFormatter.getMimeType();

		final Response response = generateSuccessfulResponse(result, mimeType);

		return response;
	}

	/**
	 * Generate a successful response according to specified result and mime-type
	 * 
	 * @param result
	 * @param mimeType
	 * 
	 * @return
	 */
	public Response generateSuccessfulResponse(final String result, final String mimeType) {
		final Response response = Response.ok(result).build();
		final MultivaluedMap<String, Object> headers = response.getHeaders();
		headers.putSingle(CONTENT_TYPE_HEADER_KEY, mimeType);

		return response;
	}
}
