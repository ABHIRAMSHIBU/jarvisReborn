//Project SSAL IoT Core
/* Author Abhiram Shibu, Preet Patel
 * Previous Author Abhijith N Raj
 * Copyright (c) TeamDestroyer Projects 2018
 * Copyright (c) 2019 SSAL
 * Copyright (c) 2019 BrainNet Technlogies
 * Copyright (c) 2018 ARCTotal
 * For queries goto https://forums.arctotal.com
 */
//Includes
#include<string.h>
#include<SoftwareSerial.h>
#include<EEPROM.h>
//#include<TimerOne.h> - Timer Depriciation, interfering with i2c.
#include<MemoryFree.h>
#include<Wire.h>
#include<avr/wdt.h>

//Convience Definitions
#define True 1
#define False 0
#define ESPTX 11
#define ESPRX 12
#define ESPBAUD 19200
#define BAUD 115200
#define DEBUG true
#define VERSION 2.1
#define INSPECT 180619
SoftwareSerial ESP(ESPTX,ESPRX);
bool checkESP=true;
int ROM_ADDRESS = 0;
int counter=0;
int counter_disconnect=0;
bool pins[11];    // Pin stats are stored here
float temp;
/** EEPROM BEGIN **/
byte encodeByteArray(bool * pin,int offset){
  byte val=0;
  for(int i=0;i<8;i++){
      val|=pin[i+offset]<<i;
  }
  return val;
}
void decodeIntAndRestore(bool * pin,byte val,int offset){
  byte temp;
  for(int i=0;i<8;i++){
    temp=val&(1<<i);
    if(temp==(1<<i)){
      pin[i+offset]=1;
    }
    else{
      pin[i+offset]=0;
    }
  }
}
int findLocation(){
    int i;
    for(i=0;i<1024;i+=2){
        if(EEPROM.read(i)!=0){
            break;
        }
    }
    if(i>1024){
        i=0;
    }
    return i;
}

/* Needs a improvement */ //Overflow is there
void dumpToEEPROM(){
    byte val=encodeByteArray(pins,2);
    int loc=findLocation();
    EEPROM.write((loc)%1024,0);
    EEPROM.write((loc+1)%1024,0);
    EEPROM.write((loc+2)%1024,10);
    EEPROM.write((loc+3)%1024,val);
    Serial.print(F("Write location:"));
    Serial.println(loc);
}
void loadFromEEPROM(){
    byte val;
    int loc=findLocation();
    val=EEPROM.read(loc+1);
    decodeIntAndRestore(pins,val,2);
}
/** END EEPROM **/

