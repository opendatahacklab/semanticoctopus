/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.opendatahacklab.semanticoctopus.SystemOutputConsole;
import org.opendatahacklab.semanticoctopus.aggregation.AbstractAggregationTest;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.InconsistenOntologyException;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDonwloadHandler;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDownloadError;

/**
 * Test cases for the {@link AsyncAggregationEngine}
 * 
 * @author Cristiano Longo
 *
 */
public class JenaPelletDownloadTaskTest extends AbstractAggregationTest {

	/**
	 * @throws MalformedURLException
	 */
	public JenaPelletDownloadTaskTest() throws MalformedURLException {
		super();
	}

	/**
	 * Create a query engine whose underlying model is the result of the
	 * aggregation of the specified ontologies
	 * 
	 * @param ontologies
	 * @return
	 */
	@Override
	public QueryEngine createSuccesTestSubject(final Collection<URL> ontologies) {
		final AtomicReference<QueryEngine> queryReference = new AtomicReference<QueryEngine>(null);
		final Runnable downloadTask = (new JenaPelletQueryEngineFactory()).getDownloadTask(ontologies,
				new OntologyDonwloadHandler() {

					@Override
					public void complete(final QueryEngine result) {
						queryReference.set(result);
					}

					@Override
					public void error(final OntologyDownloadError error) {
						fail(error.getCause().getMessage());
					}

					@Override
					public void error(final InconsistenOntologyException error) {
						fail("Unexpected inconsistent ontology");
					}
				}, SystemOutputConsole.INSTANCE);
		downloadTask.run();
		return queryReference.get();
	}

	/**
	 * Test that downloading an inconsistent ontology cause an error
	 */
	@Test
	public void testInconsistentOntology() {
		final AtomicBoolean errorCalled = new AtomicBoolean();
		final Runnable downloadTask = (new JenaPelletQueryEngineFactory())
				.getDownloadTask(Collections.singletonList(inconsistentOntology), new OntologyDonwloadHandler() {

					@Override
					public void complete(final QueryEngine result) {
						fail("Unexpected call to complete");
					}

					@Override
					public void error(final OntologyDownloadError error) {
						fail(error.getCause().getMessage());
					}

					@Override
					public void error(final InconsistenOntologyException error) {
						assertFalse(errorCalled.getAndSet(true));
					}
				}, SystemOutputConsole.INSTANCE);
		downloadTask.run();
		assertTrue(errorCalled.get());
	}
	
	/**
	 * Test that a download error cause an error
	 */
	@Test
	public void testDowloadFailure() {
		final URL[] ontologies = {ontologyA, ontologyB, noSuchOntology, ontologyC};
		final AtomicBoolean errorCalled = new AtomicBoolean();
		final Runnable downloadTask = (new JenaPelletQueryEngineFactory())
				.getDownloadTask(Arrays.asList(ontologies), new OntologyDonwloadHandler() {

					@Override
					public void complete(final QueryEngine result) {
						fail("Unexpected call to complete");
					}

					@Override
					public void error(final OntologyDownloadError error) {
						assertFalse(errorCalled.getAndSet(true));
						assertEquals(noSuchOntology, error.getOntologyURL());
					}

					@Override
					public void error(final InconsistenOntologyException error) {
						fail("Unexpected inconsistent ontology");
					}
				}, SystemOutputConsole.INSTANCE);
		downloadTask.run();
		assertTrue(errorCalled.get());
	}

}
