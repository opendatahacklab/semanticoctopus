package org.opendatahacklab.semanticoctopus.aggregation.service;

import static org.mockito.Mockito.mock;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.formatters.ResultSetFormatterProvider;
import org.opendatahacklab.semanticoctopus.service.AggregatorBasedQueryExecutorServiceFactory;
import org.opendatahacklab.semanticoctopus.service.QueryExecutor;
import org.opendatahacklab.semanticoctopus.service.QueryExecutorService;

import com.sun.net.httpserver.HttpServer;

/**
 * Test class for {@link QueryExecutor}
 *
 * @author OOL
 */
@Ignore
public class QueryExecutorTest {

	// Testbed
	private static final String HOST = "http://localhost/";
	private static final int PORT = 9876;
	private static final String QUERY = "SELECT ?s ?p ?o WHERE { ?s ?p ?o . } LIMIT 1";

	// Accepted formats
	private static final String CSV = "text/csv";
	private static final String TSV = "text/tab-separated-values";
	private static final String SPARQL_JSON = "application/sparql-results+json";
	private static final String JSON = "application/json";
	private static final String SPARQL_XML = "application/sparql-results+xml";
	private static final String XML = "application/xml";

	/**
	 * Prepares base URI, then opens and starts server with specified aggregator and formatter
	 * 
	 * @param aggregator
	 *            TODO
	 * @param formatter
	 *            TODO
	 */
	private HttpServer prepareServer() {
		final UriBuilder baseBuilder = UriBuilder.fromUri(HOST).port(PORT);
		final URI baseUri = baseBuilder.build();

		final AggregationEngine aggregator = mock(AggregationEngine.class);
		// TODO

		final ResultSetFormatterProvider formatterProvider = mock(ResultSetFormatterProvider.class);
		// TODO

		final ResourceConfig config = new ResourceConfig();
		config.registerInstances(new AggregatorBasedQueryExecutorServiceFactory(aggregator, formatterProvider));
		final HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);

		return server;
	}

	/**
	 * @param baseUri
	 * @return
	 */
	private Builder prepareInvocationBuilder(final String mimeType) {
		final UriBuilder baseBuilder = UriBuilder.fromUri(HOST).port(PORT);
		final URI targetUri = baseBuilder.path("sparql").build();

		final Client client = ClientBuilder.newClient();
		final WebTarget resourceTarget = client.target(targetUri);
		final Builder invocationBuilder = resourceTarget.request(mimeType);

		return invocationBuilder;
	}

	/**
	 * Stops and destroy server
	 */
	private static void disposeServer(final HttpServer server) {
		server.stop(0);
	}

	private QueryExecutorService createQueryExecutorService() {
		final QueryExecutorService service = mock(QueryExecutorService.class);

		// when(service.execQuery(anyString())).thenReturn(response);

		// TODO

		return service;
	}

	/**
	 *
	 */
	@Test
	public void testQueryExecutionGET() {
		final QueryExecutorService service = createQueryExecutorService();

		final Invocation client = prepareInvocationBuilder(CSV).buildGet();
		final String query = client.invoke(String.class);

		// TODO
	}
}
