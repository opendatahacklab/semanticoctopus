/**
 * 
 */
package org.opendatahacklab.semanticoctopus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.junit.Test;

/**
 * Test cases for the {@link AggregationEngine}
 * 
 * @author Cristiano Longo
 *
 */
public class AggregationEngineTest {
	private static final String RELATIVES_QUERY = "PREFIX testbed:<http://opendatahacklab.org/semanticoctopus/testbed/>\n"
			+ "SELECT ?x ?y { ?x testbed:relative ?y }  ORDER BY ?x ?y";

	/**
	 * An item of the result of a query about relatives
	 * 
	 * @author Cristiano Longo
	 *
	 */
	class RelativePair {
		final String x;
		final String y;

		RelativePair(final String x, final String y) {
			this.x = x;
			this.y = y;
		}
	}

	private final URL vocabulary;
	private final URL ontologyA;
	private final URL ontologyB;
	private final URL ontologyC;
	private final String individualA;
	private final String individualB;
	private final String individualC;
	private final String individualD;

	/**
	 * @throws MalformedURLException
	 * 
	 */
	public AggregationEngineTest() throws MalformedURLException {
		vocabulary = new URL("http://opendatahacklab.org/semanticoctopus/testbed/V.owl");		
		ontologyA = new URL("http://opendatahacklab.org/semanticoctopus/testbed/A.owl");
		ontologyB = new URL("http://opendatahacklab.org/semanticoctopus/testbed/B.owl");
		ontologyC = new URL("http://opendatahacklab.org/semanticoctopus/testbed/C.owl");
		individualA = "http://opendatahacklab.org/semanticoctopus/testbed/a";
		individualB = "http://opendatahacklab.org/semanticoctopus/testbed/b";
		individualC = "http://opendatahacklab.org/semanticoctopus/testbed/c";
		individualD = "http://opendatahacklab.org/semanticoctopus/testbed/d";
	}

	/**
	 * Perform a query about relatives against the ontology obtained by
	 * aggregating a set of ontologies.
	 * 
	 * @param ontologies URL of the ontologies to be aggregated
	 * @param expected pairs expected to be returned as result of the relative query
	 */
	private void testRelatives(final List<URL> ontologies, final List<RelativePair> expected) {
		final AggregationEngine engine = new AggregationEngine(ontologies);
		engine.write();
		final ResultSet actual = engine.execQuery(RELATIVES_QUERY);
		final Iterator<RelativePair> expectedIt = expected.iterator();
		while(expectedIt.hasNext()){
			final RelativePair expectedPair = expectedIt.next();
			System.out.println(expectedPair.x+" "+expectedPair.y);
			assertTrue("Too less pairs returned", actual.hasNext());
			final QuerySolution actualPair = actual.next();
			assertEquals(expectedPair.x, actualPair.get("x").asResource().getURI());
			assertEquals(expectedPair.y, actualPair.get("y").asResource().getURI());
		}
		assertFalse("Too much pairs returned", actual.hasNext());
	}

	/**
	 * Conveniente method
	 * 
	 * @param ontologies
	 * @param expected
	 */
	private void testRelatives(final URL[] ontologies, final RelativePair[] expected) {
		testRelatives(Arrays.asList(ontologies), Arrays.asList(expected));
	}
	
	/**
	 * Check that if not ontology is provided as argument, the resulting
	 * ontology is empty.
	 */
	@Test
	public void shouldLoadNoOntology() {
		testRelatives(new URL[0], new RelativePair[0]);
	}

	/**
	 * Check loading a single ontology
	 */
	@Test
	public void shouldLoadOneOntologyWithARelativeAssertion() {
		final URL[] ontologies = {ontologyA};
		final RelativePair[] expected = {new RelativePair(individualA, individualB)};
		testRelatives(ontologies, expected);
	}

	/**
	 * Check loading a multiple ontologies
	 */
	@Test
	public void shouldLoadSeveralOntologiesWithSeveralRelativeAssertions() {
		final URL[] ontologies = {ontologyA, ontologyB, ontologyC};
		final RelativePair[] expected = {new RelativePair(individualA, individualB),
				new RelativePair(individualB, individualC),
				new RelativePair(individualC, individualD)};
		testRelatives(ontologies, expected);
	}

}
