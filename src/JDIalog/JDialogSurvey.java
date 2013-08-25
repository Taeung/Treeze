package JDIalog;import java.awt.BasicStroke;import java.awt.BorderLayout;import java.awt.Color;import java.awt.Component;import java.awt.Container;import java.awt.Dimension;import java.awt.Font;import java.awt.Graphics;import java.awt.Graphics2D;import java.awt.GridBagConstraints;import java.awt.GridBagLayout;import java.awt.GridLayout;import java.awt.Insets;import java.awt.RenderingHints;import java.awt.event.ActionListener;import java.awt.event.MouseEvent;import java.awt.event.MouseListener;import java.io.IOException;import java.io.OutputStream;import java.io.PrintWriter;import java.io.UnsupportedEncodingException;import java.util.ArrayList;import javax.swing.BorderFactory;import javax.swing.ButtonGroup;import javax.swing.JButton;import javax.swing.JDialog;import javax.swing.JFrame;import javax.swing.JLabel;import javax.swing.JPanel;import javax.swing.JRadioButton;import javax.swing.border.EmptyBorder;import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;import com.google.gson.Gson;import com.treeze.data.TreezeData;import com.treeze.data.TreezeStaticData;import com.treeze.data.Survey.Survey;public class JDialogSurvey extends JDialog {	JButton okBtn = new JButton("확  인");	JPanel questionBox = new JPanel();	SurveyExamplesPanel surveyExamplesPanel = new SurveyExamplesPanel();	JPanel questionBox3 = new JPanel();	 Insets insets = new Insets(10, 20, 10, 20);	GridBagLayout gbl = new GridBagLayout();	GridBagConstraints gbc = new GridBagConstraints();	OutputStream os;	Survey survey;	ButtonGroup btnGroup = new ButtonGroup();	public JDialogSurvey(final Survey survey,final OutputStream os) {		// TODO Auto-generated constructor stub		setModal(true);	//	setResizable(false);		this.survey = survey;		this.os = os;		// this.setLayout(new BorderLayout(10, 10));		this.setBackground(Color.YELLOW);		this.setSize(700, survey.getSurveyType().getSurveyExamplesInfo().size()*100);		questionBox.setBackground(Color.WHITE);		gbc.fill = GridBagConstraints.BOTH;		okBtn.setSize(100, 50);		questionBox3.setBackground(Color.WHITE);		this.setLayout(new BorderLayout());		questionBox.setLayout(gbl);		String str = null;				try {			 str = new String(survey.getContents().getBytes(), "UTF8");		} catch (UnsupportedEncodingException e) {			// TODO Auto-generated catch block			e.printStackTrace();		}		System.out.println("[survet Contents] "+str);		str = survey.getContents();		JLabel questionContents = new JLabel(str);		questionContents.setFont(new Font(str, Font.BOLD, 20));		//addGrid(gbl, gbc, new JLabel(survey.getContents()), 0, 0, 1, 1, 1, 2, questionBox);		addGrid(gbl, gbc, questionContents, 0, 0, 1, 1, 1, 2, questionBox);		addGrid(gbl, gbc, surveyExamplesPanel, 0, 1, 1, 1, 1, 15, questionBox);		addGrid(gbl, gbc, questionBox3, 0, 2, 1, 1, 1, 1, questionBox);				RadioBtnGroupSet();		questionBox3.add(okBtn,BorderLayout.CENTER);		this.add(questionBox);		// this.add(questionBox,BorderLayout.CENTER);		// this.add(okBtn,BorderLayout.SOUTH);		// this.getContentPane().add(questionBox,BorderLayout.CENTER);		// this.getContentPane().add(okBtn,BorderLayout.SOUTH);		// addGrid(gbl, gbc, questionBox, 0, 0, 1, 1, 1, 1, this);		// addGrid(gbl, gbc, okBtn, 0, 1, 1, 1, 1, 1, this);		// this.getContentPane().add(questionBox);		// this.getContentPane().add(okBtn);				okBtn.addMouseListener(new MouseListener() {						@Override			public void mouseReleased(MouseEvent arg0) {				// TODO Auto-generated method stub							}						@Override			public void mousePressed(MouseEvent arg0) {				// TODO Auto-generated method stub								System.out.println("[RadioGroup Slect] "+btnGroup.getSelection().getActionCommand());				TreezeData TD = new TreezeData();				TD.setDataType(TreezeData.SURVEYVALUE);				Gson gson = new Gson();								survey.getSurveyType().fillOutSurvey(btnGroup.getSelection().getActionCommand());				TD.getArgList().add(gson.toJson(survey));					try {						os.write(gson.toJson(TD).getBytes("UTF-8"));						os.flush();						System.out.print("[Socket Send Value] " + gson.toJson(TD));						setVisible(false);					} catch (IOException e) {						// TODO Auto-generated catch block						e.printStackTrace();					}													}						@Override			public void mouseExited(MouseEvent arg0) {				// TODO Auto-generated method stub							}						@Override			public void mouseEntered(MouseEvent arg0) {				// TODO Auto-generated method stub							}						@Override			public void mouseClicked(MouseEvent arg0) {				// TODO Auto-generated method stub							}		});		setVisible(true);		}	public void RadioBtnGroupSet(){		ArrayList<String> strList = survey.getSurveyType().getSurveyExamplesInfo();		surveyExamplesPanel.setLayout(new GridLayout(strList.size(),1));		for(int i=0;i<strList.size();i++){			JRadioButton radioBtn = new JRadioButton(strList.get(i));						if(i==0) radioBtn.setSelected(true);			radioBtn.setActionCommand(strList.get(i));			btnGroup.add(radioBtn);			surveyExamplesPanel.add(radioBtn);			//radioBtn.addActionListener(new ActionListener)		}			}	private void addGrid(GridBagLayout gbl, GridBagConstraints gbc,			Component c, int gridx, int gridy, int gridwidth, int gridheight,			int weightx, int weighty, Container container) {		gbc.gridx = gridx;		gbc.gridy = gridy;		gbc.gridwidth = gridwidth;		gbc.gridheight = gridheight;		gbc.weightx = weightx;		gbc.weighty = weighty;		gbc.insets = insets;		gbl.setConstraints(c, gbc);		container.add(c);	}	class SurveyExamplesPanel extends JPanel {		protected Color shadowColor = Color.black;		/** Sets if it drops shadow */		protected boolean shady = true;		/** Sets if it has an High Quality view */		protected boolean highQuality = true;		protected int shadowGap = 1;		/** The offset of shadow. */		protected int shadowOffset = 1;		/** The transparency value of shadow. ( 0 - 255) */		protected int shadowAlpha = 150;		protected int strokeSize = 2;		protected Dimension arcs = new Dimension(30, 30);				public SurveyExamplesPanel() {			// TODO Auto-generated constructor stub			this.setBackground(TreezeStaticData.TREEZE_BG_COLOR);			this.setSize(10, 10);		}		protected void paintComponent(Graphics g) { // 테두리를 둥글게 하기위해			super.paintComponent(g);			int width = getWidth();			int height = getHeight();			int shadowGap = this.shadowGap;			Color shadowColorA = new Color(shadowColor.getRed(),					shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);			Graphics2D graphics = (Graphics2D) g;			// Sets antialiasing if HQ.			if (highQuality) {				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,						RenderingHints.VALUE_ANTIALIAS_ON);			}			// Draws shadow borders if any.			if (shady) {				graphics.setColor(shadowColorA);				graphics.fillRoundRect(shadowOffset,// X position						shadowOffset,// Y position						width - strokeSize - shadowOffset, // width						height - strokeSize - shadowOffset, // height						arcs.width, arcs.height);// arc Dimension			} else {				shadowGap = 1;			}			// Draws the rounded opaque panel with borders.			graphics.setColor(TreezeStaticData.TREEZE_BG_COLOR);			graphics.fillRoundRect(0, 0, width - shadowGap, height - shadowGap,					arcs.width, arcs.height);			graphics.setColor(TreezeStaticData.TREEZE_BG_COLOR);			graphics.setStroke(new BasicStroke(strokeSize));			graphics.drawRoundRect(0, 0, width - shadowGap, height - shadowGap,					arcs.width, arcs.height);			// Sets strokes to default, is better.			graphics.setStroke(new BasicStroke());		}	}}