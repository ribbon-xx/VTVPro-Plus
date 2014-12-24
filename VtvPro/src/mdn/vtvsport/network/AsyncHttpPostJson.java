/*
 * Name: $RCSfile: AsyncHttpPost.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Apr 21, 2011 2:43:05 PM $
 *
 * Copyright (C) 2011 COMPANY NAME, Inc. All rights reserved.
 */

package mdn.vtvsport.network;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * AsyncHttpGet makes http post request based on AsyncTask
 * 
 * @author Tyson
 */
public class AsyncHttpPostJson extends AsyncHttpBase {

	private JSONObject json;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param jsonParam
	 */
	public AsyncHttpPostJson(Context context,
			AsyncHttpResponseListener listener, JSONObject jsonParam) {
		super(context, listener);
		this.json = jsonParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.seta.medtalkdroid.app.network.AsyncHttpBase#request(java.lang.String)
	 */
	@Override
	protected String request(String url) {
		try {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);
			HttpClient httpclient = createHttpClient(url, params);

			HttpPost httppost = new HttpPost(url);
			headers = WebServiceUtility.createPostHeader();

			/** Set header */
			if (headers != null && headers.size() > 0) {
				for (NameValuePair header : headers) {
					httppost.setHeader(header.getName(), header.getValue());

				}
				Log.i("POST", "Header = " + headers.toString());
			}

			if (json != null) {
				StringEntity entity = new StringEntity(json.toString());
				entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						"application/json"));
				entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_ENCODING,
						"UTF-8"));
				httppost.setEntity(entity);
			}
			response = httpclient.execute(httppost);
			resString = EntityUtils.toString(response.getEntity());
			statusCode = RESPONSE_OK;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
