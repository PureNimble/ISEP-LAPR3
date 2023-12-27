// C library headers
#include <stdio.h>
#include <string.h>

// Linux headers
#include <fcntl.h>   // Contains file controls like O_RDWR
#include <errno.h>   // Error integer and strerror() function
#include <termios.h> // Contains POSIX terminal control definitions
#include <unistd.h>  // write(), read(), close()
// malloc
#include <stdlib.h>
// time
#include <time.h>

#include "../Headers/processadorDeDados.h"
#include "../Headers/asm.h"
void processadorDeDados(char *valuesPath, char *configPath, char *directoryPath, int numberOfReads)
{
    int const NUM_SENSORS = numberOfLines(configPath);
    Sensor *ptrSensores = (Sensor *)malloc(NUM_SENSORS * sizeof(Sensor));

    if (ptrSensores == NULL)
    {
        printf("Erro ao criar a array dinâmico de estruturas\n");
        exit(0);
    }
    createSensors(ptrSensores, configPath);

    /* int serial_port = open(valuesPath, O_RDWR);
    if (serial_port < 0)
    {
        printf("Erro %i ao abir: %s\n", errno, strerror(errno));
        // freeSensors(sensors, numLines);
        return;
    }

    struct termios tty;
    if (tcgetattr(serial_port, &tty) != 0)
    {
        printf("Erro %i no tcgetattr: %s\n", errno, strerror(errno));
        close(serial_port);
        // freeSensors(sensors, numLines);
        return;
    }
    cfsetispeed(&tty, B9600); */

    /* while (1)
    { */
    int i;
    for (i = 0; i < numberOfReads; i++)
    {
        char *data = test(); // getData(serial_port);
        int *info = extractInfo(data);
        printf("id: %i, x: %i, %i\n", info[0], info[1], info[2]);
        insertInfo(info, ptrSensores, NUM_SENSORS);
        //  free(data);
    }
    for (i = 0; i < NUM_SENSORS; i++)
    {
        if (numberOfReads > ptrSensores[i].window_len)
            moving_median(&ptrSensores[i]);
    }
    //}
    printAllSensors(ptrSensores, NUM_SENSORS);

    // close(serial_port);
    freeSensors(ptrSensores, NUM_SENSORS);
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
    char *read_buf = calloc(256, sizeof(char));
    if (read_buf == NULL)
    {
        perror("Erro ao alocar espaço na memória");
        return NULL;
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
    }
    return output;
}

void createSensors(Sensor *ptr, char *configPath)
{
    FILE *configFile = fopen(configPath, "r");
    if (configFile == NULL)
    {
        perror("Erro ao abrir o ficheiro de configuração");
        exit(0);
    }
    unsigned short id;
    char type[30];
    char unit[20];
    unsigned int buffer_size;
    unsigned int window_len;
    int timeout;
    Sensor s;

    while (fscanf(configFile, "%hd#%[^#]#%[^#]#%d#%d#%d\n", &id, type, unit, &buffer_size, &window_len, &timeout) == 6)
    {
        s.sensor_type = malloc(strlen(type) + 1);
        s.unit = malloc(strlen(unit) + 1);
        s.id = id;
        strcpy(s.sensor_type, type);
        strcpy(s.unit, unit);
        int *buffer_circular = malloc(buffer_size * sizeof(int));
        if (buffer_circular == NULL)
        {
            printf("Erro ao alocar memória\n");
            exit(0);
        }
        s.buffer_circular = buffer_circular;
        int *median_array = malloc(buffer_size * sizeof(int));
        if (median_array == NULL)
        {
            printf("Erro ao alocar memória\n");
            exit(0);
        }
        s.median_array = median_array;
        s.instate_temporal_ultima_leitura = 0;
        s.timeout = timeout;
        s.write_counter = 0;
        s.window_len = window_len;
        s.buffer_size = buffer_size;
        s.buffer_read = 0;
        s.buffer_write = 0;
        s.medianIndex = 0;

        *ptr = s;
        ptr++;
    }
    fclose(configFile);
}

char *test()
{
    char *text[] = {"sensor_id:1#value:10#time:10", "sensor_id:2#value:20#time:20", "sensor_id:3#value:30#time:30", "sensor_id:4#value:40#time:40"};
    srand(time(NULL));
    return text[0];
}

void insertInfo(int *info, Sensor *sensors, int count)
{
    int sensorIndex = findSensor(info[0], sensors, count);

    Sensor currentChange = sensors[sensorIndex];

    enqueue_value(currentChange.buffer_circular, currentChange.buffer_size, &currentChange.buffer_read, &currentChange.buffer_write, info[1]);
    currentChange.instate_temporal_ultima_leitura = info[2];
    sensors[sensorIndex] = currentChange;
}

int findSensor(int id, Sensor *sensors, int count)
{
    unsigned short i;
    for (i = 0; i < count; i++)
    {
        if (id == sensors[i].id)
            return i;
    }
    printf("Sensor não existente no ficheiro de configuração.\n");
    exit(0);
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
        printf("id: %i; ", sensors[i].id);
        printf("type: %s; ", sensors[i].sensor_type);
        printf("unit: %s; ", sensors[i].unit);
        printf("write_counter: %i; ", sensors[i].write_counter);
        printf("instate_temporal_ultima_leitura: %d; ", sensors[i].instate_temporal_ultima_leitura);
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
        printf("; median_array: ");
        for (int j = 0; j < sensors[i].buffer_size; j++)
        {
            printf("%i ", sensors[i].median_array[j]);
        }
        printf("\n\n");
    }
}

void moving_median(Sensor *sensors)
{
    int window_len = sensors->window_len;
    int write = sensors->buffer_write;
    int read = sensors->buffer_read;
    do
    {
        int vec[window_len];
        move_num_vec(sensors->buffer_circular, sensors->buffer_size, &read, &write, window_len, vec);
        sensors->median_array[sensors->medianIndex] = mediana(vec, window_len);
        sensors->medianIndex++;
        read = -window_len + 1;

    } while (read == write);

    sensors->buffer_read = read;
    sensors->write_counter++;
}