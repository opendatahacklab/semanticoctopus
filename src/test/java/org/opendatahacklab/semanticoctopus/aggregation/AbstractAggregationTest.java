/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationTestUtils.RelativePair;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Generic tests to check that an aggregation mechanism works and performs
 * inferences
 * 
 * @author Cristiano Longo
 *
 *         This file is part of Semantic Octopus.
 * 
 *         Copyright 2017 Cristiano Longo, Antonio Pisasale
 *
 *         Semantic Octopus is free software: you can redistribute it and/or
 *         modify it under the terms of the GNU Lesser General Public License as
 *         published by the Free Software Foundation, either version 3 of the
 *         License, or (at your option) any later version.
 *
 *         Semantic Octopus is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         Lesser General Public License for more details.
 *
 *         You should have received a copy of the GNU Lesser General Public
 *         License along with this program. If not, see
 *         <http://www.gnu.org/licenses/>.
 */
public abstract class AbstractAggregationTest {

	private final AggregationTestUtils utils = new AggregationTestUtils();
	private static final String TESTBED_PREFIX = "PREFIX testbed:<http://opendatahacklab.org/semanticoctopus/testbed/>\n";
	private static final String RELATIVES_QUERY = TESTBED_PREFIX
			+ "SELECT ?x ?y { ?x testbed:relative ?y }  ORDER BY ?x ?y";


	protected final URL vocabulary;
	protected final URL ontologyA;
	protected final URL ontologyB;
	protected final URL ontologyC;
	protected final URL ontologySubclass;
	protected final URL ontologySubproperty;
	protected final URL inconsistentOntology;
	protected final URL noSuchOntology;
	private final String individualA;
	private final String individualB;
	private final String individualC;
	private final String individualD;

