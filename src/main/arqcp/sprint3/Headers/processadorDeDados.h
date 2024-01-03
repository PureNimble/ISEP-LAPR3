#ifndef PROCESSADORDEDADOS_H
#define PROCESSADORDEDADOS_H
#include <stdbool.h>
#include <sys/types.h>
#include <sys/stat.h>
// Estrutura dos sensores
typedef struct
{
    int *buffer_circular;                         // 8 bytes
    int *median_array;                            // 8 bytes
    char *sensor_type;                            // 8 bytes
    char *unit;                                   // 8 bytes
    int instate_temporal_ultima_leitura;          // 4 bytes
    int timeout;                                  // 4 bytes
    unsigned int window_len;                      // 4 bytes
    unsigned int buffer_size;                     // 4 bytes
    int buffer_read;                              // 4 bytes
    int buffer_write;                             // 4 bytes
    unsigned short write_counter;                 // 2 bytes
    unsigned short id;                            // 2 bytes
    unsigned short medianIndex;                   // 2 bytes
    bool isError;                                 // 1 byte
} Sensor;

// main function
void processadorDeDados(char *valuePath, char *configPath, char *outputPath, int numberOfReads, pid_t fatherPid);
// get the number of lines in a file (configFile)
int numberOfLines(char *path);
// create sensors
void createSensors(Sensor *sensors, char *configPath);
// get data from serial port
char *getData(int serial_port);
// extract info from data
int *extractInfo(char *data);
// isto so esta aqui porque é o menino rafael que tem o sensor :(
char *test();
// insert info into sensors
void insertInfo(int *info, Sensor *sensors, int count);
// find sensor
int findSensor(int id, Sensor *sensors, int count);
// calculate median
float median(int *array, int size);
// print all sensors
void printAllSensors(Sensor *sensors, int count);
// free sensors
void freeSensors(Sensor *sensors, int count);

void printString(char *string);

void moving_median(Sensor *sensors, int numberOfReads);

int doesDirectoryExist(const char *path);
void removeFilenameFromPath(char *path);

void serialize(Sensor *sensor, char *directoryPath, char **output);

void createOutputFile(char *directoryPath, char **output, int numberOfSensors);

char *insert_at_start(char *original, char *to_insert);
void killProcess();

void createConfigFile(char *configPath);

#endif