/**
 * 
 */
package org.opendatahacklab.semanticoctopus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Command line engine to control an {@link AggregationEngine} instance
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
public class CommandConsole{

	interface ConsoleCommand {
		/**
		 * @param command
		 *            the command entered via command line
		 * @param engine
		 *            the engine
		 * @param out
		 *            console to send output
		 * @return true if the entered command can be handled by this, false
		 *         otherwise
		 */
		boolean handle(String command, AggregationEngine engine, OutputConsole out);
	}


	/**
	 * build or rebuild the knowledge base
	 */
	private final ConsoleCommand build = new ConsoleCommand() {
		
		@Override
		public boolean handle(final String command, final AggregationEngine engine, final OutputConsole out) {
			if (!"build".equals(command))
				return false;
			engine.build();
			return true;
		}
	};

	/**
	 * print the current state
	 */
	private final ConsoleCommand state = new ConsoleCommand() {
		
		@Override
		public boolean handle(final String command, final AggregationEngine engine, final OutputConsole out) {
			if (!"state".equals(command))
				return false;
			out.println(engine.getState().name());
			return true;
		}
	};

	/**
	 * print information about the engine currently in use
	 */
	private final ConsoleCommand info = new ConsoleCommand() {
		
		@Override
		public boolean handle(final String command, final AggregationEngine engine, final OutputConsole out) {
			if (!"info".equals(command))
				return false;
			out.println(engine.getInfo());
			return true;
		}
	};

	/**
	 * print the list of ontologies
	 */
	private final ConsoleCommand list = new ConsoleCommand() {
		
		@Override
		public boolean handle(final String command, final AggregationEngine engine, final OutputConsole out) {
			if (!"list".equals(command))
				return false;
			out.println("Ontologies ------------");
			for(final URL url : engine.getOntologies())
				out.println(url.toExternalForm());
			out.println("------------------------");
			return true;
		}
	};

	/**
	 * perform a sparql query
	 */
	private final ConsoleCommand query = new ConsoleCommand() {
		
		@Override
		public boolean handle(final String command, final AggregationEngine engine, final OutputConsole out) {
			if (!command.startsWith("query"))
				return false;
			final String query = command.substring("query".length()).trim();
			final ResultSet r = engine.execQuery(query);
			while(r.hasNext()){
				final QuerySolution s = r.next();
				out.println(s.toString());
			}
			return true;
		}
	};

	/**
	 * Just print help
	 */
	private final ConsoleCommand help = new ConsoleCommand() {
		
		@Override
		public boolean handle(final String command, final AggregationEngine engine, final OutputConsole out) {
			//help is the default
			out.println("Available commands:");
			out.println("- build start (or restart) downloading and aggregating ontologies");
			out.println("- state print the current state of the engine");
			out.println("- list print the urls of the ontologies which will be aggregated");
			out.println("- help print a list of available commands");
			out.println("- query <query> perform a query against the aggregated knowledge base");
			out.println("- exit close the engine and this executable");
			return true;
		}
	};

	public static final String EXIT_COMMAND = "exit";

	private final AggregationEngine engine;
	private final BufferedReader in;
	private final OutputConsole out;
	private final ConsoleCommand[] availableCommands = {build, state, list, info, query, help};
	
	/**
	 * @param engine
	 *            the controlled engine
	 * @param in
	 *            where commands are entered
	 * @param out
	 *            where output is sent
	 */
	public CommandConsole(final AggregationEngine engine, final InputStream in, final OutputConsole out) {
		this.engine = engine;

		this.in = new BufferedReader(new InputStreamReader(in));
		this.out = out;
	}

	/**
	 * Start getting commands from in and serving it. This will exit when the
	 * user will enter the string exit.
	 * 
	 * @throws IOException
	 */
	public void start() throws IOException {
		while (true) {
			out.print(" > ");
			final String command = in.readLine();
			if (EXIT_COMMAND.equals(command)){
				out.println("Exiting. Bye bye");
				return;
			}
			handleCommand(command);
		}
	}

	/**
	 * Handle a command entered via command line
	 * 
	 * @param command
	 */
	private void handleCommand(final String command){
		for(final ConsoleCommand c : availableCommands)
			if (c.handle(command, engine, out))
				return;
		out.println("ERROR: Unable to handle command "+command);
	}
}
