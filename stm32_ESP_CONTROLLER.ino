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
//***** Warning pin status is the opposite of what is told by software *****//
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
/*--ADC Sensor Map ------*/
/*-----------------------*
|        PA0->0          |
|        PA1->1          |
|        PB0->2          |
|        PB1->3          |
*------------------------*

---End ADC Sensor Map    */
//LCD VARIABLES
    //count[0], count
    //count[1], count_wait
    short count[2]={0,0};
    short pinNow=0;
    //time1[0], time1 // LCD Scroll timer line 1
    //time1[1], time2 // LCD Scroll timer line 2
    long time1[2]={millis(),millis()};
    bool LCD_AVAIL;
    bool LCD_UPDATE_ENABLE=true;
    LiquidCrystal_I2C *lcd;
//END LCD VARIABLES
//SENSOR VARIABLES
    short sensor_buffer[10][4];
    short sensor_buffer_front=-1;
    short sensor_buffer_back=0;
    long last_sample=millis();            //Timer to sample
//END SENSOR VARIABLES
//ESP VARIABLES    
bool checkESP=true;
// counter[0], counter
// counter[1], counter_disconnect
short counter[2]={0,0};
//END ESP VARIABLES
//HEART BEAT VARIABLE
bool led=False;
long time=millis();          //Heart beat timer
//END HEART BEAT VARIABLE
//DOS VARIABLES
bool DOS=true;
uint8_t relaycfg[4];
//END DOS VARIABLES
bool pins[16];    // Pin stats are stored here
float temp;
/** EEPROM BEGIN **/


/** Relay Map BEGIN **/
short relayMaptoInt(uint8_t input){
    uint8_t out=1;
    if(input==0b00000000){
        return 0;
    }
    short count=0;
    for(uint8_t i=1;i<8;i++){
        count++;
        out=pow(2,i)+1;
        while((out & 0b10000000)!=0b10000000){
            if(out==input){
                return count;
            }
            count++;
            out=out<<1;
        }
        if(out==input){
            return count;
        }
    }
    return -1;
}
uint8_t inttoRelayMap(short input){
    uint8_t out=1;
    if(input==0){
        return 0b00000000;
    }
    short count=0;
    for(uint8_t i=1;i<8;i++){
        count++;
        out=pow(2,i)+1;
        while((out & 0b10000000)!=0b10000000){
            if(count==input){
                return out;
            }
            count++;
            out=out<<1;
        }
        if(count==input){
            return out;
        }
    }
    return 0b11111111;
}

//Needs to be appened some missing code
void relayConfLoadEEPROM(){
    uint32_t temp = EEPROM.read(1) | EEPROM.read(2) << 8 | EEPROM.read(3) << 16;
    short r1 = temp & 31;
    short r2 = (temp & 992) >> 5;
    short r3 = (temp & 31744) >> 10;
    short r4 = (temp & 1015808) >> 15;
    relaycfg[0]=inttoRelayMap(r1);
    relaycfg[1]=inttoRelayMap(r2);
    relaycfg[2]=inttoRelayMap(r3);
    relaycfg[3]=inttoRelayMap(r4);
    Serial.println(F("EEPROM_RELAYCONFL"));
    Serial.println(relaycfg[0]);
    Serial.println(relaycfg[1]);
    Serial.println(relaycfg[2]);
    Serial.println(relaycfg[3]);
    //std::cout<<std::bitset<8>(inttoRelayMap(r1))<<std::endl;
    //std::cout<<std::bitset<8>(inttoRelayMap(r2))<<std::endl;
    //std::cout<<std::bitset<8>(inttoRelayMap(r3))<<std::endl;
    //std::cout<<std::bitset<8>(inttoRelayMap(r4))<<std::endl;
}
void relayConfDumpEEPROM(){
    uint8_t out[3];
    short r1=relayMaptoInt(relaycfg[0]),r2=relayMaptoInt(relaycfg[1]),r3=relayMaptoInt(relaycfg[2]),r4=relayMaptoInt(relaycfg[3]);
    // std::cout<<r1<<" "<<unsigned(relayconf.r2)<<std::endl;
    uint32_t temp=r1 | r2<<5 | r3 << 10 | r4 <<15;
    Serial.println(temp,BIN);
    Serial.println();
    // std::cout<<std::bitset<32>(temp)<<std::endl;
    out[0]= 255 & temp;
    out[1]= (65280 & temp)>>8;
    out[2]= (16711680 & temp)>>16;
    //std::cout<<std::bitset<8>(out[0])<<std::endl;
    //std::cout<<std::bitset<8>(out[1])<<std::endl;
    //std::cout<<std::bitset<8>(out[2])<<std::endl;
    //relayConfLoadEEPROM(out);
    EEPROM.write(1,out[0]);
    EEPROM.write(2,out[1]);
    EEPROM.write(3,out[2]);
    Serial.println(F("EEPROM_RELAYCONFD"));
    Serial.println(relaycfg[0]);
    Serial.println(relaycfg[1]);
    Serial.println(relaycfg[2]);
    Serial.println(relaycfg[3]);
    Serial.println();
    Serial.println(out[0]);
    Serial.println(out[1]);
    Serial.println(out[2]);
}

