#include <stdio.h>
#include "asm.h"

void print_array(int *array, int length)
{
	for (int i = 0; i < length; i++)
	{
		printf("%d ", array[i]);
	}
	printf("\n");
}
int main()
{
	int length = 10;
	int array[length];
	int read = 0;
	int write = 0;

	for (int value = 1; value < 30; value++)
	{
		print_array(array, length);
		enqueue_value(array, length, &read, &write, value);
	}

	return 0;
}