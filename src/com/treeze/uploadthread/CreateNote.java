package com.treeze.uploadthread;import java.io.BufferedReader;import java.io.IOException;import java.io.InputStream;import java.io.InputStreamReader;import java.io.UnsupportedEncodingException;import java.nio.charset.Charset;import org.apache.http.HttpEntity;import org.apache.http.HttpResponse;import org.apache.http.client.ClientProtocolException;import org.apache.http.client.HttpClient;import org.apache.http.client.methods.HttpPost;import org.apache.http.entity.mime.HttpMultipartMode;import org.apache.http.entity.mime.MultipartEntity;import org.apache.http.entity.mime.content.StringBody;import org.apache.http.impl.client.DefaultHttpClient;import org.apache.http.util.EntityUtils;import com.treeze.data.TreezeData;import com.treeze.data.TreezeStaticData;public class CreateNote extends Thread {		private Long classId;	private String nodeId;	private String contents;	private String userEmail;	public CreateNote(Long classId, String nodeId, String contents,			String userEmail) {		// TODO Auto-generated constructor stub				this.classId = classId;		this.nodeId = nodeId;		this.contents = contents;		this.userEmail = userEmail;		this.setPriority(MAX_PRIORITY);		System.out.println(classId + " " + nodeId + " " + contents + " "+ userEmail);	}	@Override	public void run() {		// TODO Auto-generated method stub		super.run();		HttpClient httpClient = new DefaultHttpClient();		HttpPost post = new HttpPost("http://" + TreezeStaticData.IP				+ ":8080/treeze/createNote");				MultipartEntity multipart = new MultipartEntity(				HttpMultipartMode.BROWSER_COMPATIBLE, null,				Charset.forName("UTF-8"));		try {			StringBody classIdBody = new StringBody(classId + "",					Charset.forName("UTF-8"));			StringBody nodeIdBody = new StringBody(nodeId,					Charset.forName("UTF-8"));			StringBody contentsBody = new StringBody(contents,					Charset.forName("UTF-8"));			StringBody userEmailBody = new StringBody(userEmail,					Charset.forName("UTF-8"));			multipart.addPart("classId", classIdBody);			multipart.addPart("nodeId", nodeIdBody);						multipart.addPart("contents", contentsBody);			System.out.println("[서버에 저장한내용] = "+ contents);			multipart.addPart("userEmail", userEmailBody);			post.setEntity(multipart);			HttpResponse response = httpClient.execute(post);			HttpEntity resEntity = response.getEntity();			InputStream inputStream = resEntity.getContent();			String str = "";			String tmp;			BufferedReader in = new BufferedReader(new InputStreamReader(					inputStream, "UTF-8"));			byte[] b = new byte[4096];			while ((tmp = in.readLine()) != null)				str += tmp;			String responseContents = str;			System.out.println("[Post respone]" + responseContents);			TreezeData t = new TreezeData();			t.setDataType(TreezeData.TICKET);			t.getArgList().add(responseContents);			// System.out.println(gson.toJson(t));			EntityUtils.consume(resEntity);			System.out.println("postEnd");		} catch (UnsupportedEncodingException e) {			// TODO Auto-generated catch block			e.printStackTrace();		} catch (ClientProtocolException e) {			// TODO Auto-generated catch block			e.printStackTrace();		} catch (IOException e) {			// TODO Auto-generated catch block			e.printStackTrace();		}	}}