package Data;


public class User  {
	
	public static final String STUDENT = "student";
	public static final String PROFESSOR = "professor";
	
	private int userImgId;  
	private String userType;
	private String userName;
	private String userEmail;
	private int password;
	
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
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getPassword() {
		return password;
	}
	public void setPassword(int password) {
		this.password = password;
	}
	
}
