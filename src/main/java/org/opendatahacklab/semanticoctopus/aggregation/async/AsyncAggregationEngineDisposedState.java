/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;

/**
 * Final State- the aggregation engine can be no more used
 * @author Cristiano Longo
 *
 */
public class AsyncAggregationEngineDisposedState extends AsyncAggregationEngineState {

	/**
	 * @param stateLabel
	 * @param delegate
	 */
	public AsyncAggregationEngineDisposedState() {
		super(AggregationEngine.State.DISPOSED, null);
	}

}
