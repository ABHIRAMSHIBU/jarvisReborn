/*
Copyleft (C) 2018  ARCtotal
Copyleft (C) 2018  Abhiram Shibu

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package jarvisReborn;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import CommandHandlers.MainCMDHandler;

public class GUI {
	/* All critical GUI Components will go here */
	public JFrame frame;
	public JLabel title;
	public JButton button[];
	public JPanel ssalPanel;
	JTextArea ta;
	class enterListen implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(KeyEvent.getKeyText(e.getKeyCode()).equals("Enter")) {
				new ButtonListen().actionPerformed(null);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	};
	class ButtonListen implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String lastLine=ta.getText().substring(ta.getText().lastIndexOf("\n")+1);
			if(lastLine.contains("$")) {
				MainCMDHandler mainCMDHandler = new MainCMDHandler(lastLine,ta);
				if(mainCMDHandler.parsed) {
					ta.append("\n>"+mainCMDHandler.output);
					ta.append("\n");
				}
				else {
					ta.append("\n>"+"Invalid command check and retry!");
					ta.append("\n");
				}
			}
			/*if(e.getSource() == button[0]) {
				if(ta.getText().equals("$tel status")){
					ta.setText(""+Core.tele.getState());
					System.out.println("boo");
				}
				else if(ta.getText().contains("$run")) {
					int i = ta.getText().indexOf(" ");
					int j = ta.getText().indexOf(" ", i+1);
					int z = ta.getText().indexOf(" ",j+1);
					int pin = Integer.valueOf(ta.getText().substring(i+1,j));
					int operation = Integer.valueOf(ta.getText().substring(j+1,z));
					ta.setText(Core.telnet[Integer.valueOf(ta.getText().substring(z+1))].echo(pin+" "+operation+"\r"));
				}
				else {
					ta.setText("INVALID: "+ta.getText());
				}
			}*/
			else if(lastLine.length()>0){
				//code to be added for button 1
				try {
					int mcu = Integer.valueOf(lastLine);
					Thread control = new Thread() {
						public void run() {
							new ControlGUI(mcu, Core.telnet[mcu],ta);
						}
					};
					ta.append("\n>Launching GUI now");
					control.start();
					control.join();
					ta.append("\n");
				}
				catch(Exception e1){
					ta.append("\n>Error occured check MCU number");
					ta.append("\n");
				}
			}
		}
	}
	class Gui{
		JFrame f; //Frame
		public int x;
		public int y;
		private boolean visible=false;
		public void setVisible() {
			f.setVisible(true);
			visible=true;
		}
		public void setInvisible() {
			f.setVisible(false);
			visible=false;
		}
		public boolean getVisibilty() {
			return visible;
		}
		public void size() {
			x=600;
			y=400;
			f.setSize(x,y);
		}
		public void size(int x, int y) {
			f.setSize(x,y);
			this.x=x;
			this.y=y;
		}
		public void initPanel() {
			ssalPanel = new JPanel();
			ssalPanel.setBackground(Color.BLACK);
			ssalPanel.setForeground(Color.WHITE);
			ssalPanel.setLayout(null);
			TitledBorder border = new TitledBorder ( new EtchedBorder (), "SSAL Control Panel" ) ;
			border.setTitleColor(Color.WHITE);
			ssalPanel.setBorder(border);
			JLabel cmdLabel = new JLabel();
			cmdLabel.setText("Command Line");
			cmdLabel.setForeground(Color.WHITE);
			cmdLabel.setBounds(10,5,x,40);
			ssalPanel.add(cmdLabel);
			ssalPanel.setBounds(0,50,x,y-100);
			f.add(ssalPanel);
			addCMDLine(ssalPanel);
		}
		public void addCMDLine(JPanel panel) {
			button = new JButton[10];
			button[0]=new JButton();
			button[0].setText("Run Command");
			
			ta = new JTextArea();
			ta.setForeground(Color.BLACK);
			ta.setBackground(Color.WHITE);
			//ta.setBounds(10,50,x-200,240);
			ta.setText(">Welcome to SSAL command line\n"
					+">Enter $help for help with commands\n"
					+">Prompt is now active, you can start commanding\n");
			ta.addKeyListener(new enterListen());
			JScrollPane scrollPane;
			try {
				scrollPane= new JScrollPane(ta);
				scrollPane.setBounds(10,50,x-200,240);
			}
			catch(Exception e){
				scrollPane=null;
				System.out.println("Error contained?");
			}
			
			button[0].setBounds(x-180,100,175,40);
			panel.add(scrollPane);
			panel.add(button[0]);
			button[0].addActionListener(new ButtonListen());
			button[0].setForeground(Color.WHITE);
			button[0].setBackground(Color.BLACK);
			button[1]=new JButton("Control");
			button[1].setBounds(x-180,150,175,40);
			button[1].addActionListener(new ButtonListen());
			panel.add(button[1]);
			
		}
	}
	void kill(Gui g) {
		g.f.dispose();
	}
	Gui initGUI() {
		Gui g1=new Gui();
		g1.f=new JFrame("SSAL AI");
		g1.f.setBackground(Color.BLACK);
		g1.f.setForeground(Color.BLACK);
		g1.setVisible();
		g1.size();
		g1.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		g1.f.setLayout(null);
		g1.f.setResizable(true);
		g1.initPanel();
		return g1;
	}
	GUI(){
		Gui g1 = initGUI();
		JLabel title = new JLabel("Welcome to SSAL AI!");
		title.setBackground(Color.BLACK);
		title.setForeground(Color.BLACK);
		title.setBounds(0,0,g1.x,40);
		title.setHorizontalAlignment(JLabel.CENTER);
		this.title=title;
		g1.f.add(title);
		this.frame=g1.f;
		SwingUtilities.updateComponentTreeUI(g1.f);
		g1.f.setResizable(false);
	}
	
}
