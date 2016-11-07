package org.opendatahacklab.semanticoctopus.aggregation.service;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opendatahacklab.semanticoctopus.service.QueryExecutor;

import com.sun.net.httpserver.HttpServer;

/**
 * Test class for {@link QueryExecutor}
 *
 * @author OOL
 */
// @Ignore
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

	private static UriBuilder baseBuilder;
	private static HttpServer server;

	/**
	 * Prepares base URI, then opens and starts server
	 */
	@BeforeClass
	public static void prepareServer() {
		baseBuilder = UriBuilder.fromUri(HOST).port(PORT);
		final URI baseUri = baseBuilder.build();

		final ResourceConfig config = new ResourceConfig(QueryExecutor.class);
		server = JdkHttpServerFactory.createHttpServer(baseUri, config);
	}

	// /**
	// * @param baseUri
	// * @return
	// */
	// private Invocation prepareClientGET(final URI baseUri) {
	// final Client client = ClientBuilder.newClient();
	// final WebTarget resourceTarget = client.target(baseUri);
	// final Invocation invocation = resourceTarget.request("text/plain")
	// // .header("Content-Type", "application/sparql-query")
	// .header("Content-Encoding", "UTF-8").buildGet();
	//
	// return invocation;
	// }

	/**
	 *
	 */
	@Test
	public void testQueryExecutionGET() {

		final URI targetUri = baseBuilder.path("sparql").build();
		// final Invocation client = prepareClientGET(targetUri);
		//
		// final String query = client.invoke(String.class);
		//
		// assertEquals(Version.VERSION, version);
	}

	/**
	 * Stops and destroy server
	 */
	@AfterClass
	public static void disposeServer() {
		server.stop(0);
	}
}
