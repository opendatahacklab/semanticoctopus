package org.opendatahacklab.semanticoctopus;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;

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
	

	public AggregationEngine() {
		List<URL> ont = new ArrayList<URL>();
		try {
			URL ontologyA = new URL("http://opendatahacklab.org/semanticoctopus/testbed/A.owl");
			URL ontologyB = new URL("http://opendatahacklab.org/semanticoctopus/testbed/B.owl");
			URL ontologyC = new URL("http://opendatahacklab.org/semanticoctopus/testbed/C.owl");
			ont.add(ontologyA);
			ont.add(ontologyB);
			ont.add(ontologyC);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		this.ontologyURLs = Collections.unmodifiableList(ont);
		this.model = download(ontologyURLs);
	}
	/**
	 * 
	 */
	public AggregationEngine(final List<URL> ontologyURLs) {
		this.ontologyURLs = Collections.unmodifiableList(ontologyURLs);
		this.model = download(ontologyURLs);
	}

	/**
	 * Load a model aggregating a set of ontologies
	 * 
	 * @param ontologyURLs
	 * @return
	 */
	private OntModel download(final List<URL> ontologyURLs) {
		final OntModel baseModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		for (URL u : ontologyURLs)
			baseModel.read(u.toExternalForm());
		return baseModel;
	}

	/**
	 * Write the ontology on the standard output
	 */
	public void write(final OutputStream out, final String baseUri) {
		final OutputStreamWriter writer = new OutputStreamWriter(out);
		this.model.writeAll(writer, "RDF/XML-ABBREV", baseUri);
//		writer.flush();
//		writer.close();
	}


	/**
	 * Perform a query against the aggregated ontology
	 * 
	 * @param String
	 *            query
	 * @return
	 */
	public ResultSet execQuery(final String query) {
		final QueryExecution exe = QueryExecutionFactory.create(QueryFactory.create(query), model);
		return exe.execSelect();
	}
	
	/**
	 * Aggregate the ontologies passed as command line parameters. The resulting
	 * ontology is sent on the standard output.
	 * 
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException {
		if (args.length<1){
			System.err.println("Usage: AggregationEngine <ontologyURL1> [<ontologyURL2> [<ontologyURL3> ...]]");
		}
		final List<URL> ontologies = new ArrayList<URL>(args.length);
		
		for(String ontologyUrl : args)
			ontologies.add(new URL(ontologyUrl));
		
		final AggregationEngine e = new AggregationEngine(ontologies);
		e.write(System.out, "http://opendatahacklab.org/");
	}
}
