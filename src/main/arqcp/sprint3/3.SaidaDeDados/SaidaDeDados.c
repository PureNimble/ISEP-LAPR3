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
    outputPath = insert_at_start(outputPath, "../");
    directoryPath = insert_at_start(directoryPath, "../");
    FinalSensor *temp;

    while (1)
    {
        temp = findFile(directoryPath, &isfirstFile, &lastFileID, ptrSensores);
        if (temp != NULL)
            ptrSensores = temp;
        saidaDeDadosOutput(ptrSensores, outputPath);
        usleep(frequency * 1000);
    }
}

void createFinalSensor(FinalSensor *ptrSensores, char *fileName, char *directoryPath)
{
    fileName = insert_at_start(fileName, "/");
    char *path = (char *)malloc(100 * sizeof(char));
    if (path == NULL)
    {
        printf("Erro ao alocar memória\n");
        killProcess();
    }
    strcpy(path, directoryPath);
    strcat(path, fileName);
    FILE *fp = fopen(path, "r");
    if (fp == NULL)
    {
        printf("Erro ao abrir o ficheiro\n");
        exit(0);
    }
    unsigned short id;
    unsigned short write_counter;
    char type[30];
    char unit[20];
    char median[20];
    int median_value;
    while (fscanf(fp, "%hd,%hd,%[^,],%[^,],%s#\n", &id, &write_counter, type, unit, median) == 5)
    {
        if (!strcmp(median, "error"))
            median_value = 0;
        else
            median_value = atoi(median);
        int currentSensor = findSensors(id, ptrSensores);
        if (currentSensor == -1)
            continue;
        ptrSensores[currentSensor].median = median_value;
        ptrSensores[currentSensor].write_counter = write_counter;
    }
    fclose(fp);
    free(path);
    printf("Ficheiro lido com sucesso\n");
}

FinalSensor *findFile(char *path, bool *isFirstFile, int *lastFileId, FinalSensor *ptrSensores)
{
    DIR *d;
    struct dirent *dir;
    d = opendir(path);
    FinalSensor *ptr = NULL;

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
                    ptr = createStruct(ptrSensores, file, path);
                    *isFirstFile = false;
                }
                else if (temp > lastFileNumber)
                    createFinalSensor(ptr, file, path);

                lastFileNumber = temp;
            }
        }
    }
    closedir(d);
    *lastFileId = lastFileNumber;
    return ptr;
}
FinalSensor *createStruct(FinalSensor *ptrSensores, char *fileName, char *directoryPath)
{
    fileName = insert_at_start(fileName, "/");
    char *path = (char *)malloc(100 * sizeof(char));
    if (path == NULL)
    {
        printf("Erro ao alocar memória\n");
        killProcess();
    }
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
    FinalSensor *ptr = ptrSensores;
    unsigned short id;
    unsigned short write_counter;
    char type[30];
    char unit[20];
    char median[20];
    int median_value;
    FinalSensor s;
    while (fscanf(fp, "%hd,%hd,%[^,],%[^,],%s#\n", &id, &write_counter, type, unit, median) == 5)
    {
        if (!strcmp(median, "error"))
            median_value = 0;
        else
            median_value = atoi(median);

        s.sensor_type = (char *)calloc(strlen(type) + 1, sizeof(char));
        s.unit = malloc(strlen(unit) + 1);
        s.id = id;
        s.write_counter = write_counter;
        s.median = median_value;
        strcpy(s.sensor_type, type);
        strcpy(s.unit, unit);

        *ptrSensores = s;
        ptrSensores++;
    }

    fclose(fp);
    free(path);
    printf("Ficheiro importado com sucesso\n");
    return ptr;
}

int findSensors(int id, FinalSensor *sensors)
{
    unsigned short i;
    for (i = 0; i < numberOfL; i++)
    {
        if (id == sensors[i].id)
            return i;
    }
    printf("Não existe configuração para o sensor ID:%d\n", id);
    return -1;
}

void saidaDeDadosOutput(FinalSensor *ptr, char *outputPath)
{
    if (ptr->id == 0)
        return;
    if (!doesDirectoryExist(outputPath))
        mkdir(outputPath, 0777);
    time_t now;
    struct tm *local;
    char buffer[80];
    time(&now);
    local = localtime(&now);
    strftime(buffer, 100, "/%Y%m%d%H%M%S_FarmCoordinator.txt", local);
    int i;

    char *path = (char *)calloc(strlen(outputPath) + 25, sizeof(char));
    if (path == NULL)
    {
        printf("Erro ao alocar memória\n");
        killProcess();
    }
    strcpy(path, outputPath);
    strcat(path, buffer);
    FILE *file = fopen(path, "a+");
    if (file == NULL)
    {
        printf("Caminho invalido: %s\n", path);
        killProcess();
    }
    for (i = 0; i < numberOfL; i++)
    {
        float median = (float)ptr->median;
        if (median == 0)
            fprintf(file, "%d,%d,%s,%s,%s\n", ptr->id, ptr->write_counter, ptr->sensor_type, ptr->unit, "no data");
        else
            fprintf(file, "%d,%d,%s,%s,%.2f\n", ptr->id, ptr->write_counter, ptr->sensor_type, ptr->unit, median);

        ptr++;
    }
    fclose(file);
    free(path);
}