//Project SSAL IoT Core
//Project SSAL IoT Helper [Merge]
/* Author Abhiram Shibu, Preet Patel
 * Author Abhijith N Raj (Bug Fixes)
 * Author Harikrishnan (Code Optimization)
 * Copyright (c) TeamDestroyer Projects 2018
 * Copyright (c) 2020 SSAL
 * Copyright (c) 2020 BrainNet Technlogies
 * Copyright (c) 2020 TUXFourm
 * For queries goto https://tuxforum.com
 */
//Includes
#include<string.h>
/* STM32 */
// #include<SoftwareSerial.h>
#include<EEPROM.h>
/* STM32 */
#include<Wire_slave.h>
#include <LiquidCrystal_I2C.h>

/* STM32 */
// #include<avr/wdt.h>

//Convience Definitions
#define True 1
#define False 0
#define ESPTX 11
#define ESPRX 12
#define ESPBAUD 250000
#define BAUD 2000000
#define DEBUG true
#define VERSION 2.2 // Version from Arduino code
#define INSPECT 09022020
#define PageBaseZero 0x801F800 //PageBase0
#define PageBaseOne 0x801FC00  //PageBase1
#define ESPWriteDelay 20

/*------Pin Maps---------
*-----------------------*
|        0 -> PB2       |
|        1 -> PB2       |
|        2 -> PB3       |
|        3 -> PB4       |
|        4 -> PB5       |
|        5 -> PB10      |
|        6 -> PB11      |
|        7 -> PB8       |
|        8 -> PB9       |
|        9 -> PC14      |
|        10 -> PC15     |
|        11 -> PB2      |
|        12 -> PB2      |
|        13 -> PC13     |
*-----------------------*
---End pinMaps DOC ------*/

//LCD VARIABLES
    int count=0;
    int count_wait=0;
    int pinNow=0;
    long time1=millis();     //LCD Scroll timer line 1
    long time2=millis();     //LCD Scroll timer line 2
    int LCD_AVAIL;
    int LCD_UPDATE_ENABLE=true;
    LiquidCrystal_I2C *lcd;
//END LCD VARIABLES
//ESP VARIABLES    
bool checkESP=true;
int counter=0;
int counter_disconnect=0;
//END ESP VARIABLES
//HEART BEAT VARIABLE
bool led=False;
long time=millis();          //Heart beat timer
//END HEART BEAT VARIABLE
bool pins[16];    // Pin stats are stored here
float temp;
/** EEPROM BEGIN **/
bool PROGMEM EEPROMFormat=False;
void EEPROMinit(){
    EEPROM.PageBase0=0x800F000;
    EEPROM.PageBase1=0x800F400;
    if(EEPROMFormat==True){
        EEPROM.format();
        EEPROMFormat=False;
    }
}
byte encodeByteArray(bool * pin,int offset){
  byte val=0;
  for(int i=0;i<16;i++){
      val|=pin[i+offset]<<i;
  }
  return val;
}
void decodeIntAndRestore(bool * pin,byte val,int offset){
  byte temp;
  for(int i=0;i<16;i++){
    temp=val&(1<<i);
    if(temp==(1<<i)){
      pin[i+offset]=1;
    }
    else{
      pin[i+offset]=0;
    }
  }
  Serial.print("Restore:");
  for(int i=0;i<16;i++){
  Serial.print(pin[i]);    
 }
 Serial.println("");
}

void dumpToEEPROM(){
    byte val=encodeByteArray(pins,2);
    EEPROM.write(0,val);
    Serial.print(F("Write location:"));
}
void loadFromEEPROM(){
    byte val;
    int loc=0;
    val=EEPROM.read(loc);
    decodeIntAndRestore(pins,val,2);
}
/** END EEPROM **/
/* Sensor Handlers */
float readCurrent(int analogInput){
    int sensorVal=analogRead(analogInput);
    float voltage= (sensorVal*3.3)/4096; //STM32 3.3 v reference.
    float amps=(1.65-voltage)/0.07;
    return amps;
}
/* END Sensor */
/* Serial Communication Handlers */
String dataESP,dataSerial;
String readSerial(){          //Data from serial Save and return
  dataSerial=Serial.readString();
  dataSerial.reserve(20);
  return dataSerial;
}
String readESP(){            //Data from ESP save and return
  /* STM32 */
  dataESP=Serial1.readString();
  dataESP.reserve(100);
  return dataESP;
}

