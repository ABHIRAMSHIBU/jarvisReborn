package jarvisReborn;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import sun.security.ec.ECDHKeyAgreement;

public class GUI {
	/* All critical GUI Components will go here */
	public JFrame frame;
	public JLabel title;
	public JButton button[];
	public JPanel ssalPanel;
	JTextField tf;
	class ButtonListen implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == button[0]) {
				if(tf.getText().equals("$tel status")){
					tf.setText(""+Core.tele.getState());
					System.out.println("boo");
				}
				else if(tf.getText().contains("$run")) {
					int i = tf.getText().indexOf(" ");
					int j = tf.getText().indexOf(" ", i+1);
					int z = tf.getText().indexOf(" ",j+1);
					int pin = Integer.valueOf(tf.getText().substring(i+1,j));
					int operation = Integer.valueOf(tf.getText().substring(j+1,z));
					tf.setText(Core.telnet[Integer.valueOf(tf.getText().substring(z+1))].echo(pin+" "+operation+"\r"));
				}
				else {
					tf.setText("INVALID: "+tf.getText());
				}
			}
			else if(e.getSource() == button[1]) {
				//code to be added for button 1
				try {
					int mcu = Integer.valueOf(tf.getText());
					Thread control = new Thread() {
						public void run() {
							new ControlGUI(mcu, Core.telnet[mcu]);
						}
					};
					control.start();
					tf.setText("Launching GUI now");
				}
				catch(Exception e1){
					tf.setText("Error occured check : "+tf.getText());
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
			tf = new JTextField();
			tf.setForeground(Color.BLACK);
			tf.setBackground(Color.WHITE);
			tf.setBounds(10,50,x-200,40);
			button[0].setBounds(x-180,50,175,40);
			panel.add(tf);
			panel.add(button[0]);
			button[0].addActionListener(new ButtonListen());
			button[0].setForeground(Color.WHITE);
			button[0].setBackground(Color.BLACK);
			button[1]=new JButton("Control");
			button[1].setBounds(10,100,100,40);
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
		JLabel title = new JLabel("Welcome to jarvis!");
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
