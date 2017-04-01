/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import java.util.concurrent.Executor;

import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;

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
		super(AggregationEngine.State.READY, null);
		this.delegate=delegate;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#build(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadTaskFactory, java.util.concurrent.Executor, org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDonwloadHandler)
	 */
	@Override
	public AsyncAggregationEngineState build(final AggregatedQueryEngineFactory downloadTaskFactory,
			final Executor downloadExecutor, final OntologyDonwloadHandler handler) {
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
