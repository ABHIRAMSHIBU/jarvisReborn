package dbHandlers;

import CommandHandlers.MainCMDHandler;
import jarvisReborn.Core;
import jarvisReborn.Specification;

public class dbInit extends Thread{
	int mcu=-1;
	int retry=Specification.FETCH_RETRY_COUNT;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		if(mcu==-1) {
			System.out.println("dbInit:Error occured uninitalized mcu!");
		}
		else {
			//Core.pinData[mcu]
			for(int i=1;i<=10;i++) {
				MainCMDHandler cmd = new MainCMDHandler("$test "+i+" "+mcu, null);
				if(cmd.output=="No input available") {
					System.out.println("dbInit : Retry on mcu:"+mcu+" pin:"+i);
					if(retry==0) {
						System.out.println("dbInit : Gave up on mcu:"+mcu+" pin:"+i);
						retry=Specification.FETCH_RETRY_COUNT;
					}
					else {
						i--;
						retry--;
					}
				}
				else {
					retry=Specification.FETCH_RETRY_COUNT;
					if(cmd.output.charAt(0)=='1') {
						Core.pinData[mcu][i-1]=true;
					}
					else {
						Core.pinData[mcu][i-1]=false;
					}
				}
			}
			for(int i=0;i<10;i++) {
				System.out.println("dbInit : MCU "+mcu+" Pin "+(i+1)+" "+Core.pinData[mcu][i]);
			}
			System.out.println("dbInit : Finished for MCU "+mcu);
		}
	}
	public dbInit(int id) {
		// TODO Auto-generated constructor stub
		this.mcu=id;
	}
}
