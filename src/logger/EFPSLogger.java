package logger;
/* Author Abhijith N Raj
 * Copyleft 2020 
 * Project SSAL
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU Generalsudi Public License
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

import CommandHandlers.MainCMDHandler;
import jarvisReborn.Core;
import jarvisReborn.Specification;

public class EFPSLogger{
	static int mcuCount = 50;
	public static Thread loggerThread[] = new Thread[mcuCount];
	public static int i;
	
	public EFPSLogger() {
		i=0;
		this.createThreads();
	}
	
	public void createThreads() {
		System.out.println("EFPS: createThreads function called");
		for(i=0;i<Core.configParse.data.size();i++) {
			int temp=i;
			i=Core.configParse.data.get(i).id;
			createThread();
			i=temp;
		}
		
	}
	public void createThread() {
		try {
			if(Core.dosdb[i]) { //If DOS then dont create thread
				System.out.println("EFPSLogger: Refusing to start on DOS MCU "+i);
				return;
			}
		}
		catch(NullPointerException e) {
			System.out.println("EFPSLogger: ID:"+i+" Not found in config");
			return;
		}
		loggerThread[i] = new Thread() {
			int id=i;
			String message = "$sensors "+i;
			String output = "";
			int arr[][];
			public void run() {
				int retryCount=0;
				while(true) {
					MainCMDHandler mainCMDHandler = new MainCMDHandler(message, null);
					if(mainCMDHandler.parsed) {
						output = mainCMDHandler.output;
						try {
							arr=mainCMDHandler.sensorParser.getArray();
						}
						catch(NullPointerException e) {
							e.printStackTrace();
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							retryCount+=1;
							if(retryCount==Specification.FETCH_RETRY_COUNT) {
								break;
							}
							continue;
						}
						retryCount=0;
						//System.out.println("EFPS: OUTPUT "+output);
						if(mainCMDHandler.error==true) {
							//mainCMDHandler = new MainCMDHandler("$reset "+id, null);
							//output = mainCMDHandler.output;
							mainCMDHandler = new MainCMDHandler(message, null);
							output = mainCMDHandler.output;
							try {
								arr=mainCMDHandler.sensorParser.getArray();
							}
							catch(NullPointerException e) {
								e.printStackTrace();
								try {
									Thread.sleep(100);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								retryCount+=1;
								if(retryCount==Specification.FETCH_RETRY_COUNT) {
									break;
								}
								continue;
							}
							retryCount=0;
//							if(mainCMDHandler.error==true ) {
//								System.out.println("EFPS: Giving up on "+i);
//								break;
//							}
						}
						else {
							try { 
								System.out.println("EFPS: "+output);
								for(int i=0;i<Specification.sensorBufferLength;i++) {
									for(int j=0;j<Specification.sensorCount;j++) {
										int sensor = arr[i][j];
										Core.dbClient.insert((id*4+j)+"", id, 0, sensor);
									}
								}
							}
							catch( Exception e){
								mainCMDHandler = new MainCMDHandler("$reset "+id, null);
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
//				System.out.println("EFPS: Thread for "+id+" diying..");

			}
		};
		loggerThread[i].start();
	}
}