//Wire Stuff
void initWire(){
    Wire.begin();
    Wire.beginTransmission(0x27);
    int error = Wire.endTransmission();
    if(error ==0){
        LCD_AVAIL=true;
        lcd=new LiquidCrystal_I2C(0x27,16,2);
    }
    else{
        LCD_AVAIL=false;
    }
}
void initLCD(){
    Serial.println("Starting LCD");
    lcd->begin();
    lcd->print("Welcome to SSAL");
    lcd->setCursor(0,1);
    lcd->print("Loading...");
}
String pinData(){
    String temp;
    temp.reserve(20);
    temp=String(pinNow);
    if(pins[pinNow]==true){
        temp+=" is on";
    }
    else{
        temp+=" is off";
    }
    if((millis()-time2)>500){
        time2=millis();
        if(pinNow++==13){
            pinNow=0;
        }
        return temp;
    }
}
String assembleMessage(){
  String MSG;
	MSG=F("SSAL System UI  Status:");
	MSG+=F("Active");
	MSG+=F(", IoT Core:");
	MSG+=F("Active");
//MSG+=F(", Temp:");
//MSG+=String(temp)+F("C");
	MSG+=F(" ");
  return MSG;
}
void updateLCD(){
    if(LCD_UPDATE_ENABLE==false){
        return; // no need to update
    }
	if((millis()-time1)>300){ //Scroll and set if update time exceeds..
		time1=millis();
		String main_message=assembleMessage();
		String temp_message;
    temp_message.reserve(100);
		temp_message=String(main_message);
		String t=temp_message.substring(0,count);
		temp_message.remove(0,count);
		temp_message=temp_message+t;
		lcd->setCursor(0,0);
		lcd->print(temp_message.substring(0,16));
		if(count==0&&count_wait<10){
			count_wait++;
		}
		else{
			count_wait=0;
			count++;
			if(count==main_message.length()){
				count=0;
			}
		}
	}
	lcd->setCursor(0,1);
	lcd->print(pinData()+"  ");
}
// END WIRE STUFF

