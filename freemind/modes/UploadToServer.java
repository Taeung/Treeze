package freemind.modes;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import freemind.controller.FreemindManager;
import freemind.controller.SlideData;


public class UploadToServer {
	ArrayList<SlideData> sList;
	SlideData tmp;
	String classId;
	FreemindManager fManager = FreemindManager.getInstance();
	final String SERVERIP = fManager.SERVERIP;
	
	  public void doFileUpload() {
          try {
        	  File saveFile;//
        	  FileBody bin = null;
        	  
			for (int i = 1; i <= fManager.getPdfPage(); i++) {
				saveFile = new File(fManager.getFilePath(),
						fManager.getClassId() + "_" + i + ".jpg");

				if (saveFile.exists())
					bin = new FileBody(saveFile, "UTF-8");
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://" + SERVERIP
						+ ":8080/treeze/upload/img");

				StringBody classBody = new StringBody(fManager.getClassId() + "",
						Charset.forName("UTF-8"));

				MultipartEntity multipart = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE, null,
						Charset.forName("UTF-8")); // xml, classId, LectureName
													// ��� 蹂대�

				multipart.addPart("classId", classBody);
				multipart.addPart("upload", bin);

				post.setEntity(multipart);
				HttpResponse response = httpClient.execute(post);
				HttpEntity resEntity = response.getEntity();
			}

           System.out.println("postXmlImg");
       }catch(Exception e){e.printStackTrace();
       }
	  }
	  
	  public void lecturePost(String lectureName, String profEmail, String state) {
		  	String jsonStr;
          try {
        	  HttpClient httpClient = new DefaultHttpClient();  
        	  HttpPost post = new HttpPost("http://" + SERVERIP + ":8080/treeze/createLecture");
        	  MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        	  
        	  StringBody lectureTitle = new StringBody(lectureName, Charset.forName("UTF-8"));
        	  StringBody profEmailBody = new StringBody(profEmail, Charset.forName("UTF-8"));
        	  StringBody lectureState = new StringBody(state, Charset.forName("UTF-8"));
        	  StringBody profssorNameBody = new StringBody("이민석", Charset.forName("UTF-8"));
           
        	  multipart.addPart("lectureName", lectureTitle);  
        	  multipart.addPart("professorEmail", profEmailBody);
        	  multipart.addPart("stateOfLecture", lectureState);
        	  multipart.addPart("professorName", profssorNameBody);

        	  post.setEntity(multipart);  
        	  HttpResponse response = httpClient.execute(post);  
        	  HttpEntity resEntity = response.getEntity();
        	  System.out.println("postLecture");
          }catch(Exception e){e.printStackTrace();
          }
	  }
	  
	  public void classPost(String lectureId, String profEmail, String className) {
		  	String jsonStr;
        try {
      	  HttpClient httpClient = new DefaultHttpClient();  
      	  HttpPost post = new HttpPost("http://" + SERVERIP + ":8080/treeze/createClass"); 
      	  MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
      	  String ipStr = null;
      	  try {
				ipStr = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
      	StringBody classId = new StringBody("0", Charset.forName("UTF-8"));
      	  StringBody lectureTitle = new StringBody(lectureId, Charset.forName("UTF-8"));
      	  StringBody profEmailBody = new StringBody(profEmail, Charset.forName("UTF-8"));
      	  StringBody classNameBody = new StringBody(className, Charset.forName("UTF-8"));
      	  StringBody portBody = new StringBody("2141", Charset.forName("UTF-8"));
      	  StringBody ipBody = new StringBody(ipStr, Charset.forName("UTF-8"));
      	  
      	  multipart.addPart("classIP", ipBody);
      	  multipart.addPart("port", portBody);
      	  multipart.addPart("classId", classId);
      	  multipart.addPart("lectureId", lectureTitle);  
      	  multipart.addPart("professorEmail", profEmailBody);
      	  multipart.addPart("className", classNameBody);
      	  
      	  post.setEntity(multipart);  
      	  HttpResponse response = httpClient.execute(post);  
      	  HttpEntity resEntity = response.getEntity();
      	  System.out.println("postClass");
        }catch(Exception e){e.printStackTrace();
        }
	  }
	  
	  public void ticketPost(String ticketTitle, String classId, String position, String contents, String userName, String ticketPosition) {
		  	String jsonStr;
		  	
      try {
    	  HttpClient httpClient = new DefaultHttpClient();  
    	  HttpPost post = new HttpPost("http://" + SERVERIP + ":8080/treeze/createTicket"); 
    	  MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
    	  StringBody classIdBody = new StringBody(classId, Charset.forName("UTF-8"));
    	  StringBody ticketTitleBody = new StringBody(ticketTitle, Charset.forName("UTF-8"));
    	  StringBody positionBody = new StringBody(position, Charset.forName("UTF-8"));
    	  StringBody contentsBody = new StringBody(contents, Charset.forName("UTF-8"));
    	  StringBody userNameBody = new StringBody(userName, Charset.forName("UTF-8"));
    	  StringBody ticketPositionBody = new StringBody(ticketPosition, Charset.forName("UTF-8"));
    	  
    	  multipart.addPart("ticketTitle", ticketTitleBody);
    	  multipart.addPart("classId", classIdBody);
    	  multipart.addPart("position", positionBody);
    	  multipart.addPart("contents", contentsBody);  
    	  multipart.addPart("userName", userNameBody);
    	  multipart.addPart("ticketPosition", ticketPositionBody);

    	  post.setEntity(multipart);  
    	  HttpResponse response = httpClient.execute(post);  
    	  HttpEntity resEntity = response.getEntity();
    	  System.out.println("postClass");
      }catch(Exception e){e.printStackTrace();
      }
	  }
	  
	  public void doXmlUpload() throws IOException{
    	  String xml ="";
    	  BufferedReader br;
    	  String fileName = fManager.getDownPath() + System.getProperty("file.separator") + "upload.mm";
    	  String line = "";
    	  
          try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
                // 왜 파일인풋스트림 안에 new File(폴더, 이름) 이렇게 하면 안되는지 알수없는에러.....
                //똑같이 xml은 읽히는데 왜?????????
                //물어봐 
                while ((line = br.readLine()) != null) {
                	xml += line;
                }
                br.close();        
          } catch (Exception e) {
                e.printStackTrace();
          }
          System.out.println(xml);
          
       HttpClient httpClient = new DefaultHttpClient();  
       HttpPost post = new HttpPost("http://" + SERVERIP + ":8080/treeze/createMindMap"); 
       
       MultipartEntity multipart = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE, null,
				Charset.forName("UTF-8"));  // xml, classId, LectureName ��� 蹂대�
		
       StringBody classBody = new StringBody(fManager.getClassId() + "", Charset.forName("UTF-8"));
       StringBody xmlBody = new StringBody(xml, Charset.forName("UTF-8"));

		multipart.addPart("classId", classBody);
		multipart.addPart("mindmapXML", xmlBody);

		post.setEntity(multipart);
		HttpResponse response = httpClient.execute(post);
		HttpEntity resEntity = response.getEntity();
		System.out.println("UploadtoSErver : douploadXml()");
	  }
	  
}
