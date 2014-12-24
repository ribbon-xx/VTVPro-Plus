package mdn.vtvsport.object;

import java.io.Serializable;

/**
 * Created by RibboN on 11/2/14.
 */
public class MatchScheduleMenuObject implements Serializable{

    private long id;
    private long guid;
    private String name;
    private String code_name;

    private String image_url_large;
    private String image_url_medium;
    private String image_url_small;

    public MatchScheduleMenuObject() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGuid() {
        return guid;
    }

    public void setGuid(long guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
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
