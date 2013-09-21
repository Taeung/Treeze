package com.treeze.frame;import java.awt.BorderLayout;import java.awt.Dimension;import java.awt.GridBagConstraints;import java.awt.GridBagLayout;import java.awt.GridLayout;import java.awt.Insets;import java.awt.Toolkit;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.FocusEvent;import java.awt.event.FocusListener;import java.awt.event.MouseEvent;import java.awt.event.MouseListener;import java.awt.event.WindowEvent;import java.awt.event.WindowListener;import java.io.IOException;import java.util.ArrayList;import javax.swing.BoxLayout;import javax.swing.ImageIcon;import javax.swing.JButton;import javax.swing.JFrame;import javax.swing.JMenu;import javax.swing.JMenuBar;import javax.swing.JMenuItem;import javax.swing.JPanel;import javax.swing.JToolBar;import com.treeze.data.ClassInfo;import com.treeze.data.MindNode;import com.treeze.data.ServerSocket;import com.treeze.data.TreezeStaticData;import com.treeze.data.User;import com.treeze.draw.NoteManager;import com.treeze.draw.PPTPanel;import com.treeze.uploadthread.CreateNote;public class MainFrameManager extends JFrame implements ActionListener {	JPanel fullPanel = new JPanel();	Dimension screenSize;	MindMapMain mindmapPanel;	PPTPanel pptPanel;	MindNode node;	JPanel topPanel = new JPanel();	JPanel topLeftPanel;	JPanel topRightPanel = new JPanel();;	GridBagLayout gbl = new GridBagLayout();	GridBagConstraints gbc = new GridBagConstraints();	Insets insets = new Insets(0, 0, 0, 0);	ClassInfo classInfo;	JMenuBar jMenuBar = new JMenuBar();	PPTPanel curPtPanel;	private JMenu noteMenu;	private JMenu helpMenu;	private JMenu formatMenu;	JButton blackPenBtn;	JButton redPenBtn;	JButton yellowPenBtn;	ArrayList<PPTPanel> ptPanelAL;	public MainFrameManager(MindMapMain mindmapPanel, ClassInfo classInfo) {		// TODO Auto-generated constructor stub		// Jme		ptPanelAL = new ArrayList<PPTPanel>();		setIconImage(TreezeStaticData.TREEZE_ICON_IMG);		noteMenu = new JMenu("�ʱ�");		noteMenu.setMnemonic('N');		helpMenu = new JMenu("Help");		helpMenu.setMnemonic('H');		// Create file menu items		JMenuItem black_Pen_Item = new JMenuItem("������");		JMenuItem red_Pen_Item = new JMenuItem("������");		JMenuItem yellow_Pen_Item = new JMenuItem("������");		// aboutItem.setMnemonic(1);		black_Pen_Item.setMnemonic('1');		black_Pen_Item.setEnabled(true);		black_Pen_Item.addActionListener(this);		red_Pen_Item.setMnemonic('2');		red_Pen_Item.setEnabled(true);		red_Pen_Item.addActionListener(this);		yellow_Pen_Item.setMnemonic('3');		yellow_Pen_Item.setEnabled(true);		yellow_Pen_Item.addActionListener(this);		// Add to menu		noteMenu.add(black_Pen_Item);		noteMenu.add(red_Pen_Item);		noteMenu.add(yellow_Pen_Item);		noteMenu.addSeparator();		JMenuItem bug_Report = new JMenuItem("Bug Report");		JMenuItem check_Update = new JMenuItem("Check Update");		JMenuItem project_Site = new JMenuItem("Project Site");		bug_Report.setMnemonic('b');		bug_Report.setEnabled(true);		bug_Report.addActionListener(this);		check_Update.setMnemonic('c');		check_Update.setEnabled(true);		check_Update.addActionListener(this);		project_Site.setMnemonic('p');		project_Site.setEnabled(true);		project_Site.addActionListener(this);		addWindowListener(new WindowListener() {			@Override			public void windowOpened(WindowEvent arg0) {				// TODO Auto-generated method stub			}			@Override			public void windowIconified(WindowEvent arg0) {				// TODO Auto-generated method stub			}			@Override			public void windowDeiconified(WindowEvent arg0) {				// TODO Auto-generated method stub			}			@Override			public void windowDeactivated(WindowEvent arg0) {				// TODO Auto-generated method stub			}			@Override			public void windowClosing(WindowEvent arg0) {				// TODO Auto-generated method stub				// TODO Auto-generated method stub				ServerSocket sv = ServerSocket.getInstance();				MainFrameManager.this.setVisible(false);				new ProfileFrame();				try {					if (topLeftPanel != null) {						PPTPanel ptPanel = (PPTPanel) topLeftPanel;						User user = User.getInstance();						ClassInfo ci = ClassInfo.getInstance();						new CreateNote(ci.getClassId(), ptPanel.getNodeID(),								ptPanel.getStoreNoteContents(),								user.getUserEmail()).start();					}					System.out.println("[ServerSocket Closed]");					sv.getSocket().getInputStream().close();					sv.getSocket().close();				} catch (IOException e) {					// TODO Auto-generated catch block					e.printStackTrace();				}			}			@Override			public void windowClosed(WindowEvent arg0) {			}			@Override			public void windowActivated(WindowEvent arg0) {				// TODO Auto-generated method stub			}		});		helpMenu.add(bug_Report);		helpMenu.add(check_Update);		helpMenu.add(project_Site);		helpMenu.addSeparator();		setJMenuBar(jMenuBar);		jMenuBar.add(noteMenu);		jMenuBar.add(helpMenu);		JToolBar noteToolBar = new JToolBar();		JPanel panel = new JPanel();		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));		ImageIcon black_Pen_Img = TreezeStaticData.makeResizedImageIcon(15, 15,				TreezeStaticData.BLACK_PEN_IMG);		ImageIcon red_Pen_Img = TreezeStaticData.makeResizedImageIcon(15, 15,				TreezeStaticData.RED_PEN_IMG);		ImageIcon yellow_Pen_Img = TreezeStaticData.makeResizedImageIcon(15,				15, TreezeStaticData.YELLOW_PEN_IMG);		blackPenBtn = new JButton(black_Pen_Img);		redPenBtn = new JButton(red_Pen_Img);		yellowPenBtn = new JButton(yellow_Pen_Img);		this.blackPenBtn.addMouseListener(new ToolBarBtnMouseBtn());		this.redPenBtn.addMouseListener(new ToolBarBtnMouseBtn());		this.yellowPenBtn.addMouseListener(new ToolBarBtnMouseBtn());		noteToolBar.add(blackPenBtn);		noteToolBar.add(redPenBtn);		noteToolBar.add(yellowPenBtn);		noteToolBar.setAlignmentX(0);		panel.add(noteToolBar);		this.mindmapPanel = mindmapPanel;		this.classInfo = classInfo;		topPanel.setLayout(new GridLayout(1, 2));		mindmapPanel.setMainFrameManager(this);		gbc.fill = GridBagConstraints.BOTH;		screenSize = Toolkit.getDefaultToolkit().getScreenSize();		setSize(screenSize.width, screenSize.height);		topLeftPanel = new JPanel();		topPanel.add(topLeftPanel);		topPanel.add(topRightPanel);		this.setLayout(new BorderLayout());		fullPanel.setLayout(new GridLayout(2, 1));		fullPanel.add(topPanel,0);		fullPanel.add(mindmapPanel,1);		add(panel, BorderLayout.NORTH);		this.add(fullPanel);		this.setResizable(false);		changeTopPanel(MindNode.getRoot().getChildeNodes().get(0));		setVisible(true);		// added by doo mindnode Array list	}	public void changeTopPanel(MindNode node) {		this.setResizable(false);		this.node = node;		MindNode.setNowPPTNode(node);		// ���� �ϱ����� ����Ƽ�� ������		if (topLeftPanel != null && topLeftPanel instanceof PPTPanel) {			PPTPanel ptPanel = (PPTPanel) topLeftPanel;			MindNode exMindNode = ptPanel.getMindNode();			// ptPanel.saveNote();			// ����� ������ ��� ������..			User user = User.getInstance();			ClassInfo ci = ClassInfo.getInstance();			System.out.println("[Upload Node classInfo] = " + ci.getClassId());			new CreateNote(ci.getClassId(), ptPanel.getNodeID(),					ptPanel.getStoreNoteContents(), user.getUserEmail())					.start();		}		topPanel.remove(topLeftPanel);		topPanel.remove(topRightPanel);		fullPanel.remove(topPanel);		//fullPanel.remove(mindmapPanel);		topLeftPanel = new PPTPanel(node, this);		PPTPanel curPtPanel = (PPTPanel) topLeftPanel;		curPtPanel.setBlackPen();		topRightPanel = new TicketFrame(node, classInfo);		resetAllPanel();	}	public void resetAllPanel() {				topPanel.add(topLeftPanel);		topPanel.add(topRightPanel);		fullPanel.add(topPanel,0);		//fullPanel.add(mindmapPanel);		topLeftPanel.setVisible(false);		topLeftPanel.setVisible(true);		topRightPanel.setVisible(false);		topRightPanel.setVisible(true);			}	public void ticketRepaint(MindNode node) {		if (this.node != null && this.node == node) {			node.getTicketBtn().setVisible(false);			topPanel.remove(topRightPanel);			topRightPanel = new TicketFrame(node, classInfo);			topPanel.add(topRightPanel);			topPanel.setVisible(false);			topPanel.setVisible(true);		}	}	@Override	public void actionPerformed(ActionEvent event) {		// TODO Auto-generated method stub		PPTPanel curPtPanel = (PPTPanel) topLeftPanel;		if (event.getActionCommand().equals("������"))			curPtPanel.setBlackPen();		// System.out.println("asdfsadfsadfsdaf������");		else if (event.getActionCommand().equals("������"))			curPtPanel.setRedPen();		else if (event.getActionCommand().equals("������"))			curPtPanel.setHighliter();		else if (event.getActionCommand().equals("Bug Report"))			new BugReportFrame();		else if (event.getActionCommand().equals("Check Update"))			System.out.println("Check Update");		else if (event.getActionCommand().equals("Project Site"))			System.out.println("Project Site");			}	class ToolBarBtnMouseBtn implements MouseListener {		public ToolBarBtnMouseBtn() {			// TODO Auto-generated constructor stub		}		@Override		public void mouseClicked(MouseEvent e) {			PPTPanel curPtPanel = (PPTPanel) topLeftPanel;			// TODO Auto-generated method stub			if (e.getComponent().equals(blackPenBtn)) {				curPtPanel.setBlackPen();			} else if (e.getComponent().equals(redPenBtn)) {				curPtPanel.setRedPen();			} else if (e.getComponent().equals(yellowPenBtn)) {				curPtPanel.setHighliter();			}		}		@Override		public void mouseEntered(MouseEvent e) {			// TODO Auto-generated method stub		}		@Override		public void mouseExited(MouseEvent e) {			// TODO Auto-generated method stub		}		@Override		public void mousePressed(MouseEvent e) {			// TODO Auto-generated method stub		}		@Override		public void mouseReleased(MouseEvent e) {			// TODO Auto-generated method stub		}	}}