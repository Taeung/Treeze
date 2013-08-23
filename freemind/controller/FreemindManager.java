package freemind.controller;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import freemind.json.Ticket;
import freemind.main.ProfileFrame;
import freemind.modes.UploadToServer;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapMapModel;

public class FreemindManager {
	private static FreemindManager fInstance;
//	public String SERVERIP = "113.198.84.80";
	public String SERVERIP = "14.63.215.88";
	
	public int PORT = 2141;

	public static FreemindManager getInstance(){
		if(fInstance == null){
			fInstance = new FreemindManager();
		}
		return fInstance;
	}
	
	private FreemindManager(){
		slideShow = new SlideShow(this);
		uploadToServer = new UploadToServer(this);
	}
	
	MindMapController mc;
	Controller c;

	OutputStream os;
	InputStream in;

	private boolean isSlideShowInfo = false;
	private boolean isAddQuestionNodeInfo = false;
	private boolean isCheckNodeType = false;



	private String ticketTitle;
	private String ticketContent;
	private String ticketWriter;
	
	private String filePath;// = "/Users/dewlit/Desktop/test/Linux/";
	private String downPath = System.getProperty("user.home") + System.getProperty("file.separator") + "Treeze";

	private int classId = 1;
	private int pdfPage;
	private MindMapMapModel mModel;
	private ProfileFrame profileFrame;
	private JFrame freemindMainFrame;
	private Ticket ticket;
	
	SlideShow slideShow;
	public UploadToServer uploadToServer;
	

	public Color treezeColor = new Color(141, 198, 63);
	
	//(Toolkit.getDefaultToolkit().getImage("images/treezeLogo.png"));
	public Image treezeLogo = new ImageIcon(getClass().getClassLoader().getResource("images/treezeLogo.png")).getImage();
	public Image loginInputBar = new ImageIcon(getClass().getClassLoader().getResource("images/LoginInputBar.png")).getImage();
	public Image activateBar = new ImageIcon(getClass().getClassLoader().getResource("images/activatebar.png")).getImage();
	public Image professorImg = new ImageIcon(getClass().getClassLoader().getResource("images/minsuk.jpg")).getImage();
	
