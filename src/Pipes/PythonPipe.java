package Pipes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

import jarvisReborn.Specification;
public class PythonPipe {
	public FileInputStream inputFIS;
	public BufferedReader input;
	public PrintWriter output;
	Process p;
	String id;
	public PythonPipe(String pythonScript, String id) {
		this.id=id;
		startPython(pythonScript);
		String jPipeName = "EFPSout"+id;
	    String pPipeName =  "EFPSin"+id;
		getPipe(jPipeName,pPipeName);
	}
	public void startPython(String file) {
		Thread pythonThread;
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
		
			@Override
			public void run() {
					System.out.println("Intiated shutdown service");
					p.destroyForcibly();
			}
			
		}));

		pythonThread = new Thread(new Runnable() {
			public void run() {
				
				try {
					BufferedReader br;
					p = Runtime.getRuntime().exec("python3 "+file+" --id "+id);
					br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while(p.getInputStream().available()>0) {
						br.read();
					}
	//						System.out.println(br.readLine());
	//						Thread.sleep(1000);
					int retryCount=0;
					while(true){
						Thread.sleep(2000);
						while(p.getInputStream().available()>0) {
							br.read();
						}
	//							System.out.println(br.readLine());
						if(!p.isAlive()) {
							retryCount++;
							System.out.println("PythonPipe: Python process died for id=,"+id+ "attempting to restart.");
							p = Runtime.getRuntime().exec("python3 "+file+" --id "+id);
							br = new BufferedReader(new InputStreamReader(p.getInputStream()));
							 
							if(p.isAlive()) {
								System.out.println("PythonPipe: Python process restored to alive state.");
								
							}
						}
						else {
						}
						if(retryCount==Specification.FETCH_RETRY_COUNT) {
							break;
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
	}
	public void getPipe(String jPipeName, String pPipeName) {
		String userId="";
		try {
			InputStreamReader streamReader = new InputStreamReader(Runtime.getRuntime().exec("id -u").getInputStream());
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			 userId = bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String javaPipeName = "/run/user/"+userId+"/ssald/"+jPipeName;
		String pythonPipeName = "/run/user/"+userId+"/ssald/"+pPipeName;
		File javaPipeFile = new File(javaPipeName);
		File pythonPipeFile = new File(pythonPipeName);
		while(!(javaPipeFile.exists() && pythonPipeFile.exists())){
			try {
				Thread.sleep(500);
				System.out.println("PythonPipe:Pipe does not exist yet.");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			FileInputStream fr = new FileInputStream(pythonPipeName);
			inputFIS=fr;
			BufferedReader inputPipe = new BufferedReader(new FileReader(pythonPipeName));
			input=inputPipe;
			PrintWriter outputPipe = new PrintWriter(new FileWriter(javaPipeName));
			output=outputPipe;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String readPipe() {
		String data=null;
		while(true) {
			try {
				if(inputFIS.available()>0) {
					data=input.readLine();
					break;
				}
				else {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		return data;
	}
	public void writePipe(String data) {
		output.println(data);
		output.flush();
	}
}
