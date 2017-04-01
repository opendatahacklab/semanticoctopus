/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import org.opendatahacklab.semanticoctopus.aggregation.AbstractAggregationTest;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine;
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
	public QueryEngine createTestSubject(final Collection<URL> ontologies) {
		final AtomicReference<QueryEngine> queryReference = new AtomicReference<QueryEngine>(null);
		final Runnable downloadTask = JenaPelletSeqDownloadTask.createFactory(ontologies)
				.getDownloadTask(new OntologyDonwloadHandler() {

					@Override
					public void error(OntologyDownloadError error) {
						fail(error.getCause().getMessage());
					}

					@Override
					public void complete(QueryEngine result) {
						queryReference.set(result);
					}
				});
		downloadTask.run();
		return queryReference.get();
	}
}
