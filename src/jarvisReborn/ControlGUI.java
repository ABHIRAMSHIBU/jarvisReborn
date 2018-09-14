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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import Sockets.Telnet;

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
	public ControlGUI(int mcu, Telnet telnet,JTextArea ta) {
		this.telnet=telnet;
		JFrame frame = new JFrame("Control panel for mcu "+mcu);
		frame.setSize(250, 630);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setLayout(null);
		int z=0;
		for(int i =0;i<20;i++) {
			toggleButton[i] = new JToggleButton("MEH");
			label[i] = new JLabel();
			label[i].setText("Pin :"+i);
			label[i].setBounds(0,z,100,25);
			label[i].setHorizontalAlignment(JLabel.RIGHT);
			toggleButton[i].setBounds(100,z,100,25);
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
				ta.append("\n>Pin "+i+" is off");
			}
			frame.add(label[i]);
			toggleButton[i].addActionListener(buttonListner);
			z+=30;
		}
		frame.setVisible(true);
	}
}
