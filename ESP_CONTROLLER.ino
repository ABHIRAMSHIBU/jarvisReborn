//Project SSAL IoT Core VERSION
/* Author Abhiram Shibu, Preet Patel
 * Previous Author Abhijith N Raj
 * Copyright (c) TeamDestroyer Projects 2018
 * Copyright (c) 2018 SSAL
 * Copyright (c) 2018 BrainNet Technlogies
 * Copyright (c) 2018 ARCTotal
 * For queries goto https://forums.arctotal.com
 */
//Includes
#include<string.h>
#include<SoftwareSerial.h>
#include<EEPROM.h>
#include<TimerOne.h>
#include<MemoryFree.h>
//Convience Variables
#define True 1
#define False 0
#define ESPTX 12
#define ESPRX 11
#define ESPBAUD 19200
#define BAUD 115200
#define DEBUG False
#define VERSION 2.0
SoftwareSerial ESP(ESPTX,ESPRX);
bool checkESP=true;
int ROM_ADDRESS = 0;
int counter=0;
int counter_disconnect=0;
char readEEPROM(int address){
  ROM_ADDRESS=address;
  return EEPROM.read(ROM_ADDRESS);
}
void writeESSID_EEPROM(String data){
    int length = data.length();
    char buff[32]="\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
    data.toCharArray(buff,32);
    int reallocation=13;
    for(int i=0;i<32;i++){
        EEPROM.write(reallocation+i,buff[i]);
    }
}
String readESSID_EEPROM(){
    char buff[32]="\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
    int reallocation=13;
    for(int i=0;i<32;i++){
        buff[i]=EEPROM.read(reallocation+i);
    }
    return String(buff);
}
void writePass_EEPROM(String data){
    int length = data.length();
    char buff[32]="\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
    data.toCharArray(buff,32);
    int reallocation=45;
    for(int i=0;i<32;i++){
        EEPROM.write(reallocation+i,buff[i]);
    }
}
String readPass_EEPROM(){
    char buff[32]="\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
    int reallocation=45;
    for(int i=0;i<32;i++){
        buff[i]=EEPROM.read(reallocation+i);
    }
    return String(buff);
}
/* EEPROM CODE ** UNDER DEVELOPMENT ** */
char readByNameEEPROM(String loc){
  /* Writing to each cell in EEPROM to reduce complexity (CPU is 16MHz). */
  if(!loc.compareTo(F("PIN 0"))){
    ROM_ADDRESS=0;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 1"))){
    ROM_ADDRESS=1;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 2"))){
    ROM_ADDRESS=2;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 3"))){
    ROM_ADDRESS=3;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 4"))){
    ROM_ADDRESS=4;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 5"))){
    ROM_ADDRESS=5;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 6"))){
    ROM_ADDRESS=6;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 7"))){
    ROM_ADDRESS=7;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 8"))){
    ROM_ADDRESS=8;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 9"))){
    ROM_ADDRESS=9;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 10"))){
    ROM_ADDRESS=10;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("PIN 13"))){
    ROM_ADDRESS=11;
      return EEPROM.read(ROM_ADDRESS);
  }
  else if(!loc.compareTo(F("ADC CONFIG"))){
    ROM_ADDRESS=12;
      return EEPROM.read(ROM_ADDRESS);
  }
  else{
    return -1;
  }
}
bool writeEEPROM(int address,char data){
  ROM_ADDRESS=address;
  EEPROM.write(ROM_ADDRESS,data);
}
bool writeByNameEEPROM(String loc,char data){
  if(!loc.compareTo(F("PIN 0"))){
    ROM_ADDRESS=0;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 1"))){
    ROM_ADDRESS=1;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 2"))){
    ROM_ADDRESS=2;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 3"))){
    ROM_ADDRESS=3;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 4"))){
    ROM_ADDRESS=4;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 5"))){
    ROM_ADDRESS=5;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 6"))){
    ROM_ADDRESS=6;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 7"))){
    ROM_ADDRESS=7;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 8"))){
    ROM_ADDRESS=8;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 9"))){
    ROM_ADDRESS=9;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 10"))){
    ROM_ADDRESS=10;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("PIN 13"))){
    ROM_ADDRESS=11;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else if(!loc.compareTo(F("ADC CONFIG"))){
    ROM_ADDRESS=12;
    EEPROM.write(ROM_ADDRESS,data);
      return 0;
  }
  else{
    return -1;
  }
}

