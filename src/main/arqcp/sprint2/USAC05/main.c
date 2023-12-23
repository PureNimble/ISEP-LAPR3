#include <stdio.h>
#include "asm.h"

void printVec(int *vec, int size){
	for (int i = 0; i < size; i++)
		printf("%d ", vec[i]);
	printf("\n");
}

int main() {
  int vec[] = {1, 2, 5, 3, 1};
  int num = 5;
  int output = mediana(vec, num);
  printf("Vec: ");
  printVec(vec, num);
  printf("Mediana: %d\n", output);
  int vec1[] = {1, 3, 2, 1};
  int num1 = 4;
  int output1 = mediana(vec1, num1);
  printf("\nVec1: ");
  printVec(vec1, num1);
  printf("Mediana: %d\n", output1);
  return 0;
}