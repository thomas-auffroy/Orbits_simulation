import javax.swing.JFrame;

public class Window extends JFrame {
	private Panel pan = new Panel();	
	
	private Parameters stars = pan.stars;
	
	public Window() {
		this.setTitle("Simulation gravitation");
		this.setSize(1000,1000);		
		this.setContentPane(pan);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);	
		
		move();
	}
	
	public void move() {
		
		while (true) {
			stars.moveStars();
			pan.repaint();
			try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
		
	}
	
}