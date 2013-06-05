/*
 * MPI program that computes the value of pi using Monte Carlo
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <math.h>   /* has M_PI, fabs() */
#include "timer.h"
#include<mpi.h>
#define m 2147483647
#define a 16807
#define c 0


/* a short function to print a message, clean up, and exit */
void error_exit(char msg[]) {
    fprintf(stderr, "%s", msg);
    MPI_Finalize();
    exit(EXIT_FAILURE);
}

double generate(unsigned long *input){
    *input = ((a*(*input))+c)%m;
    return (double) (*input)/m;
}



int main(int argc, char* argv[]) {

		int nprocs;
		int myid;
        int num_samples;
        long seed=0,orig_seed;
        double start_time, end_time;
        double x,y;
        int i;
		int count =0;
        double pi;
		double sum;

		/* process command-line arguments */
        if (argc != 3)  {
            fprintf(stderr, "usage:  %s number_of_samples seed\n", argv[0]);
            exit(EXIT_FAILURE);
        }
        num_samples = atoi(argv[1]);
        orig_seed = atoi(argv[2]);
		seed = (unsigned long) orig_seed;
        if ((num_samples <= 0) || (seed <= 0)) {
            fprintf(stderr, "usage:  %s number_of_samples seed\n", argv[0]);
            exit(EXIT_FAILURE);
        }


		/* initialize for MPI */
    	if (MPI_Init(&argc, &argv) != MPI_SUCCESS) {
        	fprintf(stderr, "MPI initialization error\n");
        	return EXIT_FAILURE;
    	}
		
		/* get number of processes */
	    MPI_Comm_size(MPI_COMM_WORLD, &nprocs);

    	/* get this process's number (ranges from 0 to nprocs - 1) */
	    MPI_Comm_rank(MPI_COMM_WORLD, &myid);

		 /* record start time */
	    start_time = MPI_Wtime();

        
       	/* do calculation */

		//seed = (seed * nprocs) * (myid + 1);
        long locSeed = seed + 3 *( myid +1);

		//unsigned long state = (unsigned long)seed;
//        srandom((unsigned int) seed);
        for (i = myid; i < num_samples; i+=nprocs) {
        	x = generate(&locSeed);
				// (double) random() / (double) (RAND_MAX);
            y = generate(&locSeed);
				// (double) random() / (double) (RAND_MAX);
            if ((x*x + y*y) <= 1.0)
           		++count;
        }
        
        sum = 4.0 * (double) count / (double) num_samples;
		//printf("Value of sum: %12.10f\n", pi);	
		MPI_Reduce(&sum, &pi, 1, MPI_DOUBLE, MPI_SUM, 0, MPI_COMM_WORLD);

        /* record end time and print results */
		end_time = MPI_Wtime();

		if(myid==0)
		{
			printf("MPI Program Results: \n");
			printf("estimated pi = %12.10f\n", pi);
    		printf("difference between estimated pi and math.h M_PI = %12.10f\n",fabs(pi - M_PI));
		    printf("time to compute = %g seconds\n", end_time - start_time);
		}
		
		MPI_Finalize();

	    return EXIT_SUCCESS;
}

