package org.opendatahacklab.semanticoctopus.formatters;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Mime-type based formatters. Default package visibility is on purpose, in order to avoid misuses.
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
enum MimeTypeBasedFormatter implements ResultSetFormatter {

	ALL("*/*") {

		@Override
		protected void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			com.hp.hpl.jena.query.ResultSetFormatter.outputAsCSV(outputStream, resultSet);
		}
	},

	CSV("text/csv") {

		@Override
		protected void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			com.hp.hpl.jena.query.ResultSetFormatter.outputAsCSV(outputStream, resultSet);
		}
	},

	TSV("text/tab-separated-values") {

		@Override
		protected void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			com.hp.hpl.jena.query.ResultSetFormatter.outputAsTSV(outputStream, resultSet);
		}
	},

	SPARQL_JSON("application/sparql-results+json") {

		@Override
		protected void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			com.hp.hpl.jena.query.ResultSetFormatter.outputAsJSON(outputStream, resultSet);
		}
	},

	JSON("application/json") {

		@Override
		protected void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			SPARQL_JSON.formatInternally(outputStream, resultSet);
		}
	},

	SPARQL_XML("application/sparql-results+xml") {

		@Override
		protected void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			com.hp.hpl.jena.query.ResultSetFormatter.outputAsXML(outputStream, resultSet);
		}
	},
	XML("application/xml") {

		@Override
		protected void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			SPARQL_XML.formatInternally(outputStream, resultSet);
		}
	};

	// Associated mime-type
	private final String mimeType;

	/**
	 * @param mimeType
	 */
	private MimeTypeBasedFormatter(final String mimeType) {
		this.mimeType = mimeType;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.service.ResultSetFormatter#getMimeType() */
	@Override
	public String getMimeType() {
		return mimeType;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatter#format(com.hp.hpl.jena.query.ResultSet) */
	@Override
	public String format(final ResultSet resultSet) {
		final OutputStream outputStream = new ByteArrayOutputStream();
		formatInternally(outputStream, resultSet);
		final String formattedResultSet = outputStream.toString();

		return formattedResultSet;
	}

	protected abstract void formatInternally(OutputStream outputStream, ResultSet resultSet);
}
