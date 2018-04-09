package jarvisReborn;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToggleButton;

public class ControlGUI {
	JToggleButton toggleButton[]=new JToggleButton[20];
	JLabel label[] = new JLabel[20];
	Telnet telnet;
	ActionListener buttonListner = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			for(int i=0; i<20; i++) {
				if(e.getSource()==toggleButton[i]) {
					if(toggleButton[i].isSelected()) {
						toggleButton[i].setText("ON");
						telnet.echo(i+" 1\r");
					}
					else {
						toggleButton[i].setText("OFF");
						telnet.echo(i+" 0\r");
					}
				}
			}
		}
	};
	public ControlGUI(int mcu, Telnet telnet) {
		this.telnet=telnet;
		JFrame frame = new JFrame("Control panel for mcu "+mcu);
		frame.setSize(600, 630);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setLayout(null);
		int z=0;
		for(int i =0;i<20;i++) {
			toggleButton[i] = new JToggleButton("MEH");
			label[i] = new JLabel();
			label[i].setText("Pin :"+i);
			label[i].setBounds(0,z,450,25);
			label[i].setHorizontalAlignment(JLabel.RIGHT);
			toggleButton[i].setBounds(500,z,100,25);
			frame.add(toggleButton[i]);
			if(telnet.pinStatus(i)) {
				toggleButton[i].setText("ON");
				toggleButton[i].setSelected(true);
				System.out.println("Pin "+i+" is on");
			}
			else {
				toggleButton[i].setText("OFF");
				toggleButton[i].setSelected(false);
				System.out.println("Pin "+i+" is off");
			}
			frame.add(label[i]);
			toggleButton[i].addActionListener(buttonListner);
			z+=30;
		}
		frame.setVisible(true);
	}
}
