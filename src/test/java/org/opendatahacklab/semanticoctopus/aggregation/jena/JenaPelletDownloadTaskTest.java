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
 * This file is part of Semantic Octopus.
 * 
 * Copyright 2017 Cristiano Longo, Antonio Pisasale
 *
 * Semantic Octopus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Semantic Octopus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
