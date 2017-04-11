/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine.Parameters;

/**
 * A state in which starting a building activity is allowed
 * 
 * @author Cristiano Longo
 *
 */
public class AsyncAggregationEngineCanBuildState extends AsyncAggregationEngineState {

	/**
	 * @param stateLabel
	 * @param delegate
	 */
	public AsyncAggregationEngineCanBuildState(final AggregationEngine.State stateLabel, final QueryEngine delegate) {
		super(stateLabel, delegate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.
	 * AsyncAggregationEngineState#build(org.opendatahacklab.semanticoctopus.
	 * aggregation.async.AsyncAggregationEngine.Parameters,
	 * org.opendatahacklab.semanticoctopus.aggregation.async.
	 * OntologyDonwloadHandler)
	 */
	@Override
	public final AsyncAggregationEngineState build(final Parameters parameters, final OntologyDonwloadHandler handler) {
		final Runnable downloadTask = parameters.getQueryEngineFactory().getDownloadTask(parameters.getOntologies(),
				handler, parameters.getOutputConsole());
		parameters.getDownloadExecutor().execute(downloadTask);
		return new AsyncAggregationEngineBuildingState(getDelegate());
	}
	
	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngineState#dispose()
	 */
	@Override
	public AsyncAggregationEngineState dispose(Parameters parameters){
		getDelegate().dispose();
		return new AsyncAggregationEngineDisposedState();
	}
}
