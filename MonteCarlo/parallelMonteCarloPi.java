import java.text.DecimalFormat;

public class parallelMonteCarloPi {
    /* variables/constants to be used by all threads */

    private static double sum = 0.0;
    private static int nThreads;
    private static int count;

    /* main method */

    public static void main(String[] args) {

        /* process command-line arguments */
        if (args.length != 3)  {
            System.out.println("arguments are number_of_samples seed number_of_thread");
            System.exit(1);
        }
        int numSamples = 0;
        int seed = 0;
        try {
            numSamples = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
            nThreads = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException e) {
            System.out.println("arguments are number_of_samples seed number_of_thread");
            System.exit(1);
        }

        /* start timing */
        long startTime = System.currentTimeMillis();

        /* create threads */
        Thread[] threads = new Thread[nThreads];
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new Thread(new CodeForThread(i, numSamples, seed));
        }

        /* start them up */
        for (int i = 0; i < threads.length; ++i) {
            threads[i].start();
        }

        /* wait for them to finish */
        for (int i = 0; i < threads.length; ++i) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }

        /* finish computation */
        double pi = 4.0*(double)count/numSamples;

        /* end timing and print result */
        /* record end time and print results */
        long endTime = System.currentTimeMillis();
        DecimalFormat f12p10 = new DecimalFormat("#.0000000000");
        System.out.println("Parallel Java program results:");
        System.out.println("number of threads = " + nThreads);
        System.out.println("number of samples = " + numSamples +
                ", seed = " + seed);
        System.out.println("estimated pi = " + f12p10.format(pi));
        System.out.println("difference between estimated pi and Math.PI = " +
                f12p10.format(Math.abs(pi - Math.PI)));
        System.out.println("time to compute = " +
                ((double) (endTime - startTime) / 1000.0) + " seconds");
    }

    /* static inner class to contain code to run in each thread */


    private static class CodeForThread implements Runnable {

        private int myID;
        private int numSamples;
        private int seed;
        private double state;

        public CodeForThread(int myID, int numSamples, int seed) {
            this.myID = myID;
            this.numSamples = numSamples;
            this.seed = seed;
        }

        public void run() {
        	seed = (seed/(myID+1))*3;
            //Random randGenerator = new Random((long) seed);
            
            RandNumGen randar = new RandNumGen();
            RandNumGen.initialize(seed);
            
            int partCount = 0;
            for (int i=0; i < (numSamples/nThreads); i++) {
                double x = RandNumGen.generate();//randGenerator.nextDouble();
                //System.out.println("x value" + x);
                double y = RandNumGen.generate();//randGenerator.nextDouble();
                //System.out.println("y value" + y);
                if ((x*x + y*y) <= 1.0)
                    ++partCount; 
            }
            /* only one thread at a time can do this */
            synchronized(getClass()) {
                count += partCount;
            }
        }
    }



}
                  
