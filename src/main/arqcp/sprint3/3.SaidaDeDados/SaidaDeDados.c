#include "../Headers/saidaDeDados.h"
#include "../Headers/processadorDeDados.h"
// C library headers
#include <stdio.h>
#include <string.h>

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

// malloc
#include <stdlib.h>
// time
#include <time.h>
void saidaDeDados(char *directoryPath, char *outputPath, long frequency)
{
    directoryPath = insert_at_start(directoryPath, "../");

    Sensors *ptrSensores = (Sensors *)malloc(1 * sizeof(Sensors));

    if (ptrSensores == NULL)
    {
        printf("Erro ao criar a array dinâmico de estruturas\n");
        exit(0);
    }
    createSensor(ptrSensores, directoryPath);

    // printAllSensors(ptrSensores, NUM_SENSORS);

    // freeSensors(ptrSensores, NUM_SENSORS);
}

char *insert_at_start(char *original, char *to_insert)
{
    // Allocate memory for the new string
    char *new_string = malloc(strlen(original) + strlen(to_insert) + 1);
    if (new_string == NULL)
    {
        fprintf(stderr, "Failed to allocate memory\n");
        killProcess(fatherPid, SIGUSR1);

        exit(1);
    }

    // Copy the strings into the new string
    strcpy(new_string, to_insert);
    strcat(new_string, original);

    return new_string;
}


int numberOfLines(char *path)
{
    FILE *file = fopen(path, "r");
    if (file == NULL)
    {
        perror("Erro ao abrir o ficheiro");
        return 0;
    }
    int lines = 1;
    char c;
    while ((c = fgetc(file)) != EOF)
    {
        if (c == '\n')
        {
            lines++;
        }
    }
    fclose(file);
    return lines;
}

void createSensor(Sensors *ptrSensores, char *path)
{
    findFile(path);
    /* FILE *fp = fopen(directoryPath, "r");
    if (fp == NULL)
    {
        printf("Erro ao abrir o ficheiro\n");
        exit(0);
    }
    char line[100];
    int i = 0;
    while (fgets(line, sizeof(line), fp))
    {
        char *token = strtok(line, ",");
        ptrSensores[i].id = atoi(token);
        token = strtok(NULL, ",");
        ptrSensores[i].sensor_type = token;
        token = strtok(NULL, ",");
        ptrSensores[i].unit = token;
        token = strtok(NULL, ",");
        ptrSensores[i].timeout = atoi(token);
        token = strtok(NULL, ",");
        ptrSensores[i].window_len = atoi(token);
        token = strtok(NULL, ",");
        ptrSensores[i].buffer_size = atoi(token);
        ptrSensores[i].buffer_circular = (int *)malloc(ptrSensores[i].buffer_size * sizeof(int));
        ptrSensores[i].median_array = (int *)malloc(ptrSensores[i].window_len * sizeof(int));
        ptrSensores[i].buffer_read = 0;
        ptrSensores[i].buffer_write = 0;
        ptrSensores[i].write_counter = 0;
        ptrSensores[i].medianIndex = 0;
        ptrSensores[i].isError = false;
        i++;
    }
    fclose(fp); */
}

void printAllSensors(Sensors *ptrSensores, char *path, int NUM_SENSORS){
    findFile(path);

    char *output[NUM_SENSORS];
     for (i = 0; i < NUM_SENSORS; i++)
     {
            serializeSaida(&ptrSensores[i], directoryPath, &output[i]);
     }
     createSaidaFile(directoryPath, output, NUM_SENSORS);
}

void createSaidaFile(char *directoryPath, char **output, int numberOfSensors)
{
    time_t now;
    struct tm *local;
    char buffer[80];
    time(&now);
    local = localtime(&now);
    strftime(buffer, 100, "/%Y%m%d%H%M%S_output.txt", local);
    int i;

    char *path = (char *)calloc(strlen(directoryPath) + 25, sizeof(char));
    if (path == NULL)
    {
        printf("Erro ao alocar memória\n");
        killProcess(fatherPid, SIGUSR1);
    }
    if (!doesDirectoryExist(directoryPath))
        mkdir(directoryPath, 0777);
    strcpy(path, directoryPath);
    strcat(path, buffer);
    FILE *file = fopen(path, "a+");
    if (file == NULL)
    {
        printf("Caminho invalido: %s\n", path);
        killProcess(fatherPid, SIGUSR1);
    }
    for (i = 0; i < numberOfSensors; i++)
    {
        if (i == numberOfSensors - 1)
        {
            char *lastChar = strrchr(*output, '\n');
            if (lastChar != NULL)
                *lastChar = '\0';
        }

        fprintf(file, *output);
        **output++;
    }
    fclose(file);
    free(path);
}

void serializeSaida(Sensor *sensor, char *directoryPath, char **output)
{
    char string[100];
    if (sensor->isError)
        sprintf(string, "ID: %d, Write Counter: %d, Sensor type: %s, Sensor Unit:%s, %s\n", sensor->id, sensor->write_counter, sensor->sensor_type, sensor->unit, "error#");
    else
    {
        int mediana = 0, i;

        for (i = 0; i < sensor->medianIndex; i++)
            mediana += sensor->median_array[i];

        char med[100];
        sprintf(str, "%d", mediana);

        int position = strlen(med) - 1;
        memmove(str + position + 1, str + position, strlen(str) - position);
        str[position] = '.';

        sprintf(string, "ID: %d, Write Counter: %d, Sensor type: %s, Sensor Unit:%s, Mediana: %s#\n", sensor->id, sensor->write_counter, sensor->sensor_type, sensor->unit, med);
    }
    *output = (char *)calloc(strlen(string), sizeof(char));
    if (*output == NULL)
    {
        printf("Erro ao alocar memória\n");
        killProcess(fatherPid, SIGUSR1);
    }
    strcpy(*output, string);
    sensor->isError = false;
}


void findFile(char *path)
{
    DIR *d;
    struct dirent *dir;
    d = opendir(path);
    if (d)
    {
        while ((dir = readdir(d)) != NULL)
        {
            char *file = dir->d_name;
            if (file[0] != '.')
                printf("%s\n", file);
        }
        closedir(d);
    }
}