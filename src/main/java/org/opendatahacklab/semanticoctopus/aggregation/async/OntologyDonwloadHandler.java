package org.opendatahacklab.semanticoctopus.aggregation.async;

import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;

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
	 * @param result a query engine which will provide the aggregated ontologies
	 */
	void complete(QueryEngine result);
	
	/**
	 * Some download failed
	 * 
	 * @param error
	 */
	void error(OntologyDownloadError error);

	/**
	 * The aggregated ontology is not consistent
	 * 
	 * @param error
	 */
	void error(InconsistenOntologyException error);
}