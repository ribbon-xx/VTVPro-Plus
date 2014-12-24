package mdn.vtvsport.object;

public class ItemInteraction {
	private int type; //0_url; 1_image; 2_video; 3_audio
	private String url;
	private String thumb;
	
	public ItemInteraction() {
		this.type = -1;
		this.url = null;
		this.thumb = null;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	
}
