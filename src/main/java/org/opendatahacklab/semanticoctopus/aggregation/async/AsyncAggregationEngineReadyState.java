/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import java.io.OutputStream;
import java.util.concurrent.Executor;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * The initial state of {@link AsyncAggregationEngine}, no ontology is available
 * 
 * @author Cristiano Longo
 *
 */
public class AsyncAggregationEngineReadyState extends AsyncAggregationEngineState {

	private final QueryEngine delegate;

	/**
	 * @param delegate the model which will be served
	 */
	public AsyncAggregationEngineReadyState(final QueryEngine delegate) {
		super(AggregationEngine.State.READY);
		this.delegate=delegate;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#execQuery(java.lang.String)
	 */
	@Override
	public ResultSet execQuery(final String query) throws QueryParseException {
		return delegate.execQuery(query);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#write(java.io.OutputStream, java.lang.String)
	 */
	@Override
	public void write(final OutputStream out, final String baseUri) {
		delegate.write(out, baseUri);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#build(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadTaskFactory, java.util.concurrent.Executor, org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDonwloadHandler)
	 */
	@Override
	public AsyncAggregationEngineState build(OntologyDownloadTaskFactory downloadTaskFactory,
			Executor downloadExecutor, OntologyDonwloadHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngineState#complete(org.opendatahacklab.semanticoctopus.aggregation.QueryEngine)
	 */
	@Override
	public AsyncAggregationEngineState complete(final QueryEngine result) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngineState#error(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadError)
	 */
	@Override
	public AsyncAggregationEngineState error(OntologyDownloadError error) {
		// TODO Auto-generated method stub
		return null;
	}

}
