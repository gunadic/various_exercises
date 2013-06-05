#
# sample Makefile for MPI (OpenMPI implementation)
#
# B. Massingill, 02/05/2008
#
# usage examples:
#   "make hello" to compile "hello.c" to "hello" 
#   "make hello++" to compile "hello++.cpp" to "hello++"
#

MPICC       = /usr/bin/mpicc
CFLAGS      = -O -Wall -pedantic -std=c99

MPICXX      = /usr/bin/mpiCC
CCFLAGS     = -O -Wall -std=c++98

%: %.c
	$(MPICC) -o $@ $(CFLAGS) $<

%: %.cpp
	$(MPICXX) -o $@ $(CCFLAGS) $< 

