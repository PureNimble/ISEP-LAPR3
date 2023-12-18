#include "DHT.h"
#include <arduino-timer.h>

#define DHTPIN 16
#define DHTTYPE DHT11
#define NR_SOIL_HUMIDITY_SENSORS 2  // Sensor ID: 1, Sensor ID : 2
#define NR_ATMOSPHERIC_HUMIDITY_SENSORS 4  // Sensor ID: 3, Sensor ID : 4, Sensor ID : 5, Sensor 6
#define NR_ATMOSPHERIC_TEMPERATURE_SENSORS 4  // Sensor ID: 7, Sensor ID : 8, Sensor ID : 9, Sensor ID : 10
#define DATA_LENGTH 100
#define TYPE_NAME_LENGTH 30
#define UNIT_NAME_LENGTH 30
#define TOGGLE_LED_PERIOD 1000  //miliseconds
#define SOIL_HUMIDITY_PERIOD 5000
#define ATMOSPHERIC_HUMIDITY_PERIOD 2000
#define ATMOSPHERIC_TEMPERATURE_PERIOD 2000

char sensor_types[][TYPE_NAME_LENGTH] = { "soil_humidity", "atmospheric_humidity", "atmospheric_temperature" };

enum sensor_type {
  SOIL_HUMIDITY = 0,
  ATMOSPHERIC_HUMIDITY,
  ATMOSPHERIC_TEMPERATURE
};

char sensor_units[][UNIT_NAME_LENGTH] = {
  "percentage",
  "celsius",
};

enum sensor_unit {
  PERCENTAGE = 0,
  CELSIUS,
};

DHT dht(DHTPIN, DHTTYPE);

auto timer = timer_create_default();  // create a timer with default settings

bool toggle_led(void *) {
  digitalWrite(LED_BUILTIN, !digitalRead(LED_BUILTIN));  // toggle the LED
  return true;  // keep timer active? true
}

int get_sensor_id(int min, int max) {
  int id = random(min, max);  //generate a random value [min, max[
  return id;
}

bool read_soil_humidity(void *) {
  int min = 1;
  int max = min + NR_SOIL_HUMIDITY_SENSORS;
  int id = get_sensor_id(min, max);
  float r = dht.readHumidity();
  if (!isnan(r)) {
    send_data(id, SOIL_HUMIDITY, r, PERCENTAGE, millis());
  }
  return true;
}

bool read_atmospheric_humidity(void *) {
  int min = NR_SOIL_HUMIDITY_SENSORS + 1;
  int max = min + NR_ATMOSPHERIC_HUMIDITY_SENSORS;
  int id = get_sensor_id(min, max);
  float r = dht.readHumidity();
  if (!isnan(r)) {
    send_data(id, ATMOSPHERIC_HUMIDITY, r, PERCENTAGE, millis());
  }
  return true;
}

bool read_atmospheric_temperature(void *) {
  int min = NR_SOIL_HUMIDITY_SENSORS + NR_ATMOSPHERIC_HUMIDITY_SENSORS + 1;
  int max = min + NR_ATMOSPHERIC_TEMPERATURE_SENSORS;
  int id = get_sensor_id(min, max);
  float r = dht.readTemperature();
  if (!isnan(r)) {
    send_data(id, ATMOSPHERIC_TEMPERATURE, r, CELSIUS, millis());
  }
  return true;
}

void send_data(int id, sensor_type t, float r, sensor_unit u, unsigned long m) {
  char buffer[DATA_LENGTH];
  snprintf(buffer, DATA_LENGTH - 1,
           "sensor_id:%d#type:%s#value:%.2f#unit:%s#time:%lu",
           id, sensor_types[t], r, sensor_units[u], m);
  Serial.println(buffer);
}

// the setup function runs once when you press reset or power the board
void setup() {
  // initialize digital pin LED_BUILTIN as an output.
  pinMode(LED_BUILTIN, OUTPUT);
  randomSeed(analogRead(0));
  Serial.begin(9600);
  dht.begin();
  timer.every(TOGGLE_LED_PERIOD, toggle_led);
  timer.every(ATMOSPHERIC_TEMPERATURE_PERIOD,
              read_atmospheric_temperature);
  timer.every(ATMOSPHERIC_HUMIDITY_PERIOD,
              read_atmospheric_humidity);
  timer.every(SOIL_HUMIDITY_PERIOD, read_soil_humidity);
}

// the loop function runs over and over again forever
void loop() {
  timer.tick();  // tick the timer
}