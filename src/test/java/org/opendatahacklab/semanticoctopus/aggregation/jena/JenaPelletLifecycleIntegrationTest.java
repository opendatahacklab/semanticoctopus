/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation.jena;

import static org.junit.Assert.assertSame;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;
import org.opendatahacklab.semanticoctopus.SystemOutputConsole;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationEngine.State;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationTestUtils;
import org.opendatahacklab.semanticoctopus.aggregation.AggregationTestUtils.RelativePair;
import org.opendatahacklab.semanticoctopus.aggregation.async.AsyncAggregationEngine;

/**
 * Test that after rebuild the octopus continues working
 * 
 * @author Cristiano Longo
 * @author Cristiano Longo
 *
 *         This file is part of Semantic Octopus.
 * 
 *         Copyright 2017 Cristiano Longo
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
 *
 */
public class JenaPelletLifecycleIntegrationTest {

	private static final int MAX_BUILDING_SECONDS = 15;
	private static final long ONE_SEC_MS = 1000;
	private final AggregationTestUtils utils;

	/**
	 * @throws MalformedURLException
	 * 
	 */
	public JenaPelletLifecycleIntegrationTest() throws MalformedURLException {
		utils = new AggregationTestUtils();
	}

	/**
	 * Create an aggrebation engine which will aggregate the given ontologies
	 * 
	 * @param ontologies
	 * @return
	 */
	private AggregationEngine createAggregationEngine(final URL[] ontologies) {
		return AsyncAggregationEngine.FACTORY.create(Arrays.asList(ontologies), SystemOutputConsole.INSTANCE);
	}

	/**
	 * Perform a query after a successful build
	 * 
	 * @throws InterruptedException
	 * @throws MalformedURLException
	 */
	@Test
	@Ignore
	public void testQueryAfterBuild() throws InterruptedException, MalformedURLException {
		final URL[] ontologies = { new URL("http://localhost/~cristianolongo/opendatahacklab/site/odhl.owl"),
				utils.ontologyA };
		// final URL[] ontologies = { utils.ontologyA };
		final AggregationEngine engine = createAggregationEngine(ontologies);
		final RelativePair[] expected = { new RelativePair(utils.individualA, utils.individualB) };
		assertSame(State.IDLE, engine.getState());
		build(engine);
		utils.testRelatives(engine, Arrays.asList(expected));
		engine.dispose();
	}

	/**
	 * Perform a query after a successful build
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void testQueryAfterRebuild() throws InterruptedException {
		final URL[] ontologies = { utils.ontologyA };
		final AggregationEngine engine = createAggregationEngine(ontologies);
		final RelativePair[] expected = { new RelativePair(utils.individualA, utils.individualB) };
		assertSame(State.IDLE, engine.getState());
		build(engine);
		utils.testRelatives(engine, Arrays.asList(expected));
		build(engine);
		utils.testRelatives(engine, Arrays.asList(expected));
	}

	/**
	 * Perform build and wait until build finish
	 * 
	 * @param engine
	 * @throws InterruptedException
	 */
	private void build(final AggregationEngine engine) throws InterruptedException {
		engine.build();
		waitForBuildingCompletion(engine);
		assertSame(State.READY, engine.getState());
	}

	/**
	 * Wait until the aggregation engine exit the building state or if
	 * MAX_BUILDING_SECONDS elapses
	 * 
	 * @param engine
	 * @throws InterruptedException
	 * 
	 */
	private void waitForBuildingCompletion(final AggregationEngine engine) throws InterruptedException {
		for (int i = 0; i < MAX_BUILDING_SECONDS; i++)
			if (State.BUILDING == engine.getState()) {
				Thread.sleep(ONE_SEC_MS);
				System.out.println("Checking:" + engine.getState());
			} else
				return;
	}
}
