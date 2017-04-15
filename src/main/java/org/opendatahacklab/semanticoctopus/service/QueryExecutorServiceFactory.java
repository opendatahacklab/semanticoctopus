package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.core.Response;

import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;

/**
 * A factory of {@link QueryExecutorService}s
 * 
 * @author OOL
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
public interface QueryExecutorServiceFactory {

	/**
	 * Creates a {@link QueryExecutorService} returning {@link Response}s formatted as per specified mime-type. Accepted
	 * mime-types are: <code>text/csv</code>, <code>text/tab-separated-values<code></code>,
	 * <code>application/sparql-results+json</code>,<code>application/json</code>,
	 * <code>application/sparql-results+xml</code>,<code>application/xml</code>
	 * 
	 * @param mimeType
	 * 
	 * @return
	 * 
	 * @throws IllegalMimeTypeException
	 *             when mime-type is not accepted in the context of SPARQL SELECT queries
	 */
	QueryExecutorService createService(String mimeType) throws IllegalMimeTypeException;
}