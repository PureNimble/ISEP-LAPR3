#ifndef MAIN_H
#define MAIN_H
#define CONFIG_PATH "../config/setup.txt"

#include <signal.h>


void getParameters(char *value_path, char *config_path, char *saida_path, int *number_of_readings, char *farm_coordinator, int *frequency);
void reWriteSetup();
void invalidFormat();
char *trim(char *str);
void handleSignal(int signo, siginfo_t *sinfo, void *context);

#endif