bool relayConfDecode(uint8_t relay,short * pin1, short * pin2){
  //Bigger pin number turns off and on a device (master switch)
  //Smaller pin number switches grid
  short count=0,pos[2]={0,0},j=0;
  while(relay!=0){
    if((relay&1) == 1){
      if(j==3){
        return 0;
      }
      pos[j]=count;
      j++;
    }
    relay=relay>>1;
    count++;
  }
  *(pin1)=2+pos[0];
  *(pin2)=2+pos[1];
  return 1;
}
/** END RelayMap **/

bool PROGMEM EEPROMFormat=False;
void EEPROMinit(){
    //Old value 0x800F000, 0x800F400
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
    float amps=(voltage); 
    return amps;
}
String sendSensorBuffer(){
  //send
  String senseData="";
  for(short i=0;i<10;i++){
    short offset=(sensor_buffer_front+i)%10;
    for(short j=0;j<4;j++){
      senseData+= sensor_buffer[offset][j];
      if(j!=3){
        senseData+=", ";
      }
    }
    if(i!=9){
      senseData+="\t";
    }
  }
  return senseData;
}
void insertSensorBuffer(short a, short b, short c, short d){
  //insert
  if(sensor_buffer_front==-1){
    //first insert
    sensor_buffer_front=0;
    sensor_buffer[0][0]=a;
    sensor_buffer[0][1]=b;
    sensor_buffer[0][2]=c;
    sensor_buffer[0][3]=d;
    sensor_buffer_back+=1;
  }
  else{
   //Normal
   if(sensor_buffer_front==sensor_buffer_back){
      sensor_buffer_front+=1;
      sensor_buffer_front=sensor_buffer_front%10;
   }
   sensor_buffer[sensor_buffer_back][0]=a;
   sensor_buffer[sensor_buffer_back][1]=b;
   sensor_buffer[sensor_buffer_back][2]=c;
   sensor_buffer[sensor_buffer_back][3]=d;
   sensor_buffer_back+=1;
   sensor_buffer_back=sensor_buffer_back%10;
  }
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
        temp+=" is off";
    }
    else{
        temp+=" is on";
    }
    if((millis()-time1[1])>500){
        time1[1]=millis();
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
	if((millis()-time1[0])>300){ //Scroll and set if update time exceeds..
		time1[0]=millis();
		String main_message=assembleMessage();
		String temp_message;
    temp_message.reserve(100);
		temp_message=String(main_message);
		String t=temp_message.substring(0,count[0]);
		temp_message.remove(0,count[0]);
		temp_message=temp_message+t;
		lcd->setCursor(0,0);
		lcd->print(temp_message.substring(0,16));
		if(count[0]==0&&count[1]<10){
			count[1]++;
		}
		else{
			count[1]=0;
			count[0]++;
			if(count[0]==main_message.length()){
				count[0]=0;
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
        counter[0]++;
        if(counter[0]==3){
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
        counter[0]++;
        if(counter[0]==3){
          writeESP(F("AT+RST\r\n"));
          serialClear();
        }
        goto mux;
      }
      else{
        Serial.print(F("Link Active"));
      }
    }
    counter[0]=0;
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
void setpin(short pin,bool operation){
  pinMode(pinmap(pin),OUTPUT);
  digitalWrite(pinmap(pin),operation);
  pins[pin]=operation;
  dumpToEEPROM();
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
    pinMode(PB0,INPUT);
    pinMode(PB1,INPUT);
}
/* END pin initialization */


void setup() {
  initWire();
  initLCD();
  EEPROMinit();
  loadFromEEPROM();
  relayConfLoadEEPROM();
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
        counter[1]++;
        if(counter[1]==3){
            counter[1]=0;
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
            short pin=dataESP.substring(0,space).toInt();
            bool operation=dataESP.substring(space,dataESP.length()-2).toInt();
            operation=!operation;
            sensorFlag=false;
            setpin(pin,operation);
            String pinString;
            pinString.reserve(10);
            pinString=dataESP.substring(0,dataESP.length()-2);
            sendData(id,pinString);
        }
        else{
        /* Here pin status is retrived and send to the SSAL Core */
          if(dataESP.indexOf(F("sensor"))!=-1){
            sensorFlag=true;
          }
          else if(dataESP.indexOf(F("hwinfo"))!=-1){
            String pinString;
            pinString.reserve(5);
            pinString=String(DOS);
            sensorFlag=false;
            sendData(id,pinString);
          }
          else if(dataESP.indexOf(F("relaycfg"))!=-1){
            //relaycfg code
            //relaycfg relayNo paircode
            String pinString;
            String data = dataESP.substring(0,dataESP.length()-2);
            int space1 = data.indexOf(" ");
            int space2 = data.indexOf(" ",space1+1);
            if(space1==-1 || space2==-1){
              if(space1!=-1){
                int relayNo=data.substring(space1+1).toInt();
                pinString=String(relaycfg[relayNo-1]);
              }
              else{
                pinString=F("Error");
              }
            }
            else{
              int relayNo=data.substring(space1+1,space2).toInt();
              uint8_t value=data.substring(space2+1).toInt();
              if(relayNo>0 && relayNo<5){
                short cmpval=relayMaptoInt(value);
                if(cmpval>=0 && cmpval<=28){
                  relaycfg[relayNo-1]=value;
                  Serial.print(F("Relay "));
                  Serial.print(relayNo);
                  Serial.print(F(" "));
                  Serial.println(value);
                  relayConfDumpEEPROM();
                  pinString=F("OK");
                }
                else{
                  pinString=F("Error value");
                }
              }
              else{
                pinString=F("Error relayNo");
              }
            }
            sendData(id,pinString);
          }
          else if(dataESP.indexOf(F("getrelay"))!=-1){
              String pinString;
              String data = dataESP.substring(0,dataESP.length()-2);
              int space1 = data.indexOf(" ");
              if(space1!=-1){
                short relayNo = data.substring(space1).toInt();
                short pin1,pin2;
                relayConfDecode(relaycfg[relayNo-1],&pin1,&pin2);
                // if(value==0){
                //     setpin(pin1,1);
                //     setpin(pin2,1);     
                //   }
                //   else if(value==1){
                //     setpin(pin1,0);
                //     setpin(pin2,1);
                //   }
                //   else if(value==2){
                //     setpin(pin1,0);
                //     setpin(pin2,0);
                //   }
                short value=-1;
                if(pins[pin1]==1 && pins[pin2]==1){
                  value = 0;
                }
                else if(pins[pin1]==0 && pins[pin2]==1){
                  value = 1;
                }
                else if(pins[pin1]==0 && pins[pin2]==0){
                  value = 2;
                }
                pinString += String(value);
              }
              else{
                pinString=F("Error");
              }
              sendData(id,pinString);
          }
          else if(dataESP.indexOf(F("setrelay"))!=-1){
            /*  setrelay relaynumber mode
                ------modes------
                0-> OFF
                1-> ON GRID
                2-> ON INV
                -----------------
            */
            String pinString;
            String data = dataESP.substring(0,dataESP.length()-2);
            int space1 = data.indexOf(" ");
            int space2 = data.indexOf(" ",space1+1);
            if(space1==-1 || space2==-1){
              pinString=F("Error");
            }
            else{
              short relayNo=data.substring(space1+1,space2).toInt();
              uint8_t value=data.substring(space2+1).toInt();
              if(relayNo>0 && relayNo<5){
                short pin1,pin2;
                if(relayConfDecode(relaycfg[relayNo-1],&pin1,&pin2)){
                  pinString="OK";
                  if(value==0){
                    setpin(pin1,1);
                    setpin(pin2,1);     
                  }
                  else if(value==1){
                    setpin(pin1,0);
                    setpin(pin2,1);
                  }
                  else if(value==2){
                    setpin(pin1,0);
                    setpin(pin2,0);
                  }
                  else{
                    pinString=F("Error value");
                  }
                }
                else{
                  pinString=F("Error conf");
                }
              }
              else{
                pinString=F("Error relayNo");
              }
            }
            sendData(id,pinString);
          }
          else{
            //pin status
            int pin=dataESP.substring(0,dataESP.length()-2).toInt(); 
            String pinString;
            pinString.reserve(10);
            pinString=String(!pins[pin]);
            sensorFlag=false;
            sendData(id,pinString);
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
        String senseData=sendSensorBuffer();
        sendData(0,senseData);
    }
    dataESP="";       //ESP data never got logged wink wink
  }
  if((millis()-last_sample)>=100){
    short a = readCurrent(PA0)*1000; //Current is voltage in millivolts.
    short b = readCurrent(PA1)*1000;
    short c = readCurrent(PB0)*1000;
    short d = readCurrent(PB1)*1000;
    insertSensorBuffer(a,b,c,d);
    last_sample=millis();
  }
}
