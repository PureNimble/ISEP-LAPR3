#ifndef SAIDADEDADOS_H
#define SAIDADEDADOS_H
#include <stdbool.h>
#include <ftw.h>

// Estrutura dos sensores
typedef struct
{
    char *sensor_type;                            // 8 bytes
    char *unit;                                   // 8 bytes
    unsigned short write_counter;                 // 2 bytes
    unsigned short id;                            // 2 bytes
    unsigned int median;                          // 4 bytes
Sensors;

void saidaDeDados(char *directoryPath, char *outputPath, long frequency);
void createSensor(Sensors *sensor, char *path);
void findFile(char *path);
char *insert_at_start(char *original, char *to_insert);
int numberOfLines(char *path);
void serializeAllSensors(Sensors *ptrSensores, char *path, int NUM_SENSORS);
void createSaidaFile(char *directoryPath, char **output, int numberOfSensors);
void serializeSaida(Sensor *sensor, char *directoryPath, char **output);
char *insert_at_end(char *original, char *to_insert);
void* doOutput(Sensors *ptrSensores, char *directoryPath, char *outputPath, long frequency);
int process_file(const char *file_path, const struct stat *info, const int typeflag, struct FTW *pathinfo)

#endif