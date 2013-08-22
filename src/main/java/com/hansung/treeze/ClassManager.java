package com.hansung.treeze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hansung.treeze.model.ClassInfo;
import com.hansung.treeze.model.User;

@SuppressWarnings("serial")
public class ClassManager extends HttpServlet implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(ClassManager.class);
	private Thread classManager;
	private User professorInfo;
	private Socket professorSocket = null;
	private int professorCheck = 0;
	private ClassInfo classInfo;
	private ArrayList<ClassManager> classManagerList; // this is necessary to
														// function
														// "destroyClassManager"

	private ArrayList<StudentSocketManager> studentSocketManagerList = new ArrayList<StudentSocketManager>();

	// private BufferedReader in;
	// private PrintWriter out;
	private InputStream in;
	private OutputStream out;

	public ClassManager(ClassInfo classInfo,
			ArrayList<ClassManager> classManagerList) {

		this.classInfo = classInfo;
		this.classManagerList = classManagerList;
	}

	final String QUIT = "quit";

	public void init() throws ServletException {
		classManager = new Thread(this);
		classManager.start();
	}

	public void destroy() {
		classManager.interrupt();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			logger.info("==========================");
			logger.info("Treeze Class Manager (" + classInfo.getClassName()
					+ ") RUN");
			logger.info("==========================");
			startClassManager();
		} catch (Exception e) {
			logger.info("Class Manager failed 오류 : " + e.getMessage());
			destroyClassManager();
		}

	}

	public void startClassManager() throws IOException {

		String reqMsg = "";

		do {
			if (professorCheck == 0) {
				while (true) {
					if (professorCheck == 0) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//logger.info("서버 소켓 아직비었음 ");
						if (studentSocketManagerList.size() == 0) {
							destroyClassManager();
							return;
						}
						continue;
					} else {
						logger.info("서버 소켓 채워져서 탈출");
						break;
					}
				}
			}
			try {

				logger.info("서버 소켓 채워져서 받기시작 ");
				// reqMsg = in.readLine();
				int cnt = -1;
				byte[] b = new byte[1024];

				cnt = in.read(b);

				if (cnt == -1) {
					// destroyClassManager();
					System.out.println("Professor Socket failed");
					logger.info("==========================");
					logger.info("Treeze Professor Manager ("
							+ classInfo.getClassName() + ") CLOSE");
					logger.info("존재하는 클래스" + getClassInfo().getClassName()
							+ "[교수 수:" + (getProfessorSocket() != null)
							+ "][학생수 :" + getStudentSocketManagerList().size()
							+ "]");
					logger.info("==========================");
					try {
						professorSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					professorCheck = 0;

				} else {
					reqMsg = new String(b, 0, cnt, "UTF-8");
				}
				logger.info("Professor Request Message : " + reqMsg);

				broadcast(reqMsg);

			} catch (IOException e) {
				// TODO: handle exception
				e.printStackTrace();
				destroyClassManager();
			}

		} while (!(reqMsg.equals(QUIT)));

	}

	public void destroyClassManager() {

		logger.info("==========================");
		logger.info("Treeze Class Manager (" + classInfo.getClassName()
				+ ") CLOSE");
		logger.info("==========================");

		classManagerList.remove(this);
		destroy();
	}

	public void broadcast(String treezeData) {

		// first : to professor
		if (professorCheck != 0) {
			try {
				out.write(treezeData.getBytes("UTF-8"));
				out.flush();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (int i = 0; i < studentSocketManagerList.size(); i++)
			// second: to student
			studentSocketManagerList.get(i).send(treezeData);

	}

	public ArrayList<StudentSocketManager> getStudentSocketManagerList() {
		return studentSocketManagerList;
	}

	public void setStudentSocketManagerList(
			ArrayList<StudentSocketManager> studentSocketManagerList) {
		this.studentSocketManagerList = studentSocketManagerList;
	}

	public ClassInfo getClassInfo() {
		return classInfo;
	}

	public void setClassInfo(ClassInfo classInfo) {
		this.classInfo = classInfo;
	}

	public User getProfessorInfo() {
		return professorInfo;
	}

	public void setProfessorInfo(User professorInfo) {
		this.professorInfo = professorInfo;
	}

	public Socket getProfessorSocket() {
		return professorSocket;
	}

	public void setProfessorSocket(Socket professorSocket) {
		this.professorSocket = professorSocket;
		professorCheck = 1;
		logger.info("점검 : 교수 소켓 연결됨 ");

		try {
			in = professorSocket.getInputStream();
			out = professorSocket.getOutputStream();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}