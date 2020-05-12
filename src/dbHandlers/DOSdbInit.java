package dbHandlers;

import CommandHandlers.MainCMDHandler;
import DOS.DOSmcu;
import jarvisReborn.Core;

public class DOSdbInit {
	public void reinitialize(int mcu_id) {
		int index=-1;
		for(int i=0;i<Core.mcus.size();i++) {
			if(Core.mcus.get(i).id==mcu_id) {
				index=i;
			}
		}
		if(index==-1) {
			return;
		}
		DOSmcu mcu=Core.mcus.get(index);
		if(Core.dosdb[mcu_id]) {
			//append to DOS controllable list
			for(int j=0;j<4;j++) {
				MainCMDHandler handler = new MainCMDHandler("$relaycfg "+(j+1)+" "+mcu.id, null);
				//System.out.println("DOSCompute: Debug j+1:"+(j+1)+" mcu_id:"+mcu.id);
				String output=handler.output;
				handler = new MainCMDHandler("$testrelay "+(j+1)+" "+mcu.id, null);
				String output1=handler.output;
				try {
					int val = Integer.valueOf(output);
					mcu.relays[j][0]=val;
					int val1 = Integer.valueOf(output1);
					//System.out.println("DOSCompute: Debug relay configuration, relay id:"+(j+1)+" config:"+val+" set value:"+val1);
					mcu.relays[j][1]=val1;
				}
				catch(Exception e){
					System.out.println("DOSCompute: Error, Exception occured");
					e.printStackTrace();
				}
			}
			Core.mcus.set(index, mcu);
		}
		// Hopefully this function works...
	}
	public DOSdbInit() {
		DOSmcu mcu;
		for(int i=0;i<Core.configParse.data.size();i++) {
			System.out.println("DOSdbInit: Debug Core.configParse.data.get(i).id:"+Core.configParse.data.get(i).id);
			if(Core.dosdb[Core.configParse.data.get(i).id]) {
				//append to DOS controllable list
				mcu=new DOSmcu();
				mcu.id=Core.configParse.data.get(i).id;
				for(int j=0;j<4;j++) {
					MainCMDHandler handler = new MainCMDHandler("$relaycfg "+(j+1)+" "+mcu.id, null);
					//System.out.println("DOSCompute: Debug j+1:"+(j+1)+" mcu_id:"+mcu.id);
					String output=handler.output;
					handler = new MainCMDHandler("$testrelay "+(j+1)+" "+mcu.id, null);
					String output1=handler.output;
					try {
						int val = Integer.valueOf(output);
						mcu.relays[j][0]=val;
						int val1 = Integer.valueOf(output1);
						//System.out.println("DOSCompute: Debug relay configuration, relay id:"+(j+1)+" config:"+val+" set value:"+val1);
						mcu.relays[j][1]=val1;
					}
					catch(Exception e){
						System.out.println("DOSCompute: Error, Exception occured");
						e.printStackTrace();
					}
				}
				Core.mcus.add(mcu);
			}
		}
		System.out.println("DOSdbInit.java: Initialization finished");
	}
}
