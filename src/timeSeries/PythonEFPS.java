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

	String id;
	public PythonEFPS(int mcu, int sensor) {
		this.mcu = mcu;
		this.sensor = sensor;
		this.id = Integer.toString(this.mcu)+Integer.toString(this.sensor);
		pythonPipe = new PythonPipe("src/python_pipe.py",id);
		
	}
	
	String getNextPoints(){
		List<List <Object>> points = Core.dbClient.getValues((mcu*2+sensor)+"", 100);
		String pointsString = "";
		for(int i=99;i>=1;i--) {
			pointsString+=points.get(i).get(1)+",";
		}
		pointsString+=points.get(0).get(1);
		return pointsString;
		
	}
	public boolean testFailure() {
		try {
			Thread.sleep(Specification.EFPSLoggerInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String data=pythonPipe.readPipe();
		pythonPipe.writePipe(getNextPoints());
		if(data.equals("1")) {
			System.out.println("Device Normal");
			return true;
		}
		else if(data.equals("0")) {
			System.out.println("Device Failure");
			return false;
		}
		else {
			System.out.println("Unknown output from Python Pipe");
			return true;
		}


	}


}
