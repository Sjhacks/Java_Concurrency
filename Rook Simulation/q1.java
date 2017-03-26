package question1;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class q1 {

	//the locks array is the array that will be used to lock specific indexes of the board array
	//global rooks represents the number of threads executing(not rooks as there will be 1 left when all threads are done)
	static AtomicInteger[][] board = new AtomicInteger[8][8];
	static AtomicBoolean[][] locks = new AtomicBoolean[8][8];
	static AtomicInteger globalRooks = new AtomicInteger(0);
	
	public static class RookThread implements Runnable {
		//each thread keeps track of its postion and id
		int rid;
		AtomicBoolean dead =new AtomicBoolean(false);
		int x,y;
		/**
		 * Constructor that also places the rooks on the specified spot on the board before runnning the thread
		 * @param id unique identifier for each rook
		 * @param xpos x position on board
		 * @param ypos y position on board
		 */
		RookThread(int id, int xpos, int ypos){
			board[xpos][ypos].set(id);
			rid = id;
			x = xpos;
			y = ypos;
		}
		/**
		 * randomly chooses a plane then a direction then a number of spaces to move
		 * Once the choice is made, locks the corresponding indexes, moves the rook to new location
		 * and leaves old location empty then unlocks. If a thread finds out it has been removed, terminates.
		 * sleeps 10 ms in between each move attempt.
		 */
		public void run(){
			while(board[this.x][this.y].get() == this.rid && globalRooks.get()>1){
				int rc =(int)(Math.random()*2);
				int direction =(int)(Math.random()*2);
				
				if(rc == 0){
					if(direction == 0){
						if(this.x>0){
							int destination = (int)(Math.random()*(this.x));
							if(locks[this.x][this.y].compareAndSet(false, true)){
								if(locks[destination][this.y].compareAndSet(false, true)){
									board[this.x][this.y].set(0);
									board[destination][this.y].set(this.rid);
									locks[this.x][this.y].set(false);
									this.x = destination;
									locks[this.x][this.y].set(false);
								}else{
									locks[this.x][this.y].set(false);
									try {
										Thread.sleep(10);
									} catch (InterruptedException e) {
										//System.out.println("done");
										return;
									}
									continue;
								}
							}else{
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									//System.out.println("done");
									return;
								}
								continue;
							}
						}
						else{
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								//System.out.println("done");
								return;
							}
							continue;
						}
					}else{
						if(this.x<7){
							int destination = (int)(Math.random()*(7-this.x))+1;
							if(locks[this.x][this.y].compareAndSet(false, true)){
								if(locks[this.x+destination][this.y].compareAndSet(false, true)){
									board[this.x][this.y].set(0);
									board[destination+this.x][this.y].set(this.rid);
									locks[this.x][this.y].set(false);
									this.x = this.x+destination;
									locks[this.x][this.y].set(false);
								}else{
									locks[this.x][this.y].set(false);
									try {
										Thread.sleep(10);
									} catch (InterruptedException e) {
										//System.out.println("done");
										return;
									}
									continue;
								}
							}else{
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									//System.out.println("done");
									return;
								}
								continue;
							}
						}
						else{
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								//System.out.println("done");
								return;
							}
							continue;
						}
					}
				}else{
					if(direction==0){
						if(this.y>0){
							int destination = (int)(Math.random()*(this.y));
							if(locks[this.x][this.y].compareAndSet(false, true)){
								if(locks[this.x][destination].compareAndSet(false, true)){
									board[this.x][this.y].set(0);
									board[this.x][destination].set(this.rid);
									locks[this.x][this.y].set(false);
									this.y = destination;
									locks[this.x][this.y].set(false);
								}else{
									locks[this.x][this.y].set(false);
									try {
										Thread.sleep(10);
									} catch (InterruptedException e) {
										//System.out.println("done");
										return;
									}
									continue;
								}
							}else{
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									//System.out.println("done");
									return;
								}
								continue;
							}
						}
						else{
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								//System.out.println("done");
								return;
							}
							continue;
						}
					}else{
						if(this.y<7){
							int destination = (int)(Math.random()*(7-this.y))+1;
							if(locks[this.x][this.y].compareAndSet(false, true)){
								if(locks[this.x][this.y+destination].compareAndSet(false, true)){
									board[this.x][this.y].set(0);
									board[this.x][this.y+destination].set(this.rid);
									locks[this.x][this.y].set(false);
									this.y = this.y+destination;
									locks[this.x][this.y].set(false);
								}else{
									locks[this.x][this.y].set(false);
									try {
										Thread.sleep(10);
									} catch (InterruptedException e) {
										//System.out.println("done");
										return;
									}
									continue;
								}
							}else{
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									//System.out.println("done");
									return;
								}
								continue;
							}
						}
						else{
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								//System.out.println("done");
								return;
							}
							continue;
						}
					}
					
				}
				
				try {
						Thread.sleep(10);
				} catch (InterruptedException e) {
						//System.out.println("Thread done");
						return;
				}
			}
			globalRooks.decrementAndGet();
			return;
		}
	}
	
	public static void main(String[] args) {
		int numRooks = 0;
		//parse command line input
		if (args.length > 0) {
		    try {
		        numRooks = Integer.parseInt(args[0]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0]+" must be integer.");
		        System.exit(1);
		    }
		}
		if(numRooks >64){
			System.exit(1);
		}
		//keeps track of total approximate threads executing
		globalRooks.set(numRooks);
		
		//populates AtomicBoolean array with atomic booleans set to false
		for(int i =0;i<8;i++){
			for(int j =0;j<8;j++){
				locks[i][j] = new AtomicBoolean(false);
			}
		}
		
		//populates AtomicInteger array with 0(empty)
		for(int i =0;i<8;i++){
			for(int j =0;j<8;j++){
				board[i][j] = new AtomicInteger(0);
			}
		}
		
		//creates p rooks and places them on the board in random locations then adds them to an array
		Thread[] threads = new Thread[numRooks]; 
		for(int i = 1; i<numRooks+1; i++){
			int x =(int)(Math.random()*8);
			int y =(int)(Math.random()*8);
			if(board[x][y].get() != 0){
				i--;
				continue;
			}
			Thread t = new Thread(new RookThread(i,x,y));
			threads[i-1] = t;
		}
		
		//starts all threads in the array
		for(int i =0; i<threads.length; i++){
			threads[i].start();
		}
		
		//start time
		long start = System.currentTimeMillis();
		//stops the simulation automatically after 10 seconds or if there is only 1 rook left
		while(System.currentTimeMillis()-start<10000 && globalRooks.get()>1){
			
		}
		for(int i =0;i<threads.length;i++){
			threads[i].interrupt();
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				
			}
		}
		long runtime = System.currentTimeMillis() -start;
		int remain =0;
		
		//gets all remaining rooks in the array
		for(int i=0;i<8;i++){
			for(int j =0;j<8;j++){
				if(board[i][j].get() != 0){
					remain++;
				}
			}
		}
		System.out.print("Simulation terminated after "+runtime+"ms. ");
		System.out.println("Remaining pieces: " + remain);
		//System.out.println(globalRooks.get());
		
		//System.out.println(Arrays.deepToString(board));
		//System.out.println(Arrays.deepToString(locks));
		
	}

}
