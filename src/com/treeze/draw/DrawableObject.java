package com.treeze.draw;

import java.awt.Graphics;
import javax.swing.JPanel;


/*
 * graphic�� ����� ��ü
 */
public abstract class DrawableObject {	
	
	int x;
	int y;
	int width;
	int height;
	
	//graphic�� ����Ͽ� �׸��� �׸��� ���
	public abstract void draw(Graphics g, JPanel jpanel);
	
	public abstract boolean isRemoveItem(int x, int y);

}






