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
					MainCMDHandler mainCMDHandler = new MainCMDHandler(message, null);
					if(mainCMDHandler.parsed) {
						output = mainCMDHandler.output;
						System.out.println("EFPS: OUTPUT "+output);
					}
					else {
						System.out.println("EFPS: Parsing command "+message+" Failed");
					}

				}
			};
			loggerThread[i].start();
		}
		
	}
	
	
}
