/*
 * Name: $RCSfile: AsyncHttpPost.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Apr 21, 2011 2:43:05 PM $
 *
 * Copyright (C) 2011 COMPANY NAME, Inc. All rights reserved.
 */

package mdn.vtvpluspro.network;

import java.util.List;

import mdn.vtvpluspro.util.StringUtil;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

/**
 * AsyncHttpGet makes http post request based on AsyncTask
 * 
 * @author khanhnv
 */
public class AsyncHttpUploadFeedback extends AsyncHttpBase {

	public AsyncHttpUploadFeedback(Context context,
			AsyncHttpResponseListener listener, List<NameValuePair> headers,
			List<NameValuePair> parameters) {
		super(context, listener, headers, parameters, null);
	}

	@Override
	protected String request(String url) {
		Log.d("POST", "url = " + url);

		try {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);
			HttpClient httpclient = createHttpClient(url, params);

			HttpPost httppost = new HttpPost(url);

			/** Set header */
			if (headers != null && headers.size() > 0) {
				for (NameValuePair header : headers) {
					httppost.setHeader(header.getName(), header.getValue());
				}
				Log.i("POST", "Header = " + headers.toString());
			}

			/** Set body */
			if (!StringUtil.isEmpty(jsonBody)) {
				StringEntity entity = new StringEntity(jsonBody, "UTF-8");
				httppost.setEntity(entity);
				Log.i("POST", "jsonbody = " + "'" + jsonBody + "'");
			}

			if (parameters != null) {
				httppost.setEntity(new UrlEncodedFormEntity(parameters));
				Log.i("POST", "params = " + parameters.toString());
			}

			Log.i("POST", "url = " + url);

			response = httpclient.execute(httppost);
			resString = EntityUtils.toString(response.getEntity());
			Log.i("POST", "response = " + resString);
			statusCode = RESPONSE_OK;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			Log.e("POST", e.toString());
		}
		return null;
		// Set upload files to form
		// if (files != null) {
		// Iterator<NameValuePair> fit = files.iterator();
		// while (fit.hasNext()) {
		// NameValuePair nv = fit.next();
		// FileManager.resizeFileImage(nv.getValue(), Config.FILE_IMAGE_PATH1,
		// 400);
		// entity.addPart(nv.getName(),
		// new FileBody(new File(Config.FILE_IMAGE_PATH1)));
		// }
		// Log.d("POST", "Files = " + files.toString());

	}
}
