#ifndef SAIDADEDADOS_H
#define SAIDADEDADOS_H
#include <stdbool.h>
// Estrutura dos sensores
typedef struct
{
    int *buffer_circular;                         // 8 bytes
    int *median_array;                            // 8 bytes
    char *sensor_type;                            // 8 bytes
    char *unit;                                   // 8 bytes
    unsigned int instate_temporal_ultima_leitura; // 4 bytes
    unsigned int timeout;                         // 4 bytes
    unsigned int window_len;                      // 4 bytes
    unsigned int buffer_size;                     // 4 bytes
    int buffer_read;                              // 4 bytes
    int buffer_write;                             // 4 bytes
    unsigned short write_counter;                 // 2 bytes
    unsigned short id;                            // 2 bytes
    unsigned short medianIndex;                   // 2 bytes
    bool isError;                                 // 1 byte
} Sensors;

void saidaDeDados(char *directoryPath, char *outputPath, long frequency);
void createSensor(Sensors *sensor, char *path);
void findFile(char *path);
char *insert_at_start(char *original, char *to_insert);
int numberOfLines(char *path);
void printAllSensors(Sensors *ptrSensores, char *path, int NUM_SENSORS);
void createSaidaFile(char *directoryPath, char **output, int numberOfSensors);
void serializeSaida(Sensor *sensor, char *directoryPath, char **output);

#endif