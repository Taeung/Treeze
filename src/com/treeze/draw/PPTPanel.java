package com.treeze.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

/*
 * pptpanel�� ��Ÿ��
 */
public class PPTPanel extends JPanel {

	// canvas postion
	public static int x1;
	public static int x2;
	public static int y1;
	public static int y2;
	// ���� pptPanel �κ�
	PPTPanel pptPanel;
//	String filename;
	StateManager sm;
	
	DrawablePanel dp;
	
	NoteManager nm;

	
	public PPTPanel(String filename) {
		// TODO Auto-generated constructor stub
		super();
		System.out.println(filename);
		pptPanel = this;
		// ó�� �ʱ�ȭ
		dp = new DrawablePanel(this, filename);
		
		nm = dp.getNoteManager();
		setLayout(null);

		sm = StateManager.getStateManager();

		// Ŀ���� ���� ���� Ŀ���� ����� �ʱ�ȭ

		sm.setCurNoteMode(StateManager.NOTE_MODE_PEN);
		setCursor(StateManager.blackPenCursor);

		sm.setColor(Color.BLACK);
		sm.setBs(new BasicStroke(1));

		// ���� � ��� ����
		sm.setCurLineMode(StateManager.LINE_MODE_CURVE);


		this.addMouseListener(new MouseListener() {

			// ���콺�� �����ٰ� ��������
			@Override
			public void mouseReleased(MouseEvent e) {

				// ���� ���� �׸��κб��� ����(���̶�� �׸����, figure��� figure��)
				sm.setExMouseMode(sm.getCurMouseMode());
				sm.setCurMouseMode(StateManager.MOUSE_STATE_RELEASED);
				
				System.out.println("" + sm.isChangeSizeFlag() + sm.isMoveFlag() + sm.getExMouseMode());

				if (sm.getExMouseMode() == StateManager.MOUSE_STATE_DRAGGED) {
					if (sm.getCurNoteMode() == StateManager.NOTE_MODE_PEN) {

						nm.makePathComplete();

					} else if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {
						if (sm.getCurFigureMode() == StateManager.FIGURE_TYPE_CIRCLE
								|| sm.getCurFigureMode() == StateManager.FIGURE_TYPE_REC) {

							nm.makeFigureComplete();

						}
					}
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

				// curMouseMode = MOUSE_STATE_PRESSED;
				sm.setCurMouseMode(StateManager.MOUSE_STATE_PRESSED);

				pptPanel.grabFocus();

				x1 = e.getX();
				y1 = e.getY();

				int clickCount = e.getClickCount();

				if (clickCount == 1) {
					if (sm.getCurNoteMode() == StateManager.NOTE_MODE_PEN) {

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

				if (clickButton == StateManager.CLICK_BUTTON_LEFT) {
					
					if (clickCount == 1) {
						
						if(nm.isClickableItem(x1, y1)) {
							nm.setClick(x1, y1);
						} else {
							nm.setUnClicked();
							if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {
								if (sm.getCurFigureMode() == StateManager.FIGURE_TYPE_STAR) {
									nm.drawImage(x1, y1, 40, 40, NoteManager.IMG_TYPE_STAR);
								}

							}else if(sm.getCurNoteMode() == StateManager.NOTE_MODE_ERASER) {
								
								nm.setClick(x1, y1);
								
							}
						}



					} else if (clickCount == 2) {
						// ù��° Ŭ���ߴ� ������ ����������ȴ�.
						if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {
							if(sm.getCurFigureMode() == StateManager.FIGURE_TYPE_STAR) {
								nm.removeLastDrawableObj();
							}
						}
						// �״��� �ι�°���� textarea�� �־�������
						if(sm.getCurNoteMode() != StateManager.NOTE_MODE_ERASER)

							nm.addMemo(x1, y1, 100, 100);

					}

				} else if (clickButton == StateManager.CLICK_BUTTON_RIGHT) {

					nm.addPostIt(x1, y1, 100, 100);
					
				}

			}
		});

		new Thread() {
			public void run() {

				pptPanel.addMouseMotionListener(new MouseMotionListener() {

					@Override
					public void mouseMoved(MouseEvent e) {
						
						int x = e.getX();
						int y = e.getY();
						
						if(nm.isClickableItem(x, y)) {
							setCursor(StateManager.moveCursor);
						}else {
							setCursor(sm.getCurStateCursor());
						}
					}

					@Override
					public void mouseDragged(MouseEvent e) {
						// TODO Auto-generated method stub
						// pen ������� �ΰ��� ���еȴ�.
						// curMouseMode = MOUSE_STATE_DRAGGED;
						sm.setCurMouseMode(StateManager.MOUSE_STATE_DRAGGED);

						if (sm.getCurNoteMode() == StateManager.NOTE_MODE_PEN) {
							// shift�� ������ �����Ƿ� ������ �׸���.
							if (sm.getCurLineMode() == StateManager.LINE_MODE_STRAIGHT) {

								nm.makePath(new Point(e.getX(), y1), sm.getColor(), sm.getBs());

							} else {

								nm.makePath(new Point(e.getX(), e.getY()), sm.getColor(), sm.getBs());

							}

						} else if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {

							nm.makeFigure(x1, y1, e.getX() - x1, e.getY() - y1,
									sm.getCurFigureMode());

						}

					}
				});

			}
		}.start();

		pptPanel.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				// key release �ɶ�
				if (sm.getCurLineMode() == StateManager.LINE_MODE_STRAIGHT) {
					// curLineMode = LINE_MODE_CURVE;
					sm.setCurLineMode(StateManager.LINE_MODE_CURVE);
				}

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated metdehod stub

				int keyCode = e.getKeyCode();
				
				System.out.println("cur keycode : " + keyCode);
				
				if (keyCode == StateManager.KEY_CODE_DEL) {
					nm.removeSelectedItem();
				}					

				if (keyCode == 192) {
					nm.saveStoredNote();
					if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {

						setCursor(StateManager.blackPenCursor);
						sm.setCurNoteMode(StateManager.NOTE_MODE_PEN);

					} else {

						setCursor(StateManager.figureCursor);
						sm.setCurNoteMode(StateManager.NOTE_MODE_FIGURE);

					}
				}

				if (sm.getCurNoteMode() == StateManager.NOTE_MODE_PEN) {

					if (keyCode == StateManager.KEY_CODE_SHIFT) {

						sm.setCurLineMode(StateManager.LINE_MODE_STRAIGHT);
					}

					if (keyCode == StateManager.KEY_CODE_ONE) {

						setCursor(StateManager.blackPenCursor);
						sm.setColor(Color.BLACK);
						sm.setBs(new BasicStroke(1));
						sm.setCurPenMode(StateManager.PEN_MODE_BLACK);

					} else if (keyCode == StateManager.KEY_CODE_TWO) {

						setCursor(StateManager.redPenCursor);
						sm.setColor(Color.red);
						sm.setBs(new BasicStroke(3));
						sm.setCurPenMode(StateManager.PEN_MODE_RED);

					} else if (keyCode == StateManager.KEY_CODE_THREE) {

						setCursor(StateManager.highliterCursor);
						sm.setColor(new Color(1f, 1f, 0.2f, 0.1f));
						sm.setBs(new BasicStroke(10));
						sm.setCurPenMode(StateManager.PEN_MODE_HIGHLIGHTER);
					}
				} else if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {

					if (keyCode == StateManager.KEY_CODE_ONE) {

						setCursor(StateManager.starCursor);
						sm.setCurFigureMode(StateManager.FIGURE_TYPE_STAR);

					} else if (keyCode == StateManager.KEY_CODE_TWO) {

						setCursor(StateManager.arrowCursor);
						sm.setCurFigureMode(StateManager.FIGURE_TYPE_ARROW);

					} else if (keyCode == StateManager.KEY_CODE_THREE) {

						setCursor(StateManager.circleCursor);
						sm.setCurFigureMode(StateManager.FIGURE_TYPE_CIRCLE);

					} else if (keyCode == StateManager.KEY_CODE_FOUR) {

						setCursor(StateManager.XCursor);
						sm.setCurFigureMode(StateManager.FIGURE_TYPE_X);

					} else if (keyCode == StateManager.KEY_CODE_FIVE) {

						setCursor(StateManager.recCursor);
						sm.setCurFigureMode(StateManager.FIGURE_TYPE_REC);

					} else if (keyCode == StateManager.KEY_CODE_SIX) {

						setCursor(StateManager.textCursor);
						sm.setCurFigureMode(StateManager.FIGURE_TYPE_TEXT);

					}
				}

			}
		});

	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		dp.paintComponent(g);
		
	}
	
	public NoteManager getNoteManager() {
		return nm;
	}
	

}
