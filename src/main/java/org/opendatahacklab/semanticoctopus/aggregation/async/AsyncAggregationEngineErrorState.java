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
class AsyncAggregationEngineErrorState extends AsyncAggregationEngineState {

	/**
	 * @param delegate 
	 * @param stateLabel
	 */
	public AsyncAggregationEngineErrorState(final QueryEngine delegate) {
		super(AggregationEngine.State.ERROR, delegate);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#build(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadTaskFactory, java.util.concurrent.Executor, org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDonwloadHandler)
	 */
	@Override
	public AsyncAggregationEngineState build(AggregatedQueryEngineFactory downloadTaskFactory,
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
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#error(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadError)
	 */
	@Override
	public AsyncAggregationEngineState error(OntologyDownloadError error) {
		// TODO Auto-generated method stub
		return null;
	}

}
