package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatter;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * An implementation of {@link QueryExecutorService} based on a specifical
 * {@link ResultSetFormatter}
 * 
 * @author OOL
 */
public class FormatterBasedQueryExecutorService implements QueryExecutorService {

	// Engine for query execution
	private final AggregationEngine aggregationEngine;

	// Formatter of result set after query execution
	private final ResultSetFormatter resultSetFormatter;

	/**
	 * Constucts a {@link FormatterBasedQueryExecutorService} with specified
	 * parameters
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.service.QueryExecutorService#
	 * execQuery(java.lang.String)
	 */
	@Override
	public Response execQuery(final String query) {
		final String mimeType = resultSetFormatter.getMimeType();
		try {
			final ResultSet resultSet = aggregationEngine.execQuery(query);
			final String result = resultSetFormatter.format(resultSet);
			return generateSuccessfulResponse(result, mimeType);
		} catch (final QueryParseException e) {
			return generateFailureResponse(e, mimeType);
		}
	}

	/**
	 * Generate a successful response according to specified result and
	 * mime-type
	 * 
	 * @param result
	 * @param mimeType
	 * 
	 * @return
	 */
	public Response generateSuccessfulResponse(final String result, final String mimeType) {
		final Response response = addHeaders(Response.ok(result), mimeType).build();
		return response;
	}

	/**
	 * Generate a successful response according to specified result and
	 * mime-type
	 * 
	 * @param e
	 * @param mimeType
	 * 
	 * @return
	 */
	public Response generateFailureResponse(final Throwable e, final String mimeType) {
		final Response response = addHeaders(Response.ok(e), mimeType).build();
		e.printStackTrace();
		return response;
	}

	/**
	 * Add the required headers to a response.
	 * 
	 * @param mimeType
	 * @return
	 */
	private ResponseBuilder addHeaders(final ResponseBuilder r, final String mimeType){
		return r.header(CONTENT_TYPE_HEADER_KEY, mimeType).header(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER_KEY, "*");
	}
}
