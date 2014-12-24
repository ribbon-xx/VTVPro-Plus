package mdn.vtvsport.object;

import android.os.Parcel;
import android.os.Parcelable;

public class VodInfo extends ItemVtvPlusInfo implements Parcelable {
	private String duration;
	private String image;
	private boolean isSeries;
	private String episodeCount;
	private String tag;
	private String price;
	private String lanpImage;
	private String portImage;
	private String des;
	private boolean isFree;
	
	public VodInfo() {
		super();
		setTypeItem(1);
	}
	
	public VodInfo(Parcel in) {
		readFromParcel(in);
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean isSeries() {
		return isSeries;
	}

	public void setSeries(boolean isSeries) {
		this.isSeries = isSeries;
	}

	public String getEpisodeCount() {
		return episodeCount;
	}

	public void setEpisodeCount(String episodeCount) {
		this.episodeCount = episodeCount;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLanpImage() {
		return lanpImage;
	}

	public void setLanpImage(String lanpImage) {
		this.lanpImage = lanpImage;
	}

	public String getPortImage() {
		return portImage;
	}

	public void setPortImage(String portImage) {
		this.portImage = portImage;
	}
	
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(getId());
		dest.writeString(getName());
		dest.writeString(getDes());
		dest.writeString(getDuration());
		dest.writeString(getImage());
		if (isSeries) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}
		dest.writeString(getEpisodeCount());
		if (isFree()) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}
		dest.writeString(getTag());
		dest.writeString(getPrice());
		dest.writeString(getLanpImage());
		dest.writeString(getPortImage());
		dest.writeString(getView());
		dest.writeString(getStreamId());
		if (isFavorites()) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}
		if (isLike()) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}
		dest.writeString(getStreamUrl());
	}
	
	private void readFromParcel(Parcel in) {
		setId(in.readString());
		setName(in.readString());
		setDes(in.readString());
		setDuration(in.readString());
		setImage(in.readString());
		int tmp = in.readInt();
		if (tmp == 1) {
			setSeries(true);
		} else {
			setSeries(false);
		}
		setEpisodeCount(in.readString());
		tmp = in.readInt();
		if (tmp == 1) {
			setFree(true);
		} else {
			setFree(false);
		}
		setTag(in.readString());
		setPrice(in.readString());
		setLanpImage(in.readString());
		setPortImage(in.readString());
		setView(in.readString());
		setStreamId(in.readString());
		tmp = in.readInt();
		if (tmp == 1) {
			setFavorites(true);
		} else {
			setFavorites(false);
		}
		tmp = in.readInt();
		if (tmp == 1) {
			setLike(true);
		} else {
			setLike(false);
		}
		setStreamUrl(in.readString());
 	}
	
	public static final Parcelable.Creator<VodInfo> CREATOR = new Parcelable.Creator<VodInfo>() {

		@Override
		public VodInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new VodInfo(source);
		}

		@Override
		public VodInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new VodInfo[size];
		}

	};
	
}
