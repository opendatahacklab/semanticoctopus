package org.opendatahacklab.semanticoctopus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatterProvider;
import org.opendatahacklab.semanticoctopus.service.FormatterBasedQueryExecutorServiceFactory;
import org.opendatahacklab.semanticoctopus.service.QueryExecutor;
import org.opendatahacklab.semanticoctopus.service.Version;

import com.sun.net.httpserver.HttpServer;

/**
 * Aggregates ontologies specified as parameters and starts server for querying
 * against resulting ontology
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
public class SemanticOctopus {

	private static final OutputConsole out = SystemOutputConsole.INSTANCE;
	/**
	 * Aggregate the ontologies passed as command line parameters. The resulting
	 * ontology is sent on the standard output.
	 * 
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(final String[] args) throws MalformedURLException {
		if (args.length < 3) {
			out.println("Usage: java -jar semanticoctopus-" + Version.VERSION
					+ " <host> <port> <ontologyURL1> [ontologyURL2 [ontologyURL3 ...]]");
			System.exit(1);
		}

		try {
			final AggregationEngine engine = generateEngine(args);
			generateAndStartServer(args[0], Integer.parseInt(args[1]), engine);
			CommandConsole c = new CommandConsole(engine, System.in, out);
			c.start();
			engine.dispose();
			System.exit(0);
		} catch (final NumberFormatException e) {
			System.err.println("Port must be a numeric value");
			System.exit(2);
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(2);
		}
	}

	/**
	 * Get the ontologies URLs from command line parameters
	 * @param args
	 * 
	 * @return
	 * 
	 * @throws MalformedURLException
	 */
	private static List<URL> getOntologiesFromArgs(final String[] args) throws MalformedURLException {
		final int ontologyNumber = args.length - 2;
		final List<URL> ontologies = new ArrayList<URL>(ontologyNumber);
		for (int i = 0; i < ontologyNumber; i++)
			ontologies.add(new URL(args[i + 2]));

		return ontologies;
	}

	/**
	 * @param args
	 * 
	 * @return
	 * 
	 * @throws MalformedURLException
	 */
	private static AggregationEngine generateEngine(final String[] args) throws MalformedURLException {
		final List<URL> ontologies = getOntologiesFromArgs(args);
		return AsyncAggregationEngine.FACTORY.create(ontologies, out);
	}

	/**
	 * @param engine
	 * 
	 * @return
	 */
	private static QueryExecutor generateQueryExecutor(final QueryEngine engine) {
		final ResultSetFormatterProvider formatterProvider = new ResultSetFormatterProvider();
		final FormatterBasedQueryExecutorServiceFactory serviceFactory = new FormatterBasedQueryExecutorServiceFactory(
				engine, formatterProvider);
		final QueryExecutor queryExecutor = new QueryExecutor(serviceFactory);

		return queryExecutor;
	}

	/**
	 * Prepares base URI, then opens and starts server with specified aggregator
	 * and formatter
	 *
	 * @param executor
	 *
	 * @throws IllegalMimeTypeException
	 */
	private static HttpServer generateAndStartServer(final String host, final int port, final QueryEngine engine) {
		final QueryExecutor executor = generateQueryExecutor(engine);
		System.setProperty("logs.folder", ".");
		final UriBuilder baseBuilder = UriBuilder.fromUri(host).port(port).path("/");
		final URI baseUri = baseBuilder.build();

		final ResourceConfig config = new ResourceConfig();
		config.register(LoggingFeature.class);
		config.registerInstances(executor);
		final HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
		System.out.println("Server started at " + baseUri);

		return server;
	}
}
