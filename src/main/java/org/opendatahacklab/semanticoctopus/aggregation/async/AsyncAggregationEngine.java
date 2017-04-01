package org.opendatahacklab.semanticoctopus.aggregation.async;

import java.io.OutputStream;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.Executor;

import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngineListener;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletQueryEngineFactory;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Download an ontology from the internet, perform reasoning and provide a
 * sparql endpoint.
 * 
 * @author Cristiano Longo
 *
 */
public class AsyncAggregationEngine implements AggregationEngine, OntologyDonwloadHandler {

	private final AggregatedQueryEngineFactory downloadTaskFactory;
	private final Executor downloadExecutor;
	private AsyncAggregationEngineState state = null;

	/**
	 * Create an aggregation engine which will use the specified download task
	 * factory to generate the model.
	 * 
	 * @param downloadTaskFactory
	 *            a delegate for ontology downloads
	 * @param donwloadExecutor
	 *            the {@link Executor} which will be used to perform the
	 *            download task. It is expected to run in another thread.
	 */
	public AsyncAggregationEngine(final AggregatedQueryEngineFactory downloadTaskFactory,
			Executor donwloadExecutor) {
		this.downloadTaskFactory = downloadTaskFactory;
		this.downloadExecutor=donwloadExecutor;
		state = new AsyncAggregationEngineIdleState(downloadTaskFactory.getEmpty());
	}

	/**
	 * Get an aggregation engine which will use the default implementation of
	 * download task factory
	 * 
	 * @param ontologyURLs
	 *            the ontologies to be aggregated
	 * @param donwloadExecutor
	 *            the {@link Executor} which will be used to perform the
	 *            download task. It is expected to run in another thread.
	 */
	public AsyncAggregationEngine(final Collection<URL> ontologyURLs, final AggregatedQueryEngineFactory downloader,
			Executor donwloadExecutor) {
		this(new JenaPelletQueryEngineFactory(ontologyURLs), donwloadExecutor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine#write(
	 * java.io.OutputStream, java.lang.String)
	 */
	@Override
	public void write(final OutputStream out, final String baseUri) {
		state.write(out, baseUri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine#
	 * execQuery(java.lang.String)
	 */
	@Override
	public ResultSet execQuery(final String query) throws QueryParseException {
		// final QueryExecution execution =
		// QueryExecutionFactory.create(QueryFactory.create(query), model);
		// final ResultSet resultSet = execution.execSelect();
		return state.execQuery(query);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine#
	 * getOntologies()
	 */
	@Override
	public Collection<URL> getOntologies() {
		return downloadTaskFactory.getOntologies();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine#build()
	 */
	@Override
	public synchronized void build() {
		final AsyncAggregationEngineState destState = state.build(downloadTaskFactory, downloadExecutor, this);
		// TODO notify listeners
		state = destState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine#
	 * getState()
	 */
	@Override
	public State getState() {
		return state.getStateLabel();
	}

	@Override
	public void attach(AggregationEngineListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void detach(AggregationEngineListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.
	 * OntologyDonwloadHandler#complete(com.hp.hpl.jena.ontology.OntModel)
	 */
	@Override
	public synchronized void complete(final QueryEngine result) {
		final AsyncAggregationEngineState newState = state.complete(result);
		// TODO notify listeners
		state = newState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.
	 * OntologyDonwloadHandler#error(org.opendatahacklab.semanticoctopus.
	 * aggregation.jena.OntologyDownloadError)
	 */
	@Override
	public synchronized void error(OntologyDownloadError error) {
		final AsyncAggregationEngineState newState = state.error(error);
		// TODO notify listeners
		state = newState;
	}
}
