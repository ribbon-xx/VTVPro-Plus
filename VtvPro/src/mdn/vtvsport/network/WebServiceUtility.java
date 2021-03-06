/*
 * Name: $RCSfile: WebServiceUtility.java,v $
 * Version: $Revision: 1.0 $
 * Date: $Date: Nov 1, 2012 2:18:23 PM $
 *
 * Copyright (C) 2012 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvsport.network;

import mdn.vtvsport.fragment.HomeFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * WebServiceUtility supports to build body data and header for api request
 * 
 * @author Quan
 */
public class WebServiceUtility {
	/* ===================== HEADER ====================== */

	/**
	 * Create header for post request
	 * 
	 * @return
	 */
	public static ArrayList<NameValuePair> createPostHeader() {
		ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair(WebServiceParam.HEADER_CONTENT_TYPE,
				WebServiceParam.HEADER_CONTENT_TYPE_JSON));
		return headers;
	}

	/**
	 * Create header for post request
	 * 
	 * @return
	 */
	public static ArrayList<NameValuePair> createPostHeaderHTML() {
		ArrayList<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair(WebServiceParam.HEADER_CONTENT_TYPE,
				WebServiceParam.HEADER_CONTENT_TYPE_HTML));
		return headers;
	}

	public static List<NameValuePair> createFavoritesRecipes(String deviceId,
			String recipeId) {

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(WebServiceParam.PARAM_DEVICE_ID,
				deviceId));
		parameters.add(new BasicNameValuePair(WebServiceParam.PARAM_RECIPE_ID,
				recipeId));

		return parameters;
	}

	public static List<NameValuePair> createUpdateViewChannel(String channelId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(
				WebServiceParam.PARAM_UPDATEVIEW_CHANNEL, channelId));
		parameters.add(new BasicNameValuePair(WebServiceParam.PARAM_APP_NAME, HomeFragment.APP_NAME));
		return parameters;
	}

	public static List<NameValuePair> createUpdateViewVod(String vodId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(
				WebServiceParam.PARAM_UPDATEVIEW_VOD, vodId));
		parameters.add(new BasicNameValuePair(WebServiceParam.PARAM_APP_NAME, HomeFragment.APP_NAME));
		return parameters;
	}

	public static List<NameValuePair> createUpdateGCMRegistrationId(
			String regisId, String type, String appName, String ip, String typeNetwork) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(
				WebServiceParam.PARAM_GCM_REGISTRATIONID, regisId));
		parameters.add(new BasicNameValuePair(WebServiceParam.PARAM_GCM_TYPE,
				type));
		parameters.add(new BasicNameValuePair(
				WebServiceParam.PARAM_APP_NAME, appName));
		parameters.add(new BasicNameValuePair(WebServiceParam.PARAM_GCM_IP, ip));
		parameters.add(new BasicNameValuePair(WebServiceParam.PARAM_GCM_TYPE_CONNECT, typeNetwork));
		return parameters;
	}

	public static List<NameValuePair> createUpdateViewEpisode(String vodId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair(
				WebServiceParam.PARAM_UPDATEVIEW_EPISODE, vodId));
		parameters.add(new BasicNameValuePair(WebServiceParam.PARAM_APP_NAME, HomeFragment.APP_NAME));
		return parameters;
	}
	
	public static List<NameValuePair> createParamLogin(String userName, String passWord) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebServiceParam.PARAM_USER_NAME, userName));
		params.add(new BasicNameValuePair(WebServiceParam.PARAM_PASS, passWord));
		return params;
	}
	
	public static List<NameValuePair> createParamRegister(String user, String pass, String challenge, String captcha) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(WebServiceParam.PARAM_USER_NAME, user));
		params.add(new BasicNameValuePair(WebServiceParam.PARAM_PASS, pass));
		params.add(new BasicNameValuePair(WebServiceParam.PARAM_CHALLENGE, challenge));
		params.add(new BasicNameValuePair(WebServiceParam.PARAM_CAPTCHA, captcha));
		return params;
	}
	
	public static List<NameValuePair> createParamsLike(String id, String userName, int type) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (type == 0) {
			params.add(new BasicNameValuePair(WebServiceParam.PARAM_UPDATEVIEW_CHANNEL, id));
		} else if (type == 1) {
			params.add(new BasicNameValuePair(WebServiceParam.PARAM_UPDATEVIEW_VOD, id));
		} else if (type == 2) {
			params.add(new BasicNameValuePair(WebServiceParam.PARAM_UPDATEVIEW_EPISODE, id));
		}
		params.add(new BasicNameValuePair(WebServiceParam.PARAM_APP_NAME, HomeFragment.APP_NAME));
		params.add(new BasicNameValuePair(WebServiceParam.PARAM_USER_NAME, userName));
		return params;
	}
}