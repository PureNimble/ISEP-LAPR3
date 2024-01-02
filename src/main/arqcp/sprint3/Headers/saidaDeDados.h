#ifndef SAIDADEDADOS_H
#define SAIDADEDADOS_H
#include <stdbool.h>
#include <sys/types.h>


// Estrutura dos sensores
typedef struct
{
    char *sensor_type;                            // 8 bytes
    char *unit;                                   // 8 bytes
    unsigned short write_counter;                 // 2 bytes
    unsigned short id;                            // 2 bytes
    unsigned int median;                          // 4 bytes
}Sensors;

void saidaDeDados(char *directoryPath, char *outputPath, long frequency, pid_t pid);
void createSensor(Sensors *sensor, char *path);
void findFile(char *path);
char *insert_at_start_saida(char *original, char *to_insert);
int numberOfLines_saida(char *path);
void serializeAllSensors(Sensors *ptrSensores, char *path, int NUM_SENSORS);
void createSaidaFile(char *directoryPath, char **output, int numberOfSensors);
void serializeSaida(Sensors *sensor, char *directoryPath, char **output);
char *insert_at_end(char *original, char *to_insert);
void* doOutput(Sensors *ptrSensores, char *directoryPath, char *outputPath, long frequency);
int doesDirectoryExist_saida(const char *path);
void freeSensor(Sensors *sensors, int count);
void killProcess_saida();
int compare(const void *a, const void *b);
#endif