package org.opendatahacklab.semanticoctopus.formatters;


/**
 * Raised when a mime-type is illegal in SPARQL query/update context
 * 
 * @author OOL
 */
public class IllegalMimeTypeException extends Exception {

	private static final long serialVersionUID = -7931396532545827424L;

	// Do not use
	@SuppressWarnings("unused")
	private IllegalMimeTypeException() {
		// Intentionally empty
	}

	/**
	 * Constructs an {@link IllegalMimeTypeException} with specified illegal mime-type
	 * 
	 * @param mimeType
	 */
	public IllegalMimeTypeException(final String mimeType) {
		super("Mime-type '" + mimeType + "' is illegal in this context");
	}
}
