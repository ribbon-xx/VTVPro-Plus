/*
 * Name: $RCSfile: SharedPreferenceManager.java,v $
 * Version: $Revision: 1.0 $
 * Date: $Date: Oct 10, 2012 10:48:01 AM $
 *
 * Copyright (C) 2012 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvsport.common;

import mdn.vtvsport.object.ChannelInfo;
import mdn.vtvsport.object.Charginginfo;
import mdn.vtvsport.object.EpisodeInfo;
import mdn.vtvsport.object.ItemVtvPlusInfo;
import mdn.vtvsport.object.VodInfo;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferenceManager supports to set and get setting values
 * 
 * @author MC
 */
public class SharedPreferenceManager {
	/** Account type */

	// ================================================================
	private static final String JEMAND_PREFERENCES = "YEAH1TV_PREFERENCES";

	public static final String PRE_CHANNEL_TYPE = "PRE_CHANNEL_TYPE";
	public static final String PRE_CHANNEL_ID = "PRE_CHANNEL_ID";
	public static final String PRE_CHANNEL_NAME = "PRE_CHANNEL_NAME";
	public static final String PRE_CHANNEL_VIEW = "PRE_CHANNEL_VIEW";
	public static final String PRE_CHANNEL_LIKE = "PRE_CHANNEL_LIKE";
	public static final String PRE_CHANNEL_STREAM_ID = "PRE_CHANNEL_STREAM_ID";
	public static final String PRE_CHANNEL_STREAM_URL = "PRE_CHANNEL_STREAM_URL";

	// channel
	public static final String PRE_CHANNEL_SCREEN_URL = "PRE_CHANNEL_SCREEN_URL";
	public static final String PRE_CHANNEL_DESC = "PRE_CHANNEL_DESC";
	public static final String PRE_CHANNEL_ICON_SMALL = "PRE_CHANNEL_ICON_SMALL";
	public static final String PRE_CHANNEL_ICON = "PRE_CHANNEL_ICON";
	public static final String PRE_CHANNEL_FREE = "PRE_CHANNEL_FREE";
	public static final String PRE_CHANNEL_AUDIO = "PRE_CHANNEL_AUDIO";
	public static final String PRE_CHANNEL_FAVORIST = "PRE_CHANNEL_FAVORIST";

	// vod
	public static final String PRE_VOD_DESC = "PRE_VOD_DESC";
	public static final String PRE_VOD_DURATION = "PRE_VOD_DURATION";
	public static final String PRE_VOD_IMAGE = "PRE_VOD_IMAGE";
	public static final String PRE_VOD_FREE = "PRE_VOD_FREE";
	public static final String PRE_VOD_TAGS = "PRE_VOD_TAGS";
	public static final String PRE_VOD_PRICE = "PRE_VOD_PRICE";
	public static final String PRE_VOD_LAND_IMG = "PRE_VOD_LAND_IMG";
	public static final String PRE_VOD_PORT_IMG = "PRE_VOD_PORT_IMG";
	public static final String PRE_VOD_FAVORIST = "PRE_CHANNEL_FAVORIST";

	// episode
	public static final String PRE_EPOSIDE_DURATION = "PRE_EPOSIDE_DURATION";
	public static final String PRE_EPOSIDE_IMG_URL = "PRE_EPOSIDE_IMG_URL";
	public static final String PRE_EPOSIDE_NUMBER = "PRE_EPOSIDE_NUMBER";
	public static final String PRE_EPOSIDE_VOD_ID = "PRE_EPOSIDE_VOD_ID";

	public static final String PRE_RATING = "PRE_RATING";
	public static final String PRE_SCHEDULE_TEXT = "PRE_SCHEDULE_TEXT";
	public static final String PRE_SCHEDULE_NAME = "PRE_SCHEDULE_NAME";
	public static final String PRE_FAVORIST_COUNT = "PRE_FAVORIST_COUNT";

	// User
	public static final String PRE_USER_NAME = "PRE_USER_NAME";
	public static final String PRE_USER_PASS = "PRE_USER_PASS";
	public static final String PRE_USER_SESSION = "PRE_USER_SESSION";
	public static final String PRE_USER_SESSION_CHECKED = "PRE_USER_SESSION_CHECKED";

