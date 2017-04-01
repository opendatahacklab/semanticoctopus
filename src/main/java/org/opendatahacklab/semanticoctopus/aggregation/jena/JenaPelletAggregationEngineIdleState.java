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
 * The initial state of {@link JenaPelletAggregationEngine}, no ontology is available
 * 
 * @author Cristiano Longo
 *
 */
public class JenaPelletAggregationEngineIdleState extends JenaPelletAggregationEngineState {

	/**
	 * @param stateLabel
	 */
	public JenaPelletAggregationEngineIdleState() {
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
	public JenaPelletAggregationEngineState build(final OntologyDownloadTaskFactory downloadTaskFactory,
			final Executor downloadExecutor, final OntologyDonwloadHandler handler) {
		downloadExecutor.execute(downloadTaskFactory.getDownloadTask(handler));
		return new JenaPelletAggregationEngineBuildingState();
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#complete(com.hp.hpl.jena.ontology.OntModel)
	 */
	@Override
	public JenaPelletAggregationEngineState complete(OntModel result) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#error(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadError)
	 */
	@Override
	public JenaPelletAggregationEngineState error(OntologyDownloadError error) {
		// TODO Auto-generated method stub
		return null;
	}

}
