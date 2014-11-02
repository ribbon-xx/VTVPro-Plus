package mdn.vtvpluspro.object;

public class objMenu {
	public int resIcon = 0;
	public int id;
	public String name;
	public boolean isBanner = false;
	
	private boolean isGroupHeader = false;
	
	public objMenu(int id, String name, boolean isGroupHeader){
		this.id = id;
		this.name = name;
//		this.resIcon = resIcon;
//		this.isBanner = isBanner;
		this.isGroupHeader = isGroupHeader;
	}
	
	public objMenu(int id, String name, int resIcon, boolean isGroupHeader){
		this.id = id;
		this.name = name;
		this.resIcon = resIcon;
//		this.isBanner = isBanner;
		this.isGroupHeader = isGroupHeader;
	}
		
	public boolean isGroupHeader() {
		return isGroupHeader;
	}

	public void setGroupHeader(boolean isGroupHeader) {
		this.isGroupHeader = isGroupHeader;
	}

}
