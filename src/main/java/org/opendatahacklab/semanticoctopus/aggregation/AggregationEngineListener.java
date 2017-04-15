/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

/**
 * A listener for state changes of {@link AggregationEngine} instances
 * 
 * @author Cristiano Longo
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
