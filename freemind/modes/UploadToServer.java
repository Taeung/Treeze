package freemind.modes;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import freemind.controller.SlideData;


public class UploadToServer {
	ArrayList<SlideData> sList;
	SlideData tmp;
	String classId;
	  public void doFileUpload(ArrayList<SlideData> sList, String fileFullPath, String fileName, String classId) {
          try {
        	  this.classId = classId;
        	  this.sList = sList;
        	  
        	  String dirPath;
        	  String imgFileName;
        	  String xml ="";
        	  
        	  dirPath = fileFullPath.substring(0, fileFullPath.length() - 4);
        	  
        	  BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(dirPath + ".mm"), "UTF-8"));
              String s = "";

              while ((s = in.readLine()) != null) {
            	  xml += s;
                //System.out.println(s);
              }
              in.close(); // read xml
        	  
           HttpClient httpClient = new DefaultHttpClient();  
           //String url = serverUrl;
           HttpPost post = new HttpPost("http://113.198.84.74:8080/treeze/upload/img"); 
           String path = "";
           //http://61.43.139.10:8080/treeze/upload/img
           // 파일 path 잡아주고 for 돌며면서 이미지 보내
           
           
           StringBody classBody = new StringBody(classId, Charset.forName("UTF-8"));
           
           StringBody xmlBody = new StringBody(xml, Charset.forName("UTF-8"));
           
           MultipartEntity multipart = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE, null,
					Charset.forName("UTF-8"));  // xml, classId, LectureName 한번 보내
			

			multipart.addPart("XML", xmlBody);
			multipart.addPart("ClassId", classBody);

			post.setEntity(multipart);
			HttpResponse response;// = httpClient.execute(post);
			HttpEntity resEntity;// = response.getEntity();

			FileBody imgBody;//
			File saveFile;//
           for(int i = 0; i < sList.size(); i++){
        	   
        	 httpClient = new DefaultHttpClient();  
             post = new HttpPost("http://61.43.139.10:8080/treeze/upload/img"); 
             saveFile = new File(path);
             if(saveFile.exists()){
            	 FileBody bin =  new FileBody(saveFile, "UTF-8");

					StringBody body = new StringBody("4",
							Charset.forName("UTF-8"));

					multipart = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);
//					multipart = new MultipartEntity(
//		  					HttpMultipartMode.BROWSER_COMPATIBLE, null,
//		  					Charset.forName("UTF-8"));
					
					multipart.addPart("classId", body);
					multipart.addPart("upload", bin);

					post.setEntity(multipart);
					response = httpClient.execute(post);
					resEntity = response.getEntity(); // 태웅이형소스
				}
        	   
        	   
        	   
        	   
        	   
//        	   httpClient = new DefaultHttpClient();
//        	   post = new HttpPost("http://61.43.139.10:8080/treeze/upload/img"); 
//        	   multipart = new MultipartEntity(
//   					HttpMultipartMode.BROWSER_COMPATIBLE, null,
//   					Charset.forName("UTF-8")); // classId에 맞는 img 다 보내
//   			
//        	   tmp = sList.get(i);
//        	   imgFileName = dirPath + "\\" + tmp.getNodeName() + ".jpg"; 
//        	   saveFile = new File(imgFileName);
//				if (saveFile.exists()) {
//
//					imgBody = new FileBody(saveFile, "UTF-8");
//
//					multipart.addPart("classId", classBody);
//					multipart.addPart("upload", imgBody);
//
//					post.setEntity(multipart);
//					response = httpClient.execute(post);
//					resEntity = response.getEntity();
//				} 내가하던거
           }
           System.out.println("postXmlImg");
       }catch(Exception e){e.printStackTrace();
       }
	  }
	  
	  public void lecturePost(String lectureName, String profEmail, String state) {
		  	String jsonStr;
//			FreemindGson myGson = new FreemindGson();
//			Lecture createLecture = new Lecture();
//			createLecture.setLectureName(lectureTitle);
//			createLecture.setProfessorEmail("minsuk@hansung.ac.kr");
//			createLecture.setStateOfLecture(false);
//			jsonStr = myGson.toJson(createLecture);
          try {
        	  HttpClient httpClient = new DefaultHttpClient();  
        	  //String url = serverUrl;
        	  HttpPost post = new HttpPost("http://113.198.84.74:8080/treeze/createLecture"); 
        	  MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        	  //파일 path 잡아주고 for 돌며면서 이미지 보내
        	  
        	  StringBody lectureTitle = new StringBody(lectureName, Charset.forName("UTF-8"));
        	  StringBody profEmailBody = new StringBody(profEmail, Charset.forName("UTF-8"));
        	  StringBody lectureState = new StringBody(state, Charset.forName("UTF-8"));
           
        	  multipart.addPart("lectureName", lectureTitle);  
        	  multipart.addPart("professorEmail", profEmailBody);
        	  multipart.addPart("stateOfLecture", lectureState);

        	  post.setEntity(multipart);  
        	  HttpResponse response = httpClient.execute(post);  
        	  HttpEntity resEntity = response.getEntity();
        	  System.out.println("postLecture");
          }catch(Exception e){e.printStackTrace();
          }
	  }
	  
	  public void classPost(String lectureName, String profEmail, String className) {
		  	String jsonStr;
//			FreemindGson myGson = new FreemindGson();
//			Lecture createLecture = new Lecture();
//			createLecture.setLectureName(lectureTitle);
//			createLecture.setProfessorEmail("minsuk@hansung.ac.kr");
//			createLecture.setStateOfLecture(false);
//			jsonStr = myGson.toJson(createLecture);
        try {
      	  HttpClient httpClient = new DefaultHttpClient();  
      	  //String url = serverUrl;
      	  HttpPost post = new HttpPost("http://61.43.139.10:8080/treeze/createClass"); 
      	  MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
      	  //파일 path 잡아주고 for 돌며면서 이미지 보내
      	  Random random = new Random();
      	  
      	  StringBuffer str = new StringBuffer(); // 변수를 바꿨으면 의미 없는 주석 달기
      	  for (int i = 1; i < 12; i++) {
      		  str.append((random.nextInt(10)));
      	  }
      	  StringBody classId = new StringBody(str.toString(), Charset.forName("UTF-8"));
      	  StringBody lectureTitle = new StringBody(lectureName, Charset.forName("UTF-8"));
      	  StringBody profEmailBody = new StringBody(profEmail, Charset.forName("UTF-8"));
      	  StringBody classNameBody = new StringBody(className, Charset.forName("UTF-8"));
      	  StringBody tmp = new StringBody("tmp", Charset.forName("UTF-8"));
      	  
      	  multipart.addPart("classIp", tmp);
      	  multipart.addPart("port", tmp);
      	  multipart.addPart("classId", classId);
      	  multipart.addPart("lectureName", lectureTitle);  
      	  multipart.addPart("professorEmail", profEmailBody);
      	  multipart.addPart("className", classNameBody);
      	  
      	  post.setEntity(multipart);  
      	  HttpResponse response = httpClient.execute(post);  
      	  HttpEntity resEntity = response.getEntity();
      	  System.out.println("postClass");
        }catch(Exception e){e.printStackTrace();
        }
	  }
}
