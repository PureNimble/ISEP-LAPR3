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

#include "../Headers/processadorDeDados.h"
#include "../Headers/extractToken.h"
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
    for (int i = 0; i < numberOfReads; i++)
    {
        char *data = test(); // getData(serial_port);
        int *info = extractInfo(data);
        printf("id: %i, x: %i, %i\n", info[0], info[1], info[2]);
        insertInfo(info, ptrSensores, NUM_SENSORS);
        //  free(data);
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
    int id;
    char type[50];
    char unit[50];
    int value;
    int interval;
    int duration;
    Sensor s;

    while (fscanf(configFile, "%d#%[^#]#%[^#]#%d#%d#%d\n", &id, type, unit, &value, &interval, &duration) == 6)
    {

        s.id = id;
        s.unit = unit;
        int *buffer_circular = malloc(value * sizeof(int));
        if (buffer_circular == NULL)
        {
            printf("Erro ao alocar memória\n");
            exit(0);
        }
        s.buffer_circular = buffer_circular;
        int *median_array = malloc(value * sizeof(int));
        if (median_array == NULL)
        {
            printf("Erro ao alocar memória\n");
            exit(0);
        }
        s.median_array = median_array;
        s.instate_temporal_ultima_leitura = 0;
        s.timeout = interval = 0;
        s.write_counter = 0;

        *ptr = s;
        ptr++;
    }
    fclose(configFile);
}

char *test()
{
    char *text[] = {"sensor_id:1#value:10#time:10", "sensor_id:2#value:20#time:20", "sensor_id:3#value:30#time:30", "sensor_id:4#value:40#time:40"};

    return text[rand() % 4];
}

void insertInfo(int *info, Sensor *sensors, int count)
{
    int sensorIndex = findSensor(info[0], sensors, count);

    Sensor currentChange = sensors[sensorIndex];

    currentChange.buffer_circular[currentChange.write_counter] = info[1];
    currentChange.instate_temporal_ultima_leitura = info[2];
    currentChange.write_counter++;

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
        printf("id: %i\n", sensors[i].id);
        printf("unit: %s\n", sensors[i].unit);
        printf("write_counter: %i\n", sensors[i].write_counter);
        printf("instate_temporal_ultima_leitura: %d\n", sensors[i].instate_temporal_ultima_leitura);
        printf("timeout: %i\n", sensors[i].timeout);
        printf("buffer_circular: ");
        for (int j = 0; j < sensors[i].write_counter; j++)
        {
            printf("%i ", sensors[i].buffer_circular[j]);
        }
        printf("\n");
        printf("median_array: ");
        for (int j = 0; j < sensors[i].write_counter; j++)
        {
            printf("%i ", sensors[i].median_array[j]);
        }
        printf("\n");
    }
}

float median(int *array, int size)
{
    int *sortedArray = malloc(size * sizeof(int));
    if (sortedArray == NULL)
    {
        printf("Erro ao alocar memória\n");
        exit(0);
    }
    memcpy(sortedArray, array, size * sizeof(int));
    int temp;
    for (int i = 0; i < size; i++)
    {
        for (int j = i; j > 0; j--)
        {
            if (sortedArray[j] < sortedArray[j - 1])
            {
                temp = sortedArray[j];
                sortedArray[j] = sortedArray[j - 1];
                sortedArray[j - 1] = temp;
            }
        }
    }
    float median;
    if (size % 2 == 0)
    {
        median = (sortedArray[size / 2] + sortedArray[(size / 2) - 1]) / 2.0;
    }
    else
    {
        median = sortedArray[size / 2];
    }
    free(sortedArray);
    return median;
}