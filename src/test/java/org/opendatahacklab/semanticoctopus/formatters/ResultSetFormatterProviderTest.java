package org.opendatahacklab.semanticoctopus.formatters;

import static org.junit.Assert.assertEquals;

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
 * 
 * This file is part of Semantic Octopus.
 * 
 * Copyright 2017 Cristiano Longo, Antonio Pisasale
 *
 * Semantic Octopus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Semantic Octopus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

	private static final String XML_REPR = "<?xml version=\"1.0\"?>\n"
			+ "<sparql xmlns=\"http://www.w3.org/2005/sparql-results#\">\n"
			+ "  <head>\n"
			+ "    <variable name=\"s\"/>\n"
			+ "    <variable name=\"p\"/>\n"
			+ "    <variable name=\"o\"/>\n"
			+ "  </head>\n"
			+ "  <results>\n"
			+ "    <result>\n"
			+ "      <binding name=\"s\">\n"
			+ "        <uri>http://eu.ool.ex/resources#ool</uri>\n"
			+ "      </binding>\n"
			+ "      <binding name=\"p\">\n"
			+ "        <uri>http://www.w3.org/1999/02/22-rdf-syntax-ns#type</uri>\n"
			+ "      </binding>\n"
			+ "      <binding name=\"o\">\n"
			+ "        <uri>http://eu.ool.ex/resources#Person</uri>\n"
			+ "      </binding>\n"
			+ "    </result>\n"
			+ "  </results>\n"
			+ "</sparql>\n";

	private static final String JSON_REPR = "{\n"
			+ "  \"head\": {\n"
			+ "    \"vars\": [ \"s\" , \"p\" , \"o\" ]\n"
			+ "  } ,\n"
			+ "  \"results\": {\n"
			+ "    \"bindings\": [\n"
			+ "      {\n"
			+ "        \"s\": { \"type\": \"uri\" , \"value\": \"http://eu.ool.ex/resources#ool\" } ,\n"
			+ "        \"p\": { \"type\": \"uri\" , \"value\": \"http://www.w3.org/1999/02/22-rdf-syntax-ns#type\" } ,\n"
			+ "        \"o\": { \"type\": \"uri\" , \"value\": \"http://eu.ool.ex/resources#Person\" }\n"
			+ "      }\n"
			+ "    ]\n"
			+ "  }\n"
			+ "}\n";

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
	public void shouldBuildSPARQLJSONFormatter() throws IllegalMimeTypeException {
		checkFormatter(SPARQL_JSON_MT, createResultSet(), JSON_REPR);
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	public void shouldBuildJSONFormatter() throws IllegalMimeTypeException {
		checkFormatter(JSON_MT, createResultSet(), JSON_REPR);
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	public void shouldBuildSPARQLXMLFormatter() throws IllegalMimeTypeException {
		checkFormatter(SPARQL_XML_MT, createResultSet(), XML_REPR);
	}

	/**
	 * @throws IllegalMimeTypeException
	 *
	 */
	@Test
	public void shouldBuildXMLFormatter() throws IllegalMimeTypeException {
		checkFormatter(XML_MT, createResultSet(), XML_REPR);
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
