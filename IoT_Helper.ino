//CopyLeft (c) 2018 BainNet Technologies
//CopyLeft (c) 2018 Abhiram Shibu
//CopyLeft (c) 2018 ARCTotal
#include <LiquidCrystal.h>
#include<Arduino.h>
#include<Wire.h>
#define DEBUG true
#define VERSION 1.0
#define BAUD 115200
using namespace std;
const int rs = 12, en = 11, d4 = 8, d5 = 7, d6 = 6, d7 = 5;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
String main_message;
long time;
int count=0;
int count_wait=0;
float temp=0;
float final_temp=0;
String connection="Failed";
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
		String temp_message=String(main_message);
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
void sendData(){
	Wire.write("1");
	char t[20];
	String send=pinData+String(int(final_temp*100));
	send.toCharArray(t,send.length());
	Wire.write(t);
	//String(int(temp*100)).toCharArray(t,String(int(temp*100)).length());
	Wire.write(t);
}
String wireData;
void wireBody(){
	Wire.requestFrom(7,11);
	wireData="";
	int wait=0;
	while(Wire.available()<1 && wait<100){
		wait++;
		Serial.print(".");
	};
	if(Wire.available()>1){
		Serial.println();
		char recived=char(Wire.read());
		wireData+=recived;
		//Serial.println(recived);
		if(recived='1'){
			while(Wire.available()>1){
				int read=Wire.read();
				wireData+=char(read);
				//Serial.print(char(read));
				if(read==255){
					connection="Failed";
					break;
				}
				else{
					connection="Connected";
				}
			}
			//Serial.println();
			Serial.println(wireData);
		}
	}
}
int pinID=2;
long timeProcessData=millis();
String processData(){
	wireData.remove(0,1);
	pinData=String(wireData);
	int pinMode=wireData.substring(0+pinID-2,1+pinID-2).toInt();
	String pinModeStr;
	if(pinMode==1){
		pinModeStr="ON";
	}
	else if(pinMode==0){
		pinModeStr="OFF";
	}
	String output="Pin "+String(pinID)+" is "+pinModeStr;
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
String editMessage="";
long timeWire=millis();
void loop(){
	update();
	Wire.onRequest(sendData);
	if((millis()-timeWire)>100){
		wireBody();
		timeWire=millis();
	}
}
