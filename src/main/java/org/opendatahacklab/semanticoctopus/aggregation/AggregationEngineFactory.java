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
	 * Create an aggregation engine which will aggregate and serve the specified ontologies
	 * 
	 * @param ontologyURLs
	 * @return
	 */
	AggregationEngine create(Collection<URL> ontologyURLs);
}
