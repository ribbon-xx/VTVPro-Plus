/*
 * Name: $RCSfile: AsyncHttpBase.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 4:21:50 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvpluspro.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

/**
 * AsyncHttpBase is base class for AsyncHttpGet and AsyncHttpPost class
 * 
 * @author MC
 */
public class AsyncHttpBase extends AsyncTask<String, Integer, String> {
	// Network status
	public static final int NETWORK_STATUS_OK = 0;
	public static final int NETWORK_STATUS_OFF = 1;
	public static final int NETWORK_STATUS_ERROR = 2;
	public static final int RESPONSE_OK = 200;
	protected Context context;
	protected ArrayList<NameValuePair> headers;
	protected AsyncHttpResponseListener listener;
	protected List<NameValuePair> parameters;
	protected List<NameValuePair> files;
	protected HttpResponse response;
	protected String resString;
	protected int statusCode;
	protected String appVersion;
	protected String jsonBody;
	protected int timeOut;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param parameters
	 */
	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters) {
		this.context = context;
		this.listener = listener;
		this.appVersion = WebServiceConfig.APP_API_VERSION;
		this.parameters = parameters;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param parameters
	 */
	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> headers, List<NameValuePair> parameters,
			List<NameValuePair> files) {
		this.context = context;
		this.listener = listener;
		this.appVersion = WebServiceConfig.APP_API_VERSION;
		this.parameters = parameters;
		this.files = files;
	}

	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener) {
		this.context = context;
		this.listener = listener;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param header
	 * @param jsonBody
	 */
	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener,
			ArrayList<NameValuePair> headers, String jsonBody) {
		this.context = context;
		this.listener = listener;
		this.headers = headers;
		this.jsonBody = jsonBody;
		this.appVersion = WebServiceConfig.APP_API_VERSION;
		this.timeOut = 0;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param listener
	 * @param header
	 * @param jsonBody
	 */
	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener,
			ArrayList<NameValuePair> headers, String jsonBody, int timeOut) {
		this.context = context;
		this.listener = listener;
		this.headers = headers;
		this.jsonBody = jsonBody;
		this.appVersion = WebServiceConfig.APP_API_VERSION;
		this.timeOut = timeOut;
	}

	public AsyncHttpBase(Context context) {
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() {

		if (listener != null)
			listener.before();
		super.onPreExecute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(String... args) {
		if (NetworkUtility.isNetworkAvailable(context)) {
			// Request to server if network is available
			return request(args[0]);
		} else {
			// Return status if network is not available
			statusCode = NETWORK_STATUS_OFF;
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		// Call method to process http status code and response
		if (listener != null)
			listener.after(statusCode, resString);
	}

	/**
	 * Send request to server
	 * 
	 * @param url
	 * @return
	 */
	protected String request(String url) {
		// This function will be implemented in AsyncHttpGet and AsyncHttpPost
		// class
		return null;
	}

	// ============================================================================

	/**
	 * Create HttpClient based on HTTP or HTTPS protocol that is parsed from url
	 * parameter. With HTTPS protocol, we accept all SSL certificates.
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	protected HttpClient createHttpClient(String url, HttpParams params) {
		if ((url.toLowerCase().startsWith("https"))) {
			// HTTPS process
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						params, registry);
				return new DefaultHttpClient(ccm, params);
			} catch (Exception e) {
				return new DefaultHttpClient(params);
			}
		} else {
			// HTTP process
			return new DefaultHttpClient(params);
		}
	}

	// ============================ Https functions ============================

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			// @Override
			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			// @Override
			@Override
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			// @Override
			@Override
			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open HTTPS connection. Use this method to setup and accept all SSL
	 * certificates from HTTPS protocol.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static HttpsURLConnection openSConnection(String url)
			throws IOException {
		URL theURL = new URL(url);
		trustAllHosts();
		HttpsURLConnection https = (HttpsURLConnection) theURL.openConnection();
		https.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		return https;
	}

	/**
	 * Open HTTP connection
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static HttpURLConnection openConnection(String url)
			throws IOException {
		URL theURL = new URL(url);
		return (HttpURLConnection) theURL.openConnection();
	}

	protected String getHeaderUserAgent(String apiVersion) {

		StringBuffer buf = new StringBuffer();
		buf.append("Jukebox Hero/");
		buf.append(apiVersion);
		buf.append(" (Android ");
		buf.append(Build.VERSION.RELEASE);
		buf.append("; ");
		buf.append(Build.MODEL);
		buf.append(" Build/");
		buf.append(Build.ID);
		buf.append(")");
		return buf.toString();
	}
}
