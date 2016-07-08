package org.opendatahacklab.semanticoctopus;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Download an ontology from the internet, perform reasoning and provide a
 * sparql endpoint.
 * 
 * @author Cristiano Longo
 *
 */
public class AggregationEngine {

	private final URL ontologyURL;
	private final Model model;

	/**
	 * 
	 */
	public AggregationEngine(final URL ontologyURL) {
		this.ontologyURL = ontologyURL;
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	}

	/**
	 * Load the triples from the remote ontology URL.
	 */
	public void load() {
		System.out.println("Loading " + ontologyURL.toExternalForm());
		this.model.read(ontologyURL.toExternalForm(), null);
		this.model.write(System.out);
	}

	/**
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException {
		final URL ontologyURL = new URL("http://protege.stanford.edu/ontologies/pizza/pizza.owl");
		final AggregationEngine e = new AggregationEngine(ontologyURL);
		e.load();

		// TODO Auto-generated method stub

	}

}
