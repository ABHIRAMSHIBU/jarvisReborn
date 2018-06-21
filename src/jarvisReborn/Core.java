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
		System.out.println("SSAL version 1, Copyright (C) 2018 Abhiram Shibu\n" + 
				"SSAL comes with ABSOLUTELY NO WARRANTY; for details\n" + 
				"This is free software, and you are welcome\n" + 
				"to redistribute it under certain conditions;");
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
