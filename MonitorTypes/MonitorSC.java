package question3;

public class MonitorSC extends Monitor {
	
	/**
	 * frees up monitor queue upon exiting
	 */
	public void exit(){
		
		monitorSem.release();;
		
	}
	/**
	 * current thread leave monitor and allows other thread to enter while it blocks
	 * on a condition. Once the condition is acquired, this thread is then moved to
	 * the monitor queue
	 */
	public void await() throws InterruptedException{
		
		System.out.println("released");
		monitorSem.release();;
		
		condition.acquire();
		monitorSem.acquire();
	}
	/**
	 * current thread signals another thread waiting on the condition queue.
	 * current thread continues executing in the monitor after signaling the condition queue.
	 */
	public void signal(){
		if(condition.hasQueuedThreads()){
			condition.release();
			System.out.println("signaled");
			
		}
	}
}
