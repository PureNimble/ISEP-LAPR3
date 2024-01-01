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