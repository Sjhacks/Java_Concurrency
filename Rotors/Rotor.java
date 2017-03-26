
public class Rotor implements Runnable {
	
	private int checks,maxSpeed,successes = 0;
	private volatile int goalSpeed = 0;
	private int checkInterval;
	private int id;
	private volatile static int speedr1,speedr2,speedr3,speedr4 = 0;
	/**
	 * constructor
	 * @param id
	 * @param check
	 */
	public Rotor(int id, int check){
		this.checkInterval = check;
		this.id = id;
	}
	
	@Override
	/**
	 * checks the speed goal set by the controller every y seconds and changes
	 * its own(id) speed according to the drain available.
	 * prints out the result after interruption from the controller
	 */
	public void run() {
		while(true){
			//System.out.format("Thread %d:%d%n", id,goalSpeed);
			synchronized(this){
			switch(id){
				case 1:checks++;
					if(speedr1!=goalSpeed){
						if(speedr2+speedr3+speedr4+goalSpeed >=20){
							speedr1 = Math.max(0, 20-(speedr2+speedr3+speedr4));
						}else{
							speedr1 = goalSpeed;
							successes++;
						}
						if (speedr1>maxSpeed){
							maxSpeed=speedr1;
						}
				}break;
				case 2:checks++;
					if(speedr2!=goalSpeed){
						if(speedr1+speedr3+speedr4+goalSpeed >=20){
							speedr2 = Math.max(0, 20-(speedr1+speedr3+speedr4));
						}else{
							speedr2 = goalSpeed;
							successes++;
						}
						if (speedr2>maxSpeed){
							maxSpeed=speedr2;
						}
				}break;
				case 3:checks++;
					if(speedr3!=goalSpeed){
						if(speedr2+speedr1+speedr4+goalSpeed >=20){
							speedr3 = Math.max(0, 20-(speedr2+speedr1+speedr4));
						}else{
							speedr3 = goalSpeed;
							successes++;
						}
						if (speedr3>maxSpeed){
							maxSpeed=speedr3;
						}
				}break;
				case 0:checks++;
					if(speedr4!=goalSpeed){
						if(speedr2+speedr3+speedr1+goalSpeed >=20){
							speedr4 = Math.max(0, 20-(speedr2+speedr3+speedr1));
						}else{
							speedr4 = goalSpeed;
							successes++;
						}
						if (speedr4>maxSpeed){
							maxSpeed=speedr4;
						}
				}break;
			}
			//System.out.format("Current Drain:%d%n", speedr1+speedr2+speedr3+speedr4);
			}
			try {
				Thread.sleep(checkInterval);
			} catch (InterruptedException e) {
				//System.out.println("stopped");
				System.out.format("Rotor %d: checks =%d, success rate=%f, max=%d%n",id,checks,(float)successes/checks,maxSpeed);
				return;
			}
		}
	}
	/**
	 * Setter for the speed goal of a rotor. Used by the controller
	 * @param speed
	 */
	public void setGoal(int speed){
		this.goalSpeed = speed;
	}
	
}
