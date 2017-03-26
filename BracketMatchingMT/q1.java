import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class q1 {
	
	static Triple[] results;
	
	public static void main(String[] args) {
		
		int n = 0;
		int t = 0;
		int jobs;
		
		//parse command line input
		if (args.length > 1) {
		    try {
		        n = Integer.parseInt(args[0]);
		        t = Integer.parseInt(args[1]);
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0]+"and"+ args[1] + " must be integers.");
		        System.exit(1);
		    }
		}
		else{
			System.out.println("incorrect number of arguments");
		}
		
		char[] b = Bracket.construct(n);
		//boolean v = Bracket.verify();
		//Bracket.print();
		//System.out.println(v);
		//Triple triple = verifyPart(b,0,n-1);
		//triple.print();
		int workingSize = n/t;
		if(t>n){
			jobs = n;
		}else{
			jobs = t;
		}
		results = new Triple[jobs];
		//System.out.println("");
		long start = System.currentTimeMillis();
		ExecutorService executor = Executors.newFixedThreadPool(t);
		for (int i =0; i<jobs;i++){
			if(i==jobs-1){
				Runnable worker = new WorkerThread(b, i*workingSize,n-1,i);
				executor.execute(worker);
			}else{
			Runnable worker = new WorkerThread(b, i*workingSize, ((i+1)*workingSize)-1, i);
			executor.execute(worker);
			}
		}
		executor.shutdown();
        while (!executor.isTerminated()) {
        }
        //System.out.println("Finished all threads");
        System.out.println("execution time: "+ (System.currentTimeMillis()-start)+"ms");
        /*results[0].print();
        results[1].print();
        results[2].print();
        System.out.println("");
        combine(combine(results[0],results[1]),results[2]).print();*/
        Triple answer = results[0];
        if(results.length ==1){
        	//System.out.print(results[0].ok);
        }else{
	        for (int i =1;i<results.length;i++){
	        	answer = combine(answer,results[i]);
	        	
	        }
        }
        System.out.print(answer.ok + " ");
        System.out.print(Bracket.verify());
        //answer.print();
		
	}

	public static Triple combine(Triple a, Triple b){
		boolean okay = (a.ok && b.ok) || (((a.f+b.f)==0) && (a.m>=0) && ((a.f+b.m)>=0));
		int eff = a.f + b.f;
		int emm = Math.min(a.m, (a.f+b.m));
		
		Triple triple = new Triple(okay,eff,emm);
		
		return triple; 
	}
	
	public static Triple verifyPart(char[] array, int start, int end) {
        int b = 0;
        int min = 0;
        boolean ver = false;
        for (int i=start;i<=end;i++) {
            if (array[i]=='(') {
                b++;
            } else if (array[i]==')') {
                b--;
                if (b<min) min =b;
            }
        }
        if ((b!=0)||(min<0)){
        	ver = false;
        }else{
        	ver = true;
        }
        Triple result = new Triple(ver,b,min);
        return result;
	}
	
	public static class Triple {
		public boolean ok;
		public int f,m;
		
		public Triple (boolean ok, int f, int m){
			this.ok = ok;
			this.f = f;
			this.m = m;
		}
		
		public void print(){
			System.out.println(ok);
			System.out.println(f);
			System.out.println(m);
		}
	}
	public static class WorkerThread implements Runnable {
		  
	    public char[] string;
	    int start,end,id;
	    public Triple result;
	    
	    public WorkerThread(char[] string, int start, int end, int id){
	        this.string = string;
	        this.start = start;
	        this.end = end;
	        this.id = id;
	    }

	    @Override
	    public void run() {
	    	int counter = 0;
	        int min = 0;
	        boolean ver = false;
	        for (int i=start;i<=end;i++) {
	            if (string[i]=='(') {
	                counter++;
	            } else if (string[i]==')') {
	                counter--;
	                if (counter<min) min =counter;
	            }
	        }
	        if ((counter!=0)||(min<0)){
	        	ver = false;
	        }else{
	        	ver = true;
	        }
	        Triple result = new Triple(ver,counter,min);
	        results[id] = result;
	    }

	}
	/* Template code for making a random, sometimes matching bracket sequence. */
	public static class Bracket  {
	    public static int size;
	    public static char[] array;

	    // Static function to construct the sequence.
	    public static char[] construct(int _size) {
	        Random r = new Random();
	        size = _size; // record size for easier validation later
	        array = new char[size];
	        
	        int b = 0; // current bracket count

	        for (int i=0;i<size;) {
	            int c = r.nextInt(3);

	            // we choose the char randomly from (,),x, but with some constraints
	            switch(c) {
	            case 0:
	                // don't generate an opening bracket if there's not enough chars left to close it
	                if (size-i>b) {
	                    array[i] = '(';
	                    b++;
	                    i++;
	                }
	                break;
	            case 1:
	                // dont' generate a closing bracket if there are no open ones pending
	                if (b>0) {
	                    array[i] = ')';
	                    b--;
	                    i++;
	                }
	                break;
	            default:
	                // don't generate a non-bracket if there's not enough chars left to close the brackets
	                if (size-i>b) {
	                    array[i] = 'x';
	                    i++;
	                }
	            }
	        }
	        return array;
	    }

	    // A static, sequential verifier for the sequence.
	    public static boolean verify() {
	        int b = 0;
	        for (int i=0;i<size;i++) {
	            if (array[i]=='(') {
	                b++;
	            } else if (array[i]==')') {
	                b--;
	                if (b<0) return false;
	            }
	        }
	        if (b!=0)
	            return false;
	        return true;
	    }

	    // For debugging (of small arrays), show the array as a string.
	    public static void print() {
	        System.out.println(new String(array));
	    }
	}
}
