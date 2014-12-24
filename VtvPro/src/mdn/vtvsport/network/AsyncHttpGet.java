/*
 * Name: $RCSfile: AsyncHttpGet.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: May 12, 2011 2:38:36 PM $
 *
 * Copyright (C) 2011 COMPANY NAME, Inc. All rights reserved.
 */

package mdn.vtvsport.network;

import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;

/**
 * AsyncHttpGet makes http get request based on AsyncTask
 * 
 * @author MC
 */
public class AsyncHttpGet extends AsyncHttpBase {
	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param parameters
	 */
	public AsyncHttpGet(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters) {
		super(context, listener, parameters);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 */
	public AsyncHttpGet(Context context, AsyncHttpResponseListener listener) {
		super(context, listener);
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
			if (parameters != null) {
				Iterator<NameValuePair> it = parameters.iterator();
				while (it.hasNext()) {
					NameValuePair nv = it.next();
					params.setParameter(nv.getName(), nv.getValue());
				}
			}
			HttpConnectionParams.setConnectionTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params,
					WebServiceConfig.NETWORK_TIME_OUT);

			HttpClient httpclient = createHttpClient(url, params);
			HttpGet httpget = new HttpGet(url);
			response = httpclient.execute(httpget);
			resString = EntityUtils.toString(response.getEntity());
			statusCode = RESPONSE_OK;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
