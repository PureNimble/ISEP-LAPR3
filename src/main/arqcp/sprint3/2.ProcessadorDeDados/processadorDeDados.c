// C library headers
#include <stdio.h>
#include <string.h>

// Linux headers
#include <fcntl.h>   // Contains file controls like O_RDWR
#include <errno.h>   // Error integer and strerror() function
#include <termios.h> // Contains POSIX terminal control definitions
#include <unistd.h>  // write(), read(), close()
#include <sys/stat.h>
#include <sys/types.h>
#include <signal.h>
#include <unistd.h>
// malloc
#include <stdlib.h>
// time
#include <time.h>

#include <math.h>
#include <limits.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <ctype.h>
#include <string.h>

#include "../Headers/processadorDeDados.h"
#include "../Headers/asm.h"

pid_t fatherPid;
void processadorDeDados(char *valuesPath, char *configPath, char *directoryPath, int numberOfReads, pid_t pid)
{
    fatherPid = pid;
    configPath = insert_at_start(configPath, "../");
    directoryPath = insert_at_start(directoryPath, "../");

    int const NUM_SENSORS = numberOfLines(configPath);
    Sensor *ptrSensores = (Sensor *)malloc(NUM_SENSORS * sizeof(Sensor));
    if (ptrSensores == NULL)
        printf("Erro ao criar a array dinâmico de estruturas\n");

    createSensors(ptrSensores, configPath);

    int serial_port = open(valuesPath, O_RDWR);
    if (serial_port < 0)
    {
        printf("Erro %i ao abir: %s\n", errno, strerror(errno));
        freeSensors(ptrSensores, NUM_SENSORS);
        return;
    }

    struct termios tty;
    if (tcgetattr(serial_port, &tty) != 0)
    {
        printf("Erro %i no tcgetattr: %s\n", errno, strerror(errno));
        close(serial_port);
        freeSensors(ptrSensores, NUM_SENSORS);
        return;
    }
    cfsetispeed(&tty, B9600);

    int i;
    while (1)
    {
        printf("A processar dados...\n");
        for (i = 0; i < numberOfReads; i++)
        {
            char *data = getData(serial_port);
            int *info = extractInfo(data);
            printf("id: %i, valor: %i, time:%i\n", info[0], info[1], info[2]);
            fflush(stdout);
            insertInfo(info, ptrSensores, NUM_SENSORS);
            free(data);
            free(info);
        }
        char *output[NUM_SENSORS];
        for (i = 0; i < NUM_SENSORS; i++)
        {
            moving_median(&ptrSensores[i], numberOfReads);
            serialize(&ptrSensores[i], directoryPath, &output[i]);
        }
        createOutputFile(directoryPath, output, NUM_SENSORS);
        printAllSensors(ptrSensores, NUM_SENSORS);
    }
    freeSensors(ptrSensores, NUM_SENSORS);

    close(serial_port);
}

void freeSensors(Sensor *sensors, int count)
{
    for (int i = 0; i < count; i++)
    {
        free(sensors[i].buffer_circular);
        free(sensors[i].median_array);
        free(sensors[i].sensor_type);
        free(sensors[i].unit);
    }
    free(sensors);
}

char *getData(int serial_port)
{
    char *read_buf = (char *)calloc(256, sizeof(char));
    if (read_buf == NULL)
    {
        fprintf(stderr, "Failed to allocate memory for read_buf.\n");
        killProcess(fatherPid, SIGUSR1);
        exit(1);
    }
    do
    {
        read(serial_port, read_buf, 256);
    } while (read_buf[0] != 's');

    return read_buf;
}

int *extractInfo(char *data)
{
    int *output = malloc(3 * sizeof(int));
    if (output == NULL)
    {
        printf("Erro ao alocar memória\n");
        return NULL;
    }

    char *tokens[] = {"sensor_id:", "value:", "time:"};
    for (int i = 0; i < 3; i++)
    {
        if (extract_token(data, tokens[i], &output[i]) == 0)
        {
            printf("Erro ao extrair o token %s\n", tokens[i]);
            return NULL;
        }
        if (i == 1)
            output[i] = round(output[i] / 100.0);
    }
    return output;
}

