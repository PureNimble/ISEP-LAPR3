#ifndef PROCESSADORDEDADOS_H
#define PROCESSADORDEDADOS_H

// Estrutura dos sensores
typedef struct
{
    float *buffer_circular;                 // 8 bytes
    float *median_array;                    // 8 bytes
    char *sensor_type;                      // 8 bytes
    char *unit;                             // 8 bytes
    float instate_temporal_ultima_leitura;  // 4 bytes
    int timeout;                            // 4 bytes
    int write_counter;                      // 4 bytes
    unsigned short id;                      // 2 bytes
} Sensor;

void processadorDeDados(char *valuesPath, char *configPath, char *directoryPath, int numberOfSensors);
char *getData(int serial_port);
int *extractInfo(char *data);
int createStructs(Sensor *sensors, char* configPath);
void freeSensors(Sensor *sensors, int count);
#endif