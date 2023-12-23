// Library -> standard library
#include <stdio.h>
// Library -> more portable code -> set of typedefs
#include <stdint.h>
#include "Header/main.h"
#include "Header/coletorDeDados.h"
#include "Header/processadorDeDados.h"
// Library -> String copy
#include <string.h>
// Library -> malloc
#include <stdlib.h>
#include <ctype.h>
int main()
{
	char option = mainMenu();
}

char mainMenu()
{
	char output = -1;
	do
	{
		printf("main menu\n\n");
		printf("1 - Ler dados dos sensores\n");
		printf("2 - Processar os dados\n");
		printf("0 - Sair\n\n");
		scanf("%d", &output);
	} while (output < 0 && output > 2);

	switch (output)
	{
	case 1:
		char *resultados = coletorDeDados();
		break;
	case 2:
		processadorDeDados(VALUE_PATH, CONFIG_PATH, SAIDA_PATH, 4);
		break;
	case 0:
		exit(0);
	}

	return output;
}
