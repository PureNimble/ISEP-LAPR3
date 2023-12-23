#ifndef PROCESSADORDEDADOS_H
#define PROCESSADORDEDADOS_H

// Estrutura dos sensores
typedef struct
{
    unsigned short id;                      // id do sensor ( serve para identificar o sensor)
    char *sensor_type;                      // id do tipo de sensor (serve para identificar o tipo de sensor)
    char *unit;                             // valor da unidade
    float *buffer_circular;                 // buffer circular
    float *median_array;                    // array de medianas
    int buffer_size;                        // tamanho do buffer circular
    double instate_temporal_ultima_leitura; // instante temporal da ultima leitura
    int timeout;                            // tempo de timeout
    int median_size;                        // tamanho do array de medianas
    int write_counter;                      // contador de escrita
} Sensor;

void processadorDeDados(char *valuesPath, char *configPath, char *directoryPath, int numberOfSensors);
#endif