package org.opendatahacklab.semanticoctopus;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.SimpleAggregationEngine;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatterProvider;
import org.opendatahacklab.semanticoctopus.service.FormatterBasedQueryExecutorServiceFactory;
import org.opendatahacklab.semanticoctopus.service.QueryExecutor;
import org.opendatahacklab.semanticoctopus.service.Version;

import com.sun.net.httpserver.HttpServer;

/**
 * Aggregates ontologies specified as parameters and starts server for querying against resulting ontology
 * 
 * @author Cristiano Longo, OOL
 */
public class SemanticOctopus {

	/**
	 * Aggregate the ontologies passed as command line parameters. The resulting ontology is sent on the standard
	 * output.
	 * 
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(final String[] args) throws MalformedURLException {
		if (args.length < 3) {
			System.err.println(
					"Usage: java -jar semanticoctopus-" + Version.VERSION
							+ " <host> <port> <ontologyURL1> [ontologyURL2 [ontologyURL3 ...]]");
			System.exit(1);
		}

		try {
			final AggregationEngine engine = generateEngine(args);
			final QueryExecutor executor = generateQueryExecutor(engine);
			generateAndStartServer(args[0], Integer.parseInt(args[1]), executor);

			engine.write(System.out, "http://opendatahacklab.org/");
		} catch (final NumberFormatException e) {
			System.err.println("Port must be a numeric value");
			System.exit(2);
		}

		System.out.println("\n***** Endpoint started *****\n");
	}

	/**
	 * @param args
	 * 
	 * @return
	 * 
	 * @throws MalformedURLException
	 */
	private static List<URL> loadOntologies(final String[] args) throws MalformedURLException {
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
		final List<URL> ontologies = loadOntologies(args);
		final AggregationEngine engine = new SimpleAggregationEngine(ontologies);

		return engine;
	}

	/**
	 * @param engine
	 * 
	 * @return
	 */
	private static QueryExecutor generateQueryExecutor(final AggregationEngine engine) {
		final ResultSetFormatterProvider formatterProvider = new ResultSetFormatterProvider();
		final FormatterBasedQueryExecutorServiceFactory serviceFactory = new FormatterBasedQueryExecutorServiceFactory(
				engine, formatterProvider);
		final QueryExecutor queryExecutor = new QueryExecutor(serviceFactory);

		return queryExecutor;
	}

	/**
	 * Prepares base URI, then opens and starts server with specified aggregator and formatter
	 *
	 * @param executor
	 *
	 * @throws IllegalMimeTypeException
	 */
	private static HttpServer generateAndStartServer(final String host, final int port, final QueryExecutor executor) {
		final UriBuilder baseBuilder = UriBuilder.fromUri(host).port(port).path("/");
		final URI baseUri = baseBuilder.build();

		final ResourceConfig config = new ResourceConfig();
		config.registerInstances(executor);
		final HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);

		return server;
	}
}
