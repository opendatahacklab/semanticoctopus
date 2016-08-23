package org.opendatahacklab.semanticoctopus;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;

public class TestQuery {
	protected ArrayList<String> query = new ArrayList<String>();
	
	public TestQuery(ArrayList<String> query) {
		this.query = query;	
	}
	@Test
	public static void main(String[] args) throws MalformedURLException {
		URL ontology = new URL("http://protege.stanford.edu/ontologies/pizza/pizza.owl");

		URL A = new URL("http://opendatahacklab.github.io/semanticoctopus/testbed/A.owl");
		URL B = new URL("http://opendatahacklab.github.io/semanticoctopus/testbed/B.owl");
		URL C = new URL("http://opendatahacklab.github.io/semanticoctopus/testbed/C.owl");
		List<URL> ontologyURLs = new ArrayList<URL>();
		ontologyURLs.add(ontology);
		ontologyURLs.add(A);
		ontologyURLs.add(B);
		ontologyURLs.add(C);

		final AggregationEngine e = new AggregationEngine(ontologyURLs);
		
		ArrayList<String> array = new ArrayList<String>(); 
		String TESTBED_PREFIX = "PREFIX testbed:<http://opendatahacklab.org/semanticoctopus/testbed/>\n";
		
		/* select all */
		array.add(TESTBED_PREFIX + "SELECT ?s ?p ?o { ?s ?p ?o }");
		/* select all where s is equal as <http://opendatahacklab.github.io/semanticoctopus/testbed/a  */
		array.add(TESTBED_PREFIX + "SELECT ?p ?o { <http://opendatahacklab.github.io/semanticoctopus/testbed/a> ?p ?o }" );
		/* select all where s is equal as <http://opendatahacklab.github.io/semanticoctopus/testbed/b  */
		array.add(TESTBED_PREFIX + "SELECT ?p ?o { <http://opendatahacklab.github.io/semanticoctopus/testbed/b> ?p ?o }");
		/* select all where s is equal as <http://www.co-ode.org/ontologies/pizza/pizza.owl#Veneziana>  */
		array.add(TESTBED_PREFIX + "SELECT ?p ?o { <http://www.co-ode.org/ontologies/pizza/pizza.owl#Veneziana> <http://www.w3.org/2002/07/owl#disjointWith>  ?o }");
		
		TestQuery t = new TestQuery(array);
		for (String st : t.query) {
			ResultSet r = e.execQuery(st);
			ResultSetFormatter.out(r);
			System.out.println("*************************************");
		}
	}
}
