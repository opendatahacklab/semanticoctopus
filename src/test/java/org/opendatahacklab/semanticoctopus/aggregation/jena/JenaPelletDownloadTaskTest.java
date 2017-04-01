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
import org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletAggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.jena.JenaPelletSeqDownloadTask;
import org.opendatahacklab.semanticoctopus.aggregation.jena.JenaQueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDonwloadHandler;
import org.opendatahacklab.semanticoctopus.aggregation.jena.OntologyDownloadError;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * Test cases for the {@link JenaPelletAggregationEngine}
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
		final AtomicReference<OntModel> modelReference = new AtomicReference<OntModel>(null);
		final Runnable downloadTask = JenaPelletSeqDownloadTask.createFactory(ontologies)
				.getDownloadTask(new OntologyDonwloadHandler() {

					@Override
					public void error(OntologyDownloadError error) {
						fail(error.getCause().getMessage());
					}

					@Override
					public void complete(OntModel result) {
						modelReference.set(result);
					}
				});
		downloadTask.run();
		return new JenaQueryEngine(modelReference.get());
	}
}
