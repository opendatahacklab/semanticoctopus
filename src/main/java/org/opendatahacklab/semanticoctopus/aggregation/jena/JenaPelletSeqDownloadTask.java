/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.mindswap.pellet.jena.PelletReasonerFactory;

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
		final OntModel baseModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		for (final URL u : ontologyURLs)
			try {
				baseModel.read(u.toExternalForm());
			} catch (Exception e) {
				handler.error(new OntologyDownloadError(u, e));
				baseModel.close();
			}
		handler.complete(baseModel);
	}

	/**
	 * Create a factory for {@link JenaPelletSeqDownloadTask} to download the
	 * specified set of ontologies
	 * 
	 * @param ontologyURLs
	 * @return
	 */
	public static OntologyDownloadTaskFactory createFactory(final Collection<URL> ontologyURLs) {
		final Set<URL> urls = new TreeSet<>(new Comparator<URL>() {

			@Override
			public int compare(final URL o1, final URL o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		urls.addAll(ontologyURLs);
		return new OntologyDownloadTaskFactory() {

			@Override
			public Collection<URL> getOntologies() {
				return urls;
			}

			@Override
			public Runnable getDownloadTask(OntologyDonwloadHandler handler) {
				return new JenaPelletSeqDownloadTask(ontologyURLs, handler);
			}
		};
	}
}
