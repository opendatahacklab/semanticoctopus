/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.net.URL;
import java.util.Collection;

import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.opendatahacklab.semanticoctopus.OutputConsole;
import org.opendatahacklab.semanticoctopus.aggregation.async.InconsistenOntologyException;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDonwloadHandler;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDownloadError;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.ValidityReport;

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
	private final OutputConsole out;

	/**
	 * @param ontologyURLs
	 *            ontologies to be downloaded
	 * @param handler
	 *            handle download results
	 * @param out TODO
	 */
	public JenaPelletSeqDownloadTask(final Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler, OutputConsole out) {
		this.ontologyURLs = ontologyURLs;
		this.handler = handler;
		this.out = out;
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
				out.println("Loading " + u.toExternalForm());
				model.read(u.toExternalForm());
				out.println("Loaded " + u.toExternalForm());
			} catch (Exception e) {
				model.close();
				handler.error(new OntologyDownloadError(u, e));
				return;
			}
		out.println("Consistency check strted");
		final ValidityReport report = model.validate();
		if (report.isValid())
			handler.complete(new JenaQueryEngine(model));
		else {
			model.close();
			handler.error(new InconsistenOntologyException());
		}
		out.println("Consistency check complete");
	}
}
