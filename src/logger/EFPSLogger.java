package logger;


import CommandHandlers.MainCMDHandler;
import jarvisReborn.Core;

public class EFPSLogger{
	static int mcuCount = Core.configParse.data.size();
	public static Thread loggerThread[] = new Thread[mcuCount];
	private static int i;
	
	public EFPSLogger() {
		this.createThreads();
	}
	
	public void createThreads() {
		System.out.println("EFPS: createThreads function called");
		for(i=0;i<Core.configParse.data.size();i++) {
			System.out.println("EFPS: loop iterations 1");
			loggerThread[i] = new Thread() {
				String message = "$sensors "+i;
				String output = "";
				public void run() {
					while(true) {
						MainCMDHandler mainCMDHandler = new MainCMDHandler(message, null);
						if(mainCMDHandler.parsed) {
							output = mainCMDHandler.output;
							System.out.println("EFPS: OUTPUT "+output);
							if(mainCMDHandler.error==true) {
								mainCMDHandler = new MainCMDHandler("$reset "+i, null);
								output = mainCMDHandler.output;
								mainCMDHandler = new MainCMDHandler(message, null);
								output = mainCMDHandler.output;
								if(mainCMDHandler.error==true) {
									System.out.println("EFPS: Giving up on "+i);
									break;
								}
							}
						}
						else {
							System.out.println("EFPS: Parsing command "+message+" Failed");
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("EFPS: Thread for "+i+" diying..");

				}
			};
			loggerThread[i].start();
		}
		
	}
	
	
}