	public static final String PRE_SMS_GATEWAY = "PRE_SMS_GATEWAY";
	public static final String PRE_NON_SMS_GATEWAY = "PRE_NON_SMS_GATEWAY";
	public static final String PRE_DELETED_SMS_GATEWAY = "PRE_DELETED_SMS_GATEWAY";
	public static final String PRE_DELETED_NON_SMS_GATEWAY = "PRE_DELETED_NON_SMS_GATEWAY";
	// show policy
	public static final String PRE_SHOW_POLICY = "PRE_SHOW_POLICY";

	private static SharedPreferenceManager instance = null;
	private Context context;

	/**
	 * Constructor
	 */
	private SharedPreferenceManager() {
	}

	/**
	 * Get class instance
	 * 
	 * @param context
	 * @return
	 */
	public static SharedPreferenceManager getInstance(Context context) {
		if (instance == null) {
			instance = new SharedPreferenceManager();
			instance.context = context;
		}
		return instance;
	}

	// ======================== PREFERENCE FUNCTIONS =====================

	// ======================== UTILITY FUNCTIONS =====================

	public void setRating(boolean isRating) {
		putBooleanValue(PRE_RATING, isRating);
	}

	public boolean isRating() {
		return getBooleanValue(PRE_RATING);
	}

	public void setScheduleText(String schedule, int index) {
		putStringValue(PRE_SCHEDULE_TEXT + index, schedule);
	}

	public String getScheduleText(int index) {
		return getStringValue(PRE_SCHEDULE_TEXT + index);
	}

	public void setScheduleName(String schedule, int index) {
		putStringValue(PRE_SCHEDULE_NAME + index, schedule);
	}

	public String getScheduleName(int index) {
		return getStringValue(PRE_SCHEDULE_NAME + index);
	}

