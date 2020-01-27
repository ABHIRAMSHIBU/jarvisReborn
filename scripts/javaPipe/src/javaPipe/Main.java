package javaPipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String userId="";
		
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
					outputPipe.write(data+"\n");
					outputPipe.flush();
					System.out.println(data);
				}
				else {
					//System.out.println("Not avail");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
