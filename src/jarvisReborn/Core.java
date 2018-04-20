package jarvisReborn;

import Config.ConfigParse;
import Sockets.Telnet;
import Sockets.TelnetServer;
import tg.SSALTeleInit;

public class Core {
	static Thread tele;
	public static Thread telnetThread;
	public static Telnet telnet[];
	public static void main(String[] args) {
		new Core();
	}
	Core(){
		GUI gui = new GUI();
		Thread telegram = new Thread() {
			public void run() {
				new SSALTeleInit(gui);
			}
		};
		telegram.start();
		tele=telegram;
		/** 
		 * Config Section
		 * File in /home/username/SSAL/ssal.conf 
		 * Please write this file with 
		 * ID IP PINS.
		 * Should be seperated with single space.
		 */
		ConfigParse configParse = new ConfigParse();
		telnet = new Telnet[50];        //Supports 50 clients
		for (int i=0;i<configParse.data.size();i++) {
			int id=configParse.data.get(i).id;
			String ip=configParse.data.get(i).ip;
			System.out.println("Starting telnet for id:"+id+" ip:"+ip);
			telnet[configParse.data.get(i).id] = new Telnet(configParse.data.get(i).ip,23);
		}
		new TelnetServer(8000);
	}
}
