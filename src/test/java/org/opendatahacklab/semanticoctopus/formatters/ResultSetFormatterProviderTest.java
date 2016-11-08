package org.opendatahacklab.semanticoctopus.formatters;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.impl.ResourceImpl;

/**
 * Test class of {@link ResultSetFormatterProvider}
 *
 * @author OOL
 */
public class ResultSetFormatterProviderTest {

	// Testbed
	private static final String CSV_MT = "text/csv";
	private static final String TSV_MT = "text/tab-separated-values";
	private static final String SPARQL_JSON_MT = "application/sparql-results+json";
	private static final String JSON_MT = "application/json";
	private static final String SPARQL_XML_MT = "application/sparql-results+xml";
	private static final String XML_MT = "application/xml";
	private static final String TEXT_MT = "text/plain"; // Unaccepted

	private static final String SUBJECT = "http://eu.ool.ex/resources#ool";
	private static final String PREDICATE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private static final String SHORT_PREDICATE = "rdf:type";
	private static final String OBJECT = "http://eu.ool.ex/resources#Person";
	private static final String QM = "?";
	private static final String SUBJ_VAR = "s";
	private static final String PRED_VAR = "p";
	private static final String OBJ_VAR = "o";

	private static final String SPACE = " ";
	private static final String LF = "\n";
	private static final String CR = "\r";
	private static final String A_CAPO = CR + LF;
	private static final String COMMA = ",";
	private static final String TAB = "\t";

	private static final String CSV_REPR = SUBJ_VAR + COMMA + PRED_VAR + COMMA + OBJ_VAR + A_CAPO +
					SUBJECT + COMMA + PREDICATE + COMMA + OBJECT + A_CAPO;
	private static final String TSV_REPR = QM + SUBJ_VAR + TAB + QM + PRED_VAR + TAB + QM + OBJ_VAR + LF +
					"<" + SUBJECT + ">" + TAB + SHORT_PREDICATE + TAB + "<" + OBJECT + ">" + LF;

	private static final String TRIPLE = QM + SUBJ_VAR + SPACE + QM + PRED_VAR + SPACE + QM + OBJ_VAR;
	private static final String QUERY = "SELECT " + TRIPLE + " WHERE {" + TRIPLE + "}";

	/**
	 * @return
	 */
	private ResultSetFormatterProvider createTestSubject() {
		return new ResultSetFormatterProvider();
	}

	/**
	 * @return
	 */
	private ResultSet createResultSet() {
		final Model model = ModelFactory.createDefaultModel();
		model.add(new ResourceImpl(SUBJECT), ResourceFactory.createProperty(PREDICATE), new ResourceImpl(OBJECT));

		final QueryExecution execution = QueryExecutionFactory.create(QueryFactory.create(QUERY), model);
		final ResultSet resultSet = execution.execSelect();

		return resultSet;
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
		checkFormatter(TSV_MT, createResultSet(), TSV_REPR);
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	@Ignore
	public void shouldBuildSPARQLJSONFormatter() throws IllegalMimeTypeException {
		checkFormatter(SPARQL_JSON_MT, createResultSet(), ""); // TODO
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	@Ignore
	public void shouldBuildJSONFormatter() throws IllegalMimeTypeException {
		checkFormatter(JSON_MT, createResultSet(), ""); // TODO
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	@Ignore
	public void shouldBuildSPARQLXMLFormatter() throws IllegalMimeTypeException {
		checkFormatter(SPARQL_XML_MT, createResultSet(), ""); // TODO
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	@Ignore
	public void shouldBuildXMLFormatter() throws IllegalMimeTypeException {
		checkFormatter(XML_MT, createResultSet(), ""); // TODO
	}

	/**
	 *
	 */
	@Test(expected = IllegalMimeTypeException.class)
	public void shouldRaiseExceptionOnIllegalMimeType() throws IllegalMimeTypeException {
		final ResultSetFormatterProvider testSubject = createTestSubject();
		testSubject.getFormatter(TEXT_MT);
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
		final ResultSetFormatterProvider testSubject = createTestSubject();
		final ResultSetFormatter formatter = testSubject.getFormatter(mimeType);
		assertEquals("Wrong mime type", mimeType, formatter.getMimeType());

		final String formattedResult = formatter.format(resultSet);
		assertEquals("Wrong result", expectedFormattedResult, formattedResult);
	}
}