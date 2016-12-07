/**
 * 
 */
package org.opendatahacklab.semanticoctopus.formatters;

/**
 * Unable to parse the body of a URLEncoded query.
 * 
 * @author cristiano longo
 *
 */
public class IllegalRequestBodyException extends Exception {

	/**
	 * @param message
	 */
	public IllegalRequestBodyException(String message) {
		super(message);
	}

	/**
	 * @param arg0
	 */
	public IllegalRequestBodyException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public IllegalRequestBodyException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
}
