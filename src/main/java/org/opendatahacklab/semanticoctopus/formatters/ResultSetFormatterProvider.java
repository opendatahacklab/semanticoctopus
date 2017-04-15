package org.opendatahacklab.semanticoctopus.formatters;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory of {@link ResultSetFormatter}s based on mime-type
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
	public ResultSetFormatter getFormatter(final String mimeType) throws IllegalMimeTypeException {
		final ResultSetFormatter resultSetFormatter = formatters.get(mimeType);

		if (resultSetFormatter != null)
			return resultSetFormatter;
		else
			throw new IllegalMimeTypeException(mimeType);
	}
}
