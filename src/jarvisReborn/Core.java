package jarvisReborn;

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
		telnet = new Telnet[50];        //Supports 50 clients
		telnetThread = new Thread() {
			public void run() {
				telnet[0] = new Telnet("192.168.43.6",23);
			}
		};
		telnetThread.start();
		new TelnetServer(8000);
	}
}
