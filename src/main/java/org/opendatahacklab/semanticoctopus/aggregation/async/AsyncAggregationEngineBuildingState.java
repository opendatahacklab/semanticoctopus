/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine.State;
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
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngineState#complete(org.opendatahacklab.semanticoctopus.aggregation.QueryEngine)
	 */
	@Override
	public AsyncAggregationEngineState complete(final QueryEngine result) {
		getDelegate().dispose();
		return new AsyncAggregationEngineCanBuildState(State.READY, result);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngineState#error(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadError)
	 */
	@Override
	public AsyncAggregationEngineState error(final OntologyDownloadError error) {
		return new AsyncAggregationEngineCanBuildState(State.ERROR, getDelegate());
	}
}
