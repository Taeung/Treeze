package com.treeze.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.treeze.util.Var;


/*
 * pptpanel�� ��Ÿ��
 */
public class PPTPanel extends JPanel {

	// canvas postion
	public static int x1;
	public static int x2;
	public static int y1;
	public static int y2;

	// draw type
	public static int DRAW_TYPE_CIRCLE = 0;
	public static int DRAW_TYPE_RECTANGLE = 1;
	public static int DRAW_TYPE_X = 2;
	public static int DRAW_TYPE_LINE = 3;
	public static int DRAW_TYPE_STAR = 4;
	public static int DRAW_TYPE_ARROW = 5;
	// public static int DRAW_TYPE_RECTANGLE = 6;



	// keycode
	public static final int KEY_CODE_MODE = 192;
	public static final int KEY_CODE_ONE = 49;
	public static final int KEY_CODE_TWO = 50;
	public static final int KEY_CODE_THREE = 51;
	public static final int KEY_CODE_FOUR = 52;
	public static final int KEY_CODE_FIVE = 53;
	public static final int KEY_CODE_SIX = 54;
	public static final int KEY_CODE_SHIFT = 16;

	// �ʱ� ���
	public static final int NOTE_MODE_PEN = 0;
	public static final int NOTE_MODE_FIGURE = 1;
	public static final int NOTE_MODE_ERASER = 2;

	// figure type
	public static final int FIGURE_TYPE_STAR = 0;
	public static final int FIGURE_TYPE_ARROW = 1;
	public static final int FIGURE_TYPE_CIRCLE = 2;
	public static final int FIGURE_TYPE_X = 3;
	public static final int FIGURE_TYPE_REC = 4;
	public static final int FIGURE_TYPE_TEXT = 5;

	// LineMode
	public static final int LINE_MODE_STRAIGHT = 0;
	public static final int LINE_MODE_CURVE = 1;

	// button
	public static final int CLICK_BUTTON_LEFT = 1;
	public static final int CLICK_BUTTON_RIGHT = 3;
	
	public static final int MOUSE_STATE_CLICK = 0;

	private int curNoteMode;
	private int curFigureMode;
	private int curLineMode;
	private boolean curMouseState;

	// this is for cursor
	private Toolkit toolkit;
	private Image image;
	Point hotspot;

	// pen cursor;
	Cursor blackPenCursor;
	Cursor redPenCursor;
	Cursor highliterCursor;
	Cursor figureCursor;

	// figure cursor;
	Cursor starCursor;
	Cursor arrowCursor;
	Cursor circleCursor;
	Cursor XCursor;
	Cursor recCursor;
	Cursor textCursor;

	// set line property
	Color color;
	BasicStroke bs;

	//�ʱ⸦ �����ϴ� �κ�
	NoteManager nm;
	
	//���� pptPanel �κ�
	PPTPanel pptPanel;
	String filename;

	public PPTPanel(String filename) {
		// TODO Auto-generated constructor stub
		
		pptPanel = this;
		
		//ó�� �ʱ�ȭ
		this.filename = filename;
		nm = getNoteManager();
		setLayout(null);

		

		//Ŀ���� ���� ���� Ŀ���� ����� �ʱ�ȭ
		initCursor();
		setCurMode(NOTE_MODE_PEN);
		setCursor(blackPenCursor);
		bs = new BasicStroke(1);
		color = Color.BLACK;

		// ���� � ��� ����
		curLineMode = LINE_MODE_CURVE;

		this.addMouseListener(new MouseListener() {
			
			//���콺�� �����ٰ� ��������
			@Override
			public void mouseReleased(MouseEvent e) {
				
				//���� ���� �׸��κб��� ����(���̶�� �׸����, figure��� figure��)
				if (curNoteMode == NOTE_MODE_PEN) {

					nm.makePathComplete();

				} else if (curNoteMode == NOTE_MODE_FIGURE) {
					if(curFigureMode == FIGURE_TYPE_CIRCLE || curFigureMode == FIGURE_TYPE_REC) {
						
						nm.makeFigureComplete();
						
					}
				}
			}
			
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

				pptPanel.grabFocus();

				x1 = e.getX();
				y1 = e.getY();

				int clickCount = e.getClickCount();

				if (clickCount == 1) {
					if (curNoteMode == NOTE_MODE_PEN) {

						nm.initPath();

					}
				}

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

				int clickCount = e.getClickCount();
				int clickButton = e.getButton();

				if (clickButton == CLICK_BUTTON_LEFT) {
					if (clickCount == 1) {

						if (curNoteMode == NOTE_MODE_FIGURE) {
							if(curFigureMode == FIGURE_TYPE_STAR) {
								nm.drawImage(x1, y1, 25, 25, Var.IMG_ADDR +"star.png");
							}
							
						}

					} else if (clickCount == 2) {
						// ù��° Ŭ���ߴ� ������ ����������ȴ�.
						if (curNoteMode == NOTE_MODE_FIGURE) {
							nm.removeLastDrawableObj();
						}
						// �״��� �ι�°���� textarea�� �־�������.
						nm.addTextField(x1, y1, 100, 100);
					}

				} else if (clickButton == CLICK_BUTTON_RIGHT) {
					nm.addMemo(x1, y1, 100, 100);
				}

			}
		});

