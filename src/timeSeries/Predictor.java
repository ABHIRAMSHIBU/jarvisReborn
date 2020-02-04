package timeSeries;

import jarvisReborn.Core;

public class Predictor {
	protected static int id;
	public static int mcuCount = Core.configParse.data.size();
	Thread t[] = new Thread[mcuCount];
	public Predictor() {
		for(int i=0;i<mcuCount;i++) {
			id=i;
			t[i] = new Thread() {
				int i=id;
				public void run() {
					PythonEFPS pythonEFPSSensor1 = new PythonEFPS(i,0);
					PythonEFPS pythonEFPSSensor2 = new PythonEFPS(i,1);
					while(true) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pythonEFPSSensor1.testFailure();
						pythonEFPSSensor2.testFailure();
					}

					

				}
			};
			t[i].start();
			
		}
	}
}
