package org.opendatahacklab.semanticoctopus.aggregation.service;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.opendatahacklab.semanticoctopus.formatters.IllegalMimeTypeException;
import org.opendatahacklab.semanticoctopus.service.QueryExecutor;
import org.opendatahacklab.semanticoctopus.service.QueryExecutorService;
import org.opendatahacklab.semanticoctopus.service.QueryExecutorServiceFactory;

import com.sun.net.httpserver.HttpServer;

/**
 * Test class for {@link QueryExecutor}
 *
 * @author OOL
 */
@Ignore
public class QueryExecutorTest {

	private static final String QUERY_PARAM = "query";
	// Testbed
	private static final String HOST = "http://localhost/";
	private static final int PORT = 9876;
	private static final String QUERY = "SELECT ?s ?p ?o WHERE { ?s ?p ?o . } LIMIT 1";
	private static final String MIME_TYPE = "text/plain";
	private static final Response RESPONSE = mock(Response.class);

	private QueryExecutor createTestSubject(final QueryExecutorServiceFactory serviceFactory) {
		return new QueryExecutor(serviceFactory);
	}

	/**
	 * Prepares base URI, then opens and starts server with specified aggregator and formatter
	 *
	 * @param executor
	 *
	 * @throws IllegalMimeTypeException
	 */
	private HttpServer prepareServer(final QueryExecutor executor) throws IllegalMimeTypeException {
		final UriBuilder baseBuilder = UriBuilder.fromUri(HOST).port(PORT);
		final URI baseUri = baseBuilder.build();

		final ResourceConfig config = new ResourceConfig();
		config.registerInstances(executor);
		final HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);

		return server;
	}

	/**
	 * @param service
	 *            TODO
	 * @param mimeType
	 *            if <code>null</code>, force formatter to raise an {@link IllegalMimeTypeException}
	 * @return
	 *
	 * @throws IllegalMimeTypeException
	 */
	private QueryExecutorServiceFactory createServiceFactory(final QueryExecutorService service, final String mimeType)
			throws IllegalMimeTypeException {
		final QueryExecutorServiceFactory serviceFactory = mock(QueryExecutorServiceFactory.class);
		when(serviceFactory.createService((String) null)).thenThrow(new IllegalMimeTypeException(mimeType));
		when(serviceFactory.createService(mimeType)).thenReturn(service);

		return serviceFactory;
	}

	private QueryExecutorService createService() {
		final QueryExecutorService service = mock(QueryExecutorService.class);
		when(service.execQuery(QUERY)).thenReturn(RESPONSE);
		return service;
	}

	/**
	 * @param baseUri
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws UriBuilderException
	 * @throws IllegalArgumentException
	 */
	private Builder prepareInvocationBuilder(final String mimeType, final String query)
			throws IllegalArgumentException, UriBuilderException, UnsupportedEncodingException {
		final UriBuilder baseBuilder = UriBuilder.fromUri(HOST).port(PORT);
		final URI targetUri = baseBuilder.path(QueryExecutor.ENDPOINT_NAME)
				.queryParam(QUERY_PARAM, URLEncoder.encode(query, "UTF-8").replace("+", "%20")).build();

		final Client client = ClientBuilder.newClient();
		final WebTarget resourceTarget = client.target(targetUri);
		final Builder invocationBuilder = resourceTarget.request(mimeType);

		return invocationBuilder;
	}

	/**
	 * Stops and destroy server
	 */
	private void disposeServer(final HttpServer server) {
		server.stop(0);
	}

	/**
	 * @throws IllegalMimeTypeException
	 * @throws UnsupportedEncodingException
	 * @throws UriBuilderException
	 * @throws IllegalArgumentException
	 */
	@Test
	public void testQueryExecutionGET() throws IllegalMimeTypeException, IllegalArgumentException, UriBuilderException,
			UnsupportedEncodingException {
		final QueryExecutorService service = createService();
		final QueryExecutorServiceFactory serviceFactory = createServiceFactory(service, MIME_TYPE);
		final QueryExecutor testSubject = createTestSubject(serviceFactory);

		final HttpServer server = prepareServer(testSubject);

		final Invocation client = prepareInvocationBuilder(MIME_TYPE, QUERY).buildGet();
		final Response response = client.invoke(Response.class);

		disposeServer(server);

		verify(serviceFactory.createService(MIME_TYPE));
		verify(service.execQuery(QUERY));
		assertSame("Wrong response", RESPONSE, response);
	}
}
