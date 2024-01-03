#ifndef SAIDADEDADOS_H
#define SAIDADEDADOS_H
#include <stdbool.h>
#include <sys/types.h>

// Estrutura dos sensores
typedef struct
{
    char *sensor_type;              // 8 bytes
    char *unit;                     // 8 bytes
    int* array_median;              // 4 bytes
    int  array_read;                // 4 bytes
    int  array_write;               // 4 bytes
    int  array_size;                // 4 bytes
    unsigned short id;              // 2 bytes
    unsigned short write_counter;   // 2 bytes

} FinalSensor;

void saidaDeDados(char *directoryPath, char *outputPath, long frequency);
void createFinalSensor(FinalSensor *ptrSensores, char *fileName, char *directoryPath);
void findFile(char *path, bool *isFirstFile, int *lastFileId, FinalSensor *ptrSensores);
void createStruct(FinalSensor *ptrSensores, char *fileName, char *directoryPath);
int findSensors(int id, FinalSensor *sensors, int count);



#endif