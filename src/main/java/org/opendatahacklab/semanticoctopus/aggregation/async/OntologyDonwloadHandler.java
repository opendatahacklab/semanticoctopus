package org.opendatahacklab.semanticoctopus.aggregation.async;

import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;

/**
 * Handle the ontologies download completion and errors
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