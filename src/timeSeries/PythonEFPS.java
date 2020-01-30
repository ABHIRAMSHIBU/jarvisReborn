package timeSeries;
import Pipes.PythonPipe;
import java.util.Random;

public class PythonEFPS {
	PythonPipe pythonPipe;
	static String getNextPoints(){
		String s="";
		Random r = new Random();
		for(int i=0;i<99;i++) {
		s+=r.nextDouble()+",";
		}
		s+=r.nextDouble();
		return s;	
	}
	public void testFailure() {
		while(true) {
			
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
		pythonPipe = new PythonPipe("src/python_pipe.py","EFPSout","EFPSin");
	}

}
