package org.opendatahacklab.semanticoctopus.formatters;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Mime-type based formatters. Default package visibility is on purpose, in order to avoid misuses.
 * 
 * @author OOL
 */
enum MimeTypeBasedFormatter implements ResultSetFormatter {

	CSV("text/csv") {

		@Override
		public void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			com.hp.hpl.jena.query.ResultSetFormatter.outputAsCSV(outputStream, resultSet);
		}
	},

	TSV("text/tab-separated-values") {

		@Override
		public void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			com.hp.hpl.jena.query.ResultSetFormatter.outputAsTSV(outputStream, resultSet);
		}
	},

	SPARQL_JSON("application/sparql-results+json") {

		@Override
		public void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			com.hp.hpl.jena.query.ResultSetFormatter.outputAsJSON(outputStream, resultSet);
		}
	},

	JSON("application/json") {

		@Override
		public void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			SPARQL_JSON.formatInternally(outputStream, resultSet);
		}
	},

	SPARQL_XML("application/sparql-results+xml") {

		@Override
		public void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
			com.hp.hpl.jena.query.ResultSetFormatter.outputAsXML(outputStream, resultSet);
		}
	},
	XML("application/xml") {

		@Override
		public void formatInternally(final OutputStream outputStream, final ResultSet resultSet) {
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
