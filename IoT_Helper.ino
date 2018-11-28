//CopyLeft (c) 2018 BainNet Technologies
//CopyLeft (c) 2018 Abhiram Shibu
//CopyLeft (c) 2018 ARCTotal
#include <LiquidCrystal.h>
#include<Arduino.h>
#include<Wire.h>
#include<MemoryFree.h>
#define DEBUG true
#define VERSION 1.1
#define BAUD 115200

//LCD declarations
#define rs 12
#define en 11
#define d4 5
#define d5 6
#define d6 7
#define d7 8

// using namespace std;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
String main_message;
long time;
int count=0;
int count_wait=0;
float temp=0;           // Temperature temp varable
float final_temp=0;     // Final Temperature is stored here
String connection;
void readTemp(){
	temp=0;
	lcd.cursor();
	long vcc = 1100;
	for(int i=0;i<1000;i++){
		temp+=(analogRead(A0)*(vcc/1000))/1024.0;
	}
	temp/=10;
	final_temp=temp;
}
void assembleMessage(){
    main_message.reserve(100);
	main_message=F("SSAL System UI  Status:");
	main_message+=F("Passive");
	main_message+=F(", Connection:");
	main_message+=connection;
	main_message+=F(", IoT Core:");
	main_message+=F("Active");
	main_message+=F(", Temp:");
	main_message+=String(temp)+F("C");
	main_message+=F(" ");
}
void update(){
	if((millis()-time)>300){ //Scroll and set if update time exceeds..
		time=millis();
		assembleMessage();
		String temp_message;
        temp_message.reserve(100);
		temp_message=String(main_message);
		String t=temp_message.substring(0,count);
		temp_message.remove(0,count);
		temp_message=temp_message+t;
		lcd.setCursor(0,0);
		lcd.print(temp_message.substring(0,16));
		if(count==0&&count_wait<10){
			count_wait++;
		}
		else{
			count_wait=0;
			count++;
			if(count==main_message.length()){
				count=0;
			}
			//Serial.println(main_message);
		}
	}
	lcd.setCursor(0,1);
	readTemp();
	lcd.noCursor();
	lcd.print(processData()+"  ");
}
void initialize(){
	if(DEBUG){
		Serial.begin(BAUD);
		Serial.println(F("Initallizing"));
	}
	pinMode(A0,INPUT);
	lcd.begin(16,2);
	assembleMessage();
	lcd.print(main_message.substring(0,16));
	time=millis();
	analogReference(INTERNAL);
}
String pinData;
// void sendData(){
// 	Wire.write("1");
// 	char t[20];
// 	String send=pinData+String(int(final_temp*100));
// 	send.toCharArray(t,send.length());
// 	Wire.write(t);
// 	//String(int(temp*100)).toCharArray(t,String(int(temp*100)).length());
// 	Wire.write(t);
// }
String wireData;
void wireBody(){
    Wire.beginTransmission(8);  
    int error = Wire.endTransmission();
    if(error==0){
        Wire.requestFrom(8,11);
        wireData=F("");
        wireData.reserve(20);
        int wait=0;
        while(Wire.available()<1 && wait<100){
            wait++;
    // 		Serial.print(".");
        };
        if(Wire.available()>1){
    // 		Serial.println();
            char recived=char(Wire.read());
            wireData+=recived;
            //Serial.println(recived);
            if(recived='1'){
                while(Wire.available()>1){
                    int read=Wire.read();
                    wireData+=char(read);
                    //Serial.print(char(read));
                    connection.reserve(10);
                    if(read==255){
                        connection=F("Failed");
                        break;
                    }
                    else{
                        connection=F("Connected");
                    }
                }
                //Serial.println();
     			Serial.println(wireData);
            }
        }
	}
	else{
        connection=F("Failed");
    }
    Wire.endTransmission();
    Wire.beginTransmission(8); 
    Wire.write("1");
	char t[20];
	String send=pinData+String(int(final_temp*100));
	send.toCharArray(t,send.length());
	Wire.write(t);
	//String(int(temp*100)).toCharArray(t,String(int(temp*100)).length());
	Wire.write(t);
    Serial.print("Ending transmission:");
    Serial.println(Wire.endTransmission());    // stop transmitting
}
int pinID=2;
long timeProcessData=millis();
String processData(){
	wireData.remove(0,1);
	pinData=String(wireData);
    pinData.reserve(20);
	int pinMode=wireData.substring(0+pinID-2,1+pinID-2).toInt();
	String pinModeStr;
    pinModeStr.reserve(4);
	if(pinMode==1){
		pinModeStr=F("ON");
	}
	else if(pinMode==0){
		pinModeStr=F("OFF");
	}
	String output;
	output.reserve(15);
	output=F("Pin ");
	output+=String(pinID);
	output+=F(" is ");
	output+=pinModeStr;
	if((millis()-timeProcessData)>1000){
		pinID++;
		if(pinID==11){
			pinID=2;
		}
		timeProcessData=millis();
	}
	return output;
}
void setup(){
	initialize();
	Wire.begin(8);
}
// String editMessage="";
long timeWire=millis();
void loop(){
    Serial.println(freeMemory());
	update();
	if((millis()-timeWire)>100){
		wireBody();
		timeWire=millis();
	}
	Serial.println(freeMemory());
	
}
