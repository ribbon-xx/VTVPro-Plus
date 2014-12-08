/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mdn.vtvpluspro.gcm;

import mdn.vtvpluspro.network.ApiManager;
import mdn.vtvpluspro.network.IApiCallback;
import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public final class ServerUtilities {

	public static final String SENDER_ID = "237993844903";

	public static boolean register(final Context context, final String regId) {
		try {
			postRegistrationId(context, regId);
			GCMRegistrar.setRegisteredOnServer(context, true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void unregister(final Context context, final String regId) {
		try {
			postRegistrationId(context, regId);
			GCMRegistrar.setRegisteredOnServer(context, false);
		} catch (Exception e) {
		}
	}

	public static void postRegistrationId(final Context context,
			final String regId) {
//		ApiManager.callUpdateGCMRegistrationId(context, new IApiCallback() {
//
//			@Override
//			public void responseSuccess(String response) {
//				Log.e("response", response);
//
//			}
//
//			@Override
//			public void responseFailWithCode(int statusCode) {
//			}
//		}, regId, "android", HomeFragment.APP_NAME, NetworkUtility.getIpAddress(), NetworkUtility.getTypeNetwork(context));
		
		// update
		ApiManager.callUrlPushNotification(context, new IApiCallback() {
			
			@Override
			public void responseSuccess(String response) {
				Log.e("response", response);
			}
			
			@Override
			public void responseFailWithCode(int statusCode) {
				Log.e("response", "responseFailWithCode:" + statusCode);
			}
		}, regId);
	}
}
