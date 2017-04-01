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
class AsyncAggregationEngineBuildingState extends AsyncAggregationEngineState {

	/**
	 * @param delegate 
	 * @param stateLabel
	 */
	public AsyncAggregationEngineBuildingState(final QueryEngine delegate) {
		super(AggregationEngine.State.BUILDING, delegate);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#build(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadTaskFactory, java.util.concurrent.Executor, org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDonwloadHandler)
	 */
	@Override
	public AsyncAggregationEngineState build(final AggregatedQueryEngineFactory downloadTaskFactory,
			final Executor downloadExecutor, final OntologyDonwloadHandler handler) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngineState#complete(org.opendatahacklab.semanticoctopus.aggregation.QueryEngine)
	 */
	@Override
	public AsyncAggregationEngineState complete(final QueryEngine result) {
		return new AsyncAggregationEngineReadyState(result);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngineState#error(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadError)
	 */
	@Override
	public AsyncAggregationEngineState error(final OntologyDownloadError error) {
		return new AsyncAggregationEngineErrorState(getDelegate());
	}
}
