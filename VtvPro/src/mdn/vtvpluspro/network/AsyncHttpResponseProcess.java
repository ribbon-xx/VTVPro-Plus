/*
 * Name: $RCSfile: AsyncHttpResponseProcess.java,v $ Version: $Revision: 1.1 $
 * Date: $Date: Dec 8, 2011 9:54:28 AM $
 * 
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvpluspro.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import mdn.vtvpluspro.common.DialogManager;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

/**
 * AsyncHttpResponseProcess: process server response
 * 
 * @author MC
 */
public class AsyncHttpResponseProcess implements AsyncHttpResponseListener {
	private static final String TAG = "AsyncHttpResponseProcess";

	private Context context;
	private String msgWaiting = "";
	private Dialog simpleProgressDialog = null;
	private boolean isShowMessage = false;
	private boolean isShowDialog = true;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public AsyncHttpResponseProcess(Context context) {
		this.context = context;
		this.msgWaiting = "";
		this.isShowMessage = false;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param msgWaiting
	 *            : pass null to use default message "Please wait"
	 */
	public AsyncHttpResponseProcess(Context context, String msgWaiting) {
		this.context = context;
		this.msgWaiting = msgWaiting;
		this.isShowMessage = false;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param isShowDialog
	 */
	public AsyncHttpResponseProcess(Context context, boolean isShowDialog) {
		this.context = context;
		this.isShowDialog = isShowDialog;
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param msgWaiting
	 * @param isShowMessage
	 */
	public AsyncHttpResponseProcess(Context context, String msgWaiting,
			boolean isShowMessage) {
		this.context = context;
		this.msgWaiting = msgWaiting;
		this.isShowMessage = isShowMessage;
	}

	@Override
	public void before() {
		/** Show waiting dialog during connection */
		if (context != null) {

			if (isShowDialog) {
				DialogManager.showSimpleProgressDialog(context);
			}

		}
	}

	@Override
	public void after(int statusCode, String resString) {
		if (context != null) {
			/** Close common progress dialog */
			if (isShowDialog) {
				DialogManager.closeProgressDialog();
			}

			/** Close progress dialog for start, sync api */
			// DialogManager.closeSyncJukeboxDialog();

			/** Process server response */
			Log.d(TAG, "Response code:" + statusCode);
			switch (statusCode) {
			case AsyncHttpBase.NETWORK_STATUS_OFF:
				processIfResponseFailWithCode(statusCode);
				break;
			case AsyncHttpBase.RESPONSE_OK:
				processHttpResponse(resString);
				break;
			default:
				processIfResponseFailWithCode(statusCode, resString);
			}
		}
	}

	/**
	 * Process HttpResponse
	 * 
	 * @param response
	 */
	public void processHttpResponse(String resString) {

		try {
			/** Get json response */
			if (resString == null) {
				Log.e(TAG, "Can not extract server response" + resString);
				return;
			}

			/** Parser information */
			Log.d(TAG, "200 OK - response string:" + resString);
			processIfResponseSuccess(resString);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
			e.printStackTrace();
		}
	}

	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		is.close();
		return sb.toString();
	}

	/**
	 * Interface function
	 */
	public void processIfResponseSuccess(String response) {
		// TODO: Implement for case response success
	}

	/**
	 * Interface function
	 */
	public void processIfResponseFailWithCode(int statusCode) {
	}// TODO: Implement for case response fail with error code

	/**
	 * Interface function
	 */
	public void processIfResponseFail() {
		// TODO: Implement for case response fail
	}

	/**
	 * Interface function
	 */
	public void processIfResponseFailWithCode(int statusCode, String resString) {
		// TODO: Implement for case response fail with error code
	}

	/**
	 * Close progress dialog
	 */
	public void closeProgressDialog() {
		if (simpleProgressDialog != null) {
			try {
				simpleProgressDialog.cancel();
				simpleProgressDialog = null;
			} catch (Exception e) {
				// Handle exception: do nothing here
			}
		}
	}

}