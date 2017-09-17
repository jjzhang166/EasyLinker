#include <SPI.h>
#include <Ethernet.h>
#include <PubSubClient.h>
byte mac[]    = {  0xDE, 0xED, 0xBA, 0xFE, 0xFE, 0xED };
IPAddress server(192, 168, 199, 107);
EthernetClient ethClient;
PubSubClient client(ethClient);
int LED_GPIO = 5;
String deviceId="c56ff6d3-541d-4862-b3db-a26f44c921d2";
String topic="device";
char * id="device/c56ff6d3-541d-4862-b3db-a26f44c921d2";
void onMessageReceived(char* topic, byte* payload, unsigned int length) {
  String message = "";
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }
   Serial.println(message);

  if (message == "ON") {
    digitalWrite(LED_GPIO, HIGH);
  } else if (message == "OFF") {
    digitalWrite(LED_GPIO, LOW);
  }
 
}


void reconnect() {
  while (!client.connected()) {
    Serial.println("Connection interrupted!Attempting MQTT resume...");
    if (client.connect("arduinoClient-c56ff6d3-541d-4862-b3db-a26f44c921d2")) {
      client.subscribe("device/c56ff6d3-541d-4862-b3db-a26f44c921d2");
    } else {
      delay(2000);
    }
  }
}

void setup()
{
  Serial.begin(57600);
  pinMode(LED_GPIO, OUTPUT);
  Serial.println("Start device...");
  client.setServer(server, 1883);
  client.setCallback(onMessageReceived);
  Ethernet.begin(mac);
  client.connect("arduinoClient-c56ff6d3-541d-4862-b3db-a26f44c921d2");
  client.subscribe("device/c56ff6d3-541d-4862-b3db-a26f44c921d2");
  Serial.println("Start device finished.");
  delay(1500);
}

void loop()
{
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
}
