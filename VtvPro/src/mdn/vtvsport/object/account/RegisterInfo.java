package mdn.vtvsport.object.account;

public class RegisterInfo {
	private boolean isSuccess;
	private String message;
	
	public RegisterInfo() {
		this.isSuccess = false;
		this.message = "";
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
