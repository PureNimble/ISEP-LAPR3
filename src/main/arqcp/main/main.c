#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include "../ex1/asm.h"
#include "../ex2/asm.h"
#include "../ex3/asm.h"
#include "../ex4/asm.h"
#include "../ex5/asm.h"

int main() {
    int i, length = 18, num = 13, read = 0, write = 0;
    int array[length], vec[num];
    char baseInput[] = "sensor_id:8#type:atmospheric_temperature#value:21.60#unit:celsius#time:", token[] = "time";
    time_t t;
    srand((unsigned)time(&t));

    printf("A Executar Ex1 e Ex2:\n");
    sleep(2);
    for (i = 0; i < 50; i++) {
        int output = 0;
        char input[100];
        sprintf(input, "%s%d", baseInput, (rand() % 99999) + 10000);
        printf("\nA processar: %s\n", input);
        extract_token(input, token, &output);
        printf("A armazenar %i num Buffer Circular...\n", output);
        enqueue_value(array, length, &read, &write, output);
        printf("Valor armazenado no array[%i], Read Index: %i, Write Index: %i\n", write, read, write);
	}

    printf("\nArray final:\n");
    for (i = 0; i < length; i++) {
        printf("Array[%i]: %i\n", i, array[i]);
	}
    printf("Read Index Atual: %i\nWrite Index Atual: %i\n", read, write);

    printf("\nA Executar Ex3:\n");
    sleep(2);
    if (move_num_vec(array, length, &read, &write, num, vec)) {
        printf("%i elementos foram copiados com sucesso para vec:\n", num);
        for (i = 0; i < num; i++) {
            printf("Vec[%i]: %i\n", i, vec[i]);
        }
    } else printf("O número de elementos a copiar é inválido\n");

    printf("\nA Executar Ex4:\n");
    sleep(2);
    sort_array(vec, num);
    printf("Vec Ordenado:\n");
    for (i = 0; i < num; i++) {
        printf("Vec[%i]: %i\n", i, vec[i]);
    }

    printf("\nA Executar Ex5:\n");
    sleep(2);
    int median = mediana(vec, num);
    printf("Mediana: %i\n", median);

    return 0;
}