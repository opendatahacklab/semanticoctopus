/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.net.URL;
import java.util.Collection;

import org.opendatahacklab.semanticoctopus.OutputConsole;
import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.OntologyDonwloadHandler;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Factory to generate Jena Based query engines backed by ontologies downloaded
 * from the web
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
public class JenaPelletQueryEngineFactory implements AggregatedQueryEngineFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.
	 * OntologyDownloadTaskFactory#getEmpty()
	 */
	@Override
	public QueryEngine getEmpty() {
		final OntModel model = ModelFactory.createOntologyModel();
		return new JenaQueryEngine(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.
	 * OntologyDownloadTaskFactory#getDownloadTask(org.opendatahacklab.
	 * semanticoctopus.aggregation.async.OntologyDonwloadHandler)
	 */
	@Override
	public Runnable getDownloadTask(Collection<URL> ontologyURLs, final OntologyDonwloadHandler handler,
			final OutputConsole out) {
		return new JenaPelletSeqDownloadTask(ontologyURLs, handler, out);
	}

}
