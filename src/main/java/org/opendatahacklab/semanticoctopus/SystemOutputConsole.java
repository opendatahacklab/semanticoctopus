package org.opendatahacklab.semanticoctopus;

/**
 * Default implementatoio of {@link OutputConsole} which uses System.out and which is thread-safe
 * @author Cristiano Longo
 *
 */
public class SystemOutputConsole implements OutputConsole {

	public static final OutputConsole INSTANCE = new SystemOutputConsole();
	
	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.OutputConsole#println(java.lang.String)
	 */
	@Override
	public synchronized void println(final String message) {
		System.out.println(message);
	}

	/* (non-Javadoc)
	 * @see org.opendatahacklab.semanticoctopus.OutputConsole#print(java.lang.String)
	 */
	@Override
	public synchronized void print(final String message) {
		System.out.print(message);
	}
}
