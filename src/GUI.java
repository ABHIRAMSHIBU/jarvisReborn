import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.net.MalformedURLException;
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
	void splash() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		String imageDir="/home/abhiram/eclipse-workspace/JarvisReborn/src/splash.jpg";
		JWindow window = new JWindow();
		window.getContentPane().add(new JLabel("", new ImageIcon(new ImageIcon(imageDir).getImage().getScaledInstance(width/2, height/2, Image.SCALE_DEFAULT)), SwingConstants.CENTER));
		window.setBounds(500, 150, 300, 200);
		window.setVisible(true);
		try {
		    Thread.sleep(5000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		window.setVisible(false);
		window.dispose();
		
	}
	Gui initGUI() throws MalformedURLException {
		
		splash();
		Gui g1=new Gui();
		g1.f=new JFrame();
		g1.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		g1.setVisible();
		g1.size();
		return g1;
	}
	GUI(){
		Gui g1=null;
		try {
			g1 = initGUI();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel title = new JLabel("Welcome to jarvis!");
		title.setBounds(0, 0, 0, 0);
		g1.f.add(title);
	}
	
}
