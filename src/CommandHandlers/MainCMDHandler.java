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

import java.util.List;
import jarvisReborn.Specification;

import javax.swing.JTextArea;
import org.jfree.ui.RefineryUtilities;

import Config.ConfigParse;
import Sockets.EFPSPredictorHandler;
import Sockets.Telnet;
import jarvisReborn.Core;
import jarvisReborn.Specification;
import jarvisReborn.PlotCurrentGUI;
import jarvisReborn.SensorParser;

public class MainCMDHandler {
	
	
	
	public boolean parsed=false;
	public String output=""; 
	public boolean error=false;
	public SensorParser sensorParser;
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
		else if(input.contains("$getFailure")) {
			parseGetFailure(input.substring(input.indexOf(" ")+1));
		}
		else if(input.contains("$get")) {
			parseGET(input.substring(input.indexOf(" ")+1));
		}
		else if(input.contains("$sensors")) {
			parseSensors(input.substring(input.indexOf(" ")+1));
			//System.out.println(input.substring(input.indexOf(" ")+1));
		}
		else if(input.contains("$plot")) {
			try {
				System.out.println("MainCMDHandler: Starting Plot");
				plotSensors(input.substring(input.indexOf(" ")+1));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		else if(input.contains("$checkdos")) {
			parseCheckDos(input.substring(input.indexOf(" ")+1));
		}
	}
	private void parseCheckDos(String input) {
		parsed=true;
		int mcu=Integer.valueOf(input);
		try {
			synchronized (Core.telnet[mcu]) {
				output=Core.telnet[mcu].echo("hwinfo\r");
				if(output.equals("No input available")) {
					Core.telnet[mcu].checkTelnet(0);
					output=Core.telnet[mcu].echo("hwinfo\r");
				}
			}
		}
		catch(Exception e){
			output="Error contacting ESP";
			error=true;
		}
		if(!Core.telnet[mcu].run) {
			output="Error contacting ESP";
			error=true;
		}
	}
	private void parseGetFailure(String input) {
		int space1=input.indexOf(" ");
		int sensorid=Integer.valueOf(input.substring(0, space1)), 
			roomid=Integer.valueOf(input.substring(space1+1));
		try {
			//System.out.println(Core.efpsPredictorDispatcher); //will get null cuz of some issue noted in EFPSPredictorDispatcher.java
			EFPSPredictorHandler efpsPredictorHandler=Core.efpsPredictorDispatcher.activeThread.get(4*roomid+sensorid);
			output=efpsPredictorHandler.data;
		}
		catch (NullPointerException e) {
			output="Python backend not yet connected!";
			e.printStackTrace();
		}
		parsed=true;
	}
	private void plotSensors(String substring) {
		parsed=true;
		String[] substringCuts = substring.split("\\s+");
		if(substringCuts.length!=2) {
			output = "MainCMDHandler: PlotCurrent:Internal error ( Unknown format )";
			
		}
		else {
			output = "Plot will be displayed in a new window "+substringCuts[0]+" "+substringCuts[1];
			Thread t = new Thread(new Runnable() {
				public void run() {
					Specification.plotInput=substring;
					PlotCurrentGUI chart = new PlotCurrentGUI("Sensor Plot "+substringCuts[0]+" "+substringCuts[1], "MCU "+substringCuts[1]+" Sensor "+substringCuts[0]);
					
					chart.pack( );
					RefineryUtilities.centerFrameOnScreen( chart );
					chart.setVisible(true);
					System.out.println("Set Visible True");
					chart.update();
					while(chart.userCloseButtonClick==false) {
				    	  try {
							Thread.sleep(Specification.plotRefreshInterval);
						      chart.update();				
						  } 
				    	  catch (InterruptedException e) {
							e.printStackTrace();
						  }
				    }
			   }
			}
			);
			t.start();
		}
	}
	public void parseSensors(String input) {
		parsed=true;
		int mcu=Integer.valueOf(input);
		try {
			synchronized (Core.telnet[mcu]) {
				output=Core.telnet[mcu].echo("sensor"+"\r");
				if(output.equals("No input available")) {
					error=true;
					Core.telnet[mcu].checkTelnet(0);
					output=Core.telnet[mcu].echo("sensor"+"\r");
				}
			}
		}
		catch(Exception e){
			output="Error contacting ESP";
			error=true;
		}
		if(!Core.telnet[mcu].run) {
			output="Error contacting ESP";
			error=true;
		}
		if(error!=true) {
			sensorParser=new SensorParser(output);
			output=sensorParser.toString();
		}
	}
	public void parseTEST(String input) {
		parsed=true;
		int space1=input.indexOf(" ");
		int pin=Integer.valueOf(input.substring(0, space1)), 
				mcu=Integer.valueOf(input.substring(space1+1));
		try {
			synchronized (Core.telnet[mcu]) {
				output=Core.telnet[mcu].echo(pin+"\r");
				if(output.equals("No input available")) {
					Core.telnet[mcu].checkTelnet(0);
					output=Core.telnet[mcu].echo(pin+"\r");
				}
			}
		}
		catch(Exception e){
			output="Error contacting ESP";
			error=true;
		}
		if(!Core.telnet[mcu].run) {
			output="Error contacting ESP";
			error=true;
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
					Core.efpsLogger.i=id;
					Core.efpsLogger.createThread();
					output="Reset Success!";
				}
			}
		}
		catch(Exception e){
			output="Reset Failure!";
			error=true;
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
				output=Core.telnet[mcu].echo(pin+" "+operation+"\r");
				if(!output.equals("No input available")) {
					if(pin-1<=10 && pin-1>0) {
						Core.pinData[mcu][pin-1]=(operation==1);
					}
				}
				else {
					Core.telnet[mcu].checkTelnet(1);
					output=Core.telnet[mcu].echo(pin+" "+operation+"\r");
					if(!output.equals("No input available")) {
						if(pin-1<=10 && pin-1>0) {
							Core.pinData[mcu][pin-1]=(operation==1);
						}
					}
				}
			}
		}
		catch(Exception e) {
			output="Error contacting ESP";
		}
	}
	public void parseGET(String input) {
		parsed=true;
		int space1=input.indexOf(" ");
		int pin=Integer.valueOf(input.substring(0, space1)), 
				mcu=Integer.valueOf(input.substring(space1+1));
		if(pin-1<=10 && pin-1>=0) {
			output=Core.pinData[mcu][pin-1].toString();
		}
		else {
			output="Out of range";
			error=true;
		}
		if(!Core.telnet[mcu].run) {
			output="Error contacting ESP";
			error=true;
		}
	}
	
}
