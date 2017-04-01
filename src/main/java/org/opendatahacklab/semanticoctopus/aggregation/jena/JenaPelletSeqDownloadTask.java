/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.net.URL;
import java.util.Collection;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDonwloadHandler;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDownloadError;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Perform a download using Jena for the model and Pellet for reasoning.
 * Ontologies are downloaded in a sequential order.
 * 
 * @author Cristiano Longo
 *
 */
public class JenaPelletSeqDownloadTask implements Runnable {

	private final Collection<URL> ontologyURLs;
	private final OntologyDonwloadHandler handler;

	/**
	 * @param ontologyURLs
	 *            ontologies to be downloaded
	 * @param handler
	 *            handle download results
	 */
	public JenaPelletSeqDownloadTask(final Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler) {
		this.ontologyURLs = ontologyURLs;
		this.handler = handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		final OntModel model = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		for (final URL u : ontologyURLs)
			try {
				model.read(u.toExternalForm());
			} catch (Exception e) {
				handler.error(new OntologyDownloadError(u, e));
				model.close();
			}
		handler.complete(new JenaQueryEngine(model));
	}
}
