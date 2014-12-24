package mdn.vtvsport.object;

public class AdsInfo extends ItemVtvPlusInfo {
	private String image;
	private String poster;
	private String link;
	private int typeAds; //0_banner, 1_advertise, 2_video
	private String url;
	
	public AdsInfo() {
		super();
		setTypeItem(3);
		this.typeAds = -1;
		setView("");
		setCountLike("");
		setStreamId("");
		this.url = null;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getTypeAds() {
		return typeAds;
	}

	public void setTypeAds(int typeAds) {
		this.typeAds = typeAds;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
