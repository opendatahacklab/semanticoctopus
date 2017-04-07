/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import java.io.OutputStream;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine.Parameters;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Handle calls to the {@link AggregationEngine} in a given state
 * 
 * @author cristiano longo
 *
 */
abstract class AsyncAggregationEngineState {

	private final AggregationEngine.State stateLabel;
	private final QueryEngine delegate;

	/**
	 * @param stateLabel
	 * @param delegate the delegate used to perform queries
	 */
	public AsyncAggregationEngineState(final AggregationEngine.State stateLabel, final QueryEngine delegate) {
		this.stateLabel = stateLabel;
		this.delegate=delegate;
	}

	/**
	 * Get the current state of the engine.
	 * 
	 * @return
	 */
	public final AggregationEngine.State getStateLabel() {
		return stateLabel;
	}

	/**
	 * @param query
	 * @return
	 * @throws QueryParseException
	 */
	public final ResultSet execQuery(final String query) throws QueryParseException{
		return delegate.execQuery(query);
	}

	/**
	 * Write out the current knowledge base
	 * 
	 * @param out
	 * @param baseUri
	 */
	public final void write(final OutputStream out, final String baseUri){
		delegate.write(out, baseUri);
	}

	/**
	 * In the default implementation the event is just ignored. 
	 * 
	 * @param parameters
	 * @param handler
	 * @return
	 */
	public AsyncAggregationEngineState build(final Parameters parameters,
			final OntologyDonwloadHandler handler){
		return this;
	}

	/**
	 * In the default implementation the event is just ignored. 
	 * 
	 * Handle the download complete event
	 * 
	 * @param result
	 * @return the destination state
	 */
	public AsyncAggregationEngineState complete(QueryEngine result){
		return this;
	}

	/**
 	 * In the default implementation the event is just ignored. 
 	 * 
	 * Handle the download error event
	 * 
	 * @return the destination state
	 */
	public AsyncAggregationEngineState error(){
		return this;
	}

	/**
	 * Get the delegate query engine actually in use
	 * 
	 * @return
	 */
	protected QueryEngine getDelegate(){
		return delegate;
	}
}
