/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.net.URL;
import java.util.Collection;

import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.
	 * OntologyDownloadTaskFactory#getEmpty()
	 */
	@Override
	public QueryEngine getEmpty() {
		final OntModel model = ModelFactory.createOntologyModel();
		return new JenaQueryEngine(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.
	 * OntologyDownloadTaskFactory#getDownloadTask(org.opendatahacklab.
	 * semanticoctopus.aggregation.async.OntologyDonwloadHandler)
	 */
	@Override
	public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler) {
		return new JenaPelletSeqDownloadTask(ontologyURLs, handler);
	}

}
