package question2;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;


public class q2 {

	static AtomicIntegerArray array;
	static AtomicBoolean[] check;
	
	/**
	 * 
	 * @author Shawn
	 *Sorting threads
	 *chooses random indices and swap atomically if required, otherwise chooses new indices.
	 */
	public static class sortThread implements Runnable{
		
		//run method uses atomic swapping algorithm on two random indices of the array
		public void run(){
			while(true){
				int index = (int)(Math.random()*array.length()-1);
				int j = index+1;
				if(array.get(index)>array.get(j)){
					while (true) {
					      int ai = array.getAndSet(index, -1);
					      if (ai == -1) continue;
					      int aj = array.getAndSet(j, -1);
					      if (aj == -1) {
					        array.set(index, ai);
					        continue;
					      }
					      array.set(index, aj);
					      array.set(j, ai);
					      break;
					}
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					System.out.println("done");
					return;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		int m = 0;
		int n = 0;
		
		//parse command line input
		if (args.length > 1) {
		    try {
		        m = Integer.parseInt(args[0]);
		        n = Integer.parseInt(args[1]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0]+"and"+ args[1] + " must be integers.");
		        System.exit(1);
		    }
		}
		else{
			System.out.println("incorrect number of arguments");
		}
		
		//set array length
		array = new AtomicIntegerArray(m);
		check = new AtomicBoolean[array.length()];
		
		//fill array with random ints 0-1000
		for(int i =0; i<array.length(); i++){
			array.set(i, (int)(Math.random()*1000));
		}
		//array sorted if size <=1
		if(array.length() <= 1){
			System.out.println("Sorted");
			if(array.length() == 1){
				System.out.println(array.get(0));
			}
			return;
		}
		
		// verifier thread that runs until sorted
		Thread verThread = new Thread(new Runnable(){
			public void run (){
				for(int i = 0; i<array.length()-1;i++){
					if(array.get(i)>array.get(i+1)){
						i=0;
					}
				}
				//System.out.println("sorted array:");
			}
		});
		
		//initializes n threads
		Thread[] threads = new Thread[n];
		sortThread s0 = new sortThread();
		for(int i=0;i<n;i++){
			Thread t0 = new Thread(s0);
			threads[i] = t0;
			t0.start();
		}
		verThread.start();
		
		//wait until all threads complete
		try {
			verThread.join();
		} catch (InterruptedException e) {
		}
		for(Thread t: threads){
			t.interrupt();
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("sorted array:");
		//print out sorted array
		for(int i =0; i<array.length(); i++){
			System.out.print(array.get(i)+" ");
		}
	}

}
