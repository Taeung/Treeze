package com.treeze.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.google.gson.Gson;

/*
 * NoteManager��ü �ʱ⿡ �ش�Ǵ� �κ��� NoteManager���� �����Ѵ�.
 */
public class NoteManager {

	protected ArrayList<DrawableObject> drawobjList;
	protected ArrayList<ComponentJPanel> componentList;
	
	protected Path path;
	private JPanel jpanel;


	protected FigureObject figureObj;

	protected Gson gson;
	protected FileIOManager fim;
	
	protected static final int IMG_TYPE_STAR = 0;
	
	public static final int IMG_SIZE_NO_DECIDED = -1;

	public static Image STAR_IMG; 	 
	
	private NoteManager nm;

	protected NoteManager(JPanel jpanel) {
		// TODO Auto-generated constructor stub
		this.drawobjList = new ArrayList<DrawableObject>();
		this.componentList = new ArrayList<ComponentJPanel>();
		this.jpanel = jpanel;

		gson = new Gson();
		fim = new FileIOManager();
		
		nm = this;
		
		jpanel.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				nm.setRelativeLocation();
				nm.repaint();
				
				
			}
			
			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	protected void initPath() {

		path = new Path();

	}

	protected void makePath(LinePoint point, Color color, BasicStroke bs) {

		path.setBs(bs);
		path.setColor(color);
//		path.getPoints().add(point);
		path.points.add(point);
		repaint();

	}

	protected void makePathComplete() {
		drawobjList.add(new LineObject(this.path, jpanel.getWidth(), jpanel.getHeight()));
		this.path = null;
//		System.out.println("drawobj size : " + drawobjList.size());
	}

	protected void makeFigure(int x, int y, int width, int height, int type) {
		this.figureObj = new FigureObject(x, y, width, height,jpanel.getWidth(),jpanel.getHeight(), type);
		repaint();

	}

	protected void makeFigureComplete() {
		drawobjList.add(this.figureObj);
		this.figureObj = null;
//		System.out.println("drawobj size : " + drawobjList.size());
	}
	
	protected void setFeatureByRate() {
		for(int i = 0; i < drawobjList.size(); i++) {
			drawobjList.get(i).setFeatureByRate(jpanel.getWidth(), jpanel.getHeight());
		}
		for(int i = 0; i < componentList.size(); i++) {
			componentList.get(i).setFeatureByRate(jpanel.getWidth(), jpanel.getHeight());
		}
	}
	
	protected void restore() {
		drawAll(jpanel.getGraphics());
		addAllToPanel();
	}

	protected void drawAll(Graphics g) {
		
		for (int i = 0; i < drawobjList.size(); i++) {
			
			drawobjList.get(i).draw(g, jpanel);

		}

	}
	
	protected void addAllToPanel() {
		for (int i = 0; i < componentList.size(); i++) {

			ComponentJPanel component = componentList.get(i);
			component.addToPanel(jpanel, this);
		}
	}

	protected void drawLineByRealTime(Graphics g) {

		if (path != null) {

			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(path.getColor());
			g2.setStroke(path.getBs());

			for (int i = 0; i < path.getPoints().size() - 1; i++) {
				g.drawLine(path.getPoints().get(i).x,
						path.getPoints().get(i).y,
						path.getPoints().get(i + 1).x,
						path.getPoints().get(i + 1).y);
			}

		}

	}

	protected void drawFigureByRealTime(Graphics g) {
		
//		System.out.println(figureObj);

		if (figureObj != null) {
			figureObj.draw(g, jpanel);
		}

	}

	protected void drawFigure(int x, int y, int width, int height, int type) {
		drawobjList.add(new FigureObject(x, y, width, height, jpanel.getWidth(),jpanel.getHeight(),type));
		repaint();
	}


	protected void drawImage(int x, int y, double width, double height,
			int  type) {
//		if(IMG_SIZE_WIDTH != IMG_SIZE_NO_DECIDED) {
//			width = IMG_SIZE_WIDTH;
//			height = IMG_SIZE_HEIGHT;
//		}
		drawobjList.add(new ImageObject(x, y, (int)width, (int)height,jpanel.getWidth(), jpanel.getHeight(), type));
		repaint();
	}

	protected void repaint() {

		jpanel.repaint();

	}



	protected void addPostIt(int x, int y, int width, int height) {
		
		PostItPanel postItPanel = new PostItPanel(x,y,width,height,jpanel.getWidth(), jpanel.getHeight());
		

		jpanel.add(postItPanel);
		jpanel.validate();
		
		componentList.add(postItPanel);
		repaint();
		



	}


