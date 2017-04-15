/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

import java.net.URL;
import java.util.Collection;

import org.opendatahacklab.semanticoctopus.OutputConsole;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDonwloadHandler;

/**
 * Factory to create {@link Runnable} instances to load a model by aggregating a
 * fixed set of ontologies.
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
public interface AggregatedQueryEngineFactory {

	/**
	 * @return a query engine providing an empty ontology
	 */
	QueryEngine getEmpty();

	/**
	 * Create a task which will perform download. Download results will be
	 * notified to the handler.
	 * 
	 * @param ontologyURLs
	 *            TODO
	 * @param handler
	 *            handle the download operation results
	 * @param out
	 *            TODO
	 * @return a runnable which will perform download
	 * @return
	 */
	Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler,
			final OutputConsole out);
}
