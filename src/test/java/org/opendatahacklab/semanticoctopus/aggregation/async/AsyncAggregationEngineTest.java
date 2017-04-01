/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.engine.QueryEngineFactory;
import com.hp.hpl.jena.sparql.engine.binding.Binding;

/**
 * @author cristianolongo
 *
 */
public class AsyncAggregationEngineTest {

	private static final ResultSet RESULT0 = createDummyResultSet();
	private static final ResultSet RESULT1 = createDummyResultSet();

	class TestFactory implements AggregatedQueryEngineFactory {

		private final QueryEngine emptyQueryEngine = new FixedQueryEngine("none", RESULT0);

		@Override
		public Collection<URL> getOntologies() {
			fail("unexpected method call");
			return null;
		}

		@Override
		public QueryEngine getEmpty() {
			return emptyQueryEngine;
		}

		@Override
		public Runnable getDownloadTask(OntologyDonwloadHandler handler) {
			fail("unexpected method call");
			return null;
		}

	}

	private final ExecutorService executor;

	/**
	 * 
	 */
	public AsyncAggregationEngineTest() {
		executor = Executors.newSingleThreadExecutor();
	}

	/**
	 * @param factory
	 * @return
	 */
	private AsyncAggregationEngine createTestSubject(final AggregatedQueryEngineFactory factory) {
		return new AsyncAggregationEngine(factory, executor);
	}

	/**
	 * Ensure that all the tasks submitted to the executor have been performed
	 */
	private void ensureAllTasksPerformed() {
		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		assertTrue(executor.shutdownNow().isEmpty());
	}

	/**
	 * Test that the aggregation engine starts in IDLE state
	 */
	@Test
	public void shouldEngineStartInIdleState() {
		final AsyncAggregationEngine testSubject = createTestSubject(new TestFactory());
		assertSame(AggregationEngine.State.IDLE, testSubject.getState());
	}

	/**
	 * An aggregation engine in idle state use as delegate the
	 * {@link QueryEngine} returned by the getEmpty method of the provided
	 * {@link QueryEngineFactory}
	 */
	@Test
	public void shouldIdleEngineUseEmptyQueryEngine() {
		final String query = "query";
		final QueryEngine queryEngine = new FixedQueryEngine(query, RESULT0);
		final AggregatedQueryEngineFactory factory = new TestFactory() {

			@Override
			public QueryEngine getEmpty() {
				return queryEngine;
			}
		};
		final AsyncAggregationEngine testSubject = createTestSubject(factory);
		assertSame(RESULT0, testSubject.execQuery(query));
	}

	/**
	 * Test performing build in IDLE state
	 */
	@Test
	public void testBuildingInIdleState() {
		final AsyncAggregationEngine testSubject = createTestSubject(new TestFactory() {
			@Override
			public Runnable getDownloadTask(OntologyDonwloadHandler handler) {
				return new Runnable() {

					@Override
					public void run() {
						// do nothing!
					}
				};
			}
		});
		testSubject.build();
		assertSame(AggregationEngine.State.BUILDING, testSubject.getState());
	}

	/**
	 * An aggregation engine in building state uses as delegate the
	 * {@link QueryEngine} returned by the getEmpty method of the provided
	 * {@link QueryEngineFactory}
	 */
	@Test
	public void shouldBuildEngineUseEmptyQueryEngine() {
		final String query = "query";
		final QueryEngine queryEngine = new FixedQueryEngine(query, RESULT0);
		final AggregatedQueryEngineFactory factory = new TestFactory() {

			@Override
			public QueryEngine getEmpty() {
				return queryEngine;
			}

			@Override
			public Runnable getDownloadTask(OntologyDonwloadHandler handler) {
				return new Runnable() {

					@Override
					public void run() {
						// do nothing!
					}
				};
			}
		};
		final AsyncAggregationEngine testSubject = createTestSubject(factory);
		assertSame(RESULT0, testSubject.execQuery(query));
	}

	/**
	 * Test receiving a complete notification in BUILDING state
	 */
	@Test
	public void testCompleteInBuildingState() {
		final String query = "testCompleteInBuildingStateQuery";
		final QueryEngine aggregatedQueryEngine = new FixedQueryEngine(query, RESULT1);
		final AsyncAggregationEngine testSubject = createTestSubject(new TestFactory() {
			@Override
			public Runnable getDownloadTask(final OntologyDonwloadHandler handler) {
				return new Runnable() {

					@Override
					public void run() {
						handler.complete(aggregatedQueryEngine);
					}
				};
			}
		});
		testSubject.build();
		ensureAllTasksPerformed();
		assertSame(AggregationEngine.State.READY, testSubject.getState());
	}

	/**
	 * @return
	 */
	private static ResultSet createDummyResultSet() {
		return new ResultSet() {

			@Override
			public QuerySolution nextSolution() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Binding nextBinding() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public QuerySolution next() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public int getRowNumber() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public List<String> getResultVars() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Model getResourceModel() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
