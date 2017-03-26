package Question2;

import java.util.EmptyStackException;
import java.util.Random;
import java.util.concurrent.atomic.*;



public class q2 {

	static EliminationBackoffStack<Integer> eliminationStack;
	static int n = 0;
	static int t = 0;
	static int p = 0;
	static int d = 0;
	static int e = 0;
	static AtomicInteger pushes = new AtomicInteger();
	static AtomicInteger pops = new AtomicInteger();
	static AtomicInteger fails = new AtomicInteger();
	
	public static void main(String[] args) {
		
		//parse command line input
		if (args.length > 1) {
		    try {
		        p = Integer.parseInt(args[0]);
		        d = Integer.parseInt(args[1]);
		        n = Integer.parseInt(args[2]);
		        t = Integer.parseInt(args[3]);
		        e = Integer.parseInt(args[4]);
		        
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0]+"and"+ args[1] + " must be integers.");
		        System.exit(1);
		    }
		}
		else{
			System.out.println("incorrect number of arguments");
		}
		
		eliminationStack = new EliminationBackoffStack<Integer>(e,t);

		Thread[] threads = new Thread[p];
        long start = System.currentTimeMillis();
        StackThread stackThread = new StackThread();
        for(int i=0;i<p;i++){
        	//cThread c = new cThread(height,width);
        	Thread t = new Thread(stackThread);
        	threads[i] = t;
        	t.start();
        }
        for(Thread t:threads){
        	try {
				t.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        System.out.println((System.currentTimeMillis()-start)+ "ms");
        System.out.print("Pushes: "+pushes.get()+" ");
        System.out.print("Pops: "+(pops.get())+ " ");
        int remaining = 0;
        while(true){
        	try {
				eliminationStack.pop();
				remaining++;
			} catch (EmptyStackException | InterruptedException e1) {
				break;
			}
        }
        System.out.print("Remaining: " + remaining);
		
	}
	public static class StackThread implements Runnable{
		//int pushes;
		//int pops;
		//int failpops;
		public void run(){
			for(int i = 0; i<n;i++){
				Random random = new Random();
				int choice = random.nextInt(2);
				if(choice == 0){
					Node<Integer> n = new Node<Integer>(1);
					try {
						eliminationStack.push(n);
						pushes.incrementAndGet();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					try {
						eliminationStack.pop();
						pops.incrementAndGet();
					} catch (EmptyStackException | InterruptedException e) {
						//System.out.println("stack is empty");
						//fails.incrementAndGet();
					}
				}
				try {
					Thread.sleep(random.nextInt(d));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