void writeESP(String data){           //String to ESP
  /* STM32 */
  Serial1.print(data);
}
void writeSerial(String data){        //String to Serial
  Serial.print(data);
}
void passThrough(){                    // Serial -> ESP, ESP -> Serial
  if(Serial.available()){
    writeESP(readSerial());
  }
  /* STM32 */
  if(Serial1.available()){
    writeSerial(readESP());
  }
}
void serialClear(){                     // Clear extra data
  /* STM32 */
  Serial1.flush();
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
    delay(ESPWriteDelay);
    String data;
    data.reserve(100);
    data=readESP();
    if(data.indexOf("CWMODE:1")==-1){
        writeESP(F("AT+CWMODE=1\r\n"));
        Serial.println(F("Setting ap mode")); //Need to be removed..
        delay(ESPWriteDelay);
        Serial.println(readESP());
        count++;
        if(count==10){
            writeESP(F("AT+RST\r\n"));
            Serial.println(F("Reset ESP"));
            delay(ESPWriteDelay);
            Serial.println(readESP());
            count=0;
        }
        goto mux;
    }
    writeESP(F("AT+CIPMUX=1\r\n"));   //Set mux to 1 thereby allowing multi connection
    delay(ESPWriteDelay);
    data =readESP();
    /* REST ESP ON UNKNOWN STATE */
    if(data.indexOf(F("ERROR"))>-1){
      if(data.indexOf(F("builded"))>-1){
        counter++;
        if(counter==3){
          writeESP(F("AT+RST\r\n"));
          serialClear();
        }
        goto mux;
      }
    }
    writeESP(F("AT+CIPSERVER=1,23\r\n"));  //Start server
    delay(ESPWriteDelay);
    Serial.print(F("Data:"));
    data =readESP();
    Serial.println(data);
    /* REST ESP ON UNKNOWN STATE */
    if(data.indexOf(F("ERROR"))>-1){
      if(data.indexOf(F("builded"))>-1){
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
/* ESP sendData function for automatically doing all sending procedure */
void sendData(int id, String pinString){
    String temp="AT+CIPSEND=";//<link ID>,<length>
    temp.concat(id);
    temp.concat(F(","));
    temp.concat(pinString.length()+2);
    temp.concat(F("\r\n"));
    writeESP(temp);
    delay(ESPWriteDelay);
    String inputData=readESP();
    if(inputData.indexOf(F("OK"))>-1){
        pinString.concat(F("\r\n"));
        writeESP(pinString);
    }
    delay(ESPWriteDelay);
    readESP();
}
/* Interrupt Service Routine for legacy timer one, ESP reset check routine */
void CUSTOM_ISR(void){
  if(led){
    led=False;
    digitalWrite(pinmap(13),led);
  }
  else{
    led=True;
    digitalWrite(pinmap(13),led);
  }
  checkESP=true;
}

/* pinmap definition */
/* 
 * Standard Map for pins from stm32 to arduino
 * PA0  0
 * PA1  1
 * PA2  2
 * PA3  3
 * PA4  4
 * PA5  5
 * PA6  6
 * PA7  7
 * PA8  8
 * PA9  9
 * PA10 10
 * PA11 11
 * PA12 12
 * PA13 13
 * PA14 14
 * PA15 15
 * PB0  16
 * PB1  17
 * PB2  18
 * PB3  19
 * PB4  20
 * PB5  21
 * PB6  22
 * PB7  23
 * PB8  24  
 * PB9  25
 * PB10 26
 * PB11 27
 * PB12 28
 * PB13 29
 * PB14 30
 * PB15 31
 * PC0  32
 * PC1  33
 * PC2  34
 * PC3  35 
 * PC4  36
 * PC5  37
 * PC6  38
 * PC7  39
 * PC8  40
 * PC9  41
 * PC10 42
 * PC11 43
 * PC12 44
 * PC13 45
 * PC14 46
 * PC15 47
 */
int pinmap(int x){
    short pin[14]; // No std cpp libs in arduino, implementing map from scratch
    pin[0]=PB2;  // PB2 Boot 1 jumper.. Too much in need for pins.
    pin[1]=PB2;  // PB2
    pin[2]=PB3;  // PB3
    pin[3]=PB4;  // PB4
    pin[4]=PB5;  // PB5
    pin[5]=PB10;  // PB6
    pin[6]=PB11;  // PB7
    pin[7]=PB8;  // PB8
    pin[8]=PB9;  // PB9
    pin[9]=PB14; // PC14
    pin[10]=PC15;// PC15
    pin[11]=PB2; // PB2 
    pin[12]=PB2; // PB2
    pin[13]=PC13; // PC13 // LED
    // PB10 and PB11 is used for i2c
    return pin[x];
}
/* END pinmap */

/* pin initialization */
void pinInit(){
    for(int i=0;i<=13;i++){
        pinMode(pinmap(i),OUTPUT);
    }
    for(int i=0;i<=13;i++){
        if(pins[i]==1){
            digitalWrite(pinmap(i),HIGH);
        }
        else{
            digitalWrite(pinmap(i),LOW);
        }
    }
    pinMode(PA0,INPUT);
    pinMode(PA1,INPUT);
}
/* END pin initialization */


void setup() {
  initWire();
  initLCD();
  EEPROMinit();
  loadFromEEPROM();
  pinInit();
  if(DEBUG){
    Serial.begin(BAUD); //Serial to debugger 0,1
  }
  /* STM32 */
  Serial1.begin(ESPBAUD); //Serial to ESP mainly 11,12
  Serial.setTimeout(10); // Set string read timeout, without this readString is slow
  Serial1.setTimeout(10);   // Reduce rx tx timeout, we dont have decades to wait.
  initializeESP();
  Serial.println(F("Welcome to SSAL IoT Core"));
}
void loop() {
  updateLCD();
  //ISR is now replaced by millis watchdog
  if((millis()-time)>5000){ // Run timer every 5 seconds
    CUSTOM_ISR();  //Call legacy ISR
    time=millis(); //Reset virtual timer 
    LCD_UPDATE_ENABLE=true;
  }
  if(checkESP){
    Serial.println("CheckESP");
      /* ESP RESET/SERVER CHECK */
    writeESP(F("AT+CIPMUX?\r\n"));  //Check MUX status, for server to run it should be 1
    delay(ESPWriteDelay);
    String data;
    data.reserve(100);
    data=readESP();
    if(data.indexOf(F("+CIPMUX:1"))==-1){  // not 1 make it 1
      initializeESP();                  //Reinitiallize there by making mux 1
      Serial.println(F("Re-initallized ESP"));
    }
    dataESP="";
    checkESP=false;
    
    /* ESP CONNECTION CHECK */
    writeESP(F("AT+CIPSTATUS\r\n"));
    delay(ESPWriteDelay);
    data = readESP();
    Serial.print(F("Hotspot data:"));
    Serial.println(data);
    if(data.indexOf(F(":5"))>-1){
        counter_disconnect++;
        if(counter_disconnect==3){
            counter_disconnect=0;
            writeESP(F("AT+CWMODE_CUR=2\r\n"));
            delay(ESPWriteDelay);
            readESP();
            writeESP(F("AT+CWDHCP_CUR=0,1\r\n"));
            delay(ESPWriteDelay);
            readESP();
            Serial.println(F("Hotspot initallized!"));
            long time=millis();
            while((millis()-time)<60000){
              //Wait in hotspot mode
            }
            writeESP(F("AT+RST\r\n"));
            delay(ESPWriteDelay);
            readESP();
        }
    }
  }
  passThrough();                         //Pass data from one serial to another
  dataSerial="";                         //Clear serial data
  if(!dataESP.equals("")){               //Data from ESP available
    bool sensorFlag=false;
    if(dataESP.indexOf(F("CONNECT"))>-1){   //Check if there is a connection and absorb next line
      delay(ESPWriteDelay);                        //Wait for some time
      readESP();
    }
    //Serial.println("After ESTABLISHING Connection");
    if(dataESP.indexOf(F("+IPD,"))>-1){     //Check if data from SSAL System is available
      time=millis(); //Reset virtual timer of ISR
      LCD_UPDATE_ENABLE=false; //Stop LCD update, give priority to calls
      dataESP.remove(0,dataESP.indexOf(",")+1); //Remove header
      int id=dataESP.substring(0,1).toInt();  // Get id
      dataESP.remove(0,dataESP.indexOf(":")+1);  //Remove id
      if(id==0){                   // Allow only id 0
        int dataESPLength=dataESP.length();
        if(dataESPLength>=5 && dataESPLength<=6){
            /* Find space split into pin and operation, then compile and send */
            int space = dataESP.indexOf(F(" "));
            int pin=dataESP.substring(0,space).toInt();
            bool operation=dataESP.substring(space,dataESP.length()-2).toInt();
            pinMode(pinmap(pin),OUTPUT);
            sensorFlag=false;
            digitalWrite(pinmap(pin),operation); // do operation
            pins[pin]=operation;// update internal DB
            dumpToEEPROM();
            String pinString;
            pinString.reserve(10);
            pinString=dataESP.substring(0,dataESP.length()-2);
            sendData(id,pinString);
        }
        else{
        /* Here pin status is retrived and send to the SSAL Core */
            if(dataESP.indexOf(F("sensor"))==-1){
                int pin=dataESP.substring(0,dataESP.length()-2).toInt(); 
                String pinString;
                pinString.reserve(10);
                pinString=String(pins[pin]);
                sensorFlag=false;
                sendData(id,pinString);
            }
            else{
                sensorFlag=true;
            }
        }
      }
      else{
        /* There can be only one active connection */
        String pinString=F("Not allowed!");
        sensorFlag=false;
        String temp;
        sendData(id,pinString);
      }
    }
    writeESP(F("AT+CIPSTATUS\r\n"));
    delay(ESPWriteDelay);
    String data = readESP();
    if(data.indexOf(F(":3"))>-1 && sensorFlag){
        //Write Sensor Data
        String senseData=String(readCurrent(PA0));
        senseData+="\t";
        senseData+=String(readCurrent(PA1));
        senseData+="\n";
        String temp;
        sendData(0,senseData);
    }
    dataESP="";       //ESP data never got logged wink wink
  }
}
