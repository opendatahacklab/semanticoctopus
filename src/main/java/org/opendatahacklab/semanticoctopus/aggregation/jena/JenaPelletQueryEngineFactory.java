/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDonwloadHandler;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Factory to generate Jena Based query engines backed by ontologies downloaded
 * from the web
 * 
 * @author Cristiano Longo
 *
 */
public class JenaPelletQueryEngineFactory implements AggregatedQueryEngineFactory {
	private final Set<URL> urls;
	
	/**
	 * Create a factory to aggregate (via the download task) the specified set
	 * of ontologies
	 */
	public JenaPelletQueryEngineFactory(final Collection<URL> ontologyURLs) {
		urls = new TreeSet<>(new Comparator<URL>() {

			@Override
			public int compare(final URL o1, final URL o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		urls.addAll(ontologyURLs);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDownloadTaskFactory#getOntologies()
	 */
	@Override
	public Collection<URL> getOntologies() {
		return new ArrayList<>(urls);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDownloadTaskFactory#getEmpty()
	 */
	@Override
	public QueryEngine getEmpty() {
		final OntModel model = ModelFactory.createOntologyModel();
		return new JenaQueryEngine(model);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDownloadTaskFactory#getDownloadTask(org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDonwloadHandler)
	 */
	@Override
	public Runnable getDownloadTask(final OntologyDonwloadHandler handler) {
		return new JenaPelletSeqDownloadTask(urls, handler);
	}

}
