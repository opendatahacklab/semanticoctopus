/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.io.OutputStream;
import java.util.concurrent.Executor;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Handle calls to the {@link AggregationEngine} in a given state
 * 
 * @author cristiano longo
 *
 */
abstract class JenaPelletAggregationEngineState {

	private final AggregationEngine.State stateLabel;

	/**
	 * 
	 */
	public JenaPelletAggregationEngineState(final AggregationEngine.State stateLabel) {
		this.stateLabel = stateLabel;
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
	public abstract ResultSet execQuery(final String query) throws QueryParseException;

	/**
	 * Write out the current knowledge base
	 * 
	 * @param out
	 * @param baseUri
	 */
	public abstract void write(final OutputStream out, final String baseUri);

	/**
	 * @param downloadExecutor TODO
	 * @return the destination state
	 */
	public abstract JenaPelletAggregationEngineState build(final OntologyDownloadTaskFactory downloadTaskFactory,
			final Executor downloadExecutor, final OntologyDonwloadHandler handler);

	/**
	 * Handle the download complete event
	 * 
	 * @param result
	 * @return the destination state
	 */
	public abstract JenaPelletAggregationEngineState complete(OntModel result);

	/**
	 * Handle the download error event
	 * 
	 * @param error
	 * @return the destination state
	 */
	public abstract JenaPelletAggregationEngineState error(OntologyDownloadError error);

}
