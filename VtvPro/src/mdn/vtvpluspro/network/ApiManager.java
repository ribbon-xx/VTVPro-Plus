/*
 * Name: $RCSfile: ApiManager.java,v $
 * Version: $Revision: 1.0 $
 * Date: $Date: Nov 3, 2012 1:07:15 PM $
 *
 * Copyright (C) 2012 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvpluspro.network;

import android.content.Context;
import mdn.vtvpluspro.Config;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * ApiManager supports to call web service api
 *
 * @author MC
 */
public final class ApiManager {
	static String TAG = "ApiManager";

	/**
	 * Call top recipes
	 *
	 * @param context
	 * @param apiCallback
	 */

	public static void callListItemOfCategory(Context context,
	                                          IApiCallback apiCallback, String cateId, int offset,
	                                          String userName, boolean isShowDialog) {
		callHttpGet(context, apiCallback,
				WebServiceConfig.getItemOfCategory(cateId, offset, userName),
				isShowDialog);
	}

	public static void callListItemOfListNew(Context context,
	                                         IApiCallback apiCallback, int offset, String userName, boolean isShowDialog) {
		callHttpGet(context, apiCallback,
				WebServiceConfig.getItemOfListNew(offset, userName), isShowDialog);
	}

	public static void callListItemOfChannel(Context context,
			IApiCallback apiCallback, int offset, String userName,
			boolean isShowDialog) {
		callHttpGet(context, apiCallback,
				WebServiceConfig.getItemOfChannel(offset, userName), isShowDialog);
}
	
	public static void callListItemOfFavourist(Context context, IApiCallback apiCallback,
	                                           int offset, String userName, boolean isShowDialog) {
		callHttpGet(context, apiCallback, WebServiceConfig.getItemOfListFavoursit(offset, userName), true);
	}

	public static void callListItemOfListHot(Context context,
	                                         IApiCallback apiCallback, int offset, String userName, boolean isShowDialog) {
		callHttpGet(context, apiCallback,
				WebServiceConfig.getItemOfListHot(offset, userName), isShowDialog);
	}

	public static void callListEposideOfVod(Context context,
	                                        IApiCallback apiCallback, String deviceId, String cateId,
	                                        int offset, String userName) {
		callHttpGet(context, apiCallback,
				WebServiceConfig.getEposideOfVod(deviceId, cateId, offset, userName),
				true);
	}

	public static void callListSearch(Context context,
	                                  IApiCallback apiCallBack, String keyword, int offset, String userName) {
		callHttpGet(context, apiCallBack,
				WebServiceConfig.getUrlSearch(keyword, offset, userName), true);
	}

	public static void callListHome(Context context,
	                                IApiCallback apiCallback, String email) {
		callHttpGet(context, apiCallback, WebServiceConfig.getListHome(email), false);
	}

	public static void callListStream(Context context,
	                                  IApiCallback apiCallback, String streamId, int type, String userName, String session, String ip) {
		String netWork = "";
		if (NetworkUtility.isWifi(context)) {
			netWork = "wifi";
		} else {
			netWork = "3g";
		}
		callHttpGet(context, apiCallback,
				WebServiceConfig.getListStream(streamId, userName, ip, session, netWork, type), true);
	}

	public static void callListStream(Context context,
	                                  IApiCallback apiCallback, String urlStream) {
		callHttpGet(context, apiCallback, urlStream, true);
	}

	public static void callRelationItemVtvPlus(Context context,
	                                           IApiCallback apiCallback, String deviceId, String itemId,
	                                           String app_name, int limit, int offset, int type, String userName, boolean isDialog) {
		callHttpGet(context, apiCallback,
				WebServiceConfig.getDetailsItemVtvPlus(deviceId, itemId,
						app_name, limit, offset, userName, type), isDialog);
	}

	public static void callLogin(Context context,
	                             IApiCallback apiCallback, String userName, String passWord) {
//		callHttpPost(context, apiCallback,
//				WebServiceUtility.createParamLogin(userName, passWord),
		
		callHttpGet(context, apiCallback,
				WebServiceConfig.getUrlLogin(userName, passWord), true);
	}

	public static void callAccountInfo(Context context,
	                                   IApiCallback apiCallback, String userName, String session) {
		callHttpGet(context, apiCallback,
				WebServiceConfig.getAccountInfo(userName, session), true);
	}

	public static void callRegister(Context context,
	                                IApiCallback apiCallback, String user, String pass,
	                                String challenge, String captcha) {
		callHttpPost(context, apiCallback,
				WebServiceUtility.createParamRegister(user, pass, challenge,
						captcha), WebServiceConfig.getUrlRegiter(), true);
	}

	public static void callListSchedule(Context context,
	                                    IApiCallback apiCallback, String channelId) {
		callHttpGet(context, apiCallback,
				WebServiceConfig.getUrlSchedule(channelId), false);
	}

	public static void callUpdateRank(Context context, IApiCallback apiCallback, String rankId) {
		callHttpGet(context, apiCallback, WebServiceConfig.getUrlUpdateRank(rankId), false);
	}

	public static void callSupport(Context context, IApiCallback apiCallback, String url) {
		callHttpGet(context, apiCallback, url, true);
	}

	public static void callActionLike(Context context, IApiCallback apiCallback, int type, String id, String userName) {
		callHttpPost(context, apiCallback, WebServiceUtility.createParamsLike(id, userName, type),
				WebServiceConfig.getUrlLike(type), true);
	}

	public static void callActionFavourist(Context context, IApiCallback apiCallback, int type, String id, String userName) {
		callHttpPost(context, apiCallback, WebServiceUtility.createParamsLike(id, userName, type),
				WebServiceConfig.getUrlFavourist(type), true);
	}

