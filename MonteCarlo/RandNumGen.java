/*
 *  * Random Number Generator using a Linear congruential generator.
 *   *  nextSt = ((a(curSt) + c) % m)
 *    *  
 *     */
public class RandNumGen {


    private static long m = 2147483647;
    private static long a = 16807;
    private static long c = 0;
    static double curSt;
	static double nextSt;


    public static void main (String[] args){
        int numSamps = 10000;
        /**
 *       * come back and muck about with numSamps
 *               */

        for(int i =0; i<numSamps;i++){
            generate();
            System.out.println(nextSt/m);
            //curSt = nextSt;
        }
    }
    
    static void initialize(long seed){
    	curSt = seed;
    }

    static double generate(){
    	nextSt = ((a*curSt)+c)%m;
    	curSt = nextSt;
    	return(nextSt/m);
    }
}

