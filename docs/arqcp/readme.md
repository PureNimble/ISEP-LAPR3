# Integração com Arduino

Este projeto utiliza uma Raspberry Pico W 2022 para gerenciar dados de sensores. O Arduino lê de forma contínua dados provenientes do sensor DHT11 conectado e os transmite pela porta COM3.

O código em execução no Arduino é escrito em C++ e faz uso da biblioteca Serial incorporada no Arduino para gerenciar a comunicação serial.

### Detalhes Adicionais
O sensor DHT11 mede a temperatura e humidade. Neste projeto, o sensor é utilizado para simular a temperatura e umidade do ar e do solo.

## Tipo de Simulação e Unidades

#### Temperatura

Unidade: celsius

#### Humidade

Unidade: percentagem

## Configurações e Períodos

Pino do Sensor DHT11: 16

Tipo de Sensor DHT: DHT11

Período de Alternância do LED: 1000 milissegundos
#### Período de Leitura do Sensor: 
Solo: 5000 milissegundos

Atmosfera: 2000 milissegundos