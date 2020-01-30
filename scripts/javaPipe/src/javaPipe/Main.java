package javaPipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
public class Main {
	static String getNextPoints(){
		String s="";
		Random r = new Random();
		for(int i=0;i<99;i++) {
		s+=r.nextDouble()+",";
		}
		s+=r.nextDouble();
		return s;	
	}
	static Process p;
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		String userId="";
		Thread pythonThread;
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
					p.destroyForcibly();
			}
			
		}));

		 pythonThread = new Thread(new Runnable() {
			public void run() {
				
				try {
					BufferedReader br;
					p = Runtime.getRuntime().exec("python3 src/python_pipe.py ");
					br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					System.out.println(br.readLine());
//					System.out.println(br.readLine());
//					Thread.sleep(1000);
					while(true){
						Thread.sleep(10);
						
						System.out.println(br.readLine());
//						System.out.println(br.readLine());
						if(!p.isAlive()) {
							System.out.println("Python program has quit");
							p = Runtime.getRuntime().exec("python3 src/python_pipe.py");
							br = new BufferedReader(new InputStreamReader(p.getInputStream()));
							 
							if(p.isAlive()) {
								System.out.println("Restart of python pipe successful");
								
							}
						}
					}
				}	
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		pythonThread.start();
		try {
			InputStreamReader streamReader = new InputStreamReader(Runtime.getRuntime().exec("id -u").getInputStream());
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			 userId = bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String javaPipeName = "/run/user/"+userId+"/ssald/out";
		String pythonPipeName = "/run/user/"+userId+"/ssald/in";
		File javaPipeFile = new File(javaPipeName);
		File pythonPipeFile = new File(pythonPipeName);
		while(!(javaPipeFile.exists() && pythonPipeFile.exists())){
			try {
				Thread.sleep(500);
				System.out.println("File not exist");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			FileInputStream fr = new FileInputStream(pythonPipeName);
			BufferedReader inputPipe = new BufferedReader(new FileReader(pythonPipeName));
			PrintWriter outputPipe = new PrintWriter(new FileWriter(javaPipeName));
			while(true) {
				if(fr.available()>0) {
					String data=inputPipe.readLine();
//					outputPipe.write(data+"\n");
					outputPipe.write(getNextPoints()+"\n");
					outputPipe.flush();
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
				else {
//					System.out.println("Not avail");
//					System.out.println(pythonThread.isAlive());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
