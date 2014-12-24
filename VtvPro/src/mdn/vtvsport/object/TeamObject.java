package mdn.vtvsport.object;

/**
 * Created by RibboN on 11/3/14.
 */
public class TeamObject{
    private long mID;
    private long mGUID;
    private String mName;
    private String mCode_Name;
    private String image_url_large;
    private String image_url_medium;
    private String image_url_small;

    public long getID() {
        return mID;
    }

    public void setID(long mID) {
        this.mID = mID;
    }

    public long getGUID() {
        return mGUID;
    }

    public void setGUID(long mGUID) {
        this.mGUID = mGUID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getCode_Name() {
        return mCode_Name;
    }

    public void setCode_Name(String mCode_Name) {
        this.mCode_Name = mCode_Name;
    }

    public String getImage_url_large() {
        return image_url_large;
    }

    public void setImage_url_large(String image_url_large) {
        this.image_url_large = image_url_large;
    }

    public String getImage_url_medium() {
        return image_url_medium;
    }

    public void setImage_url_medium(String image_url_medium) {
        this.image_url_medium = image_url_medium;
    }

    public String getImage_url_small() {
        return image_url_small;
    }

    public void setImage_url_small(String image_url_small) {
        this.image_url_small = image_url_small;
    }
}
