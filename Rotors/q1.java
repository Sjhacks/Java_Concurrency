
public class q1 {

	public static void main(String[] args) {
		int X = 0;
		int Y = 0;
		long runtime = 10000000000L;
		if (args.length > 1) {
		    try {
		        X = Integer.parseInt(args[0]);
		        Y = Integer.parseInt(args[1]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0]+"and"+ args[1] + " must be integers.");
		        System.exit(1);
		    }
		}
		else{
			
		}
		
		Rotor r1 = new Rotor(1,Y);
		Rotor r2 = new Rotor(2,Y);
		Rotor r3 = new Rotor(3,Y);
		Rotor r4 = new Rotor(0,Y);
		
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		Thread t3 = new Thread(r3);
		Thread t4 = new Thread(r4);
	
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		long start = System.nanoTime();
		
		//loop that acts as the controller, interrupts all rotor threads after 10 seconds
		//generates random speed values and sets them in a random rotor every x seconds
		while(true){
			if(System.nanoTime()- start >= runtime){
				System.out.println("times up");
				t1.interrupt();
				t2.interrupt();
				t3.interrupt();
				t4.interrupt();
				break;
			}else{
				int rot = (int)(Math.random() * (4));
				int speed = (int)(Math.random() * (11));
				switch(rot){
					case 1:	r1.setGoal(speed);
							break;
					case 2:	r2.setGoal(speed);
							break;
					case 3:	r3.setGoal(speed);
							break;
					case 0:	r4.setGoal(speed);
							break;
				}
				try {
					Thread.sleep(X);
				} catch (InterruptedException e) {
					System.out.println("interrupted");
				}
			}
		}
		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("done");
		
	}

}
