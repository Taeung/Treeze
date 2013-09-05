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

import com.treeze.data.MindNode;
import com.treeze.data.TreezeStaticData;
import com.treeze.frame.MainFrameManager;

/*
 * pptpanel�� ��Ÿ��
 */
public class PPTPanel extends JPanel {

	// canvas postion
	public static int pressX;
	public static int x2;
	public static int pressY;
	public static int y2;

	private Point draggedExPoint;
	// ���� pptPanel �κ�
	private static PPTPanel pptPanel;
	// String filename;
	StateManager sm;

	DrawablePanel dp;

	NoteManager nm;

	MindNode node;

	public String getNodeID() {
		return node.getNodeID();
	}

	public void saveNote() {
		getNoteManager().saveStoredNote(node.getNodeID());
	}

	public PPTPanel(MindNode node, MainFrameManager mfm) {
		// TODO Auto-generated constructor stub
		super();

		pptPanel = this;
		this.node = node;
		// ó�� �ʱ�ȭ
		dp = new DrawablePanel(this, (TreezeStaticData.PPT_IMG_PATH + "/"
				+ node.getImgPath() + ".jpg"));
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

		nm.loadNote(node.getNodeID());

		this.addMouseListener(new MouseListener() {

			// ���콺�� �����ٰ� ��������
			@Override
			public void mouseReleased(MouseEvent e) {

				// ���� ���� �׸��κб��� ����(���̶�� �׸����, figure��� figure��)
				sm.setExMouseMode(sm.getCurMouseMode());
				sm.setCurMouseMode(StateManager.MOUSE_STATE_RELEASED);

				// System.out.println("" + sm.isChangeSizeFlag() +
				// sm.isMoveFlag() + sm.getExMouseMode() + sm.getCurNoteMode());

				if (sm.getExMouseMode() == StateManager.MOUSE_STATE_DRAGGED) {
					if (nm.isMoveFlag()) {
						nm.initMoveFlag();
						// System.out.println("initMoveFlag");
					} else {
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
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

				sm.setCurMouseMode(StateManager.MOUSE_STATE_PRESSED);

				pptPanel.grabFocus();

				pressX = e.getX();
				pressY = e.getY();

				int clickCount = e.getClickCount();

				if (clickCount == 1) {
					if (nm.isClickableItem(pressX, pressY)) {

						nm.setMoveFlag(pressX, pressY);
						draggedExPoint = new Point(pressX, pressY);
					} else {

						if (sm.getCurNoteMode() == StateManager.NOTE_MODE_PEN) {
							nm.initPath();
						}
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

						if (nm.isClickableItem(pressX, pressY)) {
							nm.setClick(pressX, pressY);
						} else {
							nm.setUnClicked();
							if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {
								if (sm.getCurFigureMode() == StateManager.FIGURE_TYPE_STAR) {
									nm.drawImage(pressX, pressY,
											(pptPanel.getWidth() * 0.05),
											(pptPanel.getHeight() * 0.05),
											NoteManager.IMG_TYPE_STAR);
								}

							} else if (sm.getCurNoteMode() == StateManager.NOTE_MODE_ERASER) {

								nm.setClick(pressX, pressY);

							}
						}

					} else if (clickCount == 2) {
						// ù��° Ŭ���ߴ� ������ ����������ȴ�.
						if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {
							if (sm.getCurFigureMode() == StateManager.FIGURE_TYPE_STAR) {
								nm.removeLastDrawableObj();
							}
						}
						// �״��� �ι�°���� textarea�� �־�������
						if (sm.getCurNoteMode() != StateManager.NOTE_MODE_ERASER)
							nm.addMemo(pressX, pressY, 100, 100);

					}

				} else if (clickButton == StateManager.CLICK_BUTTON_RIGHT) {

					// nm.saveStoredNote();

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

						if (nm.isClickableItem(x, y)) {
							setCursor(StateManager.moveCursor);
						} else {
							setCursor(sm.getCurStateCursor());
						}
					}

					@Override
					public void mouseDragged(MouseEvent e) {
						// TODO Auto-generated method stub
						// pen ������� �ΰ��� ���еȴ�.
						// curMouseMode = MOUSE_STATE_DRAGGED;
						sm.setCurMouseMode(StateManager.MOUSE_STATE_DRAGGED);

						setCursor(sm.getCurStateCursor());
						if (nm.isMoveFlag()) {
							nm.move(draggedExPoint.x, draggedExPoint.y,
									e.getX(), e.getY());
							draggedExPoint = new Point(e.getX(), e.getY());
						} else {
							if (sm.getCurNoteMode() == StateManager.NOTE_MODE_PEN) {
								// shift�� ������ �����Ƿ� ������ �׸���.
								if (sm.getCurLineMode() == StateManager.LINE_MODE_STRAIGHT) {

									nm.makePath(
											new LinePoint(e.getX(), pressY),
											sm.getColor(), sm.getBs());

								} else {

									nm.makePath(
											new LinePoint(e.getX(), e.getY()),
											sm.getColor(), sm.getBs());

								}

							} else if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {

								nm.makeFigure(pressX, pressY,
										e.getX() - pressX, e.getY() - pressY,
										sm.getCurFigureMode());
							}
						}
					}
				});

			}
		}.start();

		pptPanel.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println("sadfasdfdsafdasfdasfdsafdasfadsf");
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
				System.out.println("asdfadsfdasfadsfdasfdsafdasfdsafadsfadsfdasf");

				System.out.println("cur keycode : " + keyCode);

				if (keyCode == StateManager.KEY_CODE_DEL) {
					nm.removeSelectedItem();
				}
				// mode change
				// if (keyCode == 192) {
				//
				// if (sm.getCurNoteMode() == StateManager.NOTE_MODE_FIGURE) {
				//
				// setCursor(StateManager.blackPenCursor);
				// sm.setCurNoteMode(StateManager.NOTE_MODE_PEN);
				//
				// } else {
				//
				// setCursor(StateManager.figureCursor);
				// sm.setCurNoteMode(StateManager.NOTE_MODE_FIGURE);
				//
				// }
				// }

				if (sm.getCurNoteMode() == StateManager.NOTE_MODE_PEN) {

					if (keyCode == StateManager.KEY_CODE_SHIFT) {

						sm.setCurLineMode(StateManager.LINE_MODE_STRAIGHT);
					}

					if (keyCode == StateManager.KEY_CODE_ONE) {

						setBlackPen();

					} else if (keyCode == StateManager.KEY_CODE_TWO) {

						setRedPen();

					} else if (keyCode == StateManager.KEY_CODE_THREE) {

						setHighliter();
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

	public void setBlackPen() {

		setCursor(StateManager.blackPenCursor);
		sm.setColor(Color.BLACK);
		sm.setBs(new BasicStroke(1));
		sm.setCurPenMode(StateManager.PEN_MODE_BLACK);

	}

	public void setRedPen() {

		setCursor(StateManager.redPenCursor);
		sm.setColor(Color.red);
		sm.setBs(new BasicStroke(3));
		sm.setCurPenMode(StateManager.PEN_MODE_RED);

	}

	public void setHighliter() {

		setCursor(StateManager.highliterCursor);
		sm.setColor(new Color(1f, 1f, 0.2f, 0.1f));
		sm.setBs(new BasicStroke(10));
		sm.setCurPenMode(StateManager.PEN_MODE_HIGHLIGHTER);

	}

}
