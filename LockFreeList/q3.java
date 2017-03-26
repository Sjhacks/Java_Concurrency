package question2_2;

public class q3 {

	public static void main(String[] args) {
		LockFreeList<Character> list = new LockFreeList<Character>('A','C');
		//list.add('A', 0);
		list.add('B', 1);
		//list.add('C', 2);
		//list.add('D', 3);
		//list.add('E', 4);
		
		//list.display();
		//System.out.println(list.getHead().getData());
		
		Thread thread0 = new Thread(new Runnable(){
			public void run(){
				System.out.print("thread0 output: ");
				list.printout();
				//System.out.println("%n");
				
			}
		});
		
		Thread thread1 = new Thread(new Runnable(){
			public void run(){
				list.remover();
				
			}
		});
		
		Thread thread2 = new Thread(new Runnable(){
			public void run(){
				list.adder();
			}
		});
		
		long runtime = 5000000000L;
		long start = System.nanoTime();
		thread0.start();
		thread1.start();
		thread2.start();
		while(true){
			if(System.nanoTime()- start >= runtime){
				thread0.interrupt();
				thread1.interrupt();
				thread2.interrupt();
				System.out.format("%n");
				//System.out.println("times up");
				break;
			}
		}
		try {
			thread0.join();
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
		}
		list.display();
		
	}

}
