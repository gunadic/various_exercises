/*
 * C program to compute the value of pi using Monte Carlo
 *
 * 
 * command-line arguments:  number_of_samples, seed
*/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>   /* has M_PI, fabs() */
#include "timer.h"  /* has get_time() */
#include <omp.h>
#define m 2147483647
#define a 16807
#define c 0


double generate(unsigned long *input){
    *input = ((a*(*input))+c)%m;
    return (double) (*input)/m;
}


/* main program */

int main(int argc, char* argv[]) {
	
	
		int num_samples;
		long seed=0,orig_seed;
		double start_time, end_time;
		int count =0;
		double x,y;
		int i;
		double pi;

	
    	/* process command-line arguments */
    	if (argc != 3)  {
        	fprintf(stderr, "usage:  %s number_of_samples seed\n", argv[0]);
        	exit(EXIT_FAILURE);
    	}
    	num_samples = atoi(argv[1]);
    	orig_seed = seed = atoi(argv[2]);
    	if ((num_samples <= 0) || (seed <= 0)) {
        	fprintf(stderr, "usage:  %s number_of_samples seed\n", argv[0]);
        	exit(EXIT_FAILURE);
    	}
	
		#pragma omp parallel
		{
    		/* record start time */
    		start_time = get_time();
			
    		/* do calculation */ 
			long locSeed = seed + 3  *( (omp_get_thread_num()) +1);
    		//srandom((unsigned int) seed);
			#pragma omp for private(x,y) reduction(+:count) schedule(static)
    		for (i = 0; i < num_samples; ++i) {
        		x = generate(&locSeed);
				//	printf("value of x: %f\n", x);
					//(double) random() / (double) (RAND_MAX);
        		y = generate(&locSeed);
					//(double) random() / (double) (RAND_MAX);
    	    	if ((x*x + y*y) <= 1.0){
	        	    ++count;
					//printf("count is %d\n", count);
					if(count == 0)
						printf("count equals 0");
				}
	    	}
			//printf("count equals %d\n", count);
		}
    		pi = 4.0 * (double) count / (double) num_samples;
	
	    	/* record end time and print results */
	    	end_time = get_time();
	
    		printf("Open MP  program results:\n");
    		printf("number of samples = %d, seed = %d\n", num_samples, orig_seed);
	    	printf("estimated pi = %12.10f\n", pi);
	    	printf("difference between estimated pi and math.h M_PI = %12.10f\n", 
        	 	   fabs(pi - M_PI));
    		printf("time to compute = %g seconds\n", end_time - start_time);
	
	
    /* clean up and return */
    return EXIT_SUCCESS;
}
