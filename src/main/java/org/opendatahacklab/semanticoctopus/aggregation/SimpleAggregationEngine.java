package org.opendatahacklab.semanticoctopus.aggregation;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Download an ontology from the internet, perform reasoning and provide a sparql endpoint.
 * 
 * @author Cristiano Longo
 *
 */
public class SimpleAggregationEngine implements QueryEngine {

	private final List<URL> ontologyURLs;
	private final OntModel model;

	public SimpleAggregationEngine() {
		this(new ArrayList<URL>());
	}

	/**
	 * 
	 */
	public SimpleAggregationEngine(final List<URL> ontologyURLs) {
		this.ontologyURLs = Collections.unmodifiableList(ontologyURLs);
		this.model = download(ontologyURLs);
	}

	/**
	 * Load a model aggregating a set of ontologies
	 * 
	 * @param ontologyURLs
	 * @return
	 */
	private OntModel download(final List<URL> ontologyURLs) {
		final OntModel baseModel = ModelFactory.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		for (final URL u : ontologyURLs)
			baseModel.read(u.toExternalForm());
		return baseModel;
	}

	/* (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine#write(java.io.OutputStream,
	 * java.lang.String) */
	@Override
	public void write(final OutputStream out, final String baseUri) {
		final OutputStreamWriter writer = new OutputStreamWriter(out);
		this.model.writeAll(writer, "RDF/XML-ABBREV", baseUri);
		// writer.flush();
		// writer.close();
	}

	/* (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine#execQuery(java.lang.String) */
	@Override
	public ResultSet execQuery(final String query) throws QueryParseException {
		final QueryExecution execution = QueryExecutionFactory.create(QueryFactory.create(query), model);
		final ResultSet resultSet = execution.execSelect();
		return resultSet;
	}
}
