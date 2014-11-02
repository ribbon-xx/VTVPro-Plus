package mdn.vtvpluspro.object.account;

public class ProfileInfo {
	public String pUserName;
	public String pPassword;
	public String pSession;
	public String pIdChannel;
	
	public int pTypePack;//0: chua lay thong tin; 1: da dang ki; 2: chua dang ki
	public String pNamePack;
	public String pDayPack;
	
	public ProfileInfo() {
		this.pUserName = "";
		this.pPassword = "";
		this.pSession = "";
		this.pIdChannel = "";
		this.pNamePack = "";
		this.pDayPack = "";
		this.pTypePack = 0;;
	}
	
	public void clear() {
		this.pUserName = "";
		this.pPassword = "";
		this.pSession = "";
		this.pIdChannel = "";
		this.pNamePack = "";
		this.pDayPack = "";
		this.pTypePack = 0;
	}
}