	protected void addMemo(int x, int y, int width, int height) {
		
		MemoPanel memo = new MemoPanel(x, y, width, height, jpanel.getWidth(), jpanel.getHeight());
		memo.addToPanel(jpanel, this);
		componentList.add(memo);		
		repaint();

	}
	


	protected void removeLastDrawableObj() {

		drawobjList.remove(drawobjList.size() - 1);

	}

	
	protected void restoreNote() {
		
		StoredNoteObject sno = loadStoredNote();
		componentList = sno.getCOList();
		drawobjList = sno.getDOList();
		
		setFeatureByRate();		
		//�гΰ� �׸��� �ٽ� �����
		restore();
		//repaint ��Ŵ
		repaint();

	}
	
	

	protected StoredNoteObject loadStoredNote() {	
		
		String noteData = null;
		try {
			noteData = fim.read(Util.NOTE_ADDR + "content.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return gson.fromJson(noteData, StoredNoteObject.class);
		
		

	}

	protected void saveStoredNote() {
		System.out.println("saveStoredNote : " + componentList.size());

		StoredNoteObject sno = new StoredNoteObject(drawobjList, componentList);
		String noteContent = gson.toJson(sno);

		try {
			fim.write(Util.NOTE_ADDR + "content.txt", noteContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected void setClick(int x, int y) {
		setUnClicked();
		for(int i = 0; i< drawobjList.size(); i++) {
//			System.out.println("asdfadsfasdfsdasdafasdfasdfsadf");
			drawobjList.get(i).setClick(x, y, this);
			
				
		}
		repaint();
		
	}
	
	protected boolean isClickableItem(int x, int y) {
		for(int i = 0; i < drawobjList.size(); i++) {
			if(drawobjList.get(i).isClick(x, y, this)) {
				return true;
			}
		}
		
		for(int i = 0; i < drawobjList.size(); i++) {
			if(drawobjList.get(i).isClick(x, y, this)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	protected void removeSelectedItem() {
		for(int i = 0; i < drawobjList.size(); i++) {
			drawobjList.get(i).removeSelectedItem(this);
		}
		
		for(int i = 0; i < componentList.size(); i++) {
//			System.out.println("�ڸ���");
			componentList.get(i).removeSelectedItem(this);
		}
		repaint();
	}
	
	protected void setUnClicked() {
//		System.out.println("unclick size : " +drawobjList.size());
		for(int i = 0; i < drawobjList.size(); i++) {
			drawobjList.get(i).setUnClicked(this);
		}
		
		for(int i = 0; i < componentList.size(); i++) {
//			System.out.println("�ڸ���");
			componentList.get(i).setUnClicked(this);
		}
		repaint();
	}
	
	protected void setRelativeLocation() {
		
		for(int i = 0; i < drawobjList.size(); i++) {
			drawobjList.get(i).setRelativeLocation(this);
		}
		
		for(int i = 0; i < componentList.size(); i++) {
			
			componentList.get(i).setRelativeLocation(this);
		}
		
	}
	
	protected void setMoveFlag(int x, int y) {
		initMoveFlag();
		for(int i = 0; i < drawobjList.size(); i ++) {
			if(drawobjList.get(i).isClick(x, y, this)) {
				drawobjList.get(i).setMoveFlag(true);
//				System.out.println("asfasdfasfasdfasfasdf");
				return;
			}
		}
	}
	
	protected boolean isMoveFlag() {
		
		for(int i = 0; i < drawobjList.size(); i ++) {
			if(drawobjList.get(i).isMoveFlag()) {
				return true;				
			}
		}
		return false;
	}
	
	protected void initMoveFlag() {
		for(int i = 0; i < drawobjList.size(); i ++) {
			drawobjList.get(i).setMoveFlag(false);;
			
		}
	}	
	
	protected void move(int pressX, int pressY, int dragX, int dragY) {
		for(int i = 0; i < drawobjList.size(); i ++) {
			if(drawobjList.get(i).isMoveFlag()) {
				drawobjList.get(i).move(pressX, pressY, dragX, dragY, nm);
			}
		}
		repaint();
	}

	protected ArrayList<DrawableObject> getDrawobjList() {
		return drawobjList;
	}

	protected void setDrawobjList(ArrayList<DrawableObject> drawobjList) {
		this.drawobjList = drawobjList;
	}
	
	protected ArrayList<ComponentJPanel> getComponentList() {
		return componentList;
	}

	protected void setComponentList(ArrayList<ComponentJPanel> componentList) {
		this.componentList = componentList;
	}
	
	protected JPanel getJpanel() {
		return jpanel;
	}

	protected void setJpanel(JPanel jpanel) {
		this.jpanel = jpanel;
	}
	



}




