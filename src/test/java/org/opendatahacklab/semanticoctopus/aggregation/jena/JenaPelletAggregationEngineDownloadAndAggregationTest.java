/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import static org.junit.Assert.assertSame;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.Executor;

import org.opendatahacklab.semanticoctopus.aggregation.AbstractAggregationTest;
import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.ExplicitExecutor;
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
		final ExplicitExecutor executor = new ExplicitExecutor();
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
			public Executor getDownloadExecutor() {
				return executor;
			}
		});
		assertSame(AggregationEngine.State.IDLE, engine.getState());
		engine.build();
		executor.waitForCompletion();
		assertSame(AggregationEngine.State.READY, engine.getState());
		return engine;
	}

}
