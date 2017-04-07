/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import java.net.URL;

/**
 * An error occurred downloading a remote ontology
 * 
 * @author cristiano longo
 *
 */
public class OntologyDownloadError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 52510115414711696L;
	private final URL ontologyURL;
	
	/**
	 * @param ontologyURL
	 * @param message
	 * @param cause
	 */
	public OntologyDownloadError(URL ontologyURL, String message, Throwable cause) {
		super(cause);
		this.ontologyURL=ontologyURL;
	}

	/**
	 * @param message
	 */
	public OntologyDownloadError(URL ontologyURL, String message) {
		super(message);
		this.ontologyURL=ontologyURL;
	}

	/**
	 * @param arg0
	 */
	public OntologyDownloadError(URL ontologyURL, Throwable cause) {
		super(cause);
		this.ontologyURL=ontologyURL;
	}

	/**
	 * The ontology whose download failed.
	 * 
	 * @return
	 */
	public URL getOntologyURL(){
		return ontologyURL;
	}
}
