#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "../ex1/asm.h"
#include "../ex2/asm.h"
#include "../ex3/asm.h"
#include "../ex4/asm.h"

int main() {
    int i, length = 18, num = 13, read = 0, write = 0;
    int array[length], vec[num];
    char baseInput[] = "sensor_id:8#type:atmospheric_temperature#value:21.60#unit:celsius#time:", token[] = "time";
    time_t t;
    srand((unsigned)time(&t));

    printf("Executing Ex1 and Ex2:\n");
    sleep(2);
    for (i = 0; i < 50; i++) {
        int output = 0;
        char input[100];
        sprintf(input, "%s%d", baseInput, (rand() % 99999) + 10000);
        printf("\nProcessing: %s\n", input);
        extract_token(input, token, &output);
        printf("Storing %i in a Circular Buffer...\n", output);
        enqueue_value(array, length, &read, &write, output);
        printf("Value stored at array[%i], Read Index: %i, Write Index: %i\n", write, read, write);
	}

    printf("\nFinal array:\n");
    for (i = 0; i < length; i++) {
        printf("Array[%i]: %i\n", i, array[i]);
	}
    printf("Current Read Index: %i\nCurrent Write Index: %i\n", read, write);

    printf("\nExecuting Ex3:\n");
    sleep(2);
    if (move_num_vec(array, length, &read, &write, num, vec)) {
        printf("%i elements were successfully copied to vec:\n", num);
        for (i = 0; i < num; i++) {
            printf("Vec[%i]: %i\n", i, vec[i]);
        }
    } else printf("The number of elements to copy is invalid\n");

    printf("\nExecuting Ex4:\n");
    sleep(2);
    sort_array(vec, num);
    printf("Sorted Vec:\n");
    for (i = 0; i < num; i++) {
        printf("Vec[%i]: %i\n", i, vec[i]);
    }

    return 0;
}