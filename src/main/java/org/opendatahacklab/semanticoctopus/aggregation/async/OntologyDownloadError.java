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
