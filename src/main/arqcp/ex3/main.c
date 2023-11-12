#include <stdio.h>
#include "asm.h"

int main() {
  int length = 15, num = 12, read = 5, write = 4;
  int array[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}, vec[num];
  if (move_num_vec(array, length, &read, &write, num, vec)) {
    printf("%i elements were successfully copied to vec:\n", num);
    for (int i = 0; i < num; i++) {
      printf("vec[%i]: %i\n", i, vec[i]);
    }
  } else printf("The number of elements to copy is invalid\n");
  return 0;
}