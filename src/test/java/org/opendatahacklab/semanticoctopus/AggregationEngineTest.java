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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Test cases for the {@link AggregationEngine}
 * 
 * @author Cristiano Longo
 *
 */
public class AggregationEngineTest {
	private static final String TESTBED_PREFIX = "PREFIX testbed:<http://opendatahacklab.org/semanticoctopus/testbed/>\n";
	private static final String RELATIVES_QUERY = TESTBED_PREFIX
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
	private final URL ontologySubclass;
	private final URL ontologySubproperty;
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
		ontologySubclass = new URL("http://opendatahacklab.org/semanticoctopus/testbed/subclass.owl");
		ontologySubproperty = new URL("http://opendatahacklab.org/semanticoctopus/testbed/subproperty.owl");
		individualA = "http://opendatahacklab.org/semanticoctopus/testbed/a";
		individualB = "http://opendatahacklab.org/semanticoctopus/testbed/b";
		individualC = "http://opendatahacklab.org/semanticoctopus/testbed/c";
		individualD = "http://opendatahacklab.org/semanticoctopus/testbed/d";
	}

	/**
	 * Perform a query about relatives against the ontology obtained by
	 * aggregating a set of ontologies.
	 * 
	 * @param ontologies
	 *            URL of the ontologies to be aggregated
	 * @param expected
	 *            pairs expected to be returned as result of the relative query
	 */
	private void testRelatives(final List<URL> ontologies, final List<RelativePair> expected) {
		final AggregationEngine engine = new AggregationEngine(ontologies);
		engine.write(System.out, "http://example.org");
		final ResultSet actual = engine.execQuery(RELATIVES_QUERY);
		final Iterator<RelativePair> expectedIt = expected.iterator();
		int n = 0;
		while (expectedIt.hasNext()) {
			n++;
			final RelativePair expectedPair = expectedIt.next();
			assertTrue("Too less pairs returned rows=" + n, actual.hasNext());
			final QuerySolution actualPair = actual.next();
			assertEquals("row=" + n, expectedPair.x, actualPair.get("x").asResource().getURI());
			assertEquals("row=" + n, expectedPair.y, actualPair.get("y").asResource().getURI());
		}
		assertFalse("Too much pairs returned. Rows=" + n, actual.hasNext());
	}

	/**
	 * Convenience method
	 * 
	 * @param ontologies
	 * @param expected
	 */
	private void testRelatives(final URL[] ontologies, final RelativePair[] expected) {
		testRelatives(Arrays.asList(ontologies), Arrays.asList(expected));
	}

	// SIMPLE QUERIES

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
		final URL[] ontologies = { ontologyA };
		final RelativePair[] expected = { new RelativePair(individualA, individualB) };
		testRelatives(ontologies, expected);
	}

	/**
	 * Check loading a multiple ontologies
	 */
	@Test
	public void shouldLoadSeveralOntologiesWithSeveralRelativeAssertions() {
		final URL[] ontologies = { ontologyA, ontologyB, ontologyC };
		final RelativePair[] expected = { new RelativePair(individualA, individualB),
				new RelativePair(individualB, individualC), new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);
	}

	// REASONING

	/**
	 * Test that no inference is performed when just the ontology A is loaded
	 */
	@Test
	public void shouldInferNoTuplesWithA() {
		final URL[] ontologies = { vocabulary, ontologyA };
		final RelativePair[] expected = { new RelativePair(individualA, individualB) };
		testRelatives(ontologies, expected);

	}

	/**
	 * Test that no inference is performed when just the ontology B is loaded
	 */
	@Test
	public void shouldInferNoTuplesWithB() {
		final URL[] ontologies = { vocabulary, ontologyB };
		final RelativePair[] expected = { new RelativePair(individualB, individualC) };
		testRelatives(ontologies, expected);

	}

	/**
	 * Test that no inference is performed when just the ontology C is loaded
	 */
	@Test
	public void shouldInferNoTuplesWithC() {
		final URL[] ontologies = { vocabulary, ontologyC };
		final RelativePair[] expected = { new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);

	}

	/**
	 * Test that inferences are performed when loading ontology A and ontology B
	 */
	@Test
	public void shouldInferTuplesWithAB() {
		final URL[] ontologies = { vocabulary, ontologyA, ontologyB };
		final RelativePair[] expected = { new RelativePair(individualA, individualB),
				new RelativePair(individualA, individualC), new RelativePair(individualB, individualC) };
		testRelatives(ontologies, expected);
	}

	/**
	 * Test that no inference is performed when ontologies A and C are loaded
	 */
	@Test
	public void shouldInferNoTuplesWithAC() {
		final URL[] ontologies = { vocabulary, ontologyA, ontologyC };
		final RelativePair[] expected = { new RelativePair(individualA, individualB),
				new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);

	}

	/**
	 * Test that inferences are performed when loading ontology B and ontology C
	 */
	@Test
	public void shouldInferTuplesWithBC() {
		final URL[] ontologies = { vocabulary, ontologyB, ontologyC };
		final RelativePair[] expected = { new RelativePair(individualB, individualC),
				new RelativePair(individualB, individualD), new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);
	}

	/**
	 * Test that inferences are performed when loading ontology A and ontology B
	 * and ontology C
	 */
	@Test
	public void shouldInferTuplesWithABC() {
		final URL[] ontologies = { vocabulary, ontologyA, ontologyB, ontologyC };
		final RelativePair[] expected = { new RelativePair(individualA, individualB),
				new RelativePair(individualA, individualC), new RelativePair(individualA, individualD),
				new RelativePair(individualB, individualC), new RelativePair(individualB, individualD),
				new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);
	}

	/**
	 * Test for reasoning in presence of subclass assertion
	 */
	@Test
	public void shouldInferMemberOfSuperclasses() {
		final AggregationEngine engine = new AggregationEngine(Collections.singletonList(ontologySubclass));
		engine.write(System.out, "http://example.org");
		final ResultSet actual = engine.execQuery(TESTBED_PREFIX + "SELECT ?x { ?x a testbed:B }  ORDER BY ?x ?y");
		assertTrue(actual.hasNext());
		final String resultItem = actual.next().getResource("?x").getURI();
		assertEquals("http://opendatahacklab.org/semanticoctopus/testbed/a", resultItem);
		assertFalse(actual.hasNext());
	}

	/**
	 * Test for reasoning in presence of subproperty assertion
	 */
	@Test
	public void shouldInferRelationBecauseOfSubproperty() {
		final AggregationEngine engine = new AggregationEngine(Collections.singletonList(ontologySubproperty));
		engine.write(System.out, "http://example.org");
		final ResultSet actual = engine.execQuery(TESTBED_PREFIX
				+ "SELECT ?x ?y { ?x testbed:q ?y }");
		assertTrue(actual.hasNext());
		final QuerySolution s = actual.next();
		assertEquals(individualA, s.getResource("?x").getURI());
		assertEquals(individualB, s.getResource("?y").getURI());
		assertFalse(actual.hasNext());
	}

}