/* Serial Communication Handlers */
String dataESP,dataSerial;
String readSerial(){          //Data from serial Save and return
  dataSerial=Serial.readString();
  dataSerial.reserve(20);
  return dataSerial;
}
String readESP(){            //Data from ESP save and return
  dataESP=ESP.readString();
  dataESP.reserve(100);
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

// WatchDog Stuff
/* -----------------------*
 * Setup Watch dog timer  *
 * Watch Dog should reset *
 * CPU on overflow of     *
 * timer. Can be reset by *
 * WDTCSR function        *
 * -----------------------*/
void setupWatchDog(){
    wdt_reset();
    MCUSR|=_BV(WDRF);
    WDTCSR = _BV(WDCE) | _BV(WDE);
    WDTCSR = _BV(WDE) | _BV(WDCE) | _BV(WDP0) |_BV(WDP3);
}
// END WatchDog Stuff

//Wire Stuff
void sendData(){
  Wire.write("1");
  String pinsString="";
    pinsString.reserve(20);
  for(int i=2;i<12;i++){ // IDK why it needs 12 instead of 11
    if(pins[i]){
      pinsString=pinsString+"1";
    }
    else{
      pinsString=pinsString+"0";
    }
  }
  char buff[20];
  pinsString.toCharArray(buff,pinsString.length());
  Wire.write(buff);
}
void receiveData(int howMany) {
  char c[14];
  int i=0;
  for(i=0;i<14;i++){
    c[i]=' ';
  }
  i=0;
  while (0 < Wire.available()) { // loop through all but the last
    c[i] = Wire.read(); // receive byte as a character
    i++;
  }
  c[i]='\0';
  if(i){
    temp=0;
    for(int j=0;j<3;j++){
        temp+=int(c[i-3+j]-48);
        temp*=10;
    }
    temp/=100;
  }
}
void initializeWire(){
  Wire.begin(8);
    Wire.onRequest(sendData);         //Get data from I2C
    Wire.onReceive(receiveData);
}
// END WIRE STUFF

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
    String data;
    data.reserve(100);
    data=readESP();
    if(data.indexOf("CWMODE:1")==-1){
        writeESP(F("AT+CWMODE=1\r\n"));
        Serial.println(F("Setting ap mode")); //Need to be removed..
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
    data =readESP();
    /* REST ESP ON UNKNOWN STATE */
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
    /* REST ESP ON UNKNOWN STATE */
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

/* Interrupt Service Routine for legacy timer one, ESP reset check routine */
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

/* Interrupt initialization code for above ISR */ // Depricated!
// void initializeInterrupt(){
//   pinMode(13,1);
//   Timer1.initialize(10000000); //Heart Beat ( ESP refresh signal.. )
//   Timer1.attachInterrupt(CUSTOM_ISR); 
// }

void setup() {
  loadFromEEPROM();
  for(int i=2;i<11;i++){
    pinMode(i,1);
  }
  if(DEBUG){
  Serial.begin(BAUD); //Serial to debugger 0,1
  }
  ESP.begin(ESPBAUD); //Serial to ESP mainly 11,12
  Serial.setTimeout(10); // Set string read timeout, without this readString is slow
  ESP.setTimeout(100);   // Reduce rx tx timeout, we dont have decades to wait.
  initializeESP();
  Serial.println(F("Welcome to SSAL IoT Core"));
  for(int i=0;i<11;i++){
    digitalWrite(i,pins[i]);
  }
  Serial.print(F("freeMemory()="));
  Serial.println(freeMemory());  // Print free memory ocationally  // Can interrupt
  initializeWire();
  setupWatchDog();
}
long time=millis();
void loop() {
  //ISR is now replaced by millis watchdog
  if((millis()-time)>5000){ // Run timer every 5 seconds
    CUSTOM_ISR();  //Call legacy ISR
    time=millis(); //Reset virtual timer 
  }
  if(checkESP){
      /* ESP RESET/SERVER CHECK */
    writeESP(F("AT+CIPMUX?\r\n"));  //Check MUX status, for server to run it should be 1
    delay(300);
    String data;
    data.reserve(100);
    data=readESP();
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
        //Wire.onRequest(sendData);
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
//                         if(dataESP.startsWith(F("writeAP="))){
//                             //Syntax wiriteAP=ssid,pass no quotes needed
//                             dataESP.remove(0,dataESP.indexOf(F("="))+1);
//                             String SSID = dataESP.substring(0,dataESP.indexOf(F(",")));
//                             String PASS = dataESP.substring(dataESP.indexOf(F(","))+1);
//                             Serial.print(F("SSID ="));
//                             writeESSID_EEPROM(SSID);
//                             Serial.println(readESSID_EEPROM());
//                             Serial.print(F("PASS ="));
//                             writePass_EEPROM(PASS);
//                             Serial.println(readPass_EEPROM());
//                         }
                    }
                }
            }
            writeESP(F("AT+RST\r\n"));
            delay(300);
            readESP();
        }
    }
  }
  passThrough();                         //Pass data from one serial to another
  if(!dataSerial.equals("")){            //Data from serial is available
    dataSerial="";
  }
  if(!dataESP.equals("")){               //Data from ESP available
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
        digitalWrite(pin,operation); // do operation
        pins[pin]=operation;// update internal DB
        dumpToEEPROM();
        String pinString;
        pinString.reserve(10);
        pinString=String(pin);
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
        
        //SendData - Because ardunio dont like functions
        String pinString;
        pinString.reserve(10);
        pinString=String(pins[pin]);
        String temp="AT+CIPSEND=";//<link ID>,<length>
        temp.reserve(50);
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
        /* There can be only one active connection */
        String pinString=F("Not allowed!");
        //SendData 
          String temp;
          temp.reserve(50);
          temp="AT+CIPSEND="; //<link ID>,<length>
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
    wdt_reset();
    Wire.onRequest(sendData); //Get data from I2C
    Wire.onReceive(receiveData);
  }
}
