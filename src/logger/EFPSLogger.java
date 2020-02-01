package logger;


import CommandHandlers.MainCMDHandler;
import jarvisReborn.Core;
import jarvisReborn.Specification;

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
				int id=i;
				String message = "$sensors "+i;
				String output = "";
				public void run() {
					while(true) {
						MainCMDHandler mainCMDHandler = new MainCMDHandler(message, null);
						if(mainCMDHandler.parsed) {
							output = mainCMDHandler.output;
							//System.out.println("EFPS: OUTPUT "+output);
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
							else {
								try {
									int space = output.indexOf(" ");
									float sensor1 = Float.parseFloat(output.substring(0,space));
									float sensor2 = Float.parseFloat(output.substring(space+1));
									System.out.println("EFPS: MCU "+id+" Sensor1 "+sensor1+" Sensor2 "+sensor2);
									Core.dbClient.insert((id*2+0)+"", id, 0, sensor1);
									Core.dbClient.insert((id*2+1)+"", id, 1, sensor2);
								}
								catch( Exception e){
									e.printStackTrace();
								}
							}
						}
						else {
							System.out.println("EFPS: Parsing command "+message+" Failed");
						}
						try {
							Thread.sleep(Specification.EFPSLoggerInterval);
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
