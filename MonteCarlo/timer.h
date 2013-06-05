#include <sys/time.h>

/* function to get time in seconds since "epoch" */
/* uses Linux library function gettimeofday, so may not be portable */
double get_time(void);      /* declaration to make OpenMP compiler happy */
double get_time() {
    struct timeval tv;
    struct timezone tz;
    if (gettimeofday(&tv, &tz) == 0) 
        return (double) tv.tv_sec + ((double) tv.tv_usec / (double) 1000000);
    else
        return 0.0;
}

