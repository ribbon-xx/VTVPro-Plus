package mdn.vtvsport.facebook;

import java.util.Arrays;
import java.util.List;

public class FacebookConfig {
	/** Facebook application information */
	public static final String FB_APP_ID = "547119368707438";

	public static final String FB_APP_SECRET = "011ff88f1d292d3ec82e147e6aab78b5";

	/** Facebook permissions */
	public static final List<String> PUBLISH_PERMISSIONS = Arrays
			.asList("publish_stream");
	public static final List<String> READ_PERMISSIONS = Arrays.asList("email");

	public static final List<String> NON_SSO_PERMISSIONS = Arrays.asList(
			"publish_stream", "email", "user_birthday");
	public static final List<String> NON_SSO_PERMISSIONS_FULL = Arrays.asList(
			"publish_stream", "email", "user_photos", "user_birthday");

	/** Facebook api urls */
	public static final String URL_BASE = "https://graph.facebook.com/";
	public static final String URL_SEARCH_PLACE = URL_BASE
			+ "search?type=place";
	public static final String URL_USER_AVATAR = "/picture";
	public static final String GRAPH_POST_NEW_FEED = "me/feed";

	/** Facebook keys */
	public static final String KEY_PICTURE = "picture";
	public static final String KEY_CAPTION = "caption";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_NAME = "name";
	public static final String KEY_LINK = "link";
	public static final String KEY_APP_ID = "client_id";
	public static final String KEY_APP_SECRET = "client_secret";
	public static final String KEY_DATA = "data";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_STREET = "street";
	public static final String KEY_ZIP = "zip";
	public static final String KEY_CITY = "city";
	public static final String KEY_STATE = "state";
	public static final String KEY_LATITUDE = "latitude";
	public static final String KEY_LONGITUDE = "longitude";
	public static final String KEY_COUNTRY = "country";

	/** Graph api parameters */
	public static final String PARAM_MESSAGE = "message";
	public static final String PARAM_CENTER = "center";
	public static final String PARAM_DISTANCE = "distance";
	public static final String PARAM_ACCESS_TOKEN = "access_token";
}
