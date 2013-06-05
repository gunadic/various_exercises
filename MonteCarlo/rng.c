#include<stdio.h>
#include<stdlib.h>
#define m 2147483647
#define a 16807
#define c 0

float curSt;
float nextSt;

float generate(unsigned int *input){
	*input = ((a*(*input))+c)%m;
	return (float) (*input)/m;
}



int main(){
	unsigned int state = 9973;
	int i;
	float j;
	for (i = 0; i<100000000; i++){
		//j=generate();
		printf("Generated number %f\n", generate(&state));
		
	}

}
