#include "../Headers/saidaDeDados.h"
#include "../Headers/processadorDeDados.h"
#include "../Headers/asm.h"
// C library headers
#include <stdio.h>
#include <string.h>
#include <signal.h>

#include <string.h>

// Linux headers
#include <fcntl.h>   // Contains file controls like O_RDWR
#include <errno.h>   // Error integer and strerror() function
#include <termios.h> // Contains POSIX terminal control definitions
#include <unistd.h>  // write(), read(), close()
#include <sys/stat.h>
#include <dirent.h>

// malloc
#include <stdlib.h>
// time
#include <time.h>
// Linux headers
#include <fcntl.h>   // Contains file controls like O_RDWR
#include <errno.h>   // Error integer and strerror() function
#include <termios.h> // Contains POSIX terminal control definitions
#include <unistd.h>  // write(), read(), close()
#include <sys/stat.h>
#include <pthread.h>

// malloc
#include <stdlib.h>
// time
#include <time.h>
int numberOfL;
void saidaDeDados(char *directoryPath, char *outputPath, long frequency)
{
    FinalSensor *ptrSensores = (FinalSensor *)malloc(1 * sizeof(FinalSensor));
    if (ptrSensores == NULL)
    {
        printf("Erro ao criar a array dinâmico de estruturas\n");
        killProcess();
        exit(0);
    }
    bool isfirstFile = true;
    int lastFileID;

    directoryPath = insert_at_start(directoryPath, "../");

    while (1)
    {
        findFile(directoryPath, &isfirstFile, &lastFileID, ptrSensores);
        printSensor(ptrSensores, numberOfL);
        usleep(frequency * 1000);
        // createOutputFile(outputPath, ptrSensores);
    }
}

void createFinalSensor(FinalSensor *ptrSensores, char *fileName, char *directoryPath)
{
    fileName = insert_at_start(fileName, "/");
    char *path = (char *)malloc(100 * sizeof(char));
    strcpy(path, directoryPath);
    strcat(path, fileName);
    FILE *fp = fopen(path, "r");
    if (fp == NULL)
    {
        printf("Erro ao abrir o ficheiro\n");
        exit(0);
    }
    unsigned int id;
    unsigned int write_counter;
    char type[30];
    char unit[20];
    int median;
    FinalSensor s;
    while (fscanf(fp, "%d,%d,%[^,],%[^,],%d#\n", &id, &write_counter, type, unit, &median) == 5)
    {
        int currentSensor = findSensor(ptrSensores, id, numberOfL);
        if (currentSensor == -1)
            continue;

        enqueue_value(ptrSensores[currentSensor].array_median, ptrSensores[currentSensor].array_size, &ptrSensores[currentSensor].array_read, &ptrSensores[currentSensor].array_write, median);
    }
    fclose(fp);
    free(path);
    printf("Ficheiro lido com sucesso\n");
}

void findFile(char *path, bool *isFirstFile, int *lastFileId, FinalSensor *ptrSensores)
{
    DIR *d;
    struct dirent *dir;
    d = opendir(path);

    char *file;
    int temp, lastFileNumber = *lastFileId;
    char number[15];
    if (d)
    {
        while ((dir = readdir(d)) != NULL)
        {
            file = dir->d_name;
            if (file[14] == '_')
            {
                printf("%s\n", file);
                strncpy(number, file, 14);
                number[14] = '\0';
                temp = atoi(number);
                if (*isFirstFile)
                {
                    createStruct(ptrSensores, file, path);
                    *isFirstFile = false;
                }
                else if (temp > lastFileNumber)
                    createFinalSensor(ptrSensores, file, path);

                lastFileNumber = temp;
            }
        }
    }
    closedir(d);
    *lastFileId = lastFileNumber;
}
void createStruct(FinalSensor *ptrSensores, char *fileName, char *directoryPath)
{
    fileName = insert_at_start(fileName, "/");
    char *path = (char *)malloc(100 * sizeof(char));
    strcpy(path, directoryPath);
    strcat(path, fileName);
    FILE *fp = fopen(path, "r");
    if (fp == NULL)
    {
        printf("Erro ao abrir o ficheiro\n");
        killProcess();
        exit(0);
    }
    numberOfL = numberOfLines(path);
    FinalSensor *temp = (FinalSensor *)realloc(ptrSensores, numberOfL * sizeof(FinalSensor));
    if (temp == NULL)
    {
        printf("Erro ao criar a array dinâmico de estruturas\n");
        free(ptrSensores);
        killProcess();
        exit(0);
    }
    ptrSensores = temp;
    unsigned int id;
    unsigned int write_counter;
    char type[30];
    char unit[20];
    int median;
    FinalSensor s;
    while (fscanf(fp, "%d,%d,%[^,],%[^,],%d#\n", &id, &write_counter, type, unit, &median) == 5)
    {
        s.sensor_type = (char *)calloc(strlen(type) + 1, sizeof(char));
        s.unit = malloc(strlen(unit) + 1);
        s.id = id;
        s.write_counter = write_counter;
        s.array_median = (int *)malloc(10 * sizeof(int));
        s.array_read = 0;
        s.array_write = 0;
        s.array_size = 10;
        strcpy(s.sensor_type, type);
        strcpy(s.unit, unit);
        enqueue_value(s.array_median, s.array_size, &s.array_read, &s.array_write, median);

        *ptrSensores = s;
        ptrSensores++;
    }
    fclose(fp);
    free(path);
    printf("Ficheiro criado com sucesso\n");
}
int findSensors(int id, FinalSensor *sensors, int count)
{
    unsigned short i;
    for (i = 0; i < count; i++)
    {
        if (id == sensors[i].id)
            return i;
    }
    printf("Não existe configuração para o sensor ID:%d\n", id);
    return -1;
}

void printSensor(FinalSensor *ptr, int numberOfSensors)
{
    int i;
    for (i = 0; i < numberOfSensors; i++)
    {
        printf("ID: %d\n", ptr->id);
        printf("Write Counter: %d\n", ptr->write_counter);
        printf("Sensor Type: %s\n", ptr->sensor_type);
        printf("Unit: %s\n", ptr->unit);
        printf("Median: %d\n", ptr->array_median[ptr->array_read]);
        printf("Array Size: %d\n", ptr->array_size);
        printf("Array Read: %d\n", ptr->array_read);
        printf("Array Write: %d\n", ptr->array_write);
        printf("\n");
        ptr++;
    }
}