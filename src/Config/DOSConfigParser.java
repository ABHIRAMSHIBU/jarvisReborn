package Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DOSConfigParser {
	public boolean errors=false;
	public DOSItem dositem;
	public DOSConfigParser() {
		dositem = new DOSItem();
		File dir = new File(System.getProperty("user.home")+"/SSAL");
		File file = new File(System.getProperty("user.home")+"/SSAL/dos.conf");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		if(!file.exists()) {
			System.out.println("Config file not found "
								+System.getProperty("user.home")
								+"/SSAL/dos.conf");
		}
		else {
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				int i=0;
				String line ;
				while(true) {
					line = bufferedReader.readLine();
					if(line==null) {
						break;
					}
					else if(line.contains("#")) {
						i++;
						continue; //Handle comment.
					}
					try {
						String[] words = line.split("\\s+");
						//System.out.println("DOSConfigParser.java: line,"+line+" words.length,"+words.length);
						if(words[0].toLowerCase().equals("solar")) {
							dositem.solarPanelSensorID = Integer.parseInt(words[1]);
							dositem.solarPanelDeviceID = Integer.parseInt(words[2]);
							System.out.println("DOSConfigParser.java: solarSensor:"+
												dositem.solarPanelSensorID+
												" solarDevice:"+
												dositem.solarPanelDeviceID);
						}
						else if(words[0].toLowerCase().equals("output")) {
							dositem.outputSensorID = Integer.parseInt(words[1]);
							dositem.outputDeviceID = Integer.parseInt(words[2]);
							System.out.println("DOSConfigParser.java: outputSensor:"+
												dositem.outputSensorID+
												" outputDevice:"+
												dositem.outputDeviceID);
						}
						else if(words[0].toLowerCase().equals("battvolt")) {
							dositem.batteryVoltageSensorID = Integer.parseInt(words[1]);
							dositem.batteryVoltageDeviceID = Integer.parseInt(words[2]);
							System.out.println("DOSConfigParser.java: BattVoltSensor:"+
												dositem.outputSensorID+
												" BattVoltDevID:"+
												dositem.outputDeviceID);
						}
						else {
							System.out.println("DOSConfigParser.java: Error Invalid Purpose at line "+i);
							errors=true;
						}
						i++;
					}
					catch(Exception e){
						System.out.println("DOSConfigParser.java: Error parsing config at line "+i);
						//e.printStackTrace();
						errors=true;
					}
					i++;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
