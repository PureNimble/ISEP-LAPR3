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

pid_t fatherPid_saida;
time_t latest_creation_time = 0;
char latest_file_name[256];
void saidaDeDados(char *directoryPath, char *outputPath, long frequency, pid_t pid)
{

    fatherPid_saida = pid;
    directoryPath = insert_at_start_saida(directoryPath, "../");

    outputPath = insert_at_start_saida(outputPath, "../");

    Sensors *ptrSensores = (Sensors *)malloc(1 * sizeof(Sensors));

    if (ptrSensores == NULL)
    {
        printf("Erro ao criar a array dinâmico de estruturas\n");
        exit(0);
    }


    doOutput(ptrSensores, directoryPath, outputPath, frequency);

}

int compare(const void *a, const void *b) {
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


void* doOutput(Sensors *ptrSensores, char *directoryPath, char *outputPath, long frequency) {
    while (1) {
        DIR *dir;
        struct dirent *entry;
        struct stat file_stat;
        dir = opendir(directoryPath);
        char **files = NULL;
        int num_files = 0;
        while ((entry = readdir(dir)) != NULL) {
            if (entry->d_type == DT_REG) {
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


int numberOfLines_saida(char *path)
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

void createSensor(Sensors *ptr, char *path)
{
    findFile(path);
    FILE *fp = fopen(path, "r");
    if (fp == NULL)
    {
        printf("Erro ao abrir o ficheiro\n");
        exit(0);
    }
    char line[100];
    int i = 0;
    unsigned short id;
    char type[30];
    char unit[20];
    unsigned int mediana;
    unsigned short write_counter;
    Sensors s;

    while (fscanf(fp, "%hd,%hd,%[^#],%[^#],%d\n", &id, &write_counter, type, unit, &mediana) == 5)
        {
            s.sensor_type = (char *)calloc(strlen(type) + 1, sizeof(char));
            s.unit = malloc(strlen(unit) + 1);
            s.id = id;
            strcpy(s.sensor_type, type);
            strcpy(s.unit, unit);
            s.write_counter = write_counter;
            s.median = mediana;

            *ptr = s;
            ptr++;
        }
    fclose(fp);
}

void serializeAllSensors(Sensors *ptrSensores, char *path, int NUM_SENSORS){
    findFile(path);

    char *output[NUM_SENSORS];
     for (int i = 0; i < NUM_SENSORS; i++)
     {
            serializeSaida(&ptrSensores[i], path, &output[i]);
     }
     createSaidaFile(path, output, NUM_SENSORS);
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
        killProcess_saida(fatherPid_saida, SIGUSR1);
    }
    if (!doesDirectoryExist_saida(directoryPath))
        mkdir(directoryPath, 0777);
    strcpy(path, directoryPath);
    strcat(path, buffer);
    FILE *file = fopen(path, "a+");
    if (file == NULL)
    {
        printf("Caminho invalido: %s\n", path);
        killProcess_saida(fatherPid_saida, SIGUSR1);
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

void serializeSaida(Sensors *sensor, char *directoryPath, char **output)
{
    char str[100];
    int mediana = sensor->median;

    char med[37];
    sprintf(str, "%d", mediana);

    int position = strlen(med) - 1;
    memmove(str + position + 1, str + position, strlen(str) - position);
    str[position] = '.';

    sprintf(str, "ID: %d, Write Counter: %d, Sensor type: %s, Sensor Unit:%s, Mediana: %s#\n", sensor->id, sensor->write_counter, sensor->sensor_type, sensor->unit, med);

    *output = (char *)calloc(strlen(str), sizeof(char));
    if (*output == NULL)
    {
        printf("Erro ao alocar memória\n");
        killProcess_saida(fatherPid_saida, SIGUSR1);
    }
    strcpy(*output, str);
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

int doesDirectoryExist_saida(const char *path)
{
    struct stat statbuf;
    if (stat(path, &statbuf) != -1)
    {
        if (S_ISDIR(statbuf.st_mode))
        {
            return 1;
        }
    }
    return 0;
}

void freeSensor(Sensors *sensors, int count)
{
    for (int i = 0; i < count; i++)
    {
        free(sensors[i].sensor_type);
        free(sensors[i].unit);
    }
    free(sensors);
}

void killProcess_saida()
{
    kill(fatherPid_saida, SIGUSR1);

    exit(0);
}
