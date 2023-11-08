#include <stdio.h>
#include "asm.h"

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
	sort_array(vec, size);
	printf("Sorted array: ");
	for (int i = 0; i < size; i++)
		printf("%d ", vec[i]);
	printf("\n");

	return 0;
}