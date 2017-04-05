/**
 * 
 */
package org.opendatahacklab.semanticoctopus.aggregation;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executor;

/**
 * An executor whose tasks are performed just via an explicit request
 * 
 * @author Cristiano Longo
 *
 */
public class ExplicitExecutor implements Executor {

	/**
	 * A thread which can be wake up if it is waiting on its runnable
	 * 
	 * @author cristiano longo
	 *
	 */
	class ExplicitWakeUpThread extends Thread {
		private final Runnable runnable;

		ExplicitWakeUpThread(final Runnable runnable) {
			super(runnable);
			this.runnable = runnable;
		}

		void wakeUp() {
			synchronized (runnable) {
				runnable.notifyAll();
			}
		}
	}

	private final List<ExplicitWakeUpThread> tasks = new Vector<ExplicitWakeUpThread>();

	/**
	 * Use this runnable to have a task which remains suspended
	 */
	public static final Runnable BLOCKING = new Runnable() {

		@Override
		public synchronized void run() {
			try {
				wait();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	private static final long MAX_COMPLETE_TIME = 1000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Executor#execute(java.lang.Runnable)
	 */
	@Override
	public void execute(final Runnable r) {
		final ExplicitWakeUpThread t = new ExplicitWakeUpThread(r);
		tasks.add(t);
		t.start();
	}

	/**
	 * Wait until all queued tasks are complete
	 */
	public void waitForCompletion() {
		for (final Iterator<ExplicitWakeUpThread> it = tasks.iterator(); it.hasNext();) {
			final ExplicitWakeUpThread t = it.next();
			if (t.isAlive())
				try {
					t.join(MAX_COMPLETE_TIME);
						if (!t.isAlive())
							it.remove();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} else
					it.remove();
		}
	}

	/**
	 * Wait until all queued tasks are complete, wakeUp the sleeping one
	 */
	public void waitForCompletionForceWakeup() {
		for (final Iterator<ExplicitWakeUpThread> it = tasks.iterator(); it.hasNext();) {
			final ExplicitWakeUpThread t = it.next();
			if (t.isAlive())
				try {
					t.join(MAX_COMPLETE_TIME);
					if (t.isAlive()){
						t.wakeUp();
						t.join(MAX_COMPLETE_TIME);
						if (t.isAlive())
							System.err.println("Unable to complete task");
						else 
							it.remove();
					} else
						it.remove();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} else
					it.remove();
		}
	}

	/**
	 * Checks that there are no queued job
	 * 
	 * @return
	 */
	public boolean isEmpty(){
		return tasks.isEmpty();
	}
}
