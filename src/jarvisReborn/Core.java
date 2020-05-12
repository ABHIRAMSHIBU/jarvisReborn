package jarvisReborn;
/*
Copyleft (C) 2020  TUXForums
Copyleft (C) 2020  Abhiram Shibu

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

import dbHandlers.DOSdbInit;
import dbHandlers.InfluxDBClient;
import dbHandlers.dbInit;
import logger.EFPSLogger;
import timeSeries.PythonEFPS;
import java.util.ArrayList;
import javax.swing.UIManager;
import Config.ConfigParse;
import Config.DOSConfigParser;
import DOS.DOSCompute;
import DOS.DOSmcu;
import Sockets.EFPSPredictorDispatcher;
import Sockets.Telnet;
import Sockets.TelnetServer;

public class Core {
	static Thread tele;
	public static EFPSPredictorDispatcher python;
	public static Thread telnetThread;
	public static Telnet telnet[];
	public static Boolean pinData[][];
	public static Boolean dosdb[];                      // DOS DB ( if hardware is dos capable or not)
	public static InfluxDBClient dbClient;
	public static PythonEFPS pythonEFPS ;
	public static ConfigParse configParse ;
	public static DOSConfigParser dosConfigParser;
	public static EFPSLogger efpsLogger;
	public static Thread efpsPredictor;
	public static EFPSPredictorDispatcher efpsPredictorDispatcher;
	public static DOSCompute dosCompute;
	public static ArrayList<DOSmcu> mcus;              // DOS relay config data
	public static dbInit db[];                         // PINS DB
	public static DOSdbInit dosdbInit;
	public static void main(String[] args) {
		System.out.println("SSAL version 1.1, Copyleft (C) 2020 Abhiram Shibu\n" + 
				"SSAL comes with ABSOLUTELY NO WARRANTY; for details\n" + 
				"This is free software, and you are welcome\n" + 
				"to redistribute it under certain conditions;");
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		new Core();
	}
	Core(){
		GUI gui = new GUI();
		/* Java based telegram bot is depricated  */
//		Thread telegram = new Thread() {
//			public void run() {
//				new SSALTeleInit(gui);
//			}
//		};
//		telegram.start();
//		tele=telegram;
		/** 
		 * Config Section
		 * File in /home/username/SSAL/ssal.conf 
		 * Please write this file with 
		 * ID IP PINS.
		 * Should be seperated with single space.
		 */
		configParse = new ConfigParse();
		dosConfigParser = new DOSConfigParser();
		efpsPredictor = new Thread() {
    		public void run() {
    			Core.efpsPredictorDispatcher=new EFPSPredictorDispatcher(9999);
    		}
    	};
    	efpsPredictor.start();
    	System.out.println("Python server has started");
    	pinData=new Boolean[50][10];
    	dosdb=new Boolean[50];
    	mcus = new ArrayList<DOSmcu>();
		telnet = new Telnet[50];        //Supports 50 clients
		db=new dbInit[50];
		for (int i=0;i<configParse.data.size();i++) {
			int id=configParse.data.get(i).id;
			String ip=configParse.data.get(i).ip;
			System.out.println("Starting telnet for id:"+id+" ip:"+ip);
			telnet[configParse.data.get(i).id] = new Telnet(configParse.data.get(i).ip,23);
			db[configParse.data.get(i).id]=new dbInit(configParse.data.get(i).id);
			db[configParse.data.get(i).id].start();
		}
		Thread telnetServerThread = new Thread() {
			public void run() {
				new TelnetServer(9998);
			}
		};
		telnetServerThread.start();
		
		
		for (int i=0;i<50;i++) {
			
			try {
				db[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullPointerException e) {}
		}
		System.out.println("Core.java: Before dosdbInit");
		dosdbInit = new DOSdbInit();
		System.out.println("Core.java: After dosdbInit");

		dbClient = new InfluxDBClient();
		dbClient.connect();
		System.out.println("Core: influxdb active");
		//pythonEFPS = new PythonEFPS(1,0);
		//Predictor predictor = new Predictor();
		System.out.println("Core: python EFPS Pipe active");
		efpsLogger = new EFPSLogger();
		System.out.println("Core: python EFPS Logger active");
		dosCompute = new DOSCompute();
		System.out.println("Core: SSAL System Active!");
		System.out.println("Core: Bye..");
	}
}
