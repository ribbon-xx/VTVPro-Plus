package mdn.vtvsport.object.account;

public class LoginInfo {
	private boolean isSuccess;
	private String message;	//luu session hoac message loi
	
	public LoginInfo() {
		this.isSuccess = false;
		this.message = "";
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSucess(boolean isLogin) {
		this.isSuccess = isLogin;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
