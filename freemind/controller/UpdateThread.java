package freemind.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpdateThread extends Thread{
	@Override
	public void run() {
		
		try {
			String s;
		    /*  �ڹ� 1.4 ���Ͽ����� �̷���
		    Runtime oRuntime = Runtime.getRuntime();
		    Process oProcess = oRuntime.exec("cmd /c dir /?");
		    */

		    // �ڹ� 1.5 �̻󿡼��� �̷��� 1�ٷ�
//		    Process oProcess = new ProcessBuilder(System.getProperty("user.dir") + System.getProperty("file.separator") + "treezeUpdater.exe").start();
			Process oProcess = new ProcessBuilder(System.getProperty("user.dir") + System.getProperty("file.separator") + "Student.exe").start();
			
		    // �ܺ� ���α׷� ��� �б�
		    BufferedReader stdOut   = new BufferedReader(new InputStreamReader(oProcess.getInputStream(), "UTF-8"));
		    BufferedReader stdError = new BufferedReader(new InputStreamReader(oProcess.getErrorStream(), "UTF-8"));

		    // "ǥ�� ���"�� "ǥ�� ���� ���"�� ���
		    while ((s =   stdOut.readLine()) != null) System.out.println(s);
		    while ((s = stdError.readLine()) != null) System.err.println(s);

		    // �ܺ� ���α׷� ��ȯ�� ��� (�� �κ��� �ʼ��� �ƴ�)
//		    System.out.println("Exit Code: " + oProcess.exitValue());
//		    System.exit(oProcess.exitValue()); // �ܺ� ���α׷��� ��ȯ����, �� �ڹ� ���α׷� ��ü�� ��ȯ������ ���

		  } catch (IOException e) { // ���� ó��
		      System.err.println("����! �ܺ� ��� ���࿡ �����߽��ϴ�.\n" + e.getMessage());
		      System.exit(-1);
		    }
	}
}
