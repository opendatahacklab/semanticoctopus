/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

/**
 * Some error occurred during query ontology download and aggregation.
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
public class OntologyAggregationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4803097401890698543L;

	/**
	 * 
	 */
	public OntologyAggregationException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public OntologyAggregationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public OntologyAggregationException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public OntologyAggregationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public OntologyAggregationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
