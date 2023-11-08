#include <stdio.h>
#include "asm.h"

int main() {
  int num = 4, read = 0, write = 6;
  int array[] = {1, 2, 3, 4, 5, 6, 7, 8}, vec[num];
  if (copy_num_vec(array, read, write, num, vec)) {
    for (int i = 0; i < num; i++) {
      printf("vec[%i]: %i\n", i, vec[i]);
    }
  } else printf("The number of elements to copy is invalid\n");
  return 0;
}