package Pipes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
public class PythonPipe {
	public FileInputStream inputFIS;
	public BufferedReader input;
	public PrintWriter output;
	static Process p;
	public PythonPipe(String pythonScript, String jPipeName, String pPipeName) {
		startPython(pythonScript);
		getPipe(jPipeName,pPipeName);
	}
	public void startPython(String file) {
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
					p = Runtime.getRuntime().exec("python3.7 "+file);
					br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while(p.getInputStream().available()>0) {
						br.read();
					}
	//						System.out.println(br.readLine());
	//						Thread.sleep(1000);
					while(true){
						Thread.sleep(10);
						while(p.getInputStream().available()>0) {
							br.read();
						}
	//							System.out.println(br.readLine());
						if(!p.isAlive()) {
							System.out.println("PythonPipe: Python process died, attempting to restart.");
							p = Runtime.getRuntime().exec("python3 "+file);
							br = new BufferedReader(new InputStreamReader(p.getInputStream()));
							 
							if(p.isAlive()) {
								System.out.println("PythonPipe: Python process restored to alive state.");
								
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
