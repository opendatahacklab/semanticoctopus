package org.opendatahacklab.semanticoctopus.service;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import com.sun.net.httpserver.HttpServer;

/**
 * Test class for {@link QueryExecutor}
 *
 * @author OOL
 */
public class QueryExecutorTest {

	// Testbed
	private static final String HOST = "http://localhost/";
	private static final int PORT = 9876;
	private static final String QUERY = "SELECT ?s ?p ?o WHERE { ?s ?p ?o . } LIMIT 1";

	/**
	 * @param baseUri
	 */
	private HttpServer prepareServer(final URI baseUri) {
		final ResourceConfig config = new ResourceConfig(QueryExecutor.class);
		final HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);

		return server;
	}

	/**
	 * @param baseUri
	 * @return
	 */
	private Invocation prepareClient(final URI baseUri) {
		final Client client = ClientBuilder.newClient();
		final WebTarget resourceTarget = client.target(baseUri);
		final Invocation invocation = resourceTarget.request("text/plain")
						.header("Content-Type", "application/sparql-query")
						.header("Content-Encoding", "UTF-8").buildGet(); // TODO
		// .buildPost(new Entity<String>(QUERY, MediaType.TEXT_PLAIN_TYPE));
		return invocation;
	}

	/**
	 *
	 */
	@Test
	public void testQueryExecution() {
		final UriBuilder baseBuilder = UriBuilder.fromUri(HOST).port(PORT);

		final URI baseUri = baseBuilder.build();
		final HttpServer server = prepareServer(baseUri);

		final URI targetUri = baseBuilder.path("endpoint/execQuery").build();
		final Invocation client = prepareClient(targetUri);

		// final String version = client.invoke(String.class);
		//
		// assertEquals(Version.VERSION, version);
	}
}
