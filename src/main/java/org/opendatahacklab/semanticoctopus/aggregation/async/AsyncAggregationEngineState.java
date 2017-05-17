/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import java.io.OutputStream;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine.Parameters;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Handle calls to the {@link AggregationEngine} in a given state
 * 
 * @author cristiano longo
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
abstract class AsyncAggregationEngineState {

	private final AggregationEngine.State stateLabel;
	private final QueryEngine delegate;

	/**
	 * @param stateLabel
	 * @param delegate the delegate used to perform queries
	 */
	public AsyncAggregationEngineState(final AggregationEngine.State stateLabel, final QueryEngine delegate) {
		this.stateLabel = stateLabel;
		this.delegate=delegate;
	}

	/**
	 * Get the current state of the engine.
	 * 
	 * @return
	 */
	public final AggregationEngine.State getStateLabel() {
		return stateLabel;
	}

	/**
	 * @param query
	 * @return
	 * @throws QueryParseException
	 */
	public final ResultSet execQuery(final String query) throws QueryParseException{
		return delegate.execQuery(query);
	}

	/**
	 * Write out the current knowledge base
	 * 
	 * @param out
	 * @param baseUri
	 */
	public final void write(final OutputStream out, final String baseUri){
		delegate.write(out, baseUri);
	}

	/**
	 * In the default implementation the event is just ignored. 
	 * 
	 * @param parameters
	 * @param handler
	 * @return
	 */
	public AsyncAggregationEngineState build(final Parameters parameters,
			final OntologyDonwloadHandler handler){
		return this;
	}

	/**
	 * In the default implementation the event is just ignored. 
	 * 
	 * Handle the download complete event
	 * 
	 * @param result
	 * @return the destination state
	 */
	public AsyncAggregationEngineState complete(final QueryEngine result){
		return this;
	}

	/**
 	 * In the default implementation the event is just ignored. 
 	 * 
	 * Handle the download error event
	 * 
	 * @return the destination state
	 */
	public AsyncAggregationEngineState error(){
		return this;
	}

	/**
 	 * In the default implementation the event is just ignored. 
 	 * 
	 * Free all resources and goes to disposed state
	 * @param parameters TODO
	 * 
	 * @return the destination state
	 */
	public AsyncAggregationEngineState dispose(final Parameters parameters){
		return this;
	}

	/**
	 * Get the delegate query engine actually in use
	 * 
	 * @return
	 */
	protected final QueryEngine getDelegate(){
		return delegate;
	}
	
	/**
	 * @return
	 */
	public String getInfo(){
		return delegate.getInfo();
	}
}
