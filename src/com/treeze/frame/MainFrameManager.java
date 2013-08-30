package com.treeze.frame;import java.awt.Component;import java.awt.Container;import java.awt.Dimension;import java.awt.GridBagConstraints;import java.awt.GridBagLayout;import java.awt.GridLayout;import java.awt.Insets;import java.awt.Toolkit;import javax.swing.JFrame;import javax.swing.JMenuBar;import javax.swing.JPanel;import com.treeze.data.ClassInfo;import com.treeze.data.MindNode;import com.treeze.draw.PPTFrame;import com.treeze.draw.PPTPanel;import com.treeze.draw.Util;import com.treeze.frame.MindMapMain.SocketThread;public class MainFrameManager extends JFrame {	Dimension screenSize;	MindMapMain mindmapPanel;	PPTPanel pptPanel;	MindNode node;	JPanel topPanel = new JPanel();	JPanel topLeftPanel;	JPanel topRightPanel = new JPanel();;	GridBagLayout gbl = new GridBagLayout();	GridBagConstraints gbc = new GridBagConstraints();	Insets insets = new Insets(0, 0, 0, 0);	ClassInfo classInfo;	public MainFrameManager(MindMapMain mindmapPanel,ClassInfo classInfo) {		// TODO Auto-generated constructor stub		this.mindmapPanel = mindmapPanel;		this.classInfo = classInfo;		topPanel.setLayout(new GridLayout(1, 2));		mindmapPanel.setMainFrameManager(this);		gbc.fill = GridBagConstraints.BOTH;		screenSize = Toolkit.getDefaultToolkit().getScreenSize();		setSize(screenSize.width, screenSize.height);		topLeftPanel = new JPanel();		topPanel.add(topLeftPanel);		topPanel.add(topRightPanel);		this.setLayout(new GridLayout(2, 1));		this.add(topPanel);		this.add(mindmapPanel);			setVisible(true);	}	public void changeTopPanel(MindNode node){		this.node = node;		System.out.println("[changeTopPanel]");		topPanel.remove(topLeftPanel);		topPanel.remove(topRightPanel);		this.remove(topPanel);		this.remove(mindmapPanel);		topLeftPanel = new PPTPanel(node);		topRightPanel =  new TicketFrame(node, classInfo);		resetAllPanel();		}	public void resetAllPanel(){				topPanel.add(topLeftPanel);		topPanel.add(topRightPanel);		this.add(topPanel);		this.add(mindmapPanel);		topPanel.setVisible(false);		topPanel.setVisible(true);			}	public void ticketRepaint(MindNode node){		if(this.node!=null&&this.node ==node){		topPanel.remove(topRightPanel);		topRightPanel =  new TicketFrame(node, classInfo);		topPanel.add(topRightPanel);		topPanel.setVisible(false);		topPanel.setVisible(true);		}	}	private void addGrid(GridBagLayout gbl, GridBagConstraints gbc,Component c, int gridx, int gridy, int gridwidth, int gridheight,			int weightx, int weighty, Container container) {		gbc.gridx = gridx;		gbc.gridy = gridy;		gbc.gridwidth = gridwidth;		gbc.gridheight = gridheight;		gbc.weightx = weightx;		gbc.weighty = weighty;		gbc.insets = insets;		gbl.setConstraints(c, gbc);		container.add(c);	}}