/* Serial Communication Handlers */
String dataESP="",dataSerial="";
String readSerial(){          //Data from serial Save and return
  dataSerial=Serial.readString();
  return dataSerial;
}
String readESP(){            //Data from ESP save and return
  dataESP=ESP.readString();
  return dataESP;
}

/* Depricated because Arduino fucked up and idk sol */
// String sendDataESP(int id,String data){  
//   Serial.println(F("Entered"));
//   String temp="AT+CIPSEND=";//<link ID>,<length>
//   temp.concat(id);
//   temp.concat(F(","));
//   temp.concat(data.length()+2);
//   temp.concat(F("\r\n"));
//   writeESP(temp);
//   delay(100);
//   String inputData=readESP();
//   if(inputData.indexOf(F("OK")>-1)){
//     data.concat("\r\n");
//     writeESP(data);
//   }
//    delay(100);
//    readESP();
//    Serial.println(F("Exited"));
// }


void writeESP(String data){           //String to ESP
  ESP.print(data);
}
void writeSerial(String data){        //String to Serial
  Serial.print(data);
}
void passThrough(){                    // Serial -> ESP, ESP -> Serial
  if(Serial.available()){
    writeESP(readSerial());
  }
  if(ESP.available()){
    writeSerial(readESP());
  }
}
void serialClear(){                     // Clear extra data
  ESP.flush();
  Serial.flush();
}
void echoSerial(){                      // Test function Serial->Serial
  if(Serial.available()){
    Serial.println(Serial.readString());
  }
}

/* ESP initallizer */
void initializeESP(){
    int count=0;
    mux:
    writeESP(F("AT+CWMODE?\r\n"));
    delay(200);
    String data = readESP();
    if(data.indexOf("CWMODE:1")==-1){
        writeESP(F("AT+CWMODE=1\r\n"));
        Serial.println(F("Setting ap mode"));
        delay(200);
        Serial.println(readESP());
        count++;
        if(count==10){
            writeESP(F("AT+RST\r\n"));
            Serial.println(F("Reset ESP"));
            delay(200);
            Serial.println(readESP());
            count=0;
        }
        goto mux;
    }
    writeESP(F("AT+CIPMUX=1\r\n"));   //Set mux to 1 thereby allowing multi connection
    delay(200);
//     Serial.print("Data:");
    data =readESP();
//     Serial.println(data);
    /* Detect error and try to fix by restarting the function */
    if(data.indexOf(F("ERROR"))>-1){
      if(data.indexOf(F("builded")>-1)){
        counter++;
        if(counter==3){
          writeESP(F("AT+RST\r\n"));
          serialClear();
        }
        goto mux;
      }
      else{
//         Serial.print("Link Active");
      }
    }
    writeESP(F("AT+CIPSERVER=1,23\r\n"));  //Start server
    delay(200);
    Serial.print(F("Data:"));
    data =readESP();
    Serial.println(data);
    /* Detect error and restart function */
    if(data.indexOf(F("ERROR"))>-1){
      if(data.indexOf(F("builded")>-1)){
        counter++;
        if(counter==3){
          writeESP(F("AT+RST\r\n"));
          serialClear();
        }
        goto mux;
      }
      else{
        Serial.print(F("Link Active"));
      }
    }
    counter=0;
}
bool led=False;

/* Interrupt Service Routine for timer one, ESP reset check routine */
void CUSTOM_ISR(void){
  if(led){
    led=False;
    digitalWrite(13,led);
  }
  else{
    led=True;
    digitalWrite(13,led);
  }
  checkESP=true;
}

