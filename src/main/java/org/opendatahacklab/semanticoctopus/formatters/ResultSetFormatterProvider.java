package org.opendatahacklab.semanticoctopus.formatters;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory of {@link ResultSetFormatter}s based on mime-type
 *
 * @author OOL
 */
public class ResultSetFormatterProvider {

	// Map from mime-types to formatters
	private static Map<String, ResultSetFormatter> formatters;

	// Initialize formatters' map
	static {
		formatters = new HashMap<>();
		for (final ResultSetFormatter formatter : MimeTypeBasedFormatter.values())
			formatters.put(formatter.getMimeType(), formatter);
	}

	/**
	 * Obtains a {@link ResultSetFormatter} according to specified mime-type.<br>
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
	public static ResultSetFormatter getFormatter(final String mimeType) throws IllegalMimeTypeException {
		final ResultSetFormatter resultSetFormatter = formatters.get(mimeType);

		if (resultSetFormatter != null)
			return resultSetFormatter;
		else
			throw new IllegalMimeTypeException(mimeType);
	}
}
