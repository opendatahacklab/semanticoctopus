package org.opendatahacklab.semanticoctopus.aggregation.service;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opendatahacklab.semanticoctopus.service.Version;

import com.sun.net.httpserver.HttpServer;

/**
 * Test class for {@link Version}
 *
 * @author OOL
 *
 */
public class VersionTest {

	// Testbed
	private static final String HOST = "http://localhost/";
	private static final int PORT = 9876;

	private static UriBuilder baseBuilder;
	private static HttpServer server;

	/**
	 * Prepares base URI, then opens and starts server
	 */
	@BeforeClass
	public static void prepareServer() {
		baseBuilder = UriBuilder.fromUri(HOST).port(PORT);
		final URI baseUri = baseBuilder.build();

		final ResourceConfig config = new ResourceConfig(); // Version.class);
		config.registerInstances(new Version());
		server = JdkHttpServerFactory.createHttpServer(baseUri, config);
	}

	/**
	 * @param baseUri
	 * @return
	 */
	private Invocation prepareClient(final URI baseUri) {
		final Client client = ClientBuilder.newClient();

		final WebTarget resourceTarget = client.target(baseUri);
		final Invocation invocation = resourceTarget.request("text/plain")
						.buildGet();
		return invocation;
	}

	/**
	 *
	 */
	@Test
	public void testGetVersion() {
		final URI targetUri = baseBuilder.path("version").build();
		final Invocation client = prepareClient(targetUri);

		final String version = client.invoke(String.class);

		assertEquals(Version.VERSION, version);
	}

	/**
	 * Stops and destroy server
	 */
	@AfterClass
	public static void disposeServer() {
		server.stop(0);
	}
}
