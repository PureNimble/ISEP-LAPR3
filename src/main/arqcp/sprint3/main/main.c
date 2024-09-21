// Library -> standard library
#include <stdio.h>
#include "../Headers/main.h"
#include "../Headers/processadorDeDados.h"
#include "../Headers/saidaDeDados.h"
// Library -> String copy
#include <string.h>
// Library -> malloc
#include <stdlib.h>
#include <unistd.h>
// sigkill import
#include <signal.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <ctype.h>
#include <string.h>
#include <signal.h>
#include <string.h>
#include <stdlib.h>

pid_t pids[2];
void handleSignal(int signo, siginfo_t *sinfo, void *context)
{
	kill(pids[0], SIGKILL);
	kill(pids[1], SIGKILL);

	exit(0);
}

int main()
{
	char *value_path = (char *)malloc(30 * sizeof(char));
	char *config_path = (char *)malloc(30 * sizeof(char));
	char *saida_path = (char *)malloc(30 * sizeof(char));
	int number_of_readings, frequency;
	char *farm_coordinator = (char *)malloc(30 * sizeof(char));

	getParameters(value_path, config_path, saida_path, &number_of_readings, farm_coordinator, &frequency);
	printf("\033[1;36mConfiguration Details:\033[0m\n");
	printf("\033[1;36m----------------------\033[0m\n");
	printf("\033[1;32mColetor de dados output:\033[0m %s\n", value_path);
	printf("\033[1;32mFicheiro de configuração:\033[0m %s\n", config_path);
	printf("\033[1;32mProcessador de dados output:\033[0m %s\n", saida_path);
	printf("\033[1;32mNúmero de leituras:\033[0m %i\n", number_of_readings);
	printf("\033[1;32mFarm Coordinator:\033[0m %s\n", farm_coordinator);
	printf("\033[1;32mFrequência:\033[0m %i\n", frequency);
	printf("\033[1;36m----------------------\033[0m\n");
	int output = -1;
	do
	{
		printf("\n\033[1;33mAviso:\033[0m \033[1;31mSe pretender mudar os parametros\ndo programa tem de alterar o ficheiro\nde execução localizado na pasta config\033[0m\n");
		printf("\033[1;36m===============Main Menu===============\033[0m\n");
		printf("\033[1;33m1 - Começar o programa\033[0m\n");
		printf("\033[1;33m0 - Sair\033[0m\n");
		scanf("%i", &output);
	} while (output < 0 && output > 2);

	switch (output)
	{
	case 1:
		pid_t fatherPid = getpid();
		for (int i = 0; i < 2; i++)
		{
			pids[i] = fork();
			if (pids[i] < 0)
			{
				printf("Erro ao criar o processo\n");
				exit(0);
			}

			if (pids[i] == 0)
			{
				switch (i)
				{
				case 0:
					processadorDeDados(value_path, config_path, saida_path, number_of_readings, fatherPid);
					exit(0);
					break;
				case 1:
					saidaDeDados(saida_path, farm_coordinator, frequency);
					exit(0);
					break;
				default:
					break;
				}
			}
		}

		struct sigaction act;
		memset(&act, 0, sizeof(struct sigaction));
		sigemptyset(&act.sa_mask);
		act.sa_sigaction = handleSignal;
		sigaction(SIGUSR1, &act, NULL);

		scanf("%i", &output);

		kill(pids[0], SIGKILL);
		kill(pids[1], SIGKILL);

		break;
	case 0:
		exit(0);
	}
}

void getParameters(char *value_path, char *config_path, char *saida_path, int *number_of_readings, char *farm_coordinator, int *frequency)
{
	if (!doesDirectoryExist("../config"))
		mkdir("../config", 0777);
	int isFileValid = access(CONFIG_PATH, F_OK);

	if (isFileValid == -1)
		reWriteSetup();
	FILE *fp = fopen(CONFIG_PATH, "r+");
	if (fp == NULL)
	{
		printf("Erro ao abrir o ficheiro de configuração\n");
		exit(0);
	}
	char line[256];
	int isSaidaDeDados = 0;
	while (fgets(line, sizeof(line), fp))
	{
		if (!(line[0] == '-'))
			continue;
		char *p = strchr(line, ':');
		if (p == NULL)
			invalidFormat();
		char *output = trim(p + 1);
		if (*output == '\0' || output == NULL)
			invalidFormat();
		switch (line[3])
		{
		case 'a':
			if (isSaidaDeDados)
				strcpy(farm_coordinator, output);
			else
				strcpy(value_path, output);

			break;
		case 'b':
			strcpy(config_path, output);
		case 'c':
			if (isSaidaDeDados)
				*frequency = atoi(output);
			else
				strcpy(saida_path, trim(output));
			break;
		case 'd':
			*number_of_readings = atoi(output);
			isSaidaDeDados = 1;
			break;
		default:
			invalidFormat();
			break;
		}
	}

	fclose(fp);
}
void reWriteSetup()
{
	FILE *fp = fopen(CONFIG_PATH, "w");
	if (fp == NULL)
	{
		printf("Ficheiro de execução não existente, por favor, escreva os parametros necesários no ficheiro\n");
		exit(0);
	}
	fprintf(fp, "Processador de dados:\n\n");
	fprintf(fp, "-> a=ficheiro do Coletor de Dados:\n");
	fprintf(fp, "-> b=ficheiro de configuração:\n");
	fprintf(fp, "-> c=diretório onde colocar os resultados:\n");
	fprintf(fp, "-> d=número de leituras por ciclo:\n\n");
	fprintf(fp, "Saida de dados:\n\n");
	fprintf(fp, "-> a=diretório para o Farm Coordinator:\n");
	fprintf(fp, "-> c=frequencia da execução do componente(milisegundos):\n");

	fclose(fp);
	exit(0);
}
void invalidFormat()
{
	printf("Formato do ficheiro de configuração inválido\n");
	exit(0);
}

char *trim(char *str)
{
	// Trim leading space
	while (isspace((unsigned char)*str))
		str++;

	if (*str == 0) // All spaces?
		return str;

	// Trim trailing space
	char *end = str + strlen(str) - 1;
	while (end > str && isspace((unsigned char)*end))
		end--;

	// Write new null terminator character
	end[1] = '\0';

	return str;
}