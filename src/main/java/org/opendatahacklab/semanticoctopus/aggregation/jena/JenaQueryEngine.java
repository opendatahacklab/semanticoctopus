package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * A query engine based on Jena 
 * 
 * @author Cristiano Longo
 *
 */
public class JenaQueryEngine implements QueryEngine{

	private final OntModel model;

	/**
	 * 
	 */
	public JenaQueryEngine(OntModel model) {
		this.model=model;
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#write(java.io.OutputStream, java.lang.String)
	 */
	@Override
	public void write(OutputStream out, String baseUri) {
		final OutputStreamWriter writer = new OutputStreamWriter(out);
		this.model.writeAll(writer, "RDF/XML-ABBREV", baseUri);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#execQuery(java.lang.String)
	 */
	@Override
	public ResultSet execQuery(String query) throws QueryParseException {
		final QueryExecution execution = QueryExecutionFactory.create(QueryFactory.create(query), model);
		return execution.execSelect();
	}

}
