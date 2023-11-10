#include <stdio.h>
#include "asm.h"

int main() {
  int length = 8, num = 7, read = 4, write = 3;
  int array[] = {1, 2, 3, 4, 5, 6, 7, 8}, vec[num];
  if (copy_num_vec(array, length, &read, &write, num, vec)) {
    printf("%i elements were successfully copied to vec:\n", num);
    for (int i = 0; i < num; i++) {
      printf("vec[%i]: %i\n", i, vec[i]);
    }
  } else printf("The number of elements to copy is invalid\n");
  return 0;
}