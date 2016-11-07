package org.opendatahacklab.semanticoctopus.formatters;

import static com.hp.hpl.jena.sparql.resultset.ResultSetFormat.syntaxCSV;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.resultset.ResultSetFormat;

/**
 * A factory of {@link ResultSetFormatter}s based on mime-type
 *
 * @author OOL
 */
public class ResultSetFormatterFactory {

	// Map from mime-types into formats
	private static Map<String, ResultSetFormat> formats = new Hashtable<String, ResultSetFormat>() {

		private static final long serialVersionUID = -831687800070232103L;

		{
			put("text/csv",syntaxCSV);
			// put("text/tab-separated-values",syntaxTSV);
			// put("application/sparql-results+json",syntaxJSON);
			// put("application/json",syntaxJSON);
			// put("application/sparql-results+xml",syntaxRDF_XML);
			// put("application/xml",syntaxRDF_XML);
		}
	};


	/**
	 * Builds a {@link ResultSetFormatter} according to specified mime-type.<br>
	 * Accepted mime-types are: <code>text/csv</code>, <code>text/tab-separated-values<code></code>,
	 * <code>application/sparql-results+json</code>,<code>application/json</code>,
	 * <code>application/sparql-results+xml</code>,<code>application/xml</code>
	 *
	 * @param mimeType
	 *
	 * @return
	 *
	 * @throws IllegalMimeTypeException
	 *             when mime-type is not accepted in the context of SPARQL SELECT queries
	 */
	public static ResultSetFormatter createFormatter(final String mimeType) throws IllegalMimeTypeException {
		// final ResultSetFormatter resultSetFormatter = formatters.get(mimeType);
		//
		// if (resultSetFormatter != null)
		// return resultSetFormatter;
		// else
		// throw new IllegalMimeTypeException(mimeType);

		// throw new RuntimeException("Unimplemented yet!");

		final ResultSetFormat resultSetFormat = formats.get(mimeType);

		if (resultSetFormat != null)
			return new ResultSetFormatter() {

			@Override
			public String getMimeType() {
				return mimeType;
			}

			@Override
			public String format(final ResultSet resultSet) {
				if (resultSetFormat == syntaxCSV) {
					final OutputStream outputStream = new ByteArrayOutputStream();
						com.hp.hpl.jena.query.ResultSetFormatter.outputAsCSV(outputStream, resultSet);
						return outputStream.toString();
				}
				return formatResult(resultSet, resultSetFormat);
			}
		};
		else
			throw new IllegalMimeTypeException(mimeType);
	}

	/**
	 * Formats specified result set according to a certain syntax
	 *
	 * @param resultSet
	 * @param format
	 *            Format related to syntax
	 *
	 * @return A string representing the result set in requested format
	 */
	private static String formatResult(final ResultSet resultSet, final ResultSetFormat format) {
		final OutputStream outputStream = new ByteArrayOutputStream();
		com.hp.hpl.jena.query.ResultSetFormatter.output(outputStream, resultSet, format);
		final String formattedResultSet = outputStream.toString();

		return formattedResultSet;
	}
}
