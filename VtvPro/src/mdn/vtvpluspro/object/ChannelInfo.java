package mdn.vtvpluspro.object;

import android.os.Parcel;
import android.os.Parcelable;

public class ChannelInfo extends ItemVtvPlusInfo implements Parcelable {
	private String screenUrl;
	private String iconSmall;
	private String icon;
	private boolean isAudio;
	private String des;
	private boolean isFree;
	
	public ChannelInfo() {
		super();
		setTypeItem(0);
	}

	public ChannelInfo(Parcel in) {
		readFromParcel(in);
	}

	public String getScreenUrl() {
		return screenUrl;
	}

	public void setScreenUrl(String screenUrl) {
		this.screenUrl = screenUrl;
	}

	public String getIconSmall() {
		return iconSmall;
	}

	public void setIconSmall(String iconSmall) {
		this.iconSmall = iconSmall;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isAudio() {
		return isAudio;
	}

	public void setAudio(boolean isAudio) {
		this.isAudio = isAudio;
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
		dest.writeString(getScreenUrl());
		dest.writeString(getDes());
		dest.writeString(getIconSmall());
		dest.writeString(getIcon());
		if (isFree()) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}
		if (isAudio) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}
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
		setScreenUrl(in.readString());
		setDes(in.readString());
		setIconSmall(in.readString());
		setIcon(in.readString());
		int i = in.readInt();
		if (i == 1) {
			setFree(true);
		} else {
			setFree(false);
		}
		i = in.readInt();
		if (i == 1) {
			setAudio(true);
		} else {
			setAudio(false);
		}
		setView(in.readString());
		setStreamId(in.readString());
		i = in.readInt();
		if (i == 1) {
			setFavorites(true);
		} else {
			setFavorites(false);
		}
		i = in.readInt();
		if (i == 1) {
			setLike(true);
		} else {
			setLike(false);
		}
		setStreamUrl(in.readString());
	}
	
	public static final Parcelable.Creator<ChannelInfo> CREATOR = new Parcelable.Creator<ChannelInfo>() {

		@Override
		public ChannelInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new ChannelInfo(source);
		}

		@Override
		public ChannelInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ChannelInfo[size];
		}

	};
}
