// C library headers
#include <stdio.h>
#include <string.h>

// Linux headers
#include <fcntl.h>   // Contains file controls like O_RDWR
#include <errno.h>   // Error integer and strerror() function
#include <termios.h> // Contains POSIX terminal control definitions
#include <unistd.h>  // write(), read(), close()

#include "../Header/processadorDeDados.h"
void procesadorDeDados(char *valuesPath, char *configPath, char *directoryPath, int numberOfReads)
{
    getData(valuesPath, numberOfReads);

    Sensor *sensors = malloc(numberOfReads * sizeof(Sensor));

    if (sensors == NULL)
    {
        perror("Error allocating memory");
        return;
    }

    free(sensors);
}

getData(char *valuesPath, int numberOfReads)
{

    int serial_port = open(valuesPath, O_RDWR);

    // Check for errors
    if (serial_port < 0)
    {
        printf("Error %i from open: %s\n", errno, strerror(errno));
    }

    struct termios tty;

    // Read in existing settings, and handle any error
    // NOTE: This is important! POSIX states that the struct passed to tcsetattr()
    // must have been initialized with a call to tcgetattr() overwise behaviour
    // is undefined
    if (tcgetattr(serial_port, &tty) != 0)
    {
        printf("Error %i from tcgetattr: %s\n", errno, strerror(errno));
    }
    cfsetispeed(&tty, B9600);

    // Allocate memory for read buffer, set size according to your needs
    char read_buf[256];

    // Read bytes. The behaviour of read() (e.g. does it block?,
    // how long does it block for?) depends on the configuration
    // settings above, specifically VMIN and VTIME
    int n = read(serial_port, &read_buf, sizeof(read_buf));

    printf("Received message: %s", read_buf);

    close(serial_port);
}
