/**
 * 
 */
package org.opendatahacklab.semanticoctopus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.jena.query.ResultSet;
import org.junit.Test;

/**
 * Test cases for the {@link AggregationEngine}
 * 
 * @author Cristiano Longo
 *
 */
public class AggregationEngineTest {

	public final URL ontologyA;
	public final URL ontologyB;
	public final URL ontologyC;

	/**
	 * @throws MalformedURLException
	 * 
	 */
	public AggregationEngineTest() throws MalformedURLException {
		ontologyA = new URL("http://opendatahacklab.github.io/semanticoctopus/testbed/A.owl");
		ontologyB = new URL("http://opendatahacklab.github.io/semanticoctopus/testbed/B.owl");
		ontologyC = new URL("http://opendatahacklab.github.io/semanticoctopus/testbed/C.owl");
	}

	/**
	 * Check that if not ontology is provided as argument, the resulting
	 * ontology is empty.
	 */
	@Test
	public void shouldLoadNoOntology() {
		final List<URL> emptyList = Collections.emptyList();
		final AggregationEngine engine = new AggregationEngine(emptyList);
		final ResultSet result = engine.execQuery("SELECT * WHERE {?x ?y ?x}");
		assertEquals(0, result.getRowNumber());
	}
}
