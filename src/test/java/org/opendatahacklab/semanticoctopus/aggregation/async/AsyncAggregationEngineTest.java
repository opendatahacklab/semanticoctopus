/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine.State;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine.Parameters;

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

	private static final String QUERY0 = "query0";
	private static final String QUERY1 = "query1";
	private static final ResultSet RESULT0 = createDummyResultSet();
	private static final ResultSet RESULT1 = createDummyResultSet();

	/**
	 * A factory which use a delegate. It starts with a {@link FailingFactory}
	 * delegate
	 * 
	 * @author cristiano longo
	 *
	 */
	class TestFactory implements AggregatedQueryEngineFactory {

		private AggregatedQueryEngineFactory delegate = new FailingFactory();

		void setDelegate(AggregatedQueryEngineFactory delegate) {
			this.delegate = delegate;
		}

		@Override
		public QueryEngine getEmpty() {
			return delegate.getEmpty();
		}

		@Override
		public Runnable getDownloadTask(final Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler) {
			return delegate.getDownloadTask(ontologyURLs, handler);
		}

	}

	/**
	 * A factory which cause test failures when getDownloadTask is invoked
	 * 
	 * @author cristianolongo
	 *
	 */
	class FailingFactory implements AggregatedQueryEngineFactory {

		private final QueryEngine emptyQueryEngine = new FixedQueryEngine(QUERY0, RESULT0);

		@Override
		public QueryEngine getEmpty() {
			return emptyQueryEngine;
		}

		@Override
		public Runnable getDownloadTask(final Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler) {
			fail("unexpected method call");
			return null;
		}
	}

	/**
	 * A download task which go to sleep when invoked
	 */
	private final Runnable blockingDownloadTask = new Runnable() {

		@Override
		public synchronized void run() {
			// just blocks
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				fail(e.getMessage());
//			}
		}
	};

	private final ExecutorService executor;
	private final TestFactory factory;
	private final AggregationEngine testSubject;

	/**
	 * @throws MalformedURLException
	 * 
	 */
	public AsyncAggregationEngineTest() throws MalformedURLException {
		// just some casual urls
		final Collection<URL> urls = new ArrayList<URL>();
		urls.add(new URL("http://example1.org"));
		urls.add(new URL("http://example2.org"));

		executor = Executors.newCachedThreadPool();
		factory = new TestFactory();
		testSubject = new AsyncAggregationEngine(new Parameters() {

			@Override
			public AggregatedQueryEngineFactory getQueryEngineFactory() {
				return factory;
			}

			@Override
			public Collection<URL> getOntologies() {
				return urls;
			}

			@Override
			public ExecutorService getDownloadExecutor() {
				return executor;
			}
		});
	}

	/**
	 * Ensure that all the tasks submitted to the executor have been performed
	 */
	private void ensureAllTasksPerformed() {
		synchronized (blockingDownloadTask) {
			blockingDownloadTask.notifyAll();
		}
		executor.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(executor.isTerminated());
	}

	/**
	 * Test that the aggregation engine starts in IDLE state
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldEngineStartInIdleState() throws MalformedURLException {
		assertSame(AggregationEngine.State.IDLE, testSubject.getState());
	}

	/**
	 * An aggregation engine in idle state use as delegate the
	 * {@link QueryEngine} returned by the getEmpty method of the provided
	 * {@link QueryEngineFactory}
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldIdleEngineUseEmptyQueryEngine() throws MalformedURLException {
		assertSame(RESULT0, testSubject.execQuery(QUERY0));
	}

	/**
	 * Test performing build in IDLE state
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testBuildingInIdleState() throws MalformedURLException {
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, OntologyDonwloadHandler handler) {
				return blockingDownloadTask;
			}
		});
		testSubject.build();
		assertSame(AggregationEngine.State.BUILDING, testSubject.getState());
		// unlock the download thread
		synchronized (blockingDownloadTask) {
			blockingDownloadTask.notifyAll();
		}
	}

	/**
	 * An aggregation engine in building state uses as delegate the
	 * {@link QueryEngine} returned by the getEmpty method of the provided
	 * {@link QueryEngineFactory}
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldBuildingEngineUseEmptyQueryEngine() throws MalformedURLException {
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, OntologyDonwloadHandler handler) {
				return blockingDownloadTask;
			}
		});
		testSubject.build();
		assertSame(RESULT0, testSubject.execQuery(QUERY0));
		// unlock the download thread
		synchronized (blockingDownloadTask) {
			blockingDownloadTask.notifyAll();
		}
	}

	/**
	 * Test that a second build call is ignored when in building state
	 */
	@Test
	public void shouldBuildingEngineIgnoreBuild() {
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, OntologyDonwloadHandler handler) {
				return blockingDownloadTask;
			}
		});
		testSubject.build();
		factory.setDelegate(new FailingFactory());
		testSubject.build();
		assertSame(State.BUILDING, testSubject.getState());
		// unlock the download thread
		synchronized (blockingDownloadTask) {
			blockingDownloadTask.notifyAll();
		}
	}

	/**
	 * Test receiving a complete notification in BUILDING state
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testCompleteInBuildingState() throws MalformedURLException {
		gotToReady();
		ensureAllTasksPerformed();
		assertSame(AggregationEngine.State.READY, testSubject.getState());
	}

	/**
	 * Move the engine to the ready state from IDLE
	 */
	private void gotToReady() {
		final QueryEngine aggregatedQueryEngine = new FixedQueryEngine(QUERY1, RESULT1);
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler) {
				return new Runnable() {

					@Override
					public void run() {
						handler.complete(aggregatedQueryEngine);
					}
				};
			}
		});
		testSubject.build();
	}

	/**
	 * Test that a ready engine use the new query engine
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldReadyEngineUseTheNovelQueryEngine() throws MalformedURLException {
		gotToReady();
		ensureAllTasksPerformed();
		assertSame(RESULT1, testSubject.execQuery(QUERY1));
	}

	/**
	 * Test receiving an error notification in BUILDING state
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testErrorInBuildingState() throws MalformedURLException {
		goToError();
		ensureAllTasksPerformed();
		assertSame(AggregationEngine.State.ERROR, testSubject.getState());
	}

	/**
	 * Take the engine in error state after the first build request
	 * 
	 * @throws MalformedURLException
	 */
	private void goToError() throws MalformedURLException {
		final OntologyDownloadError error = new OntologyDownloadError(new URL("http://error.org"), "error");
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler) {
				return new Runnable() {

					@Override
					public void run() {
						handler.error(error);
					}
				};
			}
		});
		testSubject.build();
	}

	/**
	 * Test that if the first build fails, the empty query engine is still used
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldErrorEngineUseTheEmptyQueryEngine() throws MalformedURLException {
		goToError();
		ensureAllTasksPerformed();
		assertSame(RESULT0, testSubject.execQuery(QUERY0));
	}

	/**
	 * Test calling build a second time after a succesful one
	 */
	@Test
	public void testBuildInReadyState() {
		goToBuildingAfterReady();
		ensureAllTasksPerformed();
		assertSame(AggregationEngine.State.BUILDING, testSubject.getState());
	}

	/**
	 * Perform a build request after a successful building
	 */
	private void goToBuildingAfterReady() {
		gotToReady();
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, OntologyDonwloadHandler handler) {
				return blockingDownloadTask;
			}
		});
		testSubject.build();
	}

	/**
	 * Test that during rebuilding the previous query engine is used
	 */
	@Test
	public void shouldPreviousQueryEngineUsedDuringRebuildAfterReady() {
		goToBuildingAfterReady();
		ensureAllTasksPerformed();
		assertSame(RESULT1, testSubject.execQuery(QUERY1));
		synchronized (blockingDownloadTask) {
			blockingDownloadTask.notifyAll();
		}
	}

	/**
	 * @throws MalformedURLException
	 * 
	 */
	@Test
	public void testBuildInErrorState() throws MalformedURLException {
		goToBuildingAfterError();
		ensureAllTasksPerformed();
		assertSame(AggregationEngine.State.BUILDING, testSubject.getState());
		synchronized (blockingDownloadTask) {
			blockingDownloadTask.notifyAll();
		}
	}

	/**
	 * Perform a build request after an error one
	 * 
	 * @throws MalformedURLException
	 */
	private void goToBuildingAfterError() throws MalformedURLException {
		goToError();
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, OntologyDonwloadHandler handler) {
				return blockingDownloadTask;
			}
		});
		testSubject.build();
	}

	/**
	 * Test that if the first build fails, the empty query engine is still used
	 * during the rebuild
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldErrorEngineUseTheEmptyQueryEngineDuringRebuild() throws MalformedURLException {
		goToBuildingAfterError();
		ensureAllTasksPerformed();
		assertSame(RESULT0, testSubject.execQuery(QUERY0));
		synchronized (blockingDownloadTask) {
			blockingDownloadTask.notifyAll();
		}
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
