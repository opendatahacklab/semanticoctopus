package org.opendatahacklab.semanticoctopus;

import java.net.*;
import java.util.*;

import org.opendatahacklab.semanticoctopus.aggregation.*;

/**
 * Aggregates ontologies specified as parameters and starts server for querying against resulting ontology
 * 
 * @author Cristiano Longo, OOL
 */
public class SemanticOctopus {

	/**
	 * Aggregate the ontologies passed as command line parameters. The resulting ontology is sent on the standard
	 * output.
	 * 
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(final String[] args) throws MalformedURLException {
		if (args.length < 1)
			System.err.println("Usage: AggregationEngine <ontologyURL1> [<ontologyURL2> [<ontologyURL3> ...]]");
		final List<URL> ontologies = new ArrayList<URL>(args.length);

		for (final String ontologyUrl : args)
			ontologies.add(new URL(ontologyUrl));

		final AggregationEngine e = new SimpleAggregationEngine(ontologies);

		// TODO Start server

		e.write(System.out, "http://opendatahacklab.org/");

	}
}
