package org.opendatahacklab.semanticoctopus.formatters;

import com.hp.hpl.jena.query.*;

/**
 * A processor of {@link ResultSet}s which converts into strings having a format depending on implementation
 * 
 * @author OOL
 */
public interface ResultSetFormatter {

	/**
	 * @return The mime-type related to this formatter
	 */
	String getMimeType();

	/**
	 * Converts the model of specified result set into a string having the format related to this object
	 * 
	 * @param resultSet
	 * 
	 * @return A string representing the result set
	 */
	String format(ResultSet resultSet);
}