void createSensors(Sensor *ptr, char *configPath)
{
    char *directory = strdup(configPath);
    if (directory == NULL)
    {
        printf("Erro ao alocar memória\n");
        killProcess(fatherPid, SIGUSR1);
    }
    removeFilenameFromPath(directory);
    if (!doesDirectoryExist(directory))
        mkdir(directory, 0777);
    free(directory);

    if (access(configPath, F_OK) == -1)
        createConfigFile(configPath);

    FILE *file = fopen(configPath, "r");
    if (file == NULL)
    {
        printf("Erro ao abrir o ficheiro de configuração\n");
        killProcess(fatherPid, SIGUSR1);
    }

    unsigned short id;
    char type[30];
    char unit[20];
    unsigned int buffer_size;
    unsigned int window_len;
    int timeout;
    Sensor s;

    while (fscanf(file, "%hd#%[^#]#%[^#]#%d#%d#%d\n", &id, type, unit, &buffer_size, &window_len, &timeout) == 6)
    {
        s.sensor_type = (char *)calloc(strlen(type) + 1, sizeof(char));
        s.unit = malloc(strlen(unit) + 1);
        s.id = id;
        strcpy(s.sensor_type, type);
        strcpy(s.unit, unit);
        int *buffer_circular = (int *)calloc(buffer_size, sizeof(int));
        if (buffer_circular == NULL)
        {
            printf("Erro ao alocar memória\n");
            killProcess(fatherPid, SIGUSR1);
        }
        s.buffer_circular = buffer_circular;
        int *median_array = (int *)calloc(buffer_size - window_len + 1, sizeof(int));
        if (median_array == NULL)
        {
            printf("Erro ao alocar memória\n");
            killProcess(fatherPid, SIGUSR1);
        }
        s.median_array = median_array;
        s.instate_temporal_ultima_leitura = INT_MIN;
        s.timeout = timeout;
        s.write_counter = 0;
        s.window_len = window_len;
        s.buffer_size = buffer_size;
        s.buffer_read = 0;
        s.buffer_write = 0;
        s.medianIndex = 0;
        s.isError = false;

        *ptr = s;
        ptr++;
    }
    fclose(file);
}

void insertInfo(int *info, Sensor *sensors, int count)
{
    int sensorIndex = findSensor(info[0], sensors, count);
    if (sensorIndex == -1)
        return;
    if (sensors[sensorIndex].isError)
        return;
    int lastTime = sensors[sensorIndex].instate_temporal_ultima_leitura;
    int timeout = info[2] - lastTime;
    enqueue_value(sensors[sensorIndex].buffer_circular, sensors[sensorIndex].buffer_size, &sensors[sensorIndex].buffer_read, &sensors[sensorIndex].buffer_write, info[1]);
    sensors[sensorIndex].instate_temporal_ultima_leitura = info[2];
    if (timeout > sensors[sensorIndex].timeout)
        sensors[sensorIndex].isError = true;
}

int findSensor(int id, Sensor *sensors, int count)
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

void printAllSensors(Sensor *sensors, int count)
{
    for (int i = 0; i < count; i++)
    {
        printf("->id: %i; \n", sensors[i].id);
        printf("type: %s; ", sensors[i].sensor_type);
        printf("unit: %s; ", sensors[i].unit);
        printf("write_counter: %i; ", sensors[i].write_counter);
        if (sensors[i].instate_temporal_ultima_leitura != INT_MIN)
        {
            printf("instate_temporal_ultima_leitura: %d; ", sensors[i].instate_temporal_ultima_leitura);
        }
        else
        {
            printf("instate_temporal_ultima_leitura: NULL; ");
        }
        printf("timeout: %i; ", sensors[i].timeout);
        printf("buffer_read: %i; ", sensors[i].buffer_read);
        printf("buffer_write: %i; ", sensors[i].buffer_write);
        printf("window_len: %i; ", sensors[i].window_len);
        printf("buffer_size: %i; ", sensors[i].buffer_size);
        printf("buffer_circular: ");
        for (int j = 0; j < sensors[i].buffer_size; j++)
        {
            printf("%i ", sensors[i].buffer_circular[j]);
        }
        printf(";median_array: ");
        for (int j = 0; j < (sensors[i].buffer_size - sensors[i].window_len + 1); j++)
        {
            printf("%i ", sensors[i].median_array[j]);
        }
        printf("\n\n");
    }
}

