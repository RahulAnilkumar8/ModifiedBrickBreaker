package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import javax.swing.JPanel;
import javax.swing.Timer;

//The key Listener is used for the arrowkeys and actionListener for the ball
public class Gameplay extends JPanel implements KeyListener, ActionListener{
	private boolean play = false;
	private int score = 0;
	
	private Timer timer;		//need to determine how fast the ball travels
	public int delay = 8;
	
	private int playerX = 310; //starting position of the slider
	
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	public int xCount = 0;
	public int yCount = 3;
	private int totalBricks = (xCount+3) * (yCount+7);
	public int levelCount = 1;
	
	private MapGenerator map;
	
	public Gameplay() {
		map = new MapGenerator(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
		
	}
	
	public void paint(Graphics g) {
		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//map
		map.draw((Graphics2D)g);
		
		//border
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3,592);
		g.fillRect(0, 0, 692,3);
		g.fillRect(691, 0, 3,592);
		
		//Score
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD,25));
		g.drawString(""+score, 590, 30);
		
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD,25));
		g.drawString("Level: "+levelCount, 10, 30);
		
		
		// paddle
		g.setColor(Color.yellow);
		g.fillRect(playerX,515, 100, 8);
		
		//the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX,ballposY, 20, 20);
		
		if(totalBricks <=0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.yellow);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("YOU WIN ", 260, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Your Score:"+score, 250, 330);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 250, 360);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press H for hard mode!", 250, 380);
		}
			
			
		if(ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.yellow);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("GAME OVER ", 250, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Your Score:"+score, 250, 330);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 250, 360);
			
		}
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		//ball motion
		if(play) {
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 515, 100, 8))) {
				ballYdir = -ballYdir;			
			}
			
			A: for(int i = 0; i<map.map.length; i++) {
				for(int j = 0; j < map.map[0].length;j++) {
					if(map.map[i][j]>0) {
						int brickX = j*map.brickWidth+ 80;
						int brickY = i * map.brickHeight + 50;
						int brickwidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickwidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							
							if(ballposX + 19 <= brickRect.x || ballposX + 1>=brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							} else {
								ballYdir = -ballYdir;
							}
							break A;
						}
					}
					
				}
			}
			
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX <0) {
				ballXdir = -ballXdir;
			}
			if(ballposY <0) {
				ballYdir = -ballYdir;
			}
			if(ballposX > 670) {
				ballXdir = -ballXdir;
			}
		
		}		
		repaint();
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		// When it hits the right wall it moves the paddle back left
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >=600) {
				playerX = 600;
			}else {
				moveRight();
			}
		}
		// When it hits the left wall it moves the paddle back right
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX <10) {
				playerX = 10;
			}else {
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				map = new MapGenerator(xCount+3, yCount+7);
				
				repaint();
			}
		}
			
			if(e.getKeyCode() == KeyEvent.VK_H) {
				if(!play) {
					play = true;
					ballposX = 120;
					ballposY = 350;
					ballXdir = -1;
					ballYdir = -2;
					playerX = 310;
					score = 0;
					delay = 4;
					map = new MapGenerator(3+xCount, 7+yCount);
					xCount ++;
					yCount ++;
					levelCount ++;
					repaint();
				}
		}
	}
	
	public void moveRight(){
		play = true;
		playerX +=25;
	}
	
	public void moveLeft(){
		play = true;
		playerX -=25;
	}
	
	
	

	
}
