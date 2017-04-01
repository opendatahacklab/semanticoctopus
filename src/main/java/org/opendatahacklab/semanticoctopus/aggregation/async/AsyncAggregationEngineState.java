/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import java.io.OutputStream;
import java.util.concurrent.Executor;

import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;

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
	 * @param delegate TODO
	 * 
	 */
	/**
	 * @param stateLabel
	 * @param delegate the delegate used to perform queries
	 */
	public AsyncAggregationEngineState(final AggregationEngine.State stateLabel, QueryEngine delegate) {
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
	 * @param downloadExecutor TODO
	 * @return the destination state
	 */
	public abstract AsyncAggregationEngineState build(final AggregatedQueryEngineFactory downloadTaskFactory,
			final Executor downloadExecutor, final OntologyDonwloadHandler handler);

	/**
	 * Handle the download complete event
	 * 
	 * @param result
	 * @return the destination state
	 */
	public abstract AsyncAggregationEngineState complete(QueryEngine result);

	/**
	 * Handle the download error event
	 * 
	 * @param error
	 * @return the destination state
	 */
	public abstract AsyncAggregationEngineState error(OntologyDownloadError error);

	/**
	 * Get the delegate query engine actually in use
	 * 
	 * @return
	 */
	protected QueryEngine getDelegate(){
		return delegate;
	}
}
