package org.opendatahacklab.semanticoctopus;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Download an ontology from the internet, perform reasoning and provide a
 * sparql endpoint.
 * 
 * @author Cristiano Longo
 *
 */
public class AggregationEngine {

	private List<URL> ontologyURLs;
	private final OntModel model;

	/**
	 * 
	 */
	public AggregationEngine(final List<URL> ontologyURLs) {
		this.ontologyURLs = Collections.unmodifiableList(ontologyURLs);
		final OntModel baseModel = download(ontologyURLs);
		this.model = baseModel;
		this.model.rebind();
	}

	/**
	 * Load a model aggregating a set of ontologies
	 * 
	 * @param ontologyURLs
	 * @return
	 */
	private OntModel download(final List<URL> ontologyURLs) {
		final OntModel baseModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		for (URL u : ontologyURLs) {
			System.out.println("Loading " + u.toExternalForm());
			this.model.read(u.toExternalForm(), null);
		}
		return baseModel;
	}
	
	/**
	 * Write the ontology on the standard output
	 */
	public void write() {
		this.model.write(System.out, "RDF/XML-ABBREV");
	}

	/**
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException {
		// URL ontology = new
		// URL("http://protege.stanford.edu/ontologies/pizza/pizza.owl");

		URL A = new URL("http://opendatahacklab.github.io/semanticoctopus/testbed/A.owl");
		URL B = new URL("http://opendatahacklab.github.io/semanticoctopus/testbed/B.owl");
		URL C = new URL("http://opendatahacklab.github.io/semanticoctopus/testbed/C.owl");
		List<URL> ontologyURLs = new ArrayList<URL>();
		ontologyURLs.add(A);
		ontologyURLs.add(B);
		ontologyURLs.add(C);

		final AggregationEngine e = new AggregationEngine(ontologyURLs);
		String query1 = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX my: <http://www.semanticweb.org/rohit/ontologies/2014/4/untitled-ontology-10#> SELECT  ?ind WHERE { ?ind rdf:type my:Student .}";
		e.execQuery(query1);
		// e.createFile();
	}

	/**
	 * @param String
	 *            query
	 * @return
	 */
	public ResultSet execQuery(String query) {
		Query query1 = QueryFactory.create(query);
		QueryExecution exe = QueryExecutionFactory.create(query1, model);
		ResultSet results = exe.execSelect();
		return results;
	}
}
