package com.chenfu.domain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Square {
	private int x;
	private int y;
	private int state;
	private int type;
	private int stateTotal;
	private Boolean isHard;
	private Boundar yard;
	private int shapes[][][];

	public Square(Boolean isHard, Boundar yard) {
		this.yard = yard;
		this.isHard = isHard;
		init();
	}

	public void init() {
		Random r = new Random();
		x = 0;
		y = 4;
		if (isHard == false) {
			state = r.nextInt(4);
			type = r.nextInt(2);
			stateTotal = 4;
			this.shapes = simpleShapes;
		} else {
			state = r.nextInt(2);
			type = r.nextInt(4);
			stateTotal = 2;
			this.shapes = variousShapes;
		}

	}

	private final int variousShapes[][][] = new int[][][] {
			{ { 0, 0, 0, 0, 4, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 4, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 }, },
			{ { 0, 0, 0, 0, 1, 4, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 },

			}, { { 0, 0, 0, 0, 1, 1, 4, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0 },

			}, { { 0, 0, 0, 0, 1, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0 },

			}, };
			
	private final int simpleShapes[][][] = new int[][][] {
			{ { 0, 0, 0, 0, 4, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 4, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 }, },
			{ { 0, 0, 0, 0, 1, 4, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 1, 0, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0 },
					{ 0, 0, 0, 0, 1, 1, 4, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
					{ 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0 }, },
		};

	public boolean isOk(int x, int y, int type, int state) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (((shapes[type][state][i * 4 + j] == 1 || shapes[type][state][i * 4 + j] == 4)
						&& yard.getMap()[x + j][y + i] == 1)
						|| ((shapes[type][state][i * 4 + j] == 1 || shapes[type][state][i * 4 + j] == 4)
								&& yard.getMap()[x + j][y + i] == 2)
						|| ((shapes[type][state][i * 4 + j] == 1 || shapes[type][state][i * 4 + j] == 4)
								&& yard.getMap()[x + j][y + i] == 4)) {
					return false;
				}
			}
		}
		return true;
	}

	public void add(int x, int y, int type, int state) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if ((shapes[type][state][i * 4 + j] == 1 || shapes[type][state][i * 4 + j] == 4)
						&& yard.getMap()[x + j][y + i] == 0) {

					yard.getMap()[x + j][y + i] = shapes[type][state][i * 4 + j];
				}
			}
		}

	}

	public void nextstate(int state) {
		int nextstate = (state + 1) % stateTotal;
		if (isOk(x, y, type, nextstate)) {
			this.state = nextstate;
			yard.repaint();
		}
	}

	public void down() {
		if (isOk(x + 1, y, type, state)) {
			x = x + 1;
		} else {
			add(x, y, type, state);
			yard.createsquare();
			yard.deleteslant();
			yard.deleteLine();
		}
		yard.gameOver();
		yard.victory();
		yard.repaint();
	}

	public void left() {
		if (isOk(x, y - 1, type, state)) {
			y--;
			yard.repaint();
		}
	}

	public void right() {
		if (isOk(x, y + 1, type, state)) {
			y++;
			yard.repaint();
		}
	}

	public void draw(Graphics g) {
		Color c = g.getColor();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (shapes[type][state][i * 4 + j] == 1) {
					yard.setColorAndfillRect(g, yard.getSquareBodyColor(),j+x,i+y);
				}
				if (shapes[type][state][i * 4 + j] == 4) {
					yard.setColorAndfillRect(g, yard.getSquareNodeColor(),j+x,i+y);
				}
			}
		}
		g.setColor(c);
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:nextstate(state);break;
		case KeyEvent.VK_DOWN:down();break;
		case KeyEvent.VK_LEFT:left();break;
		case KeyEvent.VK_RIGHT:right();break;
		}
	}
}
