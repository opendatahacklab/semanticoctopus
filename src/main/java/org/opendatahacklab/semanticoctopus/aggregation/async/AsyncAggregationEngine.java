package org.opendatahacklab.semanticoctopus.aggregation.async;

import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.opendatahacklab.semanticoctopus.OutputConsole;
import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngineFactory;
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

	public static final AggregationEngineFactory FACTORY = new AggregationEngineFactory() {

		@Override
		public AggregationEngine create(final Collection<URL> ontologyURLs, final OutputConsole out) {
			return new AsyncAggregationEngine(new Parameters() {

				@Override
				public AggregatedQueryEngineFactory getQueryEngineFactory() {
					return new JenaPelletQueryEngineFactory();
				}

				@Override
				public Collection<URL> getOntologies() {
					return ontologyURLs;
				}

				@Override
				public Executor getDownloadExecutor() {
					return Executors.newSingleThreadExecutor();
				}

				@Override
				public OutputConsole getOutputConsole() {
					return out;
				}
			});
		}
	};

	/**
	 * Construction parameters
	 * 
	 * @author Cristiano Longo
	 *
	 */
	public interface Parameters {

		/**
		 * Get the set of ontologies which contributed to create the underlying
		 * knowledge base.
		 * 
		 * @return
		 */
		Collection<URL> getOntologies();

		/**
		 * The factory to get query engine delegates.
		 * 
		 * @return
		 */
		AggregatedQueryEngineFactory getQueryEngineFactory();

		/**
		 * Get the executor which will be used to run the ontologies download
		 * and aggregation tasks.
		 * 
		 * @return
		 */
		Executor getDownloadExecutor();
		
		/**
		 * A console to send messages to the user
		 * @return
		 */
		OutputConsole getOutputConsole();
	}

	private final Parameters parameters;
	private AsyncAggregationEngineState state = null;


	/**
	 * Create an aggregation engine which will use the specified download task
	 * factory to generate the model.
	 * 
	 * @param ontologyURLs
	 *            the ontologies to be aggregated
	 * @param queryEngineFactory
	 *            a delegate for ontology downloads
	 * @param donwloadExecutor
	 *            the {@link Executor} which will be used to perform the
	 *            download task. It is expected to run in another thread.
	 */
	public AsyncAggregationEngine(final Parameters parameters) {
		final AggregatedQueryEngineFactory queryEngineFactory = parameters.getQueryEngineFactory();
		final Executor downloadExecutor = parameters.getDownloadExecutor();
		final Collection<URL> ontologyURLs = new TreeSet<URL>(new Comparator<URL>() {

			@Override
			public int compare(final URL o1, final URL o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		ontologyURLs.addAll(parameters.getOntologies());
		final OutputConsole out=parameters.getOutputConsole();
		//create a safe copy of parameters
		this.parameters = new Parameters() {
			
			@Override
			public AggregatedQueryEngineFactory getQueryEngineFactory() {
				return queryEngineFactory;
			}
			
			@Override
			public Collection<URL> getOntologies() {
				return ontologyURLs;
			}
			
			@Override
			public Executor getDownloadExecutor() {
				return downloadExecutor;
			}

			@Override
			public OutputConsole getOutputConsole() {
				return out;
			}
		};
		
		//go to IDLE state
		setState(new AsyncAggregationEngineCanBuildState(State.IDLE, queryEngineFactory.getEmpty()));
	}

	/**
	 * Change the current state
	 * @param newState
	 */
	private void setState(final AsyncAggregationEngineState newState){
		this.state=newState;
		parameters.getOutputConsole().println("Current state "+state.getStateLabel());
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
		parameters.getOutputConsole().println("Requested execution of query");
		parameters.getOutputConsole().println("query");
		final long starTime = System.currentTimeMillis();
		final ResultSet result = state.execQuery(query);
		final long endTime = System.currentTimeMillis();
		parameters.getOutputConsole().println("query completed in "+(endTime-starTime)+" milliseconds with returning "+result.getRowNumber()+" rows.");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine#
	 * getOntologies()
	 */
	@Override
	public Collection<URL> getOntologies() {
		return new ArrayList<>(parameters.getOntologies());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine#build()
	 */
	@Override
	public synchronized void build() {
		setState(state.build(parameters, this));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.jena.
	 * OntologyDonwloadHandler#complete(com.hp.hpl.jena.ontology.OntModel)
	 */
	@Override
	public synchronized void complete(final QueryEngine result) {
		final AsyncAggregationEngineState newState = state.complete(result);
		setState(newState);
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
		final AsyncAggregationEngineState newState = state.error();
		state = newState;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDonwloadHandler#error(org.opendatahacklab.semanticoctopus.aggregation.async.InconsistenOntologyException)
	 */
	@Override
	public synchronized void error(InconsistenOntologyException error) {
		final AsyncAggregationEngineState newState = state.error();
		state = newState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#dispose()
	 */
	@Override
	public synchronized void dispose() {
		setState(state.dispose(parameters));
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#isDisposed()
	 */
	@Override
	public boolean isDisposed() {
		return false;
	}

}
