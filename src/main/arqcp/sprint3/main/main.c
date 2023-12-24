// Library -> standard library
#include <stdio.h>
// Library -> more portable code -> set of typedefs
#include <stdint.h>
#include "../Headers/main.h"
#include "../Headers/processadorDeDados.h"
// Library -> String copy
#include <string.h>
// Library -> malloc
#include <stdlib.h>
#include <ctype.h>

int main()
{
	int output = -1;
	do
	{
		printf("===============Main Menu===============\n");
		printf("1 - Processar os dados\n");
		printf("0 - Sair\n");
		scanf("%i", &output);
	} while (output < 0 && output > 2);

	switch (output)
	{
	case 1:
		processadorDeDados(VALUE_PATH, CONFIG_PATH, SAIDA_PATH, 10);
		break;
	case 0:
		exit(0);
	}
}
