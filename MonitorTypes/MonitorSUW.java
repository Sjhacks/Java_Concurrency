package question3;

import java.util.concurrent.Semaphore;

public class MonitorSUW extends Monitor {
	// Queue for the threads that have signaled and thus yielded to other threads,
	//but will be given priority once current thread finishes
	Semaphore signaled = new Semaphore(0);
	
	/**
	 * Exit method restarts thread from signaled queue if there are any, otherwise restarts threads
	 * from monitor queue/ frees the monitor
	 */
	public void exit(){
		if(signaled.hasQueuedThreads()){
			signaled.release();
		}
		else {
			monitorSem.release();;
		}
		
	}
	
	/**
	 * restarts a thread on signaled queue or monitorSem queue with signaled having priority
	 * Blocks current thread by making it wait on the condition
	 */
	public void await() throws InterruptedException{
		
		if(signaled.hasQueuedThreads()){
			signaled.release();
		}
		else {
			System.out.println("released");
			monitorSem.release();;
		}
		condition.acquire();
	}
	
	/**
	 * unblocks a thread waiting on condition then adds current thread to signaled queue
	 */
	public void signal(){
		if(condition.hasQueuedThreads()){
			condition.release();
			System.out.println("signaled");
			try {
				signaled.acquire();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
	}
}
