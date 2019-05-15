/*
Copyleft (C) 2018  ARCtotal
Copyleft (C) 2018  Abhiram Shibu

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package CommandHandlers;

import javax.print.attribute.standard.PrinterMessageFromOperator;
import javax.swing.JTextArea;

import Config.ConfigParse;
import Sockets.Telnet;
import jarvisReborn.Core;

public class MainCMDHandler {
	public boolean parsed=false;
	public String output=""; 
	JTextArea ta;
	public MainCMDHandler(String input,JTextArea ta) {
		// TODO Auto-generated constructor stub
		this.ta=ta;
		if(input.contains("$set")) {
			parseSET(input.substring(input.indexOf(" ")+1));
		}
		else if(input.contains("$test")) {
			parseTEST(input.substring(input.indexOf(" ")+1));
		}
		else if(input.contains("$reset")) {
			parseRESET(input.substring(input.indexOf(" ")+1));
		}
		else if(input.contains("$get")) {
			parseGET(input.substring(input.indexOf(" ")+1));
		}
	}
	public void parseTEST(String input) {
		parsed=true;
		int space1=input.indexOf(" ");
		int pin=Integer.valueOf(input.substring(0, space1)), 
				mcu=Integer.valueOf(input.substring(space1+1));
		try {
			synchronized (Core.telnet[mcu]) {
				if(Core.telnet[mcu].checkTelnet(0)) {
					output=Core.telnet[mcu].echo(pin+"\r");
				}
				else {
					output="Telnet faild to reconnect, giving up!";
				}
			}
		}
		catch(Exception e){
			output="Error contacting ESP";
		}
		if(!Core.telnet[mcu].run) {
			output="Error contacting ESP";
		}
	}
	public void parseRESET(String input) {
		parsed=true;
		int mcu=Integer.valueOf(input);
		try {
			ConfigParse configParse = new ConfigParse();
			for (int i=0;i<configParse.data.size();i++) {
				int id=configParse.data.get(i).id;
				String ip=configParse.data.get(i).ip;
				if(id==mcu) {
					System.out.println("Closing old connection for id:"+id);
					try {
						Core.telnet[configParse.data.get(i).id].close();
					}
					catch (Exception e) {
						
					}
					System.out.println("Starting telnet for id:"+id+" ip:"+ip);
					Core.telnet[configParse.data.get(i).id] = new Telnet(configParse.data.get(i).ip,23);
					System.out.println("Reset Success!");
					output="Reset Success!";
				}
			}
		}
		catch(Exception e){
			output="Reset Failure!";
		}
		
	}
	public void parseSET(String input) {
		// 13 0 0
		int space1=input.indexOf(" "),
			space2=input.indexOf(" ",space1+1);
		int pin=Integer.valueOf(input.substring(0, space1)), 
			operation=Integer.valueOf(input.substring(space1+1,space2)),
			mcu=Integer.valueOf(input.substring(space2+1));
		//System.out.println("Pin is:"+pin+" operation is:"+operation+" mcu is:"+mcu);
		parsed=true;
		try {
			synchronized (Core.telnet[mcu]) {
				if(Core.telnet[mcu].checkTelnet(0)) {
					output=Core.telnet[mcu].echo(pin+" "+operation+"\r");
					if(output!="No input available") {
						if(pin-1<=10 && pin-1>0) {
							Core.pinData[mcu][pin-1]=(operation==1);
						}
					}
				}
				else {
					output="Telnet faild to reconnect, giving up!";
				}
			}
		}
		catch(Exception e) {
			output="Error contacting ESP";
		}
		//Im gonna make that text view bigger
	}
	public void parseGET(String input) {
		parsed=true;
		int space1=input.indexOf(" ");
		int pin=Integer.valueOf(input.substring(0, space1)), 
				mcu=Integer.valueOf(input.substring(space1+1));
		if(pin-1<=10 && pin-1>0) {
			output=Core.pinData[mcu][pin-1].toString();
		}
		else {
			output="Out of range";
		}
		if(!Core.telnet[mcu].run) {
			output="Error contacting ESP";
		}
	}
}
