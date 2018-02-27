import javax.swing.*;
public class GUI {
	/* All critical GUI Components will go here */
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
			f.setSize(300,400);
			x=300;
			y=400;
		}
		public void size(int x, int y) {
			f.setSize(x,y);
			this.x=x;
			this.y=y;
		}
	}
	void kill(Gui g) {
		g.f.dispose();
	}
	Gui initGUI() {
		Gui g1=new Gui();
		g1.f=new JFrame();
		g1.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		g1.setVisible();
		g1.size();
		return g1;
	}
	GUI(){
		Gui g1 = initGUI();
		JLabel title = new JLabel("Welcome to jarvis!");
		title.setBounds(0,0,g1.x,40);
		title.setHorizontalAlignment(JLabel.CENTER);
		g1.f.add(title);
	}
	
}
