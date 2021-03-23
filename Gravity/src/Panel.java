import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import javax.swing.JPanel;

public class Panel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	
	public Panel() {		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	}
	
	protected Parameters stars = new Parameters();
	
	private DecimalFormat df = new DecimalFormat();
	private Font fonte = new Font("",2+1,20); // Set the font to the default one and displayed in bold and italic
			
	private int xStart, yStart, xEnd, yEnd; 
	private int offsetX = 0, offsetY = 0;
	private int focus= 0;
	private boolean isCameraMoving = false;
	private boolean click = false;
	private boolean leftArrow = false, upArrow = false, rightArrow = false, downArrow = false, spaceKey = false, pageUpKey = false, pageDownKey = false;
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		requestFocus();
		df.setRoundingMode(RoundingMode.HALF_UP);
		
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform origXform = g2.getTransform();
		origXform.setToTranslation(offsetX, offsetY);
		g2.setTransform(origXform);
		
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		if (upArrow) {
			offsetY += 10;
			upArrow = false;
		}
		if (downArrow) {
			offsetY -= 10;
			downArrow = false;
		}
		if (rightArrow) {
			offsetX -= 10;
			rightArrow = false;
		}
		if (leftArrow) {
			offsetX += 10;
			leftArrow = false;
		}
		if (spaceKey) {
			offsetX = 0;
			offsetY = 0;
			spaceKey = false;
		}
		if (pageUpKey) {
			if (focus == stars.coordinates.size()) {focus = 0;}
			offsetX = -stars.coordinates.get(focus).get(0).intValue() + (int)(this.getWidth()/2);
			offsetY = -stars.coordinates.get(focus).get(1).intValue() + (int)(this.getHeight()/2);
		}
		if (pageDownKey) {
			if (focus < 0) {focus = stars.coordinates.size() - 1;}
			else if (focus == stars.coordinates.size()) {focus = 0;}
			offsetX = -stars.coordinates.get(focus).get(0).intValue() + (int)(this.getWidth()/2);
			offsetY = -stars.coordinates.get(focus).get(1).intValue() + (int)(this.getHeight()/2);
		}
		
		for(int i = 0; i < stars.coordinates.size(); i++) {
			g2.fillOval(this.stars.coordinates.get(i).get(0).intValue()-this.stars.radii.get(i), this.stars.coordinates.get(i).get(1).intValue()-this.stars.radii.get(i), this.stars.radii.get(i)*2, this.stars.radii.get(i)*2);
		}
		
		
		for(int i = 0; i < this.stars.routesCoordinates.size(); i++) {
			for(int j = 0; j < this.stars.routesCoordinates.get(i).size(); j = j+2) {
				if(j > 1) {
					g2.drawLine(this.stars.routesCoordinates.get(i).get(j-2).intValue(), this.stars.routesCoordinates.get(i).get(j-1).intValue(), this.stars.routesCoordinates.get(i).get(j).intValue(), this.stars.routesCoordinates.get(i).get(j+1).intValue());
		
				}
			}
		}
		g2.setColor(Color.green);
		g2.fillOval(this.stars.getBarycenterCoordinates().get(0).intValue()-5, this.stars.getBarycenterCoordinates().get(1).intValue()-5, 10, 10);
		
		if(this.click) {
			g2.setColor(Color.black);
			g2.setFont(fonte);
			g2.drawLine(this.xStart, this.yStart, this.xEnd, this.yEnd);
			g2.drawString("Speed = ("+df.format((this.xEnd - this.xStart)*0.1)+";"+df.format((this.yEnd - this.yStart)*0.1)+")", this.xEnd, this.yEnd);
		}

	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		this.click = true;
		this.xStart = event.getX() - this.offsetX;
		this.yStart = event.getY() - this.offsetY;
		this.xEnd = event.getX() - this.offsetX;
		this.yEnd = event.getY() - this.offsetY;
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
		this.stars.addNewStar(this.xStart, this.yStart, (this.xEnd-this.xStart)*0.1, (this.yEnd-this.yStart)*0.1, 10.0, 10);
		this.click = false;	
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		// TODO Auto-generated method stub
		this.xEnd = event.getX() - this.offsetX;
		this.yEnd = event.getY() - this.offsetY;
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.VK_LEFT) {
			this.leftArrow = true;
			this.pageDownKey = false;
			this.pageUpKey = false;
			this.isCameraMoving = true;
		} 
		else if (event.getKeyCode() == KeyEvent.VK_UP) {
			this.upArrow = true;
			this.pageDownKey = false;
			this.isCameraMoving = true;
			this.pageUpKey = false;
		} 
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.rightArrow = true;
			this.isCameraMoving = true;
			this.pageDownKey = false;
			this.pageUpKey = false;
		} 
		else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
			this.downArrow = true;
			this.isCameraMoving = true;
			this.pageDownKey = false;
			this.pageUpKey = false;
		}
		else if (event.getKeyCode() == KeyEvent.VK_SPACE) {
			this.isCameraMoving = false;
			this.focus = 0;
			this.pageDownKey = false;
			this.pageUpKey = false;
			this.spaceKey = true;
		}
		else if (event.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			this.isCameraMoving = true;
			this.pageDownKey = true;
			this.pageUpKey = false;
			this.focus--;
		}
		else if (event.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			this.isCameraMoving = true;
			this.pageDownKey = false;
			this.pageUpKey = true;
			this.focus++;
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
}
