import java.util.Random;
import java.util.Comparator;

/*
 * Java program to test/time quicksort.
 *
 * sequential version.
 *
 * command-line arguments:  number of elements to sort, seed for RNG.
 */
public class QuickSortSeq {

    /* main method */

    public static void main(String[] args) {

        /* process command-line arguments */
        if (args.length < 2) {
            System.err.println("arguments are numElements, seed");
            System.exit(1);
        }
        int n = 0;
        int seed = 0;
        try {
            n = Integer.parseInt(args[0]);
            seed = Integer.parseInt(args[1]);
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
        sort(ints);
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
    public static void sort(Comparable[] data) { 
		sort(data, 0, data.length-1);
    }
    public static void sort(Comparable[] data, int lo, int hi){
    	if (hi<=lo) return;
    	int j = partition(data, lo, hi);
    	sort(data, lo, j-1);
    	sort(data, j+1, hi);
    	assert isSorted((Integer[])data);
    	
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
    	
    	return j;
    }
   
    
    
    
    
    
    
    
}