void moving_median(Sensor *sensors, int numberOfReads)
{
    int window_len = sensors->window_len;
    int write = sensors->buffer_write;
    int read = sensors->buffer_read;
    int buffer_size = sensors->buffer_size;

    int num_elements = (write - read + buffer_size) % buffer_size;

    if (num_elements < window_len)
        return;
    do
    {
        int vec[window_len];
        move_num_vec(sensors->buffer_circular, buffer_size, &read, &write, window_len, vec);
        sensors->median_array[sensors->medianIndex] = mediana(vec, window_len);

        if (sensors->medianIndex == sensors->buffer_size - sensors->window_len + 1)
            sensors->medianIndex = 0;
        else
            sensors->medianIndex++;
        if (read == sensors->buffer_size - 1)
            read = 0;

    } while (read != write - window_len + 2);

    sensors->buffer_read = read;
    sensors->write_counter++;
}
void removeFilenameFromPath(char *path)
{
    char *lastSlash = strrchr(path, '/');
    if (lastSlash != NULL)
    {
        *lastSlash = '\0';
    }
}

int doesDirectoryExist(const char *path)
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

void serialize(Sensor *sensor, char *directoryPath, char **output)
{
    char *string = (char *)calloc(256, sizeof(char));
    if (sensor->isError)
        sprintf(string, "%d,%d,%s,%s,%s\n", sensor->id, sensor->write_counter, sensor->sensor_type, sensor->unit, "error#");
    else
    {
        int mediana = 0, i;

        for (i = 0; i < sensor->medianIndex; i++)
            mediana += sensor->median_array[i];
        sprintf(string, "%d,%d,%s,%s,%d#\n", sensor->id, sensor->write_counter, sensor->sensor_type, sensor->unit, mediana);
    }
    *output = string;
    sensor->isError = false;
}

void createOutputFile(char *directoryPath, char **output, int numberOfSensors)
{
    time_t now;
    struct tm *local;
    char buffer[80];
    time(&now);
    local = localtime(&now);
    strftime(buffer, 100, "/%Y%m%d%H%M%S_sensors.txt", local);
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

        fprintf(file, "%s", *output);
        **output++;
    }
    fclose(file);
    free(path);
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
    // free(original);

    return new_string;
}

void killProcess()
{
    kill(fatherPid, SIGUSR1);
    exit(0);
}

void createConfigFile(char *configPath)
{
    FILE *file = fopen(configPath, "w");
    if (file == NULL)
    {
        printf("Erro ao criar o ficheiro de configuração\n");
        exit(0);
    }
    fprintf(file, "1#soil_humidity#percentage#50#10#40000\n");
    fprintf(file, "2#soil_humidity#percentage#60#15#50000\n");
    fprintf(file, "3#atmospheric_humidity#percentage#70#10#20000\n");
    fprintf(file, "4#atmospheric_humidity#percentage#80#20#20000\n");
    fprintf(file, "5#atmospheric_humidity#percentage#50#15#20000\n");
    fprintf(file, "6#atmospheric_humidity#percentage#50#10#50000\n");
    fprintf(file, "7#atmospheric_temperature#celsius#40#10#80000\n");
    fprintf(file, "8#atmospheric_temperature#celsius#50#14#400000\n");
    fprintf(file, "9#atmospheric_temperature#celsius#50#13#20000\n");
    fprintf(file, "10#atmospheric_temperature#celsius#50#12#20000");
    fflush(file);
    fclose(file);
    killProcess(fatherPid, SIGUSR1);
}