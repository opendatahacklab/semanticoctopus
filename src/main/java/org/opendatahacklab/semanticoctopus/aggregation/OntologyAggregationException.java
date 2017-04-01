/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

/**
 * Some error occurred during query ontology download and aggregation.
 * 
 * @author Cristiano Longo
 *
 */
public class OntologyAggregationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4803097401890698543L;

	/**
	 * 
	 */
	public OntologyAggregationException() {
		super();
	}

	/**
	 * @param arg0
	 */
	public OntologyAggregationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public OntologyAggregationException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public OntologyAggregationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public OntologyAggregationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