	public Image loginDefault = new ImageIcon(getClass().getClassLoader().getResource("images/loginBtn1.png")).getImage();
	public Image loginOver = new ImageIcon(getClass().getClassLoader().getResource("images/loginBtn2.png")).getImage();
	public Image loginPress = new ImageIcon(getClass().getClassLoader().getResource("images/loginBtn3.png")).getImage();
	public Image writeDefault = new ImageIcon(getClass().getClassLoader().getResource("images/write1.png")).getImage();
	public Image writeOver = new ImageIcon(getClass().getClassLoader().getResource("images/write2.png")).getImage();
	public Image writePress = new ImageIcon(getClass().getClassLoader().getResource("images/write3.png")).getImage();
	public Image closeDefault = new ImageIcon(getClass().getClassLoader().getResource("images/close1.png")).getImage();
	public Image closeOver = new ImageIcon(getClass().getClassLoader().getResource("images/close2.png")).getImage();
	public Image closePress = new ImageIcon(getClass().getClassLoader().getResource("images/close3.png")).getImage();
	public Image sendDefault = new ImageIcon(getClass().getClassLoader().getResource("images/send1.png")).getImage();
	public Image sendOver = new ImageIcon(getClass().getClassLoader().getResource("images/send2.png")).getImage();
	public Image sendPress = new ImageIcon(getClass().getClassLoader().getResource("images/send3.png")).getImage();
	public Image addLectureDefault = new ImageIcon(getClass().getClassLoader().getResource("images/addlecture1.png")).getImage();
	public Image addLectureOver = new ImageIcon(getClass().getClassLoader().getResource("images/addlecture2.png")).getImage();
	public Image addLecturePress = new ImageIcon(getClass().getClassLoader().getResource("images/addlecture3.png")).getImage();
	public Image deleteLectureDefault = new ImageIcon(getClass().getClassLoader().getResource("images/deletelecture1.png")).getImage();
	public Image deleteLectureOver = new ImageIcon(getClass().getClassLoader().getResource("images/deletelecture2.png")).getImage();
	public Image deleteLecturePress = new ImageIcon(getClass().getClassLoader().getResource("images/deletelecture3.png")).getImage();
	public Image lectureListDefault = new ImageIcon(getClass().getClassLoader().getResource("images/lecturelist1.png")).getImage();
	public Image lectureListOver = new ImageIcon(getClass().getClassLoader().getResource("images/lecturelist2.png")).getImage();
	public Image lectureListPress = new ImageIcon(getClass().getClassLoader().getResource("images/lecturelist3.png")).getImage();
	public Image addClassDefault = new ImageIcon(getClass().getClassLoader().getResource("images/addclass1.png")).getImage();
	public Image addClassOver = new ImageIcon(getClass().getClassLoader().getResource("images/addclass2.png")).getImage();
	public Image addClassPress = new ImageIcon(getClass().getClassLoader().getResource("images/addclass3.png")).getImage();
	public Image deleteClassDefault = new ImageIcon(getClass().getClassLoader().getResource("images/deleteclass1.png")).getImage();
	public Image deleteClassOver = new ImageIcon(getClass().getClassLoader().getResource("images/deleteclass2.png")).getImage();
	public Image deleteClassPress = new ImageIcon(getClass().getClassLoader().getResource("images/deleteclass3.png")).getImage();
	public Image profileDefault = new ImageIcon(getClass().getClassLoader().getResource("images/profile1.png")).getImage();
	public Image profileOver = new ImageIcon(getClass().getClassLoader().getResource("images/profile2.png")).getImage();
	public Image profilePress = new ImageIcon(getClass().getClassLoader().getResource("images/profile3.png")).getImage();
	public Image signDefault = new ImageIcon(getClass().getClassLoader().getResource("images/sign1.png")).getImage();
	public Image signOver = new ImageIcon(getClass().getClassLoader().getResource("images/sign2.png")).getImage();
	public Image signPress = new ImageIcon(getClass().getClassLoader().getResource("images/sign3.png")).getImage();
	
	
	
	
	public Ticket getTicket() {
		return ticket;
	}
	
	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}
	public boolean isCheckNodeType() {
		return isCheckNodeType;
	}
	
	public void setCheckNodeType(boolean isCheckNodeType) {
		this.isCheckNodeType = isCheckNodeType;
	}
	public int getPORT() {
		return PORT;
	}
	
	public void setPORT(int pORT) {
		PORT = pORT;
	}
	public void setSERVERIP(String sERVERIP) {
		SERVERIP = sERVERIP;
	}
	public String getSERVERIP() {
		return SERVERIP;
	}
	public JFrame getFreemindMainFrame() {
		return freemindMainFrame;
	}

	public void setFreemindMainFrame(JFrame freemindMainFrame) {
		this.freemindMainFrame = freemindMainFrame;
	}

	public ProfileFrame getProfileFrame() {
		return profileFrame;
	}

	public void setProfileFrame(JFrame profileFrame) {
		this.profileFrame = (ProfileFrame) profileFrame;
	}

	public String getDownPath() {
		return downPath;
	}
	
	public int getPdfPage() {
		return pdfPage;
	}

	public void setPdfPage(int pdfPage) {
		this.pdfPage = pdfPage;
	}


	public MindMapMapModel getmModel() {
		return mModel;
	}

	public void setmModel(MindMapMapModel mModel) {
		this.mModel = mModel;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	public OutputStream getOs() {
		return os;
	}
	
	public void setOs(OutputStream os) {
		this.os = os;
	}
	
	public InputStream getIn() {
		return in;
	}
	
	public void setIn(InputStream in) {
		this.in = in;
	}
	
	public Controller getC() {
		return c;
	}
	
	public void setC(Controller c) {
		this.c = c;
	}
	public MindMapController getMc() {
		return mc;
	}
	
	public void setMc(MindMapController mc) {
		this.mc = mc;
	}
	public SlideShow getSlideShow() {
		return slideShow;
	}
	
	public void setSlideShow(SlideShow slideShow) {
		this.slideShow = slideShow;
	}
	public boolean isAddQuestionNodeInfo() {
		return isAddQuestionNodeInfo;
	}
	
	public void setAddQuestionNodeInfo(boolean isAddQuestionNodeInfo) {
		this.isAddQuestionNodeInfo = isAddQuestionNodeInfo;
	}
	public boolean isSlideShowInfo() {
		return isSlideShowInfo;
	}
	
	public void setSlideShowInfo(boolean isSlideShowInfo) {
		this.isSlideShowInfo = isSlideShowInfo;
	}
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTicketTitle() {
		return ticketTitle;
	}

	public void setTicketTitle(String ticketTitle) {
		this.ticketTitle = ticketTitle;
	}

	public String getTicketContent() {
		return ticketContent;
	}

	public void setTicketContent(String ticketContent) {
		this.ticketContent = ticketContent;
	}

	public String getTicketWriter() {
		return ticketWriter;
	}

	public void setTicketWriter(String ticketWriter) {
		this.ticketWriter = ticketWriter;
	}

	
	
	
	
	
	
	
	
}
