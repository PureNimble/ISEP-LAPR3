#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "../ex2/asm.h"
#include "../ex3/asm.h"
#include "../ex4/asm.h"

int main() {
    int i, length = 8, num = 7, read = 0, write = 0;
    int array[] = {1, 2, 3, 4, 5, 6, 7, 8}, vec[num];
    time_t t;
    srand((unsigned)time(&t));

    printf("\nExecuting Ex1:\n");
    sleep(2);
    for (i = 0; i < 30; i++) {
		enqueue_value(array, length, &read, &write, rand() % 10000);
	}
    printf("Final array:\n");
    for (i = 0; i < length; i++) {
        printf("Array[%i]: %i\n", i, array[i]);
	}
    printf("Current Read Index: %i\nCurrent Write Index: %i\n", read, write);

    printf("\nExecuting Ex2:\n");
    sleep(2);
    if (copy_num_vec(array, length, &read, &write, num, vec)) {
        printf("%i elements were successfully copied to vec:\n", num);
        for (i = 0; i < num; i++) {
            printf("Vec[%i]: %i\n", i, vec[i]);
        }
    } else printf("The number of elements to copy is invalid\n");

    printf("\nExecuting Ex3:\n");
    sleep(2);
    sort_array(vec, num);
    printf("Sorted Vec:\n");
    for (i = 0; i < num; i++) {
        printf("Vec[%i]: %i\n", i, vec[i]);
    }

    return 0;
}