		new Thread() {
			public void run() {

				pptPanel.addMouseMotionListener(new MouseMotionListener() {

					@Override
					public void mouseMoved(MouseEvent e) {
					}

					@Override
					public void mouseDragged(MouseEvent e) {
						// TODO Auto-generated method stub
						// pen ������� �ΰ��� ���еȴ�.
						if (curNoteMode == NOTE_MODE_PEN) {
							// shift�� ������ �����Ƿ� ������ �׸���.
							if (curLineMode == LINE_MODE_STRAIGHT) {

								nm.makePath(new Point(e.getX(), y1), color, bs);

							} else {

								nm.makePath(new Point(e.getX(), e.getY()),color, bs);

							}

						} else if (curNoteMode == NOTE_MODE_FIGURE) {

							nm.makeFigure(x1, y1, e.getX() - x1, e.getY() - y1, curFigureMode);

						}

					}
				});

			}
		}.start();

		pptPanel.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				// key release �ɶ�
				if (curLineMode == LINE_MODE_STRAIGHT) {
					curLineMode = LINE_MODE_CURVE;
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

				int keyCode = e.getKeyCode();

				if (keyCode == 192) {
					if (curNoteMode == NOTE_MODE_FIGURE) {

						setCursor(blackPenCursor);
						setCurMode(NOTE_MODE_PEN);

					} else {

						setCursor(figureCursor);
						setCurMode(NOTE_MODE_FIGURE);

					}
				}

				if (curNoteMode == NOTE_MODE_PEN) {

					if (keyCode == KEY_CODE_SHIFT) {
						curLineMode = LINE_MODE_STRAIGHT;
					}

					if (keyCode == KEY_CODE_ONE) {

						setCursor(blackPenCursor);
						color = Color.black;
						bs = new BasicStroke(1);

					} else if (keyCode == KEY_CODE_TWO) {

						setCursor(redPenCursor);
						color = Color.red;
						bs = new BasicStroke(1);

					} else if (keyCode == KEY_CODE_THREE) {

						setCursor(highliterCursor);
						color = new Color(1, 0, 0, 0.1f); // Red
						bs = new BasicStroke(10);
					}
				} else if (curNoteMode == NOTE_MODE_FIGURE) {

					if (keyCode == KEY_CODE_ONE) {

						setCursor(starCursor);
						setFigureMode(FIGURE_TYPE_STAR);

					} else if (keyCode == KEY_CODE_TWO) {

						setCursor(arrowCursor);
						setFigureMode(FIGURE_TYPE_ARROW);

					} else if (keyCode == KEY_CODE_THREE) {

						setCursor(circleCursor);
						setFigureMode(FIGURE_TYPE_CIRCLE);

					} else if (keyCode == KEY_CODE_FOUR) {

						setCursor(XCursor);
						setFigureMode(FIGURE_TYPE_X);

					} else if (keyCode == KEY_CODE_FIVE) {

						setCursor(recCursor);
						setFigureMode(FIGURE_TYPE_REC);

					} else if (keyCode == KEY_CODE_SIX) {

						setCursor(textCursor);
						setFigureMode(FIGURE_TYPE_TEXT);

					}
				}

			}
		});

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Image bg = new ImageIcon(filename).getImage();
		g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);

		nm.draw(g);
		nm.drawLineByRealTime(g);
		nm.drawFigureByRealTime(g);
	}

	public NoteManager getNoteManager() {
		if (nm == null) {
			nm = new NoteManager(this);
		}
		return nm;
	}

	

	private void initCursor() {

		toolkit = Toolkit.getDefaultToolkit();
		hotspot = new Point(0, 0);

		image = toolkit.getImage(Var.IMG_ADDR + "pen.png");
		blackPenCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

		image = toolkit.getImage(Var.IMG_ADDR + "redpen.png");
		redPenCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

		image = toolkit.getImage(Var.IMG_ADDR + "highlighter.png");
		highliterCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

		image = toolkit.getImage(Var.IMG_ADDR + "figure.png");
		figureCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

		image = toolkit.getImage(Var.IMG_ADDR + "star.png");
		starCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

		image = toolkit.getImage(Var.IMG_ADDR + "arrow.png");
		arrowCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

		image = toolkit.getImage(Var.IMG_ADDR + "circle.png");
		circleCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

		image = toolkit.getImage(Var.IMG_ADDR + "X.png");
		XCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

		image = toolkit.getImage(Var.IMG_ADDR + "rectangle.png");
		recCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

		image = toolkit.getImage(Var.IMG_ADDR + "text.png");
		textCursor = toolkit.createCustomCursor(image, hotspot, "Stone");

	}

	protected int getCurNoteMode() {
		return curNoteMode;
	}

	protected int getCurFigureMode() {
		return curFigureMode;
	}

	protected int getLineMode() {
		return curLineMode;
	}

	private void setCurMode(int mode) {

		curNoteMode = mode;
	}

	private void setFigureMode(int mode) {

		curFigureMode = mode;
	}

}
