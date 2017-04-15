/**
 * 
 */
package org.opendatahacklab.semanticoctopus;

/**
 * Provide an output mechanism. Use this for any output operation
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
public interface OutputConsole {

	/**
	 * Print the specified message with a CR LF. 
	 * 
	 * @param message
	 */
	void println(final String message);
	
	/**
	 * Print the specified message. 
	 * 
	 * @param message
	 */
	void print(final String message);	
}
