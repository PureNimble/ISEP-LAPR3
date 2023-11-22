#include <stdio.h>
#include "asm.h"

void printVec(int *vec, int size){
	for (int i = 0; i < size; i++)
		printf("%d ", vec[i]);
	printf("\n");
}

int main() {
  int vec[] = {1, 2, 1};
  int num = 3;
  printf("Vec: ");
  printVec(vec, num);
  int output = mediana(vec, num);
  printf("Mediana: %d\n", output);
  int vec1[] = {1, 1, 2, 1};
  int num1 = 4;
  printf("\nVec1: ");
  printVec(vec1, num1);
  int output1 = mediana(vec1, num1);
  printf("Mediana: %d\n", output1);
  return 0;
}