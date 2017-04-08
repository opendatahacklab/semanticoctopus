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
import java.util.concurrent.Executor;

import org.junit.Test;
import org.opendatahacklab.semanticoctopus.OutputConsole;
import org.opendatahacklab.semanticoctopus.SystemOutputConsole;
import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine.State;
import org.opendatahacklab.semanticoctopus.aggregation.ExplicitExecutor;
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
	private static final String QUERY2 = "query2";
	private static final ResultSet RESULT0 = createDummyResultSet();
	private static final ResultSet RESULT1 = createDummyResultSet();
	private static final ResultSet RESULT2 = createDummyResultSet();

	private final FixedQueryEngine ENGINE0 = new FixedQueryEngine(QUERY0, RESULT0);
	private final FixedQueryEngine ENGINE1 = new FixedQueryEngine(QUERY1, RESULT1);
	private final FixedQueryEngine ENGINE2 = new FixedQueryEngine(QUERY2, RESULT2);

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
		public Runnable getDownloadTask(final Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler, OutputConsole out) {
			return delegate.getDownloadTask(ontologyURLs, handler, out);
		}

	}

	/**
	 * A factory which cause test failures when getDownloadTask is invoked
	 * 
	 * @author cristianolongo
	 *
	 */
	class FailingFactory implements AggregatedQueryEngineFactory {

		@Override
		public QueryEngine getEmpty() {
			return ENGINE0;
		}

		@Override
		public Runnable getDownloadTask(final Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler, OutputConsole out) {
			fail("unexpected method call");
			return null;
		}
	}

	private final ExplicitExecutor executor;
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

		executor = new ExplicitExecutor();
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
			public Executor getDownloadExecutor() {
				return executor;
			}

			@Override
			public OutputConsole getOutputConsole() {
				return SystemOutputConsole.INSTANCE;
			}
		});
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
		goToBuilding();
		assertSame(AggregationEngine.State.BUILDING, testSubject.getState());
		executor.waitForCompletionForceWakeup();
	}
	
	/**
	 * Go to the building state from IDLE
	 */
	private void goToBuilding(){
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, OntologyDonwloadHandler handler, OutputConsole out) {
				return ExplicitExecutor.BLOCKING;
			}
		});
		testSubject.build();
		executor.waitForCompletion();
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
		goToBuilding();
		assertSame(RESULT0, testSubject.execQuery(QUERY0));
		executor.waitForCompletionForceWakeup();
	}

	/**
	 * Test that a second build call is ignored when in building state
	 */
	@Test
	public void shouldBuildingEngineIgnoreBuild() {
		goToBuilding();
		factory.setDelegate(new FailingFactory());
		testSubject.build();
		assertSame(State.BUILDING, testSubject.getState());
		executor.waitForCompletion();
		assertSame(State.BUILDING, testSubject.getState());
		executor.waitForCompletionForceWakeup();
		assertSame(State.BUILDING, testSubject.getState());
	}

	/**
	 * Test receiving a complete notification in BUILDING state
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testCompleteInBuildingState() throws MalformedURLException {
		gotToReady();
		assertSame(AggregationEngine.State.READY, testSubject.getState());
	}

	/**
	 * Move the engine to the ready state from IDLE
	 */
	private void gotToReady() {
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler, OutputConsole out) {
				return new Runnable() {

					@Override
					public void run() {
						handler.complete(ENGINE1);
					}
				};
			}
		});
		testSubject.build();
		executor.waitForCompletion();
		assertTrue(executor.isEmpty());
	}

	/**
	 * Test that a ready engine use the new query engine
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldReadyEngineUseTheNovelQueryEngine() throws MalformedURLException {
		gotToReady();
		assertSame(RESULT1, testSubject.execQuery(QUERY1));
	}

	/**
	 * Test that the previous engine has been disposed after a succesful build
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldOldEngineDisposedInReadyState() throws MalformedURLException {
		gotToReady();
		assertTrue(ENGINE0.isDisposed());
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
		assertSame(AggregationEngine.State.ERROR, testSubject.getState());
	}

	/**
	 * Test receiving an error notification in BUILDING state
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void testInconsistentOntologyInBuildingState() throws MalformedURLException {
		goToErrorInconsistentOntology();
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
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler, OutputConsole out) {
				return new Runnable() {

					@Override
					public void run() {
						handler.error(error);
					}
				};
			}
		});
		testSubject.build();
		executor.waitForCompletion();
		assertTrue(executor.isEmpty());
	}

	/**
	 * Take the engine in error state after the first build request because of an Inconsistent Ontology
	 * 
	 * @throws MalformedURLException
	 */
	private void goToErrorInconsistentOntology(){
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler, OutputConsole out) {
				return new Runnable() {

					@Override
					public void run() {
						handler.error(new InconsistenOntologyException());
					}
				};
			}
		});
		testSubject.build();
		executor.waitForCompletion();
		assertTrue(executor.isEmpty());
	}

	/**
	 * Test that if the first build fails, the empty query engine is still used
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldErrorEngineUseTheEmptyQueryEngine() throws MalformedURLException {
		goToError();
		assertSame(RESULT0, testSubject.execQuery(QUERY0));
	}

	/**
	 * Test that if the first build fails, the empty query engine is still used
	 * 
	 * @throws MalformedURLException
	 */
	@Test
	public void shouldInconsistentOntologyEngineUseTheEmptyQueryEngine() throws MalformedURLException {
		goToErrorInconsistentOntology();
		assertSame(RESULT0, testSubject.execQuery(QUERY0));
	}

	/**
	 * Test calling build a second time after a succesful one
	 */
	@Test
	public void testBuildInReadyState() {
		goToBuildingAfterReady();
		assertSame(AggregationEngine.State.BUILDING, testSubject.getState());
		executor.waitForCompletionForceWakeup();
		assertTrue(executor.isEmpty());
	}

	/**
	 * Perform a build request after a successful building
	 */
	private void goToBuildingAfterReady() {
		gotToReady();
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, OntologyDonwloadHandler handler, OutputConsole out) {
				return ExplicitExecutor.BLOCKING;
			}
		});
		testSubject.build();
		executor.waitForCompletion();
	}

	/**
	 * Test that during rebuilding the previous query engine is used
	 */
	@Test
	public void shouldPreviousQueryEngineUsedDuringRebuildAfterReady() {
		goToBuildingAfterReady();
		assertSame(RESULT1, testSubject.execQuery(QUERY1));
		executor.waitForCompletionForceWakeup();
		assertTrue(executor.isEmpty());
	}

	/**
	 * @throws MalformedURLException
	 * 
	 */
	@Test
	public void testBuildInErrorState() throws MalformedURLException {
		goToBuildingAfterError();
		assertSame(AggregationEngine.State.BUILDING, testSubject.getState());
		executor.waitForCompletionForceWakeup();
		assertTrue(executor.isEmpty());
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
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, OntologyDonwloadHandler handler, OutputConsole out) {
				return ExplicitExecutor.BLOCKING;
			}
		});
		testSubject.build();
		executor.waitForCompletion();
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
		assertSame(RESULT0, testSubject.execQuery(QUERY0));
		executor.waitForCompletionForceWakeup();
		assertTrue(executor.isEmpty());
	}
	
	/** 
	 * Test rebuild after a successful build
	 */
	@Test
	public void testSuccesfulRebuild(){
		goToRebuild();
		assertSame(State.READY, testSubject.getState());
	}

	/**
	 * Two succesful builds happened
	 */
	private void goToRebuild(){
		gotToReady();
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler, OutputConsole out) {
				return new Runnable() {

					@Override
					public void run() {
						handler.complete(ENGINE2);
					}
				};
			}
		});
		testSubject.build();
		executor.waitForCompletion();
		assertTrue(executor.isEmpty());		
	}

	/** 
	 * 
	 */
	@Test
	public void shouldNovelEngineUsedAfterSuccesfulRebuild(){
		goToRebuild();
		assertSame(RESULT2, testSubject.execQuery(QUERY2));
	}

	/** 
	 * 
	 */
	@Test
	public void shouldOldEngineDisposedAfterSuccesfulRebuild(){
		goToRebuild();
		assertTrue(ENGINE1.isDisposed());
		assertSame(RESULT2, testSubject.execQuery(QUERY2));
	}

	/** 
	 * Test rebuild after a successful build
	 * @throws MalformedURLException 
	 */
	@Test
	public void testSuccesfulRebuildAfterError() throws MalformedURLException{
		goToRebuildAfterError();
		assertSame(State.READY, testSubject.getState());
	}

	/**
	 * A succesful rebuild after error
	 * @throws MalformedURLException 
	 */
	private void goToRebuildAfterError() throws MalformedURLException{
		goToError();
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler, OutputConsole out) {
				return new Runnable() {

					@Override
					public void run() {
						handler.complete(ENGINE1);
					}
				};
			}
		});
		testSubject.build();
		executor.waitForCompletion();
		assertTrue(executor.isEmpty());		
	}

	/**
	 * @throws MalformedURLException  
	 * 
	 */
	@Test
	public void shouldNovelEngineUsedAfterSuccesfulRebuildAfterError() throws MalformedURLException{
		goToRebuildAfterError();
		assertSame(RESULT1, testSubject.execQuery(QUERY1));
	}

	/**
	 * @throws MalformedURLException  
	 * 
	 */
	@Test
	public void shouldOldEngineDisposedAfterSuccesfulRebuildAfterError() throws MalformedURLException{
		goToRebuildAfterError();
		assertTrue(ENGINE0.isDisposed());
	}

	/** 
	 * Test rebuild after a successful build
	 * @throws MalformedURLException 
	 */
	@Test
	public void testFailingRebuild() throws MalformedURLException{
		goToFailingRebuildAfterReady();
		assertSame(State.ERROR, testSubject.getState());
	}

	/**
	 * A build fails in error state
	 * @throws MalformedURLException 
	 */
	private void goToFailingRebuildAfterReady() throws MalformedURLException{
		gotToReady();
		final OntologyDownloadError error = new OntologyDownloadError(new URL("http://error.org"), "error");
		factory.setDelegate(new FailingFactory() {
			@Override
			public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler, OutputConsole out) {
				return new Runnable() {

					@Override
					public void run() {
						handler.error(error);
					}
				};
			}
		});
		testSubject.build();
		executor.waitForCompletion();
		assertTrue(executor.isEmpty());		
	}

	/**
	 * @throws MalformedURLException  
	 * 
	 */
	@Test
	public void shouldPreviousEngineUsedAfterFailingRebuild() throws MalformedURLException{
		goToFailingRebuildAfterReady();
		assertSame(RESULT1, testSubject.execQuery(QUERY1));
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
