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
    Sensor *sensors = malloc(sizeof(Sensor));

    int numLines = createStructs(sensors, configPath);

    int serial_port = open(valuesPath, O_RDWR);
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
    cfsetispeed(&tty, B9600);

    /* while (1)
    { */
    for (int i = 0; i < numberOfReads; i++)
    {
        char *data = getData(serial_port);
        int *info = extractInfo(data);
        free(data);
    }
    //}

    close(serial_port);
    // freeSensors(sensors, numLines);
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

int createStructs(Sensor *ptr, char *configPath)
{
    FILE *configFile = fopen(configPath, "r");
    int numberOflines = 0;
    if (configFile == NULL)
    {
        perror("Erro ao abrir o ficheiro de configuração");
        return 0;
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
        s.buffer_circular = malloc(value * sizeof(int));
        s.median_array = malloc(value * sizeof(int));
        s.instate_temporal_ultima_leitura = 0;
        s.timeout = interval = 0;
        s.write_counter = 0;

        Sensor *temp = realloc(ptr, (numberOflines + 1) * sizeof(Sensor));
        if (temp == NULL)
        {
            printf("Erro na realocação de memória\n");
            return 0;
        }
        ptr = temp;
        ptr[numberOflines] = s;
        numberOflines++;
    }
    fclose(configFile);
    return numberOflines;
}