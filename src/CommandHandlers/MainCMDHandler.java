package CommandHandlers;

import javax.swing.JTextArea;
import jarvisReborn.Core;

public class MainCMDHandler {
	public boolean parsed=false;
	public String output=""; 
	JTextArea ta;
	public MainCMDHandler(String input,JTextArea ta) {
		// TODO Auto-generated constructor stub
		this.ta=ta;
		if(input.contains("$set")) {
			parseSET(input.substring(input.indexOf(" ")+1));
		}
		else if(input.contains("$test")) {
			parseTEST(input.substring(input.indexOf(" ")+1));
		}
	}
	public void parseTEST(String input) {
		parsed=true;
		int space1=input.indexOf(" ");
		int pin=Integer.valueOf(input.substring(0, space1)), 
				mcu=Integer.valueOf(input.substring(space1+1));
		output=Core.telnet[mcu].echo(pin+"\r");
	}
	public void parseSET(String input) {
		// 13 0 0
		int space1=input.indexOf(" "),
			space2=input.indexOf(" ",space1+1);
		int pin=Integer.valueOf(input.substring(0, space1)), 
			operation=Integer.valueOf(input.substring(space1+1,space2)),
			mcu=Integer.valueOf(input.substring(space2+1));
		//System.out.println("Pin is:"+pin+" operation is:"+operation+" mcu is:"+mcu);
		parsed=true;
		output=Core.telnet[mcu].echo(pin+" "+operation+"\r");
		//Im gonna make that text view bigger
	}
}
