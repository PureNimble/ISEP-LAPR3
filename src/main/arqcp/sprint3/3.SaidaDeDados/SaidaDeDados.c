#include "../Headers/saidaDeDados.h"
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
int numberOfLine = 0;
void saidaDeDados(char *directoryPath, char *outputPath, long frequency)
{
    FinalSensor *ptrSensores = (FinalSensor *)malloc(1 * sizeof(FinalSensor));
    directoryPath = insert_at_start(directoryPath, "../");
    if (ptrSensores == NULL)
    {
        printf("Erro ao criar a array dinâmico de estruturas\n");
        exit(0);
    }
    int lastFile = 0;

    while (1)
    {
        findFile(directoryPath, &lastFile, ptrSensores);
        // createOutputFile(outputPath, ptrSensores);
        usleep(frequency * 1000);
    }

    doOutput(ptrSensores, directoryPath, outputPath, frequency);
}

int compare(const void *a, const void *b)
{
    return strcmp(*(const char **)a, *(const char **)b);
}

char *insert_at_end(char *original, char *to_insert)
{
    // Allocate memory for the new string
    char *new_string = malloc(strlen(original) + strlen(to_insert) + 1);
    if (new_string == NULL)
    {
        fprintf(stderr, "Failed to allocate memory\n");
        killProcess_saida(fatherPid_saida, SIGUSR1);

        exit(1);
    }

    // Copy the strings into the new string
    strcpy(new_string, original);
    strcat(new_string, to_insert);

    return new_string;
}

void *doOutput(Sensors *ptrSensores, char *directoryPath, char *outputPath, long frequency)
{
    while (1)
    {
        DIR *dir;
        struct dirent *entry;
        struct stat file_stat;
        dir = opendir(directoryPath);
        char **files = NULL;
        int num_files = 0;
        while ((entry = readdir(dir)) != NULL)
        {
            if (entry->d_type == DT_REG)
            {
                // Regular file
                num_files++;
                files = (char **)realloc(files, num_files * sizeof(char *));
                files[num_files - 1] = strdup(entry->d_name);
            }
        }
        closedir(dir);

        qsort(files, num_files, sizeof(char *), compare);

        directoryPath = insert_at_end(directoryPath, latest_file_name);
        directoryPath = insert_at_end(directoryPath, ".txt");
        int const NUM_SENSORS = numberOfLines_saida(directoryPath);
        createSensor(ptrSensores, directoryPath);

        serializeAllSensors(ptrSensores, outputPath, NUM_SENSORS);

        freeSensor(ptrSensores, NUM_SENSORS);

        usleep(frequency / 1000);
    }
    return NULL;
}

char *insert_at_start_saida(char *original, char *to_insert)
{
    // Allocate memory for the new string
    char *new_string = malloc(strlen(original) + strlen(to_insert) + 1);
    if (new_string == NULL)
    {
        fprintf(stderr, "Failed to allocate memory\n");
        killProcess_saida(fatherPid_saida, SIGUSR1);

        exit(1);
    }

    // Copy the strings into the new string
    strcpy(new_string, to_insert);
    strcat(new_string, original);

    return new_string;
}

void createFinalSensor(FinalSensor *ptrSensores, char *fileName, char *directoryPath)
{
    fileName = insert_at_start(fileName, "/");
    char *path = (char *)malloc(100 * sizeof(char));
    strcpy(path, directoryPath);
    strcat(path, fileName);
    printf("%s\n", path);
    if (numberOfLine == 0)
    {
        numberOfLine = numberOfLines(path);
        FinalSensor *temp = (FinalSensor *)realloc(ptrSensores, numberOfLine * sizeof(FinalSensor));
        if (temp == NULL)
        {
            printf("Erro ao criar a array dinâmico de estruturas\n");
            free(ptrSensores);
            exit(0);
        }
        ptrSensores = temp;
    }
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
    float median;
    FinalSensor s;
    while (fscanf(fp, "%d,%d,%[^,],%[^,],%e#\n", &id, &write_counter, type, unit, &median) == 5)
    {
        s.sensor_type = (char *)calloc(strlen(type) + 1, sizeof(char));
        s.unit = malloc(strlen(unit) + 1);
        s.id = id;
        s.write_counter = write_counter;
        s.median = median;
        strcpy(s.sensor_type, type);
        strcpy(s.unit, unit);

        *ptrSensores = s;
        ptrSensores++;
    }
    fclose(fp);
    free(path);
    printf("Ficheiro lido com sucesso\n");
}

void findFile(char *path, int *lastFile, FinalSensor *ptrSensores)
{
    DIR *d;
    struct dirent *dir;
    d = opendir(path);
    char *file;
    int temp, lastFileNumber = *lastFile;
    char number[15];
    if (d)
    {
        while ((dir = readdir(d)) != NULL)
        {
            file = dir->d_name;
            if (file[14] == '_')
            {
                printf("%s\n", file);
                if (lastFileNumber == 0)
                    createFinalSensor(ptrSensores, file, path);
                else
                {
                    strncpy(number, file, 14);
                    number[14] = '\0';
                    int temp = atoi(number);
                    if (temp > lastFileNumber)
                    {
                        lastFileNumber = temp;
                        createFinalSensor(ptrSensores, file, path);
                    }
                }
            }
        }
        if (lastFileNumber == 0)
            *lastFile = temp;
        closedir(d);
    }
    else
    {
        printf("Cannot open directory %s\n", path);
        killProcess();
    }
}
