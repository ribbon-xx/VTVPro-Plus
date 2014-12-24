package mdn.vtvsport.object.home;

public class HomeCategory extends HomeObject {
	private String id;
	private String mNameCategory;
	private int type; //0_channel + vod; -1 moi nhat, -2 favorist; -3 hosttest
	
	public HomeCategory() {
		super();
		setIsCategory(true);
	}
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getNameCategory() {
		return this.mNameCategory;
	}
	
	public void setNameCategory(String nameCategory) {
		this.mNameCategory = nameCategory;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
