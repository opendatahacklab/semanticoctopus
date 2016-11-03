package org.opendatahacklab.semanticoctopus.service;

import static org.junit.Assert.assertEquals;

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
 * Test class for {@link Version}
 *
 * @author OOL
 *
 */
public class VersionTest {

	// Testbed
	private static final String HOST = "http://localhost/";
	private static final int PORT = 9876;

	/**
	 * @param baseUri
	 */
	private HttpServer prepareServer(final URI baseUri) {
		final ResourceConfig config = new ResourceConfig(Version.class);
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
		final Invocation invocation = resourceTarget.request("text/plain")// header("Foo", "bar")
						.buildGet();
		return invocation;
	}

	/**
	 *
	 */
	@Test
	public void testGetVersion() {
		final UriBuilder baseBuilder = UriBuilder.fromUri(HOST).port(PORT);

		final URI baseUri = baseBuilder.build();
		final HttpServer server = prepareServer(baseUri);

		final URI targetUri = baseBuilder.path("version").build();
		final Invocation client = prepareClient(targetUri);

		final String version = client.invoke(String.class);

		assertEquals(Version.VERSION, version);
	}
}