	public void removeScheduleText(int index) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(PRE_SCHEDULE_TEXT + index);
		editor.commit();
	}

	public void setVtvType(int type, int index) {
		putIntValue(PRE_CHANNEL_TYPE + index, type);
	}

	public int getVtvType(int index) {
		return getIntValue(PRE_CHANNEL_TYPE + index);
	}

	/**
	 * 
	 * @param channelId
	 */
	public void setChannelId(String channelId, int index) {
		putStringValue(PRE_CHANNEL_ID + index, channelId);
	}

	/**
	 * 
	 * @return
	 */
	public String getChannelId(int index) {
		return getStringValue(PRE_CHANNEL_ID + index, "");
	}

	/**
	 * 
	 * @param channelName
	 */
	public void setChannelName(String channelName, int index) {
		putStringValue(PRE_CHANNEL_NAME + index, channelName);
	}

	/**
	 * 
	 * @return
	 */
	public String getChannelName(int index) {
		return getStringValue(PRE_CHANNEL_NAME + index, "");
	}

	/**
	 * 
	 * @return
	 */
	public String getChannelScreenUrl(int index) {
		return getStringValue(PRE_CHANNEL_SCREEN_URL + index, "");
	}

	public void setChannelScreenUrl(String channelName, int index) {
		putStringValue(PRE_CHANNEL_SCREEN_URL + index, channelName);
	}

	/**
	 * 
	 * @param channelDesc
	 */
	public void setChannelDesc(String channelDesc, int index) {
		putStringValue(PRE_CHANNEL_DESC + index, channelDesc);
	}

	/**
	 * 
	 * @return
	 */
	public String getChannelDesc(int index) {
		return getStringValue(PRE_CHANNEL_DESC + index, "");
	}

	/**
	 * 
	 * @param channelIconSmall
	 */
	public void setChannelIconSmall(String channelIconSmall, int index) {
		putStringValue(PRE_CHANNEL_ICON_SMALL + index, channelIconSmall);
	}

	/**
	 * 
	 * @return
	 */
	public String getChannelIconSmall(int index) {
		return getStringValue(PRE_CHANNEL_ICON_SMALL + index, "");
	}

	/**
	 * 
	 * @param channelIcon
	 */
	public void setChannelIcon(String channelIcon, int index) {
		putStringValue(PRE_CHANNEL_ICON + index, channelIcon);
	}

	/**
	 * 
	 * @return
	 */
	public String getChannelIcon(int index) {
		return getStringValue(PRE_CHANNEL_ICON + index, "");
	}

	public void setChannelFree(boolean isFreen, int index) {
		putBooleanValue(PRE_CHANNEL_FREE + index, isFreen);
	}

	public boolean getChannelFree(int index) {
		return getBooleanValue(PRE_CHANNEL_FREE + index);
	}

	public void setChannelAudio(boolean isAudio, int index) {
		putBooleanValue(PRE_CHANNEL_AUDIO + index, isAudio);
	}

	public boolean getChannelAudio(int index) {
		return getBooleanValue(PRE_CHANNEL_AUDIO + index);
	}

	public String getChannelView(int index) {
		return getStringValue(PRE_CHANNEL_VIEW + index, "");
	}

	public void setChannelView(String view, int index) {
		putStringValue(PRE_CHANNEL_VIEW + index, view);
	}

	public String getChannelLike(int index) {
		return getStringValue(PRE_CHANNEL_LIKE + index, "");
	}

	public void setChannelLike(String like, int index) {
		putStringValue(PRE_CHANNEL_LIKE + index, like);
	}

	public String getChannelStreamId(int index) {
		return getStringValue(PRE_CHANNEL_STREAM_ID + index, "");
	}

	public void setChannelStreamId(String view, int index) {
		putStringValue(PRE_CHANNEL_STREAM_ID + index, view);
	}

	public void setChannelFav(boolean isFreen, int index) {
		putBooleanValue(PRE_CHANNEL_FAVORIST + index, isFreen);
	}

	public boolean getChannelFav(int index) {
		return getBooleanValue(PRE_CHANNEL_FAVORIST + index);
	}

	public String getChannelStreamUrl(int index) {
		return getStringValue(PRE_CHANNEL_STREAM_URL + index, "");
	}

	/**
	 * 
	 * @param count
	 */
	public void setFavoristCount(int count) {
		putIntValue(PRE_FAVORIST_COUNT, count);
	}

	/**
	 * 
	 * @return
	 */
	public int getFavoristCount() {
		return getIntValue(PRE_FAVORIST_COUNT);
	}

	/**
	 * 
	 * @param info
	 */
	public void setItemVtvPlusInfo(ItemVtvPlusInfo info, int index, int type) {
		switch (type) {
		case 0:
			setChannelInfo((ChannelInfo) info, index);
			break;
		case 1:
			setVodInfo((VodInfo) info, index);
			break;
		case 2:
			setEposideInfo((EpisodeInfo) info, index);
			break;
		default:
			break;
		}
	}

	public ItemVtvPlusInfo getItemVtvPlusInfo(int index, int type) {
		ItemVtvPlusInfo info = null;
		switch (type) {
		case 0:
			info = getChannelInfo(index);
			removeChannel(index);
			break;
		case 1:
			info = getVodInfo(index);
			removeVod(index);
			break;
		case 2:
			info = getEposideInfo(index);
			removeEposide(index);
			break;

		default:
			break;
		}
		return info;
	}

	public void setChannelInfo(ChannelInfo info, int index) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(PRE_CHANNEL_TYPE + index, 0);
		editor.putString(PRE_CHANNEL_ID + index, info.getId());
		editor.putString(PRE_CHANNEL_NAME + index, info.getName());
		editor.putString(PRE_CHANNEL_SCREEN_URL + index, info.getScreenUrl());
		editor.putString(PRE_CHANNEL_DESC + index, info.getDes());
		editor.putString(PRE_CHANNEL_ICON_SMALL + index, info.getIconSmall());
		editor.putString(PRE_CHANNEL_ICON + index, info.getIcon());
		editor.putBoolean(PRE_CHANNEL_FREE + index, info.isFree());
		editor.putBoolean(PRE_CHANNEL_AUDIO + index, info.isAudio());
		editor.putString(PRE_CHANNEL_VIEW + index, info.getView());
		editor.putString(PRE_CHANNEL_LIKE + index, info.getCountLike());
		editor.putString(PRE_CHANNEL_STREAM_ID + index, info.getStreamId());
		editor.putBoolean(PRE_CHANNEL_FAVORIST + index, info.isFavorites());
		editor.putString(PRE_CHANNEL_STREAM_URL + index, info.getStreamUrl());
		editor.commit();
	}

	public ChannelInfo getChannelInfo(int index) {
		ChannelInfo info = new ChannelInfo();
		info.setId(getChannelId(index));
		info.setName(getChannelName(index));
		info.setScreenUrl(getChannelScreenUrl(index));
		info.setDes(getChannelDesc(index));
		info.setIconSmall(getChannelIconSmall(index));
		info.setIcon(getChannelIcon(index));
		info.setFree(getChannelFree(index));
		info.setAudio(getChannelAudio(index));
		info.setView(getChannelView(index));
		info.setCountLike(getChannelLike(index));
		info.setStreamId(getChannelStreamId(index));
		info.setFavorites(getChannelFav(index));
		info.setStreamUrl(getChannelStreamUrl(index));
		return info;
	}

	public void removeChannel(int index) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(PRE_CHANNEL_TYPE + index);
		editor.remove(PRE_CHANNEL_ID + index);
		editor.remove(PRE_CHANNEL_NAME + index);
		editor.remove(PRE_CHANNEL_SCREEN_URL + index);
		editor.remove(PRE_CHANNEL_DESC + index);
		editor.remove(PRE_CHANNEL_ICON_SMALL + index);
		editor.remove(PRE_CHANNEL_ICON + index);
		editor.remove(PRE_CHANNEL_FREE + index);
		editor.remove(PRE_CHANNEL_AUDIO + index);
		editor.remove(PRE_CHANNEL_VIEW + index);
		editor.remove(PRE_CHANNEL_LIKE + index);
		editor.remove(PRE_CHANNEL_STREAM_ID + index);
		editor.remove(PRE_CHANNEL_FAVORIST + index);
		editor.remove(PRE_CHANNEL_STREAM_URL + index);
		editor.commit();
	}

	/**********************************************************
	 * 
	 * VOD
	 * 
	 *********************************************************/
	public void setVodDes(String des, int index) {
		putStringValue(PRE_VOD_DESC + index, des);
	}

	public String getVodDes(int index) {
		return getStringValue(PRE_VOD_DESC + index);
	}

	public void setVodDuration(String duration, int index) {
		putStringValue(PRE_VOD_DURATION + index, duration);
	}

	public String getVodDuration(int index) {
		return getStringValue(PRE_VOD_DURATION + index);
	}

	public void setVodImage(String image, int index) {
		putStringValue(PRE_VOD_IMAGE + index, image);
	}

	public String getVodImage(int index) {
		return getStringValue(PRE_VOD_IMAGE + index);
	}

	public void setVodFree(boolean isFree, int index) {
		putBooleanValue(PRE_VOD_FREE + index, isFree);
	}

	public boolean getVodFree(int index) {
		return getBooleanValue(PRE_VOD_FREE + index);
	}

	public void setVodTags(String tag, int index) {
		putStringValue(PRE_VOD_TAGS + index, tag);
	}

	public String getVodTags(int index) {
		return getStringValue(PRE_VOD_TAGS + index);
	}

	public void setVodPrice(String price, int index) {
		putStringValue(PRE_VOD_PRICE + index, price);
	}

	public String getVodPrice(int index) {
		return getStringValue(PRE_VOD_PRICE + index);
	}

	public void setVodLandImg(String image, int index) {
		putStringValue(PRE_VOD_LAND_IMG + index, image);
	}

	public String getVodLandImg(int index) {
		return getStringValue(PRE_VOD_LAND_IMG + index);
	}

	public void setVodPortImg(String image, int index) {
		putStringValue(PRE_VOD_PORT_IMG + index, image);
	}

	public String getVodPortImg(int index) {
		return getStringValue(PRE_VOD_PORT_IMG + index);
	}

	public void setVodFav(boolean isFreen, int index) {
		putBooleanValue(PRE_VOD_FAVORIST + index, isFreen);
	}

	public boolean getVodFav(int index) {
		return getBooleanValue(PRE_VOD_FAVORIST + index);
	}

	public void setVodInfo(VodInfo info, int index) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(PRE_CHANNEL_TYPE + index, 1);
		editor.putString(PRE_CHANNEL_ID + index, info.getId());
		editor.putString(PRE_CHANNEL_NAME + index, info.getName());
		editor.putString(PRE_VOD_DESC + index, info.getDes());
		editor.putString(PRE_VOD_DURATION + index, info.getDuration());
		editor.putString(PRE_VOD_IMAGE + index, info.getImage());
		editor.putBoolean(PRE_VOD_FREE + index, info.isFree());
		editor.putString(PRE_VOD_TAGS + index, info.getTag());
		editor.putString(PRE_VOD_PRICE + index, info.getPrice());
		editor.putString(PRE_VOD_LAND_IMG + index, info.getLanpImage());
		editor.putString(PRE_VOD_PORT_IMG + index, info.getPortImage());
		editor.putString(PRE_CHANNEL_VIEW + index, info.getView());
		editor.putString(PRE_CHANNEL_LIKE + index, info.getCountLike());
		editor.putString(PRE_CHANNEL_STREAM_ID + index, info.getStreamId());
		editor.putBoolean(PRE_VOD_FAVORIST + index, info.isFavorites());
		editor.putString(PRE_CHANNEL_STREAM_URL + index, info.getStreamUrl());
		editor.commit();
	}

	public VodInfo getVodInfo(int index) {
		VodInfo info = new VodInfo();
		info.setId(getChannelId(index));
		info.setName(getChannelName(index));
		info.setDes(getVodDes(index));
		info.setImage(getVodImage(index));
		info.setSeries(false);
		info.setEpisodeCount("1");
		info.setFree(getVodFree(index));
		info.setTag(getVodTags(index));
		info.setPrice(getVodPrice(index));
		info.setLanpImage(getVodLandImg(index));
		info.setPortImage(getVodPortImg(index));
		info.setView(getChannelView(index));
		info.setCountLike(getChannelLike(index));
		info.setStreamId(getChannelStreamId(index));
		info.setFavorites(getVodFav(index));
		info.setStreamUrl(getChannelStreamUrl(index));
		return info;
	}

	public void removeVod(int index) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(PRE_CHANNEL_TYPE + index);
		editor.remove(PRE_CHANNEL_ID + index);
		editor.remove(PRE_CHANNEL_NAME + index);
		editor.remove(PRE_VOD_DESC + index);
		editor.remove(PRE_VOD_DURATION + index);
		editor.remove(PRE_VOD_IMAGE + index);
		editor.remove(PRE_VOD_FREE + index);
		editor.remove(PRE_VOD_TAGS + index);
		editor.remove(PRE_VOD_PRICE + index);
		editor.remove(PRE_VOD_LAND_IMG + index);
		editor.remove(PRE_VOD_PORT_IMG + index);
		editor.remove(PRE_CHANNEL_VIEW + index);
		editor.remove(PRE_CHANNEL_LIKE + index);
		editor.remove(PRE_CHANNEL_STREAM_ID + index);
		editor.remove(PRE_VOD_FAVORIST + index);
		editor.remove(PRE_CHANNEL_STREAM_URL + index);
		editor.commit();
	}

	/**********************************************************
	 * 
	 * Eposide
	 * 
	 *********************************************************/
	public String getEposideDuration(int index) {
		return getStringValue(PRE_EPOSIDE_DURATION + index);
	}

	public void setEposideDuration(String image, int index) {
		putStringValue(PRE_EPOSIDE_DURATION + index, image);
	}

	public String getEposideImg(int index) {
		return getStringValue(PRE_EPOSIDE_IMG_URL + index);
	}

	public void setEposideImg(String img, int index) {
		putStringValue(PRE_EPOSIDE_IMG_URL + index, img);
	}

	public String getEposideNumber(int index) {
		return getStringValue(PRE_EPOSIDE_NUMBER + index);
	}

	public void setEposideNumber(String img, int index) {
		putStringValue(PRE_EPOSIDE_NUMBER + index, img);
	}

	public String getEposideVodId(int index) {
		return getStringValue(PRE_EPOSIDE_VOD_ID + index);
	}

	public void setEposideVodId(String img, int index) {
		putStringValue(PRE_EPOSIDE_VOD_ID + index, img);
	}

	public void setEposideInfo(EpisodeInfo info, int index) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(PRE_CHANNEL_TYPE + index, 2);
		editor.putString(PRE_CHANNEL_ID + index, info.getId());
		editor.putString(PRE_CHANNEL_NAME + index, info.getName());
		editor.putString(PRE_EPOSIDE_DURATION + index, info.getDuration());
		editor.putString(PRE_EPOSIDE_IMG_URL + index, info.getImage());
		editor.putString(PRE_EPOSIDE_NUMBER + index, info.getEposideNumber());
		editor.putString(PRE_CHANNEL_VIEW + index, info.getView());
		editor.putString(PRE_CHANNEL_LIKE + index, info.getCountLike());
		editor.putString(PRE_EPOSIDE_VOD_ID + index, info.getVodId());
		editor.putString(PRE_CHANNEL_STREAM_ID + index, info.getStreamId());
		editor.putString(PRE_CHANNEL_STREAM_URL + index, info.getStreamUrl());
		editor.commit();
	}

	public EpisodeInfo getEposideInfo(int index) {
		EpisodeInfo info = new EpisodeInfo();
		info.setId(getChannelId(index));
		info.setName(getChannelName(index));
		info.setDuration(getEposideDuration(index));
		info.setImage(getEposideImg(index));
		info.setEposideNumber(getEposideNumber(index));
		info.setView(getChannelView(index));
		info.setCountLike(getChannelLike(index));
		info.setVodId(getEposideVodId(index));
		info.setStreamId(getChannelStreamId(index));
		info.setStreamUrl(getChannelStreamUrl(index));
		return info;
	}

	public void removeEposide(int index) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(PRE_CHANNEL_TYPE + index);
		editor.remove(PRE_CHANNEL_ID + index);
		editor.remove(PRE_CHANNEL_NAME + index);
		editor.remove(PRE_EPOSIDE_DURATION + index);
		editor.remove(PRE_EPOSIDE_IMG_URL + index);
		editor.remove(PRE_EPOSIDE_NUMBER + index);
		editor.remove(PRE_CHANNEL_VIEW + index);
		editor.remove(PRE_CHANNEL_VIEW + index);
		editor.remove(PRE_EPOSIDE_VOD_ID + index);
		editor.remove(PRE_CHANNEL_STREAM_ID + index);
		editor.remove(PRE_CHANNEL_STREAM_URL + index);
		editor.commit();
	}

	public void setUserInfo(String userName, String pass) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(PRE_USER_NAME, userName);
		editor.putString(PRE_USER_PASS, pass);
		editor.commit();
	}

