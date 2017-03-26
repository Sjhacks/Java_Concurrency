package question3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public abstract class Monitor {
	protected Queue<Thread> e = new LinkedList<Thread>();
	//condition queue that holds threads waiting on the condition
	Semaphore condition = new Semaphore(0);
	//monitor queue that holds threads waiting to enter the monitor
	Semaphore monitorSem = new Semaphore(1);
	
	/**
	 * monitor class implements the enter method since it's the same for all
	 * monitor types.
	 */
	public void enter(){
		try {
			monitorSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//declares the abstract methods require for monitors
	public abstract void exit();
	public abstract void await() throws InterruptedException;
	public abstract void signal();
}
