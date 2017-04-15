/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.async;

import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine.Parameters;

/**
 * A state in which starting a building activity is allowed
 * 
 * @author Cristiano Longo
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
public class AsyncAggregationEngineCanBuildState extends AsyncAggregationEngineState {

	/**
	 * @param stateLabel
	 * @param delegate
	 */
	public AsyncAggregationEngineCanBuildState(final AggregationEngine.State stateLabel, final QueryEngine delegate) {
		super(stateLabel, delegate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.
	 * AsyncAggregationEngineState#build(org.opendatahacklab.semanticoctopus.
	 * aggregation.async.AsyncAggregationEngine.Parameters,
	 * org.opendatahacklab.semanticoctopus.aggregation.async.
	 * OntologyDonwloadHandler)
	 */
	@Override
	public final AsyncAggregationEngineState build(final Parameters parameters, final OntologyDonwloadHandler handler) {
		final Runnable downloadTask = parameters.getQueryEngineFactory().getDownloadTask(parameters.getOntologies(),
				handler, parameters.getOutputConsole());
		parameters.getDownloadExecutor().execute(downloadTask);
		return new AsyncAggregationEngineBuildingState(getDelegate());
	}
	
	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngineState#dispose()
	 */
	@Override
	public AsyncAggregationEngineState dispose(Parameters parameters){
		getDelegate().dispose();
		return new AsyncAggregationEngineDisposedState();
	}
}
