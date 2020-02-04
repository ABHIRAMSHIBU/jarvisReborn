package timeSeries;
import Pipes.PythonPipe;
import jarvisReborn.Core;
import jarvisReborn.Specification;

import java.util.List;
import java.util.Random;

import org.influxdb.InfluxDB;

public class PythonEFPS {
	PythonPipe pythonPipe;
	int mcu;
	int sensor;
	String outPipeName;
	String inPipeName;
	public PythonEFPS(int mcu, int sensor) {
		this.mcu = mcu;
		this.sensor = sensor;
		outPipeName = "EFPSPredictorOut"+mcu+sensor;
		inPipeName = "EFPSPredictorIn"+mcu+sensor;
		
	}
	
	String getNextPoints(){
		List<List <Object>> points = Core.dbClient.getValues((mcu*2+sensor)+"", 100);
		String pointsString = "";
		for(int i=99;i>=1;i--) {
			pointsString+=points.get(i).get(1)+",";
		}
		pointsString+=points.get(0).get(1);
		System.out.println("Hello World");
		System.out.println(pointsString);
		return pointsString;
		
	}
	public void testFailure() {
		while(true) {
			try {
				Thread.sleep(Specification.EFPSLoggerInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String data=pythonPipe.readPipe();
			pythonPipe.writePipe(getNextPoints());
			System.out.println(data);
			if(data.equals("1")) {
				System.out.println("LED Normal");
			}
			else if(data.equals("0")) {
				System.out.println("LED Failure");
			}
			else {
				System.out.println("Unknown output from Python Pipe");
			}
			
		}
	}
	public PythonEFPS() {
		pythonPipe = new PythonPipe("src/python_pipe.py",outPipeName,inPipeName);
	}

}
