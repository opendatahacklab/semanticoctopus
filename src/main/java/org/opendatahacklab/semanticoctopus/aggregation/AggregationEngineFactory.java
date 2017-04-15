/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

import java.net.URL;
import java.util.Collection;

import org.opendatahacklab.semanticoctopus.OutputConsole;

/**
 * Setup an {@link AggregationEngine} with the specified set of underlying
 * ontology.
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
public interface AggregationEngineFactory {

	/**
	 * Create an aggregation engine which will aggregate and serve the specified ontologies
	 * 
	 * @param ontologyURLs
	 * @param out a console to send output and notifications
	 * @return
	 */
	AggregationEngine create(Collection<URL> ontologyURLs, OutputConsole out);
}
