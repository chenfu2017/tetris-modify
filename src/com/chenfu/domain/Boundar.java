package com.chenfu.domain;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SuppressWarnings("serial")
public class Boundar extends Frame {
	private int wide;
	private int hight;
	private int size;
	private int cleanCount;
	private int wincount;
	private int score;
	private int speed;
	private boolean isOver;
	private boolean ispause;
	private boolean isHard;
	private int colorfiag;
	private Image image;
	private Square s;
	private int[][] map;
	private Color squareBodyColor;
	private Color squareNodeColor;
	private Color wallColor;

	public int[][] getMap() {
		return map;
	}

	public Color getSquareBodyColor() {
		return squareBodyColor;
	}

	public Color getSquareNodeColor() {
		return squareNodeColor;
	}

	public Color getWallColor() {
		return wallColor;
	}

	public static void main(String[] args) {
		Boundar b = new Boundar();
		b.init();
	}

	public void init() {
		loadProperties();
		map = new int[hight][wide];
		isOver = false;
		ispause = false;
		colorfiag = 1;
		score = 0;
		speed = 500;
		createMap();
		s = new Square(isHard, this);
		run();
	}

	public void loadProperties() {
		Properties properties = new Properties();
		InputStream inStream = Boundar.class.getClassLoader().getResourceAsStream("setting.properties");
		try {
			properties.load(inStream);
			wide = Integer.parseInt(properties.getProperty("wide"));
			hight = Integer.parseInt(properties.getProperty("hight"));
			size = Integer.parseInt(properties.getProperty("size"));
			cleanCount = Integer.parseInt(properties.getProperty("cleanCount"));
			wincount = Integer.parseInt(properties.getProperty("wincount"));
			isHard = Boolean.parseBoolean(properties.getProperty("isHard"));
			squareBodyColor = new Color(Integer.parseInt(properties.getProperty("squareBodyColor")));
			squareNodeColor = new Color(Integer.parseInt(properties.getProperty("squareNodeColor")));
			wallColor = new Color(Integer.parseInt(properties.getProperty("wallColor")));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createsquare() {
		s = new Square(isHard, this);
	}

	public void run() {
		this.setLocation(800, 50);
		this.setSize(wide * size, hight * size);
		this.setBackground(Color.WHITE);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setVisible(true);
		Reflash R = new Reflash();
		new Thread(R).start();
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				s.keyPressed(e);
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_SPACE) {
					createMap();
					isOver = false;
					score = 0;
					new Thread(R).start();
				} else if (key == KeyEvent.VK_Y) {
					createMap();
				}
			}
		});
	}

	public void createMap() {
		int i, j;
		for (i = 0; i < hight; i++) {
			for (j = 0; j < wide; j++) {
				if (i == hight - 1 || j == 0 || j == wide - 1) {
					map[i][j] = 2;
				} else {
					map[i][j] = 0;
				}
			}
		}
	}

	public void update(Graphics g) {
		if (image == null) {
			image = this.createImage(wide * size, hight * size);
		}
		Graphics a = image.getGraphics();
		Color c = a.getColor();
		a.setColor(Color.WHITE);
		a.fillRect(0, 0, wide * size, hight * size);
		a.setColor(c);
		paint(a);
		g.drawImage(image, 0, 0, null);
	}

	public void paint(Graphics g) {
		Color c = g.getColor();
		s.draw(g);
		drawMap(g);
		g.setColor(Color.BLUE);
		g.setFont(new Font("ËÎÌו", Font.BOLD, 20));
		g.drawString("Score:" + score, wide * size - 150, 50);
		if (isOver == true) {
			g.setColor(Color.BLUE);
			g.setFont(new Font("ËÎÌו", Font.BOLD, 20));
			g.drawString("author:chenfu", wide * size / 2 - 80, hight * size / 2 + 20);
			g.drawString("Version:v1.0", wide * size / 2 - 80, hight * size / 2 + 40);
			g.setColor(Color.red);
			g.setFont(new Font("ËÎÌו", Font.BOLD, 40));
			g.drawString("Game Over", wide * size / 2 - 100, hight * size / 2);
		}
		g.setColor(c);
	}

	public void drawMap(Graphics g) {
		for (int i = 0; i < hight; i++) {
			for (int j = 0; j < wide; j++) {
				switch (map[i][j]) {
				case 1:setColorAndfillRect(g, squareBodyColor, i, j);break;
				case 2:setColorAndfillRect(g, wallColor, i, j);break;
				case 4:setColorAndfillRect(g, squareNodeColor, i, j);break;
				case 3:
				case 5:if (colorfiag > 0) {
						setColorAndfillRect(g, Color.GRAY, i, j);
					} else {
						setColorAndfillRect(g, Color.WHITE, i, j);
					}
				}
			}
		}
		colorfiag *= -1;
	}
	
	public void setColorAndfillRect(Graphics g,Color c,int i,int j) {
		Color o = g.getColor();
		g.setColor(c);
		g.fillRect(j * size, i * size, size, size);
		g.setColor(o);
	}

	public void deleteslant() {
		for (int i = hight - 1; i >= 0; i--) {
			for (int j = wide - 1; j > 0; j--) {
				if (checkAll(i, j) == true) {
					checkEmptyGrid();
				}
			}
		}
	}

	public boolean check(int i, int j, char a, char b) {
		int count = 0;
		while (map[operate(i, a, count)][operate(j, b, count)] == 4) {
			count++;
			if (operate(i, a, count) < 0 || operate(j, b, count) < 0 
			|| operate(i, a, count) > hight - 1|| operate(j, b, count) > wide - 1) {
				break;
			}
		}
		if (count > cleanCount) {
			Reflash r = new Reflash();
			for (int k = 0; k < count; k++) {
				map[operate(i, a, k)][operate(j, b, k)] = 5;
			}
			r.pause(500);
			for (int k = 0; k < count; k++) {
				map[operate(i, a, k)][operate(j, b, k)] = 4;
				checkAll(operate(i, a, k), operate(j, b, k));
				map[operate(i, a, k)][operate(j, b, k)] = 0;
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("null")
	public int operate(int a, char operator, int b) {
		switch (operator) {
		case '-':
			return a - b;
		case '+':
			return a + b;
		case 'l':
			return a;
		}
		return (Integer) null;
	}

	public boolean checkAll(int i, int j) {
		if (check(i, j, '-', '-') || check(i, j, '+', '+') || check(i, j, '-', '+') || check(i, j, '+', '-')
		 || check(i, j, '-', 'l') || check(i, j, '+', 'l') || check(i, j, 'l', '+') || check(i, j, 'l', '-')) {
			return true;
		}
		return false;
	}

	public void deleteLine() {
		int k = 0;
		for (int i = hight - 1; i >= 0; i--) {
			for (int j = 0; j < wide; j++) {
				if (map[i][j] == 1) {
					k++;
				}
			}
			if (k == wide - 2) {
				score += 10;
				Reflash r = new Reflash();
				for (int j = 1; j < wide - 1; j++) {
					map[i][j] = 3;
				}
				r.pause(500);
				for (int a = i; a > 0; a--) {
					for (int b = 0; b < wide; b++) {
						map[a][b] = map[a - 1][b];
					}
				}
				i++;
			}
			k = 0;
		}
	}

	public void checkEmptyGrid() {
		int k;
		boolean empty;
		for (int i = hight - 1; i >= 0; i--) {
			for (int j = wide - 1; j >= 0; j--) {
				k = 0;
				empty = false;
				if (map[i][j] == 1 || map[i][j] == 4) {
					while (map[i + k + 1][j] == 0) {
						empty = true;
						if (i + k > hight)
							break;
						k++;
					}
					if (empty == true) {
						map[i + k][j] = map[i][j];
						map[i][j] = 0;
						empty = false;
					}
				}
			}
		}
	}

	public boolean isOver() {
		return isOver;
	}

	public void gameOver() {
		for (int i = 0; i < wide; i++) {
			if (map[1][i] == 1) {
				isOver = true;
			}
		}
	}

	public void victory() {
		if (score == wincount) {
			isOver = true;
		}
	}

	class Reflash implements Runnable {
		public void run() {
			while (!isOver) {
				try {
					s.down();
					if (!ispause) {
						repaint();
					}
					if (score <= 300) {
						Thread.sleep(speed - score);
					} else {
						Thread.sleep(200);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void pause(int i) {
			try {
				ispause = true;
				for (int count = 0; count < 10; count++) {
					repaint();
					Thread.sleep(i / 10);
				}
				ispause = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
