/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

import java.net.URL;
import java.util.Collection;

/**
 * Setup an {@link AggregationEngine} with the specified set of underlying
 * ontology.
 * 
 * @author Cristiano Longo
 *
 */
public interface AggregationEngineFactory {

	/**
	 * Start the aggregation of the specified ontologies. This method
	 * is expected to return immediately with returning an {@link AggregationEngine}
	 * in BUILDING state.
	 * 
	 * @param ontologyURLs
	 * @return
	 */
	AggregationEngine create(Collection<URL> ontologyURLs) throws OntologyAggregationException;
}
