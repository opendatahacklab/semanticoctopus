/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

import java.net.URL;
import java.util.Collection;

import org.opendatahacklab.semanticoctopus.OutputConsole;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDonwloadHandler;

/**
 * Factory to create {@link Runnable} instances to load a model by aggregating a
 * fixed set of ontologies.
 * 
 * @author Cristiano Longo
 *
 */
public interface AggregatedQueryEngineFactory {

	/**
	 * @return a query engine providing an empty ontology
	 */
	QueryEngine getEmpty();

	/**
	 * Create a task which will perform download. Download results will be
	 * notified to the handler.
	 * 
	 * @param ontologyURLs
	 *            TODO
	 * @param handler
	 *            handle the download operation results
	 * @param out
	 *            TODO
	 * @return a runnable which will perform download
	 * @return
	 */
	Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler,
			final OutputConsole out);
}
