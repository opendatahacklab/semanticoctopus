/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.opendatahacklab.semanticoctopus.aggregation.AbstractAggregationTest;
import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine.Parameters;

/**
 * TODO remove this
 * 
 * @author Cristiano Longo
 *
 */
public class JenaPelletAggregationEngineDownloadAndAggregationTest extends AbstractAggregationTest {

	/**
	 * @throws MalformedURLException
	 */
	public JenaPelletAggregationEngineDownloadAndAggregationTest() throws MalformedURLException {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.AbstractAggregationTest#
	 * createTestSubject(java.util.Collection)
	 */
	@Override
	public QueryEngine createTestSubject(final Collection<URL> ontologies) throws InterruptedException {
		final ExecutorService executor = Executors.newCachedThreadPool();
		final AggregationEngine engine = new AsyncAggregationEngine(new Parameters() {
			
			@Override
			public AggregatedQueryEngineFactory getQueryEngineFactory() {
				return new JenaPelletQueryEngineFactory();
			}
			
			@Override
			public Collection<URL> getOntologies() {
				return ontologies;
			}
			
			@Override
			public ExecutorService getDownloadExecutor() {
				return executor;
			}
		});
		assertSame(AggregationEngine.State.IDLE, engine.getState());
		engine.build();
		executor.shutdown();
		executor.awaitTermination(20, TimeUnit.SECONDS);
		assertTrue(executor.isTerminated());
		assertSame(AggregationEngine.State.READY, engine.getState());
		return engine;
	}

}
