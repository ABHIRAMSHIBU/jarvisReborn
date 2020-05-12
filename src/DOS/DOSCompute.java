package DOS;

import java.util.ArrayList;

import CommandHandlers.MainCMDHandler;
import Config.DOSConfigParser;
import jarvisReborn.Core;
import jarvisReborn.Specification;

public class DOSCompute {
	int mcuIterator=0;
	int deviceIterator=0;
	int oldmcuIterator=0;
	int olddeviceIterator=0;
	int error_now=0;
	int addload(ArrayList<DOSmcu> mcus) {
		System.out.println("DOSCompute.java: called addload()");
		while(true) {
			DOSmcu mcu = null ;
			try {
				mcu= mcus.get(mcuIterator);
			}
			catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println("DOSCompute.java: IndexOutOfBoundException detecte, reset mcuIterator");
				mcuIterator=0;
			}
			if(mcu.relays[deviceIterator][0]!=0) {
				break;
			}
			else {
				deviceIterator++;
				if(deviceIterator==4) {
					mcuIterator++;
					mcuIterator%=mcus.size();
					deviceIterator=0;
				}
			}
			
		}
		System.out.println("DOSCompute.java: found mcu,device:"+mcus.get(mcuIterator).id+","+(deviceIterator+1));
		if(mcus.get(mcuIterator).relays[deviceIterator][1]==1) { // Device On Grid, now switch it to secondary supply.
			MainCMDHandler handler = new MainCMDHandler("$setrelay "+(deviceIterator+1)+" "+2+" "+mcus.get(mcuIterator).id, null);
			System.out.println("DOSCompute.java: setrelay try1 output:"+handler.output);
			if(!handler.output.contains("OK")) {
				handler = new MainCMDHandler("$setrelay "+(deviceIterator+1)+" "+2+" "+mcus.get(mcuIterator).id, null);
				if(handler.output.contains("OK")) {
					mcus.get(mcuIterator).relays[deviceIterator][1]=2;
					olddeviceIterator=deviceIterator;
					oldmcuIterator=mcuIterator;
				}
				else {
					return -1;
				}
			}
			else {
				mcus.get(mcuIterator).relays[deviceIterator][1]=2;
			}
		}
		deviceIterator++;
		if(deviceIterator==4) {
			mcuIterator++;
			mcuIterator%=mcus.size();
			deviceIterator=0;
		}
		return 0;
	}
	int removeload(ArrayList<DOSmcu> mcus) {
		System.out.println("DOSCompute.java: called removeload()");
		while(true) {
			DOSmcu mcu = null ;
			try {
				mcu= mcus.get(mcuIterator);
			}
			catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				System.out.println("DOSCompute.java: IndexOutOfBoundException detecte, reset mcuIterator");
				mcuIterator=0;
			}
			if(mcu.relays[deviceIterator][0]!=0) {
				break;
			}
			else {
				deviceIterator++;
				if(deviceIterator==4) {
					mcuIterator++;
					mcuIterator%=mcus.size();
					deviceIterator=0;
				}
			}
			
		}
		System.out.println("DOSCompute.java: found mcu,device:"+mcus.get(mcuIterator).id+","+(deviceIterator+1));
		if(mcus.get(mcuIterator).relays[deviceIterator][1]==2) { // Device On Grid, now switch it to secondary supply.
			MainCMDHandler handler = new MainCMDHandler("$setrelay "+(deviceIterator+1)+" "+1+" "+mcus.get(mcuIterator).id, null);
			System.out.println("DOSCompute.java: setrelay try1 output:"+handler.output);
			if(!handler.output.contains("OK")) {
				handler = new MainCMDHandler("$setrelay "+(deviceIterator+1)+" "+1+" "+mcus.get(mcuIterator).id, null);
				if(handler.output.contains("OK")) {
					mcus.get(mcuIterator).relays[deviceIterator][1]=1;
				}
				else {
					return -1;
				}
			}
			else {
				mcus.get(mcuIterator).relays[deviceIterator][1]=1;
			}
		}
		deviceIterator++;
		if(deviceIterator==4) {
			mcuIterator++;
			mcuIterator%=mcus.size();
			deviceIterator=0;
		}
		return 0;
	}
	//ToDO
	/*
	 * 1. Take all mcu list and find dos capable hardware 
	 * 2. Read configuration variable from Core 
	 * 3. Get the DOS relay status Initialization
	 * 4. get data from solar and inverter using mainCMDHandler
	 * 5. Implement algorithm
	 * -----ALGORITHM------
	 * if status = OFF , don't turn it on 
	 * set slack thresholds using some method
	 * 
	 * 
	 * */
	public ArrayList<int[]> getSolarPanelOutputSensorData(DOSConfigParser dosConfigParser) {
		ArrayList<int[]> returnData=null;
		if(dosConfigParser.dositem.outputDeviceID==dosConfigParser.dositem.outputSensorID && dosConfigParser.dositem.outputSensorID==dosConfigParser.dositem.batteryVoltageDeviceID) {
			MainCMDHandler handler = new MainCMDHandler("$sensors "+dosConfigParser.dositem.outputDeviceID, null);
			int arr[][];
			returnData = new ArrayList<int[]>();
			int outputSensorData[]=new int[Specification.sensorBufferLength];
			int solarPanelSensorData[]=new int[Specification.sensorBufferLength];
			int batteryVoltageData[]=new int[Specification.sensorBufferLength];
			try {
				arr=handler.sensorParser.getArray();
			}
			catch (NullPointerException e){
				System.out.println("DOSCompute.java: Source error, device with id="+dosConfigParser.dositem.outputSensorID);
				System.out.println("DOSCompute.java: Will retry in next 1 sec");
				error_now++;
				if(error_now==Specification.FETCH_RETRY_COUNT) {
					System.out.println("DOSCompute.java: Trying to reset mcu");
					handler= new MainCMDHandler("$reset "+dosConfigParser.dositem.outputDeviceID, null);
					if(handler.output=="Reset Success!") {
						System.out.println("DOSCompute.java: Reset Failure");
					}
					error_now=0;
				}
				return null;
			}
			for(int i=0;i<Specification.sensorBufferLength;i++) {
				outputSensorData[i]=arr[i][dosConfigParser.dositem.outputSensorID];
				solarPanelSensorData[i]=arr[i][dosConfigParser.dositem.solarPanelSensorID];
				batteryVoltageData[i]=arr[i][dosConfigParser.dositem.batteryVoltageSensorID];
			}
			returnData.add(solarPanelSensorData);
			returnData.add(outputSensorData);
			returnData.add(batteryVoltageData);
		}
		return returnData;
	}
	void displayDB(ArrayList<DOSmcu> mcus) {
		for( int i=0;i<mcus.size();i++) {
			System.out.println("DOSCompute: "+"MCU id - "+mcus.get(i).id);
			for( int j=0;j<4;j++) {
				System.out.println("DOSCompute: "+"Config "+j+"- "+mcus.get(i).relays[j][0]);
				System.out.println("DOSCompute: "+"Value "+j+"- "+mcus.get(i).relays[j][1]);
			}
		}
	}
	public DOSCompute() {

		DOSmcu mcu;
		ArrayList<DOSmcu> mcus = Core.mcus;
		DOSConfigParser dosConfigParser = Core.dosConfigParser;
		
		
		
		while(true) {
			try {
				ArrayList<int[]> sensorData = getSolarPanelOutputSensorData(dosConfigParser);
				if(sensorData==null) {
					try {
						Thread.sleep(1000);
					}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
				error_now=0; // probably working so.. yeah
				int solarmean=0;
				int outmean=0;
				int batteryVoltMean=0;
				for(int i=0;i<Specification.sensorBufferLength;i++) {
					solarmean+=sensorData.get(0)[i];
					outmean+=sensorData.get(1)[i];
					batteryVoltMean+=sensorData.get(2)[i];
				}
				solarmean/=Specification.sensorBufferLength;
				outmean/=Specification.sensorBufferLength;
				batteryVoltMean/=Specification.sensorBufferLength;
				solarmean=Math.abs(1650-solarmean+0);
				outmean=Math.abs(outmean-1650-0);
				batteryVoltMean=Math.abs(batteryVoltMean-1650)*(3);
				System.out.println("DOSCompute.java: "+"solarmean :"+solarmean+" outmean:"+outmean+" battVoltMean:"+batteryVoltMean);
				//displayDB(mcus);
				//TODO add debug messages
				if(batteryVoltMean>2600 && batteryVoltMean <3800 ) {
					System.out.println("DOSCompute.java: 1");
					if((solarmean-outmean)<100 || (solarmean-outmean)>200) {
						removeload(mcus);
						System.out.println("DOSCompute.java: 2");
					}
					else if((solarmean-outmean)>150) {
						addload(mcus);
						System.out.println("DOSCompute.java: 3");
					}
				}
				else if(batteryVoltMean>3800 && batteryVoltMean <4100 ) {
					System.out.println("DOSCompute.java: 4");
					if(Math.abs(solarmean-outmean)>=60) {
						System.out.println("DOSCompute.java: 5");
						//0.05 0.44 v=ir v=i*0.44 v=0.05*0.44 0.022V*1000 = 22
						//Now we need to balance the load
						if(solarmean>outmean) {
							System.out.println("DOSCompute.java: 6");
							//We need to remove load
							int ret=addload(mcus);
							System.out.println("DOSCompute.java: "+"addload return:"+ret);
						}
						else {
							//We need to add load
							System.out.println("DOSCompute.java: 7");
							removeload(mcus);
						}
					}
				}
				else if(batteryVoltMean>=4100) {
					System.out.println("DOSCompute.java: 8");
					if((outmean-solarmean)<100) {
						System.out.println("DOSCompute.java: 9");
						addload(mcus);
					}
					else if((outmean-solarmean)>150) {
						removeload(mcus);
						System.out.println("DOSCompute.java: 10");
					}
				}
				else {
					removeload(mcus); //I suspect race condition here
					System.out.println("DOSCompute.java: 11");
				}
				
			}
			catch(Exception e){
				System.out.println("DOSCompute: Error, Exception occured");
				e.printStackTrace();
			}
			//Do some stuff with the accured sensorData..
			//Caclulating mean
			try {
				Thread.sleep(1000);
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

