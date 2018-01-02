/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Utilities for aggregation tests which uses online resources (some sort of
 * integration tests)
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
 *
 */
public class AggregationTestUtils {

	private static final String TESTBED_PREFIX = "PREFIX testbed:<http://opendatahacklab.org/semanticoctopus/testbed/>\n";
	private static final String RELATIVES_QUERY = TESTBED_PREFIX
			+ "SELECT ?x ?y { ?x testbed:relative ?y }  ORDER BY ?x ?y";

	/**
	 * An item of the result of a query about relatives
	 * 
	 * @author Cristiano Longo
	 *
	 */
	public static class RelativePair {

		final String x;
		final String y;

		public RelativePair(final String x, final String y) {
			this.x = x;
			this.y = y;
		}
	}

	public final URL vocabulary;
	public final URL ontologyA;
	public final URL ontologyAturtle;
	public final URL ontologyB;
	public final URL ontologyC;
	public final URL ontologySubclass;
	public final URL ontologySubproperty;
	public final URL inconsistentOntology;
	public final URL noSuchOntology;
	public final String individualA;
	public final String individualB;
	public final String individualC;
	public final String individualD;

	/**
	 * @throws MalformedURLException
	 * 
	 */
	public AggregationTestUtils() throws MalformedURLException {
		vocabulary = new URL("http://opendatahacklab.org/semanticoctopus/testbed/V.owl");
		ontologyA = new URL("http://opendatahacklab.org/semanticoctopus/testbed/A.owl");
		ontologyAturtle = new URL("http://opendatahacklab.org/semanticoctopus/testbed/A.ttl");
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
	 * aggregating a set of ontologies and using the specified engine.
	 * 
	 * @param engine
	 * @param expected
	 *            pairs expected to be returned as result of the relative query
	 * @throws InterruptedException
	 */
	public void testRelatives(final QueryEngine engine, final List<RelativePair> expected) throws InterruptedException {
		engine.write(System.out, "http://example.org");
		final ResultSet actual = engine.execQuery(RELATIVES_QUERY);
		final Iterator<RelativePair> expectedIt = expected.iterator();
		int n = 0;
		while (expectedIt.hasNext()) {
			n++;
			final RelativePair expectedPair = expectedIt.next();
			assertTrue("Too less pairs returned rows=" + n, actual.hasNext());
			final QuerySolution actualPair = actual.next();
			final String actualx = actualPair.get("x").asResource().getURI();
			final String actualy = actualPair.get("y").asResource().getURI();
			System.out.println("Expected (x=" + expectedPair.x + ",y=" + expectedPair.y + "); Actual (x=" + actualx
					+ ",y=" + actualy + ")");
			assertEquals("row=" + n, expectedPair.x, actualx);
			assertEquals("row=" + n, expectedPair.y, actualy);
		}
		assertFalse("Too much pairs returned. Rows=" + n, actual.hasNext());
	}
}
