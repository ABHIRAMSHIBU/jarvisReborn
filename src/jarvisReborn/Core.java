package jarvisReborn;

import tg.SSALTeleInit;

public class Core {
	static Thread tele;
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
	}
}
