package logger;
/* Author Abhijith N Raj
 * Copyleft 2020 
 * Project SSAL
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
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
