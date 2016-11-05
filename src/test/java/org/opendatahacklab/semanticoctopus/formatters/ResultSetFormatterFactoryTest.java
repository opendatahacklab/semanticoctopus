package org.opendatahacklab.semanticoctopus.formatters;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Ignore;
import org.junit.Test;

import com.hp.hpl.jena.query.ResultSet;

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

	private ResultSet createResultSet() {
		return mock(ResultSet.class); // TODO
	}

	/**
	 * @throws IllegalMimeTypeException
	 * 
	 */
	@Test
	public void shouldBuildCSVFormatter() throws IllegalMimeTypeException {
		checkFormatter(CSV_MT, createResultSet(), ""); // TODO
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
