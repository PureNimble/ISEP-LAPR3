#include <stdio.h>
#include "asm.h"

void printVec(int *vec, int size){
	for (int i = 0; i < size; i++)
		printf("%d ", vec[i]);
	printf("\n");
}

int main()
{
	int size = 8;
	int vec[] = {
		19,
		38,
		13,
		67,
		57,
		43,
		45,
		22,
	};
	printf("Vec Inicial: ");
	printVec(vec, size);
	sort_array(vec, size);
	printf("Vec Ordenado: ");
	printVec(vec, size);

	return 0;
}