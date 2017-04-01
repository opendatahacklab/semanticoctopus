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
public class AsyncAggregationEngineIdleState extends AsyncAggregationEngineState {

	/**
	 * @param stateLabel
	 */
	public AsyncAggregationEngineIdleState() {
		super(AggregationEngine.State.IDLE);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#execQuery(java.lang.String)
	 */
	@Override
	public ResultSet execQuery(String query) throws QueryParseException {
		//TODO return an empty model
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#write(java.io.OutputStream, java.lang.String)
	 */
	@Override
	public void write(final OutputStream out, final String baseUri) {
		//intentionally empty
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#build(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadTaskFactory, java.util.concurrent.Executor, org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDonwloadHandler)
	 */
	@Override
	public AsyncAggregationEngineState build(final OntologyDownloadTaskFactory downloadTaskFactory,
			final Executor downloadExecutor, final OntologyDonwloadHandler handler) {
		downloadExecutor.execute(downloadTaskFactory.getDownloadTask(handler));
		return new AsyncAggregationEngineBuildingState();
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
	public AsyncAggregationEngineState error(final OntologyDownloadError error) {
		// TODO Auto-generated method stub
		return null;
	}

}
