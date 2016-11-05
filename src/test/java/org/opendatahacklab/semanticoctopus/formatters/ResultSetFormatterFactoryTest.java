package org.opendatahacklab.semanticoctopus.formatters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.opendatahacklab.semanticoctopus.formatters.MimeTypeBasedFormatter.CSV;
import static org.opendatahacklab.semanticoctopus.formatters.MimeTypeBasedFormatter.JSON;
import static org.opendatahacklab.semanticoctopus.formatters.MimeTypeBasedFormatter.SPARQL_JSON;
import static org.opendatahacklab.semanticoctopus.formatters.MimeTypeBasedFormatter.SPARQL_XML;
import static org.opendatahacklab.semanticoctopus.formatters.MimeTypeBasedFormatter.TSV;
import static org.opendatahacklab.semanticoctopus.formatters.MimeTypeBasedFormatter.XML;

import org.junit.Test;

/**
 * Test class of {@link ResultSetFormatterFactory}
 * 
 * @author OOL
 */
public class ResultSetFormatterFactoryTest {

	// Testbed
	private static final String CSV_MT = "text/csv";
	private static final String TSV_MT = "text/tab-separated-values";
	private static final String SPARQL_JSON_MT = "application/sparql-results+json";
	private static final String JSON_MT = "application/json";
	private static final String SPARQL_XML_MT = "application/sparql-results+xml";
	private static final String XML_MT = "application/xml";
	private static final String TEXT_MT = "text/plain"; // Unaccepted

	/**
	 * @throws IllegalMimeTypeException
	 * 
	 */
	@Test
	public void shouldBuildCSVFormatter() throws IllegalMimeTypeException {
		checkFormatter(CSV_MT, CSV);
	}

	/**
	 * @throws IllegalMimeTypeException
	 * 
	 */
	@Test
	public void shouldBuildTSVFormatter() throws IllegalMimeTypeException {
		checkFormatter(TSV_MT, TSV);
	}
	/**
	 * @throws IllegalMimeTypeException
	 * 
	 */
	@Test
	public void shouldBuildSPARQLJSONFormatter() throws IllegalMimeTypeException {
		checkFormatter(SPARQL_JSON_MT, SPARQL_JSON);
	}


	/**
	 * @throws IllegalMimeTypeException
	 * 
	 */
	@Test
	public void shouldBuildJSONFormatter() throws IllegalMimeTypeException {
		checkFormatter(JSON_MT, JSON);
	}

	/**
	 * @throws IllegalMimeTypeException
	 * 
	 */
	@Test
	public void shouldBuildSPARQLXMLFormatter() throws IllegalMimeTypeException {
		checkFormatter(SPARQL_XML_MT, SPARQL_XML);
	}

	/**
	 * @throws IllegalMimeTypeException
	 * 
	 */
	@Test
	public void shouldBuildXMLFormatter() throws IllegalMimeTypeException {
		checkFormatter(XML_MT, XML);
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
	private void checkFormatter(final String mimeType, final MimeTypeBasedFormatter formatter)
			throws IllegalMimeTypeException {
		final ResultSetFormatter testSubject = ResultSetFormatterFactory.createFormatter(mimeType);

		assertSame("Wrong formatter", formatter, testSubject);
		assertEquals("Wrong mime type", mimeType, testSubject.getMimeType());
	}
}
