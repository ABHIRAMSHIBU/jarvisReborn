package Config;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.Line;


public class ConfigParse {
	public boolean status=false;
	public ArrayList<ConfigItem> data = new ArrayList<ConfigItem>();
	public ConfigParse() {
		// TODO Auto-generated constructor stub
		File dir = new File(System.getProperty("user.home")+"/SSAL");
		File file = new File(System.getProperty("user.home")+"/SSAL/ssal.conf");
		if(!dir.exists()) {
			dir.mkdirs();
		}
		if(!file.exists()) {
			System.out.println("Config file not found "
		+System.getProperty("user.home")
		+"/SSAL/ssal.conf");
		}
		else {
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
				int i=0;
				String line = bufferedReader.readLine();
				while(true) {
					line = bufferedReader.readLine();
					if(line==null) {
						break;
					}
					try {
						int id=Integer.valueOf(line.substring(0, line.indexOf(" ")));
						String ip=line.substring(line.indexOf(" ")+1,line.indexOf(" ", line.indexOf(" ")+1));
						//System.out.println("Id is:"+id+" and ip is:"+ip);
						ConfigItem configItem = new ConfigItem();
						configItem.id=id;
						configItem.ip=ip;
						data.add(configItem);
						status=true;
					}
					catch(Exception e){
						System.out.println("Error parsing config at line "+i);
						status=false;
					}
					i++;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println(System.getProperty("user.home"));
	}
}
