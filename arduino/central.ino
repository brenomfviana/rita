// TV
#define TV_R 6
#define TV_G 5
#define TV_B 3
#define FAN 11

// Channels
extern "C" const String CHANNELS[] = {"C1", "C2", "C3", "C4", "C5", "C6"};
extern "C" const int C1[]  = {255,   0,   0};
extern "C" const int C2[]  = {  0, 255,   0};
extern "C" const int C3[]  = {  0,   0, 255};
extern "C" const int C4[]  = {  0, 255, 255};
extern "C" const int C5[]  = {255,   0, 255};
extern "C" const int C6[]  = {255, 255,   0};
extern "C" const int OFF[] = {  0,   0,   0};

// Light
#define LIGHT 4

String command;
String channel;

void setup() {
  pinMode(TV_R,  OUTPUT);
  pinMode(TV_G,  OUTPUT);
  pinMode(TV_B,  OUTPUT);
  pinMode(LIGHT, OUTPUT);
  pinMode(FAN,   OUTPUT);
  Serial.begin(9600);
}

void loop() {
  command = "";
  while(Serial.available()){
    char aux = (char)Serial.read();
    if(aux != '\n'){
      command += aux;
    }
  }

  // Check command
  if(command.length() >= 1)
    Serial.print("Messae found: " + command + "\n");
  else
    Serial.print("Waiting for msg...\n");

  String cmdTvOn = command.substring(0, 15);
  delay(2000);
  if (cmdTvOn == "LIGAR_TELEVISAO") {
    channel = command.substring(16, command.length());
    if (channel == CHANNELS[0]) {
      analogWrite(TV_R, C1[0]);
      analogWrite(TV_G, C1[1]);
      analogWrite(TV_B, C1[2]);
    }
    if (channel == CHANNELS[1]) {
      analogWrite(TV_R, C2[0]);
      analogWrite(TV_G, C2[1]);
      analogWrite(TV_B, C2[2]);
    }
    if (channel == CHANNELS[2]) {
      analogWrite(TV_R, C3[0]);
      analogWrite(TV_G, C3[1]);
      analogWrite(TV_B, C3[2]);
    }
    if (channel == CHANNELS[3]) {
      analogWrite(TV_R, C4[0]);
      analogWrite(TV_G, C4[1]);
      analogWrite(TV_B, C4[2]);
    }
    if (channel == CHANNELS[4]) {
      analogWrite(TV_R, C5[0]);
      analogWrite(TV_G, C5[1]);
      analogWrite(TV_B, C5[2]);
    }
    if (channel == CHANNELS[5]) {
      analogWrite(TV_R, C6[0]);
      analogWrite(TV_G, C6[1]);
      analogWrite(TV_B, C6[2]);
    }
  } else if(command == "DESLIGAR_TELEVISAO") {
    analogWrite(TV_R, OFF[0]);
    analogWrite(TV_G, OFF[1]);
    analogWrite(TV_B, OFF[2]);
  }
  // Light
  if (command == "LIGAR_LUZ") {
    digitalWrite(LIGHT, HIGH);
  } else if(command == "DESLIGAR_LUZ") {
    digitalWrite(LIGHT, LOW);
  } 
  if(command == "LIGAR_VENTILADOR") {
    analogWrite(FAN, 255);
  } else if(command == "DESLIGAR_VENTILADOR") {
    analogWrite(FAN, 0);
  }

  
}