	public static void callInteraction(Context context, IApiCallback apiCallback, String id) {
		callHttpGet(context, apiCallback, WebServiceConfig.getUrlInteractionChannel(id), true);
	}

	public static void callAllInteraction(Context context, IApiCallback apiCallback) {
		callHttpGet(context, apiCallback, WebServiceConfig.getUrlAllInteraction(), true);
	}

	public static void callCategoryVideoMenu(Context context, IApiCallback apiCallback) {
		callHttpGet(context, apiCallback, WebServiceConfig.getCategoryVideoMenu(), true);
	}
	
	public static void callDetailAds(Context context, IApiCallback apiCallback, String idAds) {
		callHttpGet(context, apiCallback, WebServiceConfig.getUrlAds(idAds), true);
	}

	public static void callLogPush(Context context, IApiCallback apiCallback, String deviceid, String messId) {
		callHttpGet(context, null, WebServiceConfig.getUrlLogPush(deviceid, messId), false);
	}

	public static void callCheckUserCharged(Context context, String userName, String session, IApiCallback callback) {
		callHttpGet(context, callback, WebServiceConfig.getUrlCheckCharged(userName, session), true);
	}

	public static void callCheckPaymentSetting(Context context, IApiCallback callback) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("production_id", "467"));
		params.add(new BasicNameValuePair("distributor_id", "6"));
		params.add(new BasicNameValuePair("distributor_channel_id", Config.DISTRIBUTOR_CHANNEL_ID));
		params.add(new BasicNameValuePair("platform_id", "2"));
		callHttpPost(context, callback, params, WebServiceConfig.getUrlCheckChargingEnable(), true);
	}

	public static void callGetChargeSetting(Context context, IApiCallback callback, String distributorChannelId, String operatorCode) {
		String url = WebServiceConfig.getUrlGetPaymentPopupSetting(distributorChannelId, operatorCode);
		callHttpGet(context, callback, url, true);
	}

	public static void callCheckAppId(Context context, IApiCallback callback, String appId){
		String url = WebServiceConfig.getUrlCheckAppId(appId);
		callHttpGet(context, callback, url, false);
	}

	/**
	 * *********************************************
	 * <p/>
	 * Update view of channel and vod
	 * <p/>
	 * **********************************************
	 */
	public static void callUpdateViewChannel(Context context, String channelId) {
		List<NameValuePair> parameters = WebServiceUtility
				.createUpdateViewChannel(channelId);
		callHttpPost(context, null, parameters,
				WebServiceConfig.urlUpdateViewChannel(), false);
	}

	public static void callUpdateViewVod(Context context, String channelId) {
		List<NameValuePair> parameters = WebServiceUtility
				.createUpdateViewVod(channelId);
		callHttpPost(context, null, parameters,
				WebServiceConfig.urlUpdateViewVod(), false);
	}

	public static void callUpdateGCMRegistrationId(Context context,
	                                               IApiCallback apiCallback, String regisId, String type,
	                                               String appName, String ip, String typeNetwork) {
		List<NameValuePair> parameters = WebServiceUtility
				.createUpdateGCMRegistrationId(regisId, type, appName, ip, typeNetwork);
		callHttpPost(context, apiCallback, parameters,
				WebServiceConfig.gcmPostRegistrationId, false);
	}

	public static void callIpAdress(Context context, IApiCallback apiCallback) {
		callHttpGet(context, apiCallback, "http://api.vtvsport.com.vn/index.php/GetIp/", true);
	}

	/**
	 * *********************************************
	 * <p/>
	 * Check version code
	 * <p/>
	 * **********************************************
	 */
	public static void callCheckVersionCode(Context context,
	                                        IApiCallback apiCallback, String version) {
		callHttpGet(context, apiCallback,
				WebServiceConfig.checkVersionApp(version), false);
	}

	public static void callGetInfoApp(Context context,
	                                  IApiCallback apiCallback, String version) {
		callHttpGet(context, apiCallback, WebServiceConfig.getInfoApp(version),
				false);
	}

	public static void callUpdateViewEpisode(Context context, String channelId) {
		List<NameValuePair> parameters = WebServiceUtility
				.createUpdateViewEpisode(channelId);
		callHttpPost(context, null, parameters,
				WebServiceConfig.urlUpdateViewEpisode(), false);
	}

	public static void callHttpGet(Context context,
	                               final IApiCallback apiCallback, String url, boolean isShowDialog) {
		AsyncHttpGet asyncHttpGet = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess(context, isShowDialog) {
					@Override
					public void processIfResponseSuccess(String response) {
						if (apiCallback != null) {
							apiCallback.responseSuccess(response);
						}
					}

					@Override
					public void processIfResponseFailWithCode(int statusCode,
					                                          String resString) {
						if (apiCallback != null) {
							apiCallback.responseFailWithCode(statusCode);
						}
					}
				});
		asyncHttpGet.execute(url);
	}

	public static void callHttpPost(Context context,
	                                final IApiCallback callback, List<NameValuePair> paraList,
	                                String url, boolean isShowDialog) {
		AsyncHttpPost asyncHttpPost = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess(context, isShowDialog) {
					@Override
					public void processIfResponseSuccess(String response) {
						if (callback != null) {
							callback.responseSuccess(response);
						}
					}

					@Override
					public void processIfResponseFailWithCode(int statusCode,
					                                          String resString) {
						if (callback != null) {
							callback.responseFailWithCode(statusCode);
						}
					}
				}, paraList);
		asyncHttpPost.execute(url);
	}
}