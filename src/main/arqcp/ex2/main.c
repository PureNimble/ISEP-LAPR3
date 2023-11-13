#include <stdio.h>
#include "asm.h"

void print_array(int *array, int length, int read, int write) {
	for (int i = 0; i < length; i++) {
		printf("%d ", array[i]);
	}

	printf("\nRead Index Atual: %i Write Index Atual: %i\n\n", read, write);
}

int main() {
	int length = 15;
	int array[length];
	int read = 0;
	int write = 0;

	printf("Array Vazio:\n");
	print_array(array, length, read, write);
	for (int value = 1; value < 123; value++) {
		enqueue_value(array, length, &read, &write, value);
		print_array(array, length, read, write);
	}

	return 0;
}