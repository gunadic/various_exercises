import java.util.Random;
import java.util.Comparator;

/*
 * Java program to test/time quicksort.
 *
 * sequential version.
 *
 * command-line arguments:  number of elements to sort, seed for RNG.
 */
public class QuickSortParallel {

	private static int nThreads;
	
    /* main method */

    public static void main(String[] args) {

        /* process command-line arguments */
        if (args.length < 3) {
            System.err.println("arguments are numElements, seed, numThreads");
            System.exit(1);
        }
        int n = 0;
        int seed = 0;
        int numThreads = 0;
        try {
            n = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
            numThreads = Integer.parseInt(args[2]);
            }
        catch (NumberFormatException e) {
            System.err.println("arguments must be integers");
            System.exit(1);
        }

        /* generate data */
        Integer[] ints = new Integer[n];
        Random randGen = new Random((long) seed);
        for (int i = 0; i < ints.length; ++i) {
            ints[i] = new Integer(randGen.nextInt());
        }

        /* sort (time this part only) */
        long startTime = System.currentTimeMillis();  
        sort(ints, numThreads);
        long endTime = System.currentTimeMillis();

        /* check whether sort succeeded */
        if (isSorted(ints)) {
            System.out.println("sort succeeded");
        }
        else {
            System.out.println("sort failed");
        }

        /* print timing result */
        System.out.println("Time for sort of " + n + 
                " random integers (seed " + seed + ") is " +
                ((double) (endTime - startTime) / 1000) + " seconds");
    }

    /* check whether array is sorted */
    public static boolean isSorted(Integer[] data) {
        for (int i = 0; i < data.length-1; ++i) {
            if (data[i].compareTo(data[i+1]) > 0)
                return false;
        }
        return true;
    }
    
    /* Functions that make my life easier */
    
    private static boolean less(Comparable a, Comparable b) {
        return (a.compareTo(b) < 0);
    }
    
    private static void exch(Object[] data, int i, int j) {
        Object swap = data[i];
        data[i] = data[j];
        data[j] = swap;
    }
 
    /* main quicksort routine */
    public static void /*Integer[]*/ sort(Comparable[] data, int numThreads) { 
    	sort(data, 0, data.length-1, numThreads);
    	//Integer[] temp = sort(data, 0, data.length-1, numThreads);
		//return temp;
    }
    public static void /*Integer[]*/ sort(Comparable[] data, int lo, int hi,int numThreads){
    	if (hi<=lo) return;
    	
    	int j = partition(data, lo, hi);
    	Thread otherThread = null;
    	CodeForThread other = null;
    	/*sort left half of array */
    	if (numThreads>1){
    		other = new CodeForThread(data, lo, j-1, numThreads/2);
		//	System.out.println("Creating a thread");
	   		otherThread = new Thread(other);
    		otherThread.run();
    	}
    	else{
    		/*sort in same thread*/
    		sort(data,lo,j-1,numThreads/2);
    	}
    	/*sort right half*/
    	sort(data, j+1, hi, numThreads-(numThreads/2));
    	if (numThreads > 1) {
    		try{
    			otherThread.join();
    		}
    		catch (InterruptedException e){
    			System.out.println("ruh roh...");
    		}
    	}
/*    	if(numThreads==1){
    		for(int i = 0; i < data.length ; i++){
        		System.out.println(Integer.toString((Integer)data[i]));
    		}
    	}
/*    	if(isSorted((Integer[])data)==false)
    		return null;
    	else
    		return (Integer[]) data;*/
    	assert isSorted((Integer[])data);

//        for(int i = 0; i < data.length ; i++){
  //          System.out.println(Integer.toString((Integer)data[i]));
    //    }


    	
    }
    public static int partition(Comparable[] data, int lo, int hi){
    	int i = lo;
    	int j = hi + 1;
    	Comparable v = data[lo];
    	while(true){
    		
    		// find item on lo to swap
    		while(less(data[++i], v))
    			if (i == hi) break;
    		//find item on hi to swap
    		while(less(v, data[--j]))
    			if (j==lo) break;
    		
    		if (i >= j) break;
    		
    		exch(data,i,j);
    	}
    	
    	exch(data, lo, j);
    	
		//System.out.println("J = " + j);
    	return j;
    }
    
    private static class CodeForThread implements Runnable {
    	private int myID;
    	
    	private Comparable[] data;
    	private int high;
    	private int low;
    	int numThreads;
    	private Comparable[] result;
    	
    	public CodeForThread(Comparable[] d, int lo, int hi, int nThreads){
    		data = d;
    		high = hi;
    		low = lo;
    		numThreads = nThreads;
    	}
    	
		public void run() {
				sort(data, low, high, numThreads);
		}
		
      }
    
}
