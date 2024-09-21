#ifndef ASM_H
#define ASM_H
// extract values from a String
int extract_token(char *input, char *token, int *output);
// add a value to the circular buffer
void enqueue_value(int *array, int length, int *read, int *write, int value);
// move a number of values from the circular buffer to a vector
int move_num_vec(int *array, int length, int *read, int *write, int num, int *vec);
// sort a vector
void sort_array(int *vec, int num);
// calculate the median of a vector
int mediana(int *vec, int num);
#endif