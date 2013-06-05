
/*
 * Java program to compute the value of pi using Monte Carlo
 *
 * command-line arguments:  number_of_samples, seed
*/
import java.util.Random;
import java.text.DecimalFormat;

public class seqMonteCarloPi {
    public static void main(String[] args) {

        /* process command-line arguments */
        if (args.length != 2)  {
            System.out.println("arguments are number_of_samples seed");
            System.exit(1);
        }
        int numSamples = 0;
        int seed = 0;
        try {
            numSamples = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            System.out.println("arguments are number_of_samples seed");
            System.exit(1);
        }

        /* record start time */
        long startTime = System.currentTimeMillis();

        /* do calculation */ 
        RandNumGen randar = new RandNumGen();
        RandNumGen.initialize(seed);
        
        int count = 0;
        for (int i=0; i < (numSamples); i++) {
            double x = RandNumGen.generate();//randGenerator.nextDouble();
            //System.out.println("x value" + x);
            double y = RandNumGen.generate();//randGenerator.nextDouble();
            //System.out.println("y value" + y);
            if ((x*x + y*y) <= 1.0)
                ++count; 
 /*       Random randGenerator = new Random((long) seed);
        int count = 0;
        for (int i = 0; i < numSamples; ++i) {
            double x = randGenerator.nextDouble();
            double y = randGenerator.nextDouble();
            if ((x*x + y*y) <= 1.0)
                ++count;*/
        }
        double pi = 4.0 * (double) count / (double) numSamples;

        /* record end time and print results */
        long endTime = System.currentTimeMillis();
        DecimalFormat f12p10 = new DecimalFormat("#.0000000000");
        System.out.println("sequential Java program results:");
        System.out.println("number of samples = " + numSamples +
                ", seed = " + seed);
        System.out.println("estimated pi = " + f12p10.format(pi));
        System.out.println("difference between estimated pi and Math.PI = " +
                f12p10.format(Math.abs(pi - Math.PI)));
        System.out.println("time to compute = " +
                ((double) (endTime - startTime) / 1000.0) + " seconds");
    }

}
