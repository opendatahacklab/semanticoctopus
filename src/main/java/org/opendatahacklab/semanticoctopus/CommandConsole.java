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

/**
 * Command line engine to control an {@link AggregationEngine} instance
 * 
 * @author cristiano longo
 *
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
	 * Just print help
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
			out.println("- exit close the engine and this executable");
			return true;
		}
	};

	public static final String EXIT_COMMAND = "exit";

	private final AggregationEngine engine;
	private final BufferedReader in;
	private final OutputConsole out;
	private final ConsoleCommand[] availableCommands = {build, state, list, help};
	
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