/* Interrupt initialization code for above ISR */
void initializeInterrupt(){
  pinMode(13,1);
  Timer1.initialize(10000000); //Heart Beat ( ESP refresh signal.. )
  Timer1.attachInterrupt(CUSTOM_ISR); 
}

bool pins[11];    // Pin stats are stored here
void setup() {
  for(int i=2;i<11;i++){
    pinMode(i,1);
  }
  Serial.begin(BAUD); //Serial to debugger 0,1
  ESP.begin(ESPBAUD); //Serial to ESP mainly 11,12
  Serial.setTimeout(10); // Set string read timeout, without this readString is slow
  ESP.setTimeout(100);   // Up
  initializeESP();
  initializeInterrupt();
  noInterrupts();        // Dont interrupt
  Serial.println(F("Welcome to SSAL IoT Core"));
//   Serial.println("Begin EEPROM test code");
  for(int i=0;i<13;i++){
    String temp=F("PIN ");
    temp.concat(String(i));
    Serial.print(temp);
    Serial.print(F(":"));
    //writeByNameEEPROM(temp,100);
    Serial.println(int(readByNameEEPROM(temp)));
  }
//   Serial.println("End EEPROM test code");
  for(int i=0;i<11;i++){
    pins[i]=False;
  }
  Serial.print(F("freeMemory()="));
  Serial.println(freeMemory());  // Print free memory ocationally
  interrupts();                  // Can interrupt
}
void loop() {
  //ISR treggers this function indirectly
  if(checkESP){
      /* ESP RESET/SERVER CHECK */
    writeESP(F("AT+CIPMUX?\r\n"));  //Check MUX status, for server to run it should be 1
    delay(300);
    String data=readESP();
//     Serial.print("Interrpt:");
//     Serial.println(data);
    if(data.indexOf(F("+CIPMUX:1"))==-1){  // not 1 make it 1
//       Serial.println("ESP RESET DETECTED!");
      initializeESP();                  //Reinitiallize there by making mux 1
      Serial.println(F("Re-initallized ESP"));
    }
    dataESP="";
    Serial.print(F("Free RAM = "));
    Serial.println(freeMemory());        //Print free ram periodically
    checkESP=false;
    
    /* ESP CONNECTION CHECK */
    writeESP(F("AT+CIPSTATUS\r\n"));
    delay(300);
    data = readESP();
    Serial.print(F("Hotspot data:"));
    Serial.println(data);
    if(data.indexOf(F(":5"))>-1){
        counter_disconnect++;
        if(counter_disconnect==3){
            counter_disconnect=0;
            writeESP(F("AT+CWMODE_CUR=2\r\n"));
            delay(300);
            readESP();
            writeESP(F("AT+CWDHCP_CUR=0,1\r\n"));
            delay(300);
            readESP();
            Serial.println(F("Hotspot initallized!"));
            long time=millis();
            while((millis()-time)<60000){
                passThrough();
                if(dataESP.indexOf(F("+IPD,"))>-1){     //Check if data from SSAL Core is available
                    dataESP.remove(0,dataESP.indexOf(F(","))+1); //Remove header
                    int id=dataESP.substring(0,1).toInt();  // Get id
                    Serial.print(F("ID :"));       
                    Serial.println(id);
                    dataESP.remove(0,dataESP.indexOf(":")+1);  //Remove id
                    Serial.print(F("Trimmed data:"));
                    Serial.println(dataESP);     //Now u have data\r\n left
                    if(id==0){                   // Allow only id 0
                        if(dataESP.startsWith(F("writeAP="))){
                            //Syntax wiriteAP=ssid,pass no quotes needed
                            dataESP.remove(0,dataESP.indexOf(F("="))+1);
                            String SSID = dataESP.substring(0,dataESP.indexOf(F(",")));
                            String PASS = dataESP.substring(dataESP.indexOf(F(","))+1);
                            Serial.print(F("SSID ="));
                            writeESSID_EEPROM(SSID);
                            Serial.println(readESSID_EEPROM());
                            Serial.print(F("PASS ="));
                            writePass_EEPROM(PASS);
                            Serial.println(readPass_EEPROM());
                        }
                    }
                }
            }
            writeESP(F("AT+RST\r\n"));
            delay(300);
            readESP();
        }
    }
  }
  interrupts();                          //Code can be interrupted
  passThrough();                         //Pass data from one serial to another
  if(!dataSerial.equals("")){            //Data from serial is available
//     Serial.print("Captured data from Serial :");
//     Serial.println(dataSerial);
    dataSerial="";
  }
  if(!dataESP.equals("")){               //Data from ESP available
//     Serial.print("Captured data from ESP :");
//     Serial.println(dataESP);
    if(dataESP.indexOf(F("CONNECT"))>-1){   //Check if there is a connection and absorb next line
      delay(100);                        //Wait for some time
      readESP();
    }
    if(dataESP.indexOf(F("+IPD,"))>-1){     //Check if data from SSAL Core is available
      dataESP.remove(0,dataESP.indexOf(",")+1); //Remove header
      int id=dataESP.substring(0,1).toInt();  // Get id
      Serial.print(F("ID :"));       
      Serial.println(id);
      dataESP.remove(0,dataESP.indexOf(":")+1);  //Remove id
      Serial.print(F("Trimmed data:"));
      Serial.println(dataESP);     //Now u have data\r\n left
      if(id==0){                   // Allow only id 0
        if(dataESP.indexOf(F(" "))>-1){
        /* Find space split into pin and operation, then compile and send */
        int space = dataESP.indexOf(F(" "));
        int pin=dataESP.substring(0,space).toInt();
        bool operation=dataESP.substring(space,dataESP.length()-2).toInt();
//           Serial.print(F("Pin Set Request, PIN:"));
//           Serial.print(pin);
//           Serial.print(F(" Operation:"));
//           Serial.println(operation);
        digitalWrite(pin,operation); // do operation
        pins[pin]=operation;         // update internal DB
        String pinString=String(pin);
        pinString.concat(F(" "));
        if(operation){
            pinString.concat(F("on"));
        }
        else{
            pinString.concat(F("off"));
        }
        /* Send assembled reply */
        
        //SendData - Because ardunio dont like functions
        String temp="AT+CIPSEND=";//<link ID>,<length>
        temp.concat(id);
        temp.concat(F(","));
        temp.concat(pinString.length()+2);
        temp.concat(F("\r\n"));
        writeESP(temp);
        delay(100);
        String inputData=readESP();
        if(inputData.indexOf(F("OK")>-1)){
            pinString.concat(F("\r\n"));
            writeESP(pinString);
        }
        delay(100);
        readESP();
        //End SendData
        
        
        }
        else{
        /* Here pin status is retrived and send to the SSAL Core */
        int pin=dataESP.substring(0,dataESP.length()-2).toInt();
//           Serial.print(F("Pin get Request, PIN:"));
//           Serial.println(pin);
        
        //SendData - Because ardunio dont like functions
        String pinString=String(pins[pin]);
        String temp="AT+CIPSEND=";//<link ID>,<length>
        temp.concat(id);
        temp.concat(",");
        temp.concat(pinString.length()+2);
        temp.concat(F("\r\n"));
        writeESP(temp);
        delay(100);
        String inputData=readESP();
        if(inputData.indexOf("OK">-1)){
            pinString.concat("\r\n");
            writeESP(pinString);
        }
        delay(100);
        readESP();
        //End SendData
        
        }
      }
      else{
        /* Kick trustpassers */
        String pinString=F("Not allowed!");
        //SendData 
          String temp="AT+CIPSEND=";//<link ID>,<length>
          temp.concat(id);
          temp.concat(",");
          temp.concat(pinString.length()+2);
          temp.concat("\r\n");
          writeESP(temp);
          delay(100);
          String inputData=readESP();
          if(inputData.indexOf(F("OK")>-1)){
            pinString.concat(F("\r\n"));
            writeESP(pinString);
          }
          delay(100);
          readESP();
          //End SendData
      }
    }
    dataESP="";       //ESP data never got logged wink wink
  }
}
