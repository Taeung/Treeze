package freemind.json;

public class User{

	/**
	 * 
	 */
	private int userImgId;  //�̹��� �����Ų�Ŀ� �ֱ�
	private String userName;
	private String userEmail;
	
	
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getUserImgId() {
		return userImgId;
	}
	public void setUserImgId(int userImgId) {
		this.userImgId = userImgId;
	}
}
