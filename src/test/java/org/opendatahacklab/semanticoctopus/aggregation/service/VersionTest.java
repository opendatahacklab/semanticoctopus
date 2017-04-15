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

		final ResourceConfig config = new ResourceConfig();
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
