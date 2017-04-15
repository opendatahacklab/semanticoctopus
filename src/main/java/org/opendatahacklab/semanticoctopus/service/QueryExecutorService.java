package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.core.Response;

import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;

import com.hp.hpl.jena.query.QueryParseException;

/**
 * A service for SPARQL query execution by an {@link QueryEngine}
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
public interface QueryExecutorService {

	/**
	 * Key of reponse header for content type
	 */
	public static final String CONTENT_TYPE_HEADER_KEY = "Content-type";

	/**
	 * Key of reponse header for allow origin header, to avoid cross origin request blocks
	 */
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER_KEY = "Access-Control-Allow-Origin";

	/**
	 * Executes a certain SPARQL SELECT query
	 *
	 * @param query
	 *
	 * @return An HTTP {@link Response} containing the result of query
	 * @throws IllegalMimeTypeException 
	 * @throws QueryParseException 
	 */
	Response execQuery(final String query) throws QueryParseException, IllegalMimeTypeException;
}
