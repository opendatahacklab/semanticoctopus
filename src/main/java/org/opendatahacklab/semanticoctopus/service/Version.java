package org.opendatahacklab.semanticoctopus.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

/**
 * Version provider API
 * 
 * @author OOL
 *
 */
@Path("version")
public class Version {
	
	public static final String VERSION="0.1";
	
	@GET
	@Produces("text/plain")
	public String getVersion(@Context Request request) {
		return VERSION;
	}
}
