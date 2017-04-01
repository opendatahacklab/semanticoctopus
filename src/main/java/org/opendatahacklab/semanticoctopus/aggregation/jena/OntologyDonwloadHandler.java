package org.opendatahacklab.semanticoctopus.aggregation.jena;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * Handle the ontologies download completion and errors
 * 
 * @author cristiano longo
 *
 */
public interface OntologyDonwloadHandler{
	
	/**
	 * The download complete with no errors.
	 * 
	 * @param result the model containing the aggregated ontologies
	 */
	void complete(OntModel result);
	
	/**
	 * Some download failed
	 * 
	 * @param error
	 */
	void error(OntologyDownloadError error);
	
}