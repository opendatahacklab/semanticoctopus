/**
 * 
 */
package org.opendatahacklab.semanticoctopus;

/**
 * Provide an output mechanism. Use this for any output operation
 * 
 * @author cristiano longo
 *
 */
public interface OutputConsole {

	/**
	 * Print the specified message with a CR LF. 
	 * 
	 * @param message
	 */
	void println(final String message);
	
	/**
	 * Print the specified message. 
	 * 
	 * @param message
	 */
	void print(final String message);	
}
