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
				int arr[][];
				public void run() {
					while(true) {
						MainCMDHandler mainCMDHandler = new MainCMDHandler(message, null);
						if(mainCMDHandler.parsed) {
							output = mainCMDHandler.output;
							arr=mainCMDHandler.sensorParser.getArray();
							//System.out.println("EFPS: OUTPUT "+output);
							if(mainCMDHandler.error==true) {
								//mainCMDHandler = new MainCMDHandler("$reset "+id, null);
								//output = mainCMDHandler.output;
								mainCMDHandler = new MainCMDHandler(message, null);
								output = mainCMDHandler.output;
								arr=mainCMDHandler.sensorParser.getArray();
//								if(mainCMDHandler.error==true ) {
//									System.out.println("EFPS: Giving up on "+i);
//									break;
//								}
							}
							else {
								try { 
									System.out.println("EFPS: "+output);
									for(int i=0;i<Specification.sensorBufferLength;i++) {
										for(int j=0;j<Specification.sensorCount;j++) {
											int sensor = arr[i][j];
											System.out.println("EFPS: MCU "+id+" Sensor"+j+" "+sensor);
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
//					System.out.println("EFPS: Thread for "+id+" diying..");

				}
			};
			loggerThread[i].start();
		}
		
	}
	
	
}
