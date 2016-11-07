package org.opendatahacklab.semanticoctopus.formatters;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

/**
 * Test class of {@link ResultSetFormatterFactory}
 *
 * @author OOL
 */
@Ignore
public class ResultSetFormatterFactoryTest {

	// Testbed
	private static final String CSV_MT = "text/csv";
	private static final String TSV_MT = "text/tab-separated-values";
	private static final String SPARQL_JSON_MT = "application/sparql-results+json";
	private static final String JSON_MT = "application/json";
	private static final String SPARQL_XML_MT = "application/sparql-results+xml";
	private static final String XML_MT = "application/xml";
	private static final String TEXT_MT = "text/plain"; // Unaccepted

	private static final String SUBJECT = "http://eu.ool.ex/resources#ools";
	private static final String PREDICATE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static final String OBJECT = "http://eu.ool.ex/resources#Person";
	private static final String SUBJ_VAR = "s";
	private static final String PRED_VAR = "p";
	private static final String OBJ_VAR = "o";

	private static final String COMMA = ",";
	private static final String TAB = "\t";
	private static final String A_CAPO = "\n";
	private static final String CSV_REPR = SUBJ_VAR + COMMA + PRED_VAR + COMMA + OBJ_VAR + A_CAPO +
					SUBJECT + COMMA + PREDICATE + COMMA + OBJECT + A_CAPO;

	private ResultSet createResultSet() {
		final QuerySolution solution = createQuerySolution();

		final ResultSet resultSet = mock(ResultSet.class);

		when(resultSet.hasNext()).thenReturn(true, false);
		when(resultSet.next()).thenReturn(solution, (QuerySolution) null);
		// when(resultSet.nextBinding()).thenReturn(new Binding(SUBJ_VAR, SUBJECT), new Binding(PRED_VAR, PREDICATE),
		// new Binding(OBJ_VAR, OBJECT), (Binding) null);
		when(resultSet.nextSolution()).thenReturn(solution, (QuerySolution) null);
		when(resultSet.getResultVars()).thenReturn(Arrays.asList(SUBJ_VAR, PRED_VAR, OBJ_VAR));
		when(resultSet.getRowNumber()).thenReturn(1);

		return resultSet;
	}

	/**
	 * @return
	 */
	private QuerySolution createQuerySolution() {
		final QuerySolutionMap solution = new QuerySolutionMap();
		solution.add(SUBJ_VAR, new ResourceImpl(SUBJECT));
		solution.add(PRED_VAR, new ResourceImpl(PREDICATE));
		solution.add(OBJ_VAR, new ResourceImpl(OBJECT));

		return solution;
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	public void shouldBuildCSVFormatter() throws IllegalMimeTypeException {
		checkFormatter(CSV_MT, createResultSet(), CSV_REPR);
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	public void shouldBuildTSVFormatter() throws IllegalMimeTypeException {
		checkFormatter(TSV_MT, createResultSet(), ""); // TODO
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	public void shouldBuildSPARQLJSONFormatter() throws IllegalMimeTypeException {
		checkFormatter(SPARQL_JSON_MT, createResultSet(), ""); // TODO
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	public void shouldBuildJSONFormatter() throws IllegalMimeTypeException {
		checkFormatter(JSON_MT, createResultSet(), ""); // TODO
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	public void shouldBuildSPARQLXMLFormatter() throws IllegalMimeTypeException {
		checkFormatter(SPARQL_XML_MT, createResultSet(), ""); // TODO
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	public void shouldBuildXMLFormatter() throws IllegalMimeTypeException {
		checkFormatter(XML_MT, createResultSet(), ""); // TODO
	}

	/**
	 *
	 */
	@Test(expected = IllegalMimeTypeException.class)
	public void shouldRaiseExceptionOnIllegalMimeType() throws IllegalMimeTypeException {
		ResultSetFormatterFactory.createFormatter(TEXT_MT);
	}

	/**
	 * Checks that a certain mime-type corresponds to specified formatter
	 *
	 * @param mimeType
	 * @param formatter
	 *
	 * @throws IllegalMimeTypeException
	 */
	private void checkFormatter(final String mimeType, final ResultSet resultSet, final String expectedFormattedResult)
					throws IllegalMimeTypeException {
		final ResultSetFormatter testSubject = ResultSetFormatterFactory.createFormatter(mimeType);
		assertEquals("Wrong mime type", mimeType, testSubject.getMimeType());

		final String formattedResult = testSubject.format(resultSet);
		assertEquals("Wrong result", expectedFormattedResult, formattedResult);
	}
}
