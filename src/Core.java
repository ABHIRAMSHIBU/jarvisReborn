
public class Core {
	public static void main(String[] args) {
		new Core();
	}	
	Core(){
		//Creating gui thread
		Thread gui = new Thread() {
			public void run() {
				new GUI();
			}
		};
		//Creating bot thread
		Thread bot = new Thread() {
			public void run() {
				new TelegramBot();
			}
		};
		/* Start both threads */
		gui.start();
		bot.start();
	}
}