package question3;

public class MonitorSW extends Monitor {
	
	/**
	 * frees up monitor when thread calls exit
	 */
	public void exit(){
		
		monitorSem.release();;
		
	}
	/**
	 * frees up monitor and blocks current thread by waiting on condition
	 */
	public void await() throws InterruptedException{
		
		System.out.println("released");
		monitorSem.release();;
		
		condition.acquire();
	}
	/**
	 * signals a thread waiting on the condition queue, and adds
	 * this thread to threads waiting to enter monitor
	 */
	public void signal(){
		if(condition.hasQueuedThreads()){
			condition.release();
			System.out.println("signaled");
			try {
				monitorSem.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
