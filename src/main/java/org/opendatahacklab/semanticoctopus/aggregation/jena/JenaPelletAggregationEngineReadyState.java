/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.Executor;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * The initial state of {@link JenaPelletAggregationEngine}, no ontology is available
 * 
 * @author Cristiano Longo
 *
 */
public class JenaPelletAggregationEngineReadyState extends JenaPelletAggregationEngineState {

	private OntModel model;

	/**
	 * @param model the model which will be served
	 */
	public JenaPelletAggregationEngineReadyState(final OntModel model) {
		super(AggregationEngine.State.READY);
		this.model=model;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#execQuery(java.lang.String)
	 */
	@Override
	public ResultSet execQuery(final String query) throws QueryParseException {
		final QueryExecution execution = QueryExecutionFactory.create(QueryFactory.create(query), model);
		return execution.execSelect();
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#write(java.io.OutputStream, java.lang.String)
	 */
	@Override
	public void write(final OutputStream out, final String baseUri) {
		final OutputStreamWriter writer = new OutputStreamWriter(out);
		this.model.writeAll(writer, "RDF/XML-ABBREV", baseUri);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngineState#build(org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadTaskFactory, java.util.concurrent.Executor, org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDonwloadHandler)
	 */
	@Override
	public JenaPelletAggregationEngineState build(OntologyDownloadTaskFactory downloadTaskFactory,
			Executor downloadExecutor, OntologyDonwloadHandler handler) {
		// TODO Auto-generated method stub
		return null;
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