	/**
	 * @throws MalformedURLException
	 * 
	 */
	public AbstractAggregationTest() throws MalformedURLException {
		vocabulary = new URL("http://opendatahacklab.org/semanticoctopus/testbed/V.owl");
		ontologyA = new URL("http://opendatahacklab.org/semanticoctopus/testbed/A.owl");
		ontologyB = new URL("http://opendatahacklab.org/semanticoctopus/testbed/B.owl");
		ontologyC = new URL("http://opendatahacklab.org/semanticoctopus/testbed/C.owl");
		ontologySubclass = new URL("http://opendatahacklab.org/semanticoctopus/testbed/subclass.owl");
		ontologySubproperty = new URL("http://opendatahacklab.org/semanticoctopus/testbed/subproperty.owl");
		inconsistentOntology = new URL("http://opendatahacklab.org/semanticoctopus/testbed/inconsistent.owl");
		noSuchOntology = new URL("http://opendatahacklab.org/semanticoctopus/testbed/nosuchontology.owl");
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
	 * @throws InterruptedException
	 */
	private void testRelatives(final Collection<URL> ontologies, final List<RelativePair> expected)
			throws InterruptedException {
		final QueryEngine engine = createSuccesTestSubject(ontologies);
		utils.testRelatives(engine, expected);
		engine.dispose();
	}

	/**
	 * Create a query engine whose underlying model is the result of the
	 * aggregation of the specified ontologies
	 * 
	 * @param ontologies
	 * @return
	 * @throws InterruptedException
	 */
	public abstract QueryEngine createSuccesTestSubject(final Collection<URL> ontologies) throws InterruptedException;

	/**
	 * Convenience method
	 * 
	 * @param ontologies
	 * @param expected
	 * @throws InterruptedException
	 */
	private void testRelatives(final URL[] ontologies, final RelativePair[] expected) throws InterruptedException {
		testRelatives(Arrays.asList(ontologies), Arrays.asList(expected));
	}

	// SIMPLE QUERIES

	/**
	 * Check that if not ontology is provided as argument, the resulting
	 * ontology is empty.
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldLoadNoOntology() throws InterruptedException {
		testRelatives(new URL[0], new RelativePair[0]);
	}

	/**
	 * Check loading a single ontology
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldLoadOneOntologyWithARelativeAssertion() throws InterruptedException {
		final URL[] ontologies = { utils.ontologyA };
		final RelativePair[] expected = { new RelativePair(individualA, individualB) };
		testRelatives(ontologies, expected);
	}

	/**
	 * Check that turtle files are correctly recognized
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldRecognizeTurtle() throws InterruptedException {
		final URL[] ontologies = { utils.ontologyAturtle };
		final RelativePair[] expected = { new RelativePair(individualA, individualB) };
		testRelatives(ontologies, expected);
	}

	/**
	 * Check loading a multiple ontologies
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldLoadSeveralOntologiesWithSeveralRelativeAssertions() throws InterruptedException {
		final URL[] ontologies = { utils.ontologyA, utils.ontologyB, utils.ontologyC };
		final RelativePair[] expected = { new RelativePair(individualA, individualB),
				new RelativePair(individualB, individualC), new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);
	}

	// REASONING

	/**
	 * Test that no inference is performed when just the ontology A is loaded
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldInferNoTuplesWithA() throws InterruptedException {
		final URL[] ontologies = { utils.vocabulary, utils.ontologyA };
		final RelativePair[] expected = { new RelativePair(individualA, individualB) };
		testRelatives(ontologies, expected);

	}

	/**
	 * Test that no inference is performed when just the ontology B is loaded
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldInferNoTuplesWithB() throws InterruptedException {
		final URL[] ontologies = { utils.vocabulary, utils.ontologyB };
		final RelativePair[] expected = { new RelativePair(individualB, individualC) };
		testRelatives(ontologies, expected);

	}

	/**
	 * Test that no inference is performed when just the ontology C is loaded
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldInferNoTuplesWithC() throws InterruptedException {
		final URL[] ontologies = { utils.vocabulary, utils.ontologyC };
		final RelativePair[] expected = { new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);

	}

	/**
	 * Test that inferences are performed when loading ontology A and ontology B
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldInferTuplesWithAB() throws InterruptedException {
		final URL[] ontologies = { utils.vocabulary, utils.ontologyA, utils.ontologyB };
		final RelativePair[] expected = { new RelativePair(individualA, individualB),
				new RelativePair(individualA, individualC), new RelativePair(individualB, individualC) };
		testRelatives(ontologies, expected);
	}

	/**
	 * Test that disposing of an engine does not affect other ones.
	 * @throws InterruptedException 
	 * 
	 */
	@Test
	public void testSubsequentDownloads() throws InterruptedException{
		final URL[] ontologiesAB = { utils.vocabulary, utils.ontologyA, utils.ontologyB };
		final List<URL> ontologiesABlist = Arrays.asList(ontologiesAB);		
		final RelativePair[] expectedAB = { new RelativePair(individualA, individualB),
				new RelativePair(individualA, individualC), new RelativePair(individualB, individualC) };
		final List<RelativePair> expectedABlist = Arrays.asList(expectedAB);
		final QueryEngine e1 = createSuccesTestSubject(ontologiesABlist);
		utils.testRelatives(e1, expectedABlist);
		
		final URL[] ontologiesBC = { utils.vocabulary, utils.ontologyB, utils.ontologyC };
		final List<URL> ontologiesBClist = Arrays.asList(ontologiesBC);		
		final RelativePair[] expectedBC = { new RelativePair(individualB, individualC),
				new RelativePair(individualB, individualD), new RelativePair(individualC, individualD) };
		final List<RelativePair> expectedBClist = Arrays.asList(expectedBC);
		final QueryEngine e2 = createSuccesTestSubject(ontologiesBClist);
		e1.dispose();
		utils.testRelatives(e2, expectedBClist);
		e2.dispose();
	}
	/**
	 * Test that no inference is performed when ontologies A and C are loaded
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldInferNoTuplesWithAC() throws InterruptedException {
		final URL[] ontologies = { utils.vocabulary, utils.ontologyA, utils.ontologyC };
		final RelativePair[] expected = { new RelativePair(individualA, individualB),
				new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);

	}

	/**
	 * Test that inferences are performed when loading ontology B and ontology C
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldInferTuplesWithBC() throws InterruptedException {
		final URL[] ontologies = { utils.vocabulary, utils.ontologyB, utils.ontologyC };
		final RelativePair[] expected = { new RelativePair(individualB, individualC),
				new RelativePair(individualB, individualD), new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);
	}

	/**
	 * Test that inferences are performed when loading ontology A and ontology B
	 * and ontology C
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldInferTuplesWithABC() throws InterruptedException {
		final URL[] ontologies = { utils.vocabulary, utils.ontologyA, utils.ontologyB, utils.ontologyC };
		final RelativePair[] expected = { new RelativePair(individualA, individualB),
				new RelativePair(individualA, individualC), new RelativePair(individualA, individualD),
				new RelativePair(individualB, individualC), new RelativePair(individualB, individualD),
				new RelativePair(individualC, individualD) };
		testRelatives(ontologies, expected);
	}

	/**
	 * Test for reasoning in presence of subclass assertion
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldInferMemberOfSuperclasses() throws InterruptedException {
		final QueryEngine engine = createSuccesTestSubject(Collections.singletonList(utils.ontologySubclass));
		engine.write(System.out, "http://example.org");
		final ResultSet actual = engine.execQuery(TESTBED_PREFIX + "SELECT ?x { ?x a testbed:B }  ORDER BY ?x ?y");
		assertTrue(actual.hasNext());
		final String resultItem = actual.next().getResource("?x").getURI();
		assertEquals("http://opendatahacklab.org/semanticoctopus/testbed/a", resultItem);
		assertFalse(actual.hasNext());
		engine.dispose();
	}

	/**
	 * Test for reasoning in presence of subproperty assertion
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldInferRelationBecauseOfSubproperty() throws InterruptedException {
		final QueryEngine engine = createSuccesTestSubject(Collections.singletonList(utils.ontologySubproperty));
		engine.write(System.out, "http://example.org");
		final ResultSet actual = engine.execQuery(TESTBED_PREFIX + "SELECT ?x ?y { ?x testbed:q ?y }");
		assertTrue(actual.hasNext());
		final QuerySolution s = actual.next();
		assertEquals(individualA, s.getResource("?x").getURI());
		assertEquals(individualB, s.getResource("?y").getURI());
		assertFalse(actual.hasNext());
		engine.dispose();
	}

	/**
	 * A QueryParseException is thrown is the submitted string is not a valid
	 * SPARQL query
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void shouldThrowAnExceptionOnInvalidQuery() throws InterruptedException {
		try {
			final QueryEngine engine = createSuccesTestSubject(Collections.singletonList(utils.ontologySubproperty));
			engine.execQuery("An invalid query string");
			fail("expected exception not thrown");
		} catch (final QueryParseException e) {
			// expected exception
		}
	}

}
