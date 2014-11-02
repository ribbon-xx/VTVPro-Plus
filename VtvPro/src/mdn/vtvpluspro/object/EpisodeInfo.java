package mdn.vtvpluspro.object;

import android.os.Parcel;
import android.os.Parcelable;

public class EpisodeInfo extends ItemVtvPlusInfo implements Parcelable {
	private String duration;
	private String image;
	private String eposideNumber;
	private String vodId;
	
	public EpisodeInfo() {
		super();
		setTypeItem(2);
	}
	
	public EpisodeInfo(Parcel in) {
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

	public String getEposideNumber() {
		return eposideNumber;
	}

	public void setEposideNumber(String eposideNumber) {
		this.eposideNumber = eposideNumber;
	}

	public String getVodId() {
		return vodId;
	}

	public void setVodId(String vodId) {
		this.vodId = vodId;
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
		dest.writeString(getImage());
		dest.writeString(getEposideNumber());
		dest.writeString(getView());
		dest.writeString(getVodId());
		dest.writeString(getStreamId());
		dest.writeString(getStreamUrl());
		if (isLike()) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}
	}
	
	private void readFromParcel(Parcel in) {
		setId(in.readString());
		setName(in.readString());
		setImage(in.readString());
		setEposideNumber(in.readString());
		setView(in.readString());
		setVodId(in.readString());
		setStreamId(in.readString());
		setStreamUrl(in.readString());
		int tmp = in.readInt();
		if (tmp == 1) {
			setLike(true);
		} else {
			setLike(false);
		}
	}
	
	public static final Parcelable.Creator<EpisodeInfo> CREATOR = new Parcelable.Creator<EpisodeInfo>() {

		@Override
		public EpisodeInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new EpisodeInfo(source);
		}

		@Override
		public EpisodeInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new EpisodeInfo[size];
		}

	};
}