//	public void setSMSGateway(String gateway){
//		SharedPreferences pref = context.getSharedPreferences(JEMAND_PREFERENCES, 0);
//		SharedPreferences.Editor editor = pref.edit();
//		editor.putString(PRE_SMS_GATEWAY, gateway);
//		editor.commit();
//	}
//
//	public void setNonSMSGateway(String gateway){
//		SharedPreferences pref = context.getSharedPreferences(JEMAND_PREFERENCES, 0);
//		SharedPreferences.Editor editor = pref.edit();
//		editor.putString(PRE_NON_SMS_GATEWAY, gateway);
//		editor.commit();
//	}
//
//	public void setDeletedSMSGateway(int deleted){
//		SharedPreferences pref = context.getSharedPreferences(JEMAND_PREFERENCES, 0);
//		SharedPreferences.Editor editor = pref.edit();
//		editor.putInt(PRE_SMS_GATEWAY, deleted);
//		editor.commit();
//	}
//
//	public void setDeletedNonSMSGateway(int deleted){
//		SharedPreferences pref = context.getSharedPreferences(JEMAND_PREFERENCES, 0);
//		SharedPreferences.Editor editor = pref.edit();
//		editor.putInt(PRE_NON_SMS_GATEWAY, deleted);
//		editor.commit();
//	}

	public void setChargingInfo(Charginginfo info){
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(PRE_SMS_GATEWAY, info.smsGateway);
		editor.putString(PRE_NON_SMS_GATEWAY, info.nonSmsGateway);
		editor.putInt(PRE_DELETED_SMS_GATEWAY, info.isDeleteSmsReceiver);
		editor.putInt(PRE_DELETED_NON_SMS_GATEWAY, info.isDeleteNonSmsReceiver);
		editor.commit();
	}

	public String getSMSGateway(){
		return getStringValue(PRE_SMS_GATEWAY);
	}

	public String getNonSMSGateway(){
		return getStringValue(PRE_NON_SMS_GATEWAY);
	}

	public int getDeletedSMSGateway(){
		return getIntValue(PRE_DELETED_SMS_GATEWAY);
	}

	public int getDeletedNonSMSGateway(){
		return getIntValue(PRE_DELETED_NON_SMS_GATEWAY);
	}

	public String getUserName() {
		return getStringValue(PRE_USER_NAME);
	}

	public String getPassUser() {
		return getStringValue(PRE_USER_PASS);
	}

	public String getSessionCache(){
		return getStringValue(PRE_USER_SESSION);
	}

	public String getSessionChecked(){
		return getStringValue(PRE_USER_SESSION_CHECKED);
	}

	public void setSessionCache(String session){
		SharedPreferences pref = context.getSharedPreferences(JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(PRE_USER_SESSION, session);
		editor.commit();
	}

	public void setSessionChecked(String session){
		SharedPreferences pref = context.getSharedPreferences(JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(PRE_USER_SESSION_CHECKED, session);
		editor.commit();
	}

	public void setShowPolicy(boolean isShowPolicy) {
		putBooleanValue(PRE_SHOW_POLICY, isShowPolicy);
	}

	public boolean isShowPolicy() {
		return getBooleanValueDefaultTrue(PRE_SHOW_POLICY);
	}

	public void removeUserInfo() {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(PRE_USER_NAME);
		editor.remove(PRE_USER_PASS);
		editor.commit();
	}

	public boolean checkFavorist(int type, String id) {
		int count = getFavoristCount();
		for (int i = 0; i < count; i++) {
			int pType = getVtvType(i);
			if (pType == type) {
				String pId = getChannelId(i);
				if (pId.equalsIgnoreCase(id)) {
					return true;
				}
			}
		}
		return false;
	}

	// ======================== CORE FUNCTIONS ========================

	/**
	 * Clear all preferences
	 */
	public void clearAllData() {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * Save a long integer to SharedPreferences
	 * 
	 * @param key
	 * @param n
	 */
	public void putLongValue(String key, long n) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, n);
		editor.commit();
	}

	/**
	 * Read a long integer to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public long getLongValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		return pref.getLong(key, 0);
	}

	/**
	 * Save an integer to SharedPreferences
	 * 
	 * @param key
	 * @param n
	 */
	public void putIntValue(String key, int n) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, n);
		editor.commit();
	}

	/**
	 * Read an integer to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public int getIntValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		return pref.getInt(key, 0);
	}

	/**
	 * Save an string to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putStringValue(String key, String s) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, s);
		editor.commit();
	}

	/**
	 * Read an string to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public String getStringValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		return pref.getString(key, "");
	}

	/**
	 * Read an string to SharedPreferences
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getStringValue(String key, String defaultValue) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		return pref.getString(key, defaultValue);
	}

	/**
	 * Save an boolean to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putBooleanValue(String key, Boolean b) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	/**
	 * Read an boolean to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}

	/**
	 * Read an boolean to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanValueDefaultTrue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		return pref.getBoolean(key, true);
	}

	/**
	 * Save an float to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putFloatValue(String key, float f) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putFloat(key, f);
		editor.commit();
	}

	/**
	 * Read an float to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public float getFloatValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				JEMAND_PREFERENCES, 0);
		return pref.getFloat(key, 0.0f);
	}

}