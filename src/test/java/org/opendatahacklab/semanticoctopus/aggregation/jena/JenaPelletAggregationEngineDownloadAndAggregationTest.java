/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import static org.junit.Assert.assertSame;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.Executor;

import org.opendatahacklab.semanticoctopus.OutputConsole;
import org.opendatahacklab.semanticoctopus.SystemOutputConsole;
import org.opendatahacklab.semanticoctopus.aggregation.AbstractAggregationTest;
import org.opendatahacklab.semanticoctopus.aggregation.AggregatedQueryEngineFactory;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.ExplicitExecutor;
import org.opendatahacklab.semanticoctopus.aggregation.QueryEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine.Parameters;

/**
 * TODO remove this
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
public class JenaPelletAggregationEngineDownloadAndAggregationTest extends AbstractAggregationTest {

	/**
	 * @throws MalformedURLException
	 */
	public JenaPelletAggregationEngineDownloadAndAggregationTest() throws MalformedURLException {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.opendatahacklab.semanticoctopus.aggregation.AbstractAggregationTest#
	 * createTestSubject(java.util.Collection)
	 */
	@Override
	public QueryEngine createSuccesTestSubject(final Collection<URL> ontologies) throws InterruptedException {
		final ExplicitExecutor executor = new ExplicitExecutor();
		final AggregationEngine engine = new AsyncAggregationEngine(new Parameters() {
			
			@Override
			public AggregatedQueryEngineFactory getQueryEngineFactory() {
				return new JenaPelletQueryEngineFactory();
			}
			
			@Override
			public Collection<URL> getOntologies() {
				return ontologies;
			}
			
			@Override
			public Executor getDownloadExecutor() {
				return executor;
			}

			@Override
			public OutputConsole getOutputConsole() {
				return SystemOutputConsole.INSTANCE;
			}
		});
		assertSame(AggregationEngine.State.IDLE, engine.getState());
		engine.build();
		executor.waitForCompletion();
		assertSame(AggregationEngine.State.READY, engine.getState());
		return engine;
	}

}
