/*
 *  * Mutual exclusion problem.
 *   *
 *    * Program requires the following command-line arguments:
 *     *   number of threads
 *      *   number of trips through the critical region / non-critical region
 *       *     loop each thread should make
 *        *   maximum time to delay in critical region (milliseconds)
 *         *   maximum time to delay in non-critical region (milliseconds)
 *          *
 *           * No synchronization, so should not (always) work.
 *            */
#define _XOPEN_SOURCE 500 /* may be needed to get usleep() in unistd.h */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>     /* has usleep() */
#include <pthread.h>    /* has pthread_ routines */
#include <semaphore.h>


void * thread_fcn(void * thread_arg);
void enter_cr(int threadID);
void leave_cr(int threadID);
void init_synch();
void finish_synch();
void show_usage_and_exit(char* arg0);

/* global variables for runtime parameters */
int num_threads = 0;
int num_trips = 0;
int max_cr_delay = 0;
int max_non_cr_delay = 0;

/* other global variables */
volatile int cr_delay_time = 0;      /* total time in cr */
volatile int non_cr_delay_time = 0;  /* total time in non-cr */

/* global variable(s) for synchronization */
/* TODO:  add variables here as needed */

pthread_mutex_t mtx = PTHREAD_MUTEX_INITIALIZER;

/* almost certainly they should be declared volatile */

/* ---- main program ---- */

int main(int argc, char* argv[]) {

    int i;
    int * threadIDs;
    pthread_t * threads;

    if (argc < 5) {
        show_usage_and_exit(argv[0]);
    }
    num_threads = atoi(argv[1]);
    num_trips = atoi(argv[2]);
    max_cr_delay = atoi(argv[3]);
    max_non_cr_delay = atoi(argv[4]);
    if ((num_threads <= 0) || (num_trips <= 0) ||
            (max_cr_delay <= 0) || (max_non_cr_delay <= 0)) {
        show_usage_and_exit(argv[0]);
    }

    /* Initialize for synchronization. */
    init_synch();

    /* Set up IDs for threads (need a separate variable for each since they're
 *      *   shared among threads).
 *           */
    threadIDs = malloc(num_threads * sizeof(int));
    for (i = 0; i < num_threads; ++i)
        threadIDs[i] = i;

    /* Start num_threads new threads, each with different ID. */
    threads = malloc(num_threads * sizeof(pthread_t));
    for (i = 0; i < num_threads; ++i)
        pthread_create(&threads[i], NULL, thread_fcn, (void *) &threadIDs[i]);

    /* Wait for all threads to complete. */
    for (i = 0; i < num_threads; ++i)
        pthread_join(threads[i], NULL);

    /* Print some things. */
    fprintf(stdout, "Total delay time = %d (cr), %d (non-cr)\n",
            cr_delay_time, non_cr_delay_time);

    /* Clean up and exit. */
    finish_synch();
    free(threadIDs);
    free(threads);
    return EXIT_SUCCESS;
}

/* ---- code to be executed by each thread ---- */

void * thread_fcn(void * thread_arg) {
    int myID = * (int *) thread_arg;
    int i;
    int delay;

    for (i = 0; i < num_trips; ++i) {

        enter_cr(myID);

        /*   critical region:
 *            *   compute a random delay time cr_delay_time, then print
 *                     *     it and add it to the running total
 *                              *   sleep cr_delay_time milliseconds
 *                                       *   compute a random delay time non_cr_delay_time, then print
 *                                                *     it and add it to the running total
 *                                                         */
        delay = (rand() % max_cr_delay) + 1;
        cr_delay_time += delay;
        fprintf(stdout, "thread %d starts cr, cr delay %d\n", myID, delay);
        usleep(delay * 1000);
        delay = (rand() % max_non_cr_delay) + 1;
        non_cr_delay_time += delay;
        fprintf(stdout, "thread %d ends cr, non-cr delay %d\n", myID, delay);

        leave_cr(myID);

        /* non-critical region:
 *          * sleep non_cr_delay_time milliseconds
 *                   */
        usleep(delay * 1000);
    }
    pthread_exit((void* ) NULL);
}

/* ---- code for synchronization ---- */
/* TODO:  put code here as needed */

void init_synch() {
}

void finish_synch() {
}

void enter_cr(int threadID) {
    pthread_mutex_lock(&mtx);
}

void leave_cr(int threadID) {
    pthread_mutex_unlock(&mtx);
}

/* ---- other functions ---- */

void show_usage_and_exit(char* arg0) {
    fprintf(stderr,
            "Usage:  %s num_threads num_trips max_cr_delay max_non_cr_delay\n",
            arg0);
    fprintf(stderr, "  (delays in milliseconds)\n");
    exit(EXIT_FAILURE);
}


