/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

/**
 * A listener for state changes of {@link AggregationEngine} instances
 * 
 * @author Cristiano Longo
 *
 */
public interface AggregationEngineListener {

	/**
	 * The engine entered become ready
	 */
	void notifyReady();
	
	/**
	 * The specified aggregation engine started a building operation.
	 */
	void notifyBuilding();

	/**
	 * The building process ended with an error.
	 */
	void notifyError();
}
