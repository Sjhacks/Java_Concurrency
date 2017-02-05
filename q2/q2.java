package question3;
import java.awt.Color;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class q2 {

    // The image constructed
    public static BufferedImage img;

    // Image dimensions; you could also retrieve these from the img object.
    public static int width;
    public static int height;
    //volatile static int pixels;

    // Convert a small non-negative integer into a pixel value.
    // This is not great code, feel free to make your own.
    public static int rgbFromN(int n) {
        int c = 255<<24;  // ensure alpha is 0xff

        // Give the first three threads red, green, blue colours, then get creative.
        switch(n) {
        case 0:  c |= 255<<16;
            break;
        case 1:  c |= 255<<8;
            break;
        case 2:  c |= 255;
            break;
        case 3:  c |= 127<<16 | 127<<8;
            break;
        case 4:  c |= 127<<16 | 127;
            break;
        case 5:  c |= 127<<8 | 127;
            break;
        default: c |= (((99+3*n)%256)&0xff)<<16 | (((123+2*n)%256)&0xff)<<8 | ((17+n)&0xff);
        }
        return c;
    }
    
    /**
     * 
     * runnable that choses random pixels and colors them if they aren't colored already.
     * when the number of blank pixels is 0, thread terminates.
     * number of blank pixels is kept in an atomic integer to avoid concurrency issues.
     *
     */
    public static class cThread implements Runnable {
    	static AtomicInteger pixels = new AtomicInteger(0);
    	int width;
    	int height;
    	static boolean stop = false;
    	public cThread(int h, int w){
    		width =w;
    		height = h;
    		pixels.set(width*height);
    	}
    	Random rand = new Random();
    	public void run(){
    		float r = rand.nextFloat();
    		float g = rand.nextFloat();
    		float b = rand.nextFloat();
    		Color color = new Color(r,g,b);
    		
    		while(pixels.get()>0 && stop == false){
    			//System.out.println(pixels.get());
        		int x = (int)( Math.random()*width);
        		int y = (int)( Math.random()*height);
        		//synchronized(this){
	        		if(img.getRGB(x, y)==0){
	        			img.setRGB(x, y, color.getRGB());
	        			pixels.decrementAndGet();
	        		}
	        	//}
        	}
    		stop =true;
    	}

    }
    
    public static void main(String[] args) {
        //Random rand = new Random();
        try {
            if (args.length<3)
                throw new Exception("Missing arguments, only "+args.length+" were specified!");
            // arg 0 is the width
            width = Integer.parseInt(args[0]);
            // arg 1 is the height
            height = Integer.parseInt(args[1]);
            // arg 2 is the number of threads
            int nt = Integer.parseInt(args[2]);
            //pixels = width*height;
            // create an image and initialize it to all 0's
            img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
            
            /**
             * create all the threads, start them, then join them
             */
            Thread[] threads = new Thread[nt];
            long start = System.currentTimeMillis();
            cThread c = new cThread(height,width);
            for(int i=0;i<nt;i++){
            	//cThread c = new cThread(height,width);
            	Thread t = new Thread(c);
            	t.start();
            	threads[i] = t;
            }
            for(Thread t:threads){
            	t.join();
            }
            //cthread.start();
            //cthread.join();
            System.out.println(System.currentTimeMillis()-start);
            
            
            // Write out the image
            File outputfile = new File("outputimage.png");
            ImageIO.write(img, "png", outputfile);

        } catch (Exception e) {
            System.out.println("ERROR " +e);
            e.printStackTrace();
        }
    }
}
