#include <stdio.h>
#include "asm.h"

int main() {
  char input[] = "sensor_id:8#type:atmospheric_temperature#value:21.60#unit:celsius#time:2470030";
  char token[] = "time:";
  int output = 0;
  extract_token(input, token, &output);
  printf("%d\n", output);
  return 0;
}