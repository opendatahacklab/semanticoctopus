package org.opendatahacklab.semanticoctopus.formatters;

import com.hp.hpl.jena.query.ResultSet;

/**
 * Mime-type based formatters. Default package visibility is on purpose, in order to avoid misuses.
 * 
 * @author OOL
 */
enum MimeTypeBasedFormatter implements ResultSetFormatter {

	CSV("text/csv") {

		@Override
		public String format(final ResultSet resultSet) {
			// TODO Auto-generated method stub
			return null;
		}
	},

	TSV("text/tab-separated-values") {

		@Override
		public String format(final ResultSet resultSet) {
			// TODO Auto-generated method stub
			return null;
		}
	},

	SPARQL_JSON("application/sparql-results+json") {

		@Override
		public String format(final ResultSet resultSet) {
			// TODO Auto-generated method stub
			return null;
		}
	},

	JSON("application/json") {

		@Override
		public String format(final ResultSet resultSet) {
			// TODO Auto-generated method stub
			return null;
		}
	},

	SPARQL_XML("application/sparql-results+xml") {

		@Override
		public String format(final ResultSet resultSet) {
			// TODO Auto-generated method stub
			return null;
		}
	},
	XML("application/xml") {

		@Override
		public String format(final ResultSet resultSet) {
			// TODO Auto-generated method stub
			return null;
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

	@Override
	public abstract String format(ResultSet resultSet);
}