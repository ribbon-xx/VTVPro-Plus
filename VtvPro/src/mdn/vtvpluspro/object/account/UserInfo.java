package mdn.vtvpluspro.object.account;


public class UserInfo {
	private boolean isGetSucc;
	private boolean isRegister;
	private String currentService;
	private String expiryDate;
	private String liveChannel;	//luu cac kenh duoc phat hoac message loi
	private String session;
	private String message;
	
	public UserInfo() {
		this.isGetSucc = false;
		this.isRegister = false;
		this.currentService = "";
		this.expiryDate = "";
		this.liveChannel = "";
	}

	public boolean isGetSucc() {
		return isGetSucc;
	}

	public void setGetSucc(boolean isGetSucc) {
		this.isGetSucc = isGetSucc;
	}

	public boolean isRegister() {
		return isRegister;
	}

	public void setRegister(boolean isRegister) {
		this.isRegister = isRegister;
	}

	public String getCurrentService() {
		return currentService;
	}

	public void setCurrentService(String currentService) {
		this.currentService = currentService;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getLiveChannel() {
		return liveChannel;
	}

	public void setLiveChannel(String liveChannel) {
		this.liveChannel = liveChannel;
	}
	
	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
