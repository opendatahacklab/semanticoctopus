package org.opendatahacklab.semanticoctopus.aggregation;

import java.io.OutputStream;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * An engine for executing queries against an underlying knowledge base
 * 
 * @author Cristiano Longo, OOL
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
public interface QueryEngine {

	/**
	 * Write the ontology on the specified stream
	 * 
	 * @param out
	 * @param baseUri
	 */
	void write(OutputStream out, String baseUri);

	/**
	 * Perform a query against the aggregated ontology
	 * 
	 * @param String
	 *            query
	 * @return
	 * @throws QueryParseException
	 */
	ResultSet execQuery(String query) throws QueryParseException;
	
	/**
	 * Free all resources
	 */
	void dispose();
	
	/**
	 * @return
	 */
	boolean isDisposed();
}