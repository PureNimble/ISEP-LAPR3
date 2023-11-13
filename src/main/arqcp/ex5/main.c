#include <stdio.h>
#include "asm.h"

int main() {
  int vec[] = {1, 2, 1};
  int num = 3;
  int output = mediana(vec, num);
  printf("%d\n", output);
  int vec1[] = {1, 1, 2, 1};
  int num1 = 4;
  int output1 = mediana(vec1, num1);
  printf("%d\n", output1);
  return 0;
}