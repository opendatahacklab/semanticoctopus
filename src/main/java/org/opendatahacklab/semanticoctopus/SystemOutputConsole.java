package org.opendatahacklab.semanticoctopus;

/**
 * Default implementatoio of {@link OutputConsole} which uses System.out and which is thread-safe
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
public class SystemOutputConsole implements OutputConsole {

	public static final OutputConsole INSTANCE = new SystemOutputConsole();
	
	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.OutputConsole#println(java.lang.String)
	 */
	@Override
	public synchronized void println(final String message) {
		System.out.println(message);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.OutputConsole#print(java.lang.String)
	 */
	@Override
	public synchronized void print(final String message) {
		System.out.print(message);
	}
}
