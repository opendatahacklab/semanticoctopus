package org.opendatahacklab.semanticoctopus.aggregation.jena;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 *         This file is part of Semantic Octopus.
 * 
 *         Copyright 2017 Cristiano Longo, Antonio Pisasale
 *
 *         Semantic Octopus is free software: you can redistribute it and/or
 *         modify it under the terms of the GNU Lesser General Public License as
 *         published by the Free Software Foundation, either version 3 of the
 *         License, or (at your option) any later version.
 *
 *         Semantic Octopus is distributed in the hope that it will be useful,
 *         but WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         Lesser General Public License for more details.
 *
 *         You should have received a copy of the GNU Lesser General Public
 *         License along with this program. If not, see
 *         <http://www.gnu.org/licenses/>.
 */
public class JenaQueryEngine implements QueryEngine {

	private static final DateFormat DF = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
	private final OntModel model;
	private final Date creationTime;

	/**
	 * 
	 */
	public JenaQueryEngine(final OntModel model) {
		this.model = model;
		creationTime = new Date();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#write(java.io
	 * .OutputStream, java.lang.String)
	 */
	@Override
	public void write(OutputStream out, String baseUri) {
		final OutputStreamWriter writer = new OutputStreamWriter(out);
		this.model.writeAll(writer, "RDF/XML-ABBREV", baseUri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#execQuery(
	 * java.lang.String)
	 */
	@Override
	public ResultSet execQuery(final String query) throws QueryParseException {
		final QueryExecution execution = QueryExecutionFactory.create(QueryFactory.create(query), model);
		return execution.execSelect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#dispose()
	 */
	@Override
	public void dispose() {
		//System.out.println("Disposing "+model+" and graph "+model.getGraph());
		model.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#isDisposed()
	 */
	@Override
	public boolean isDisposed() {
		return model.isClosed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.QueryEngine#getInfo()
	 */
	@Override
	public String getInfo() {
		return "JenaQueryEngine created=" + DF.format(creationTime);
	}

}
