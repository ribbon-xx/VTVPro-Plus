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
package mdn.vtvpluspro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import mdn.vtvplus.R;
import mdn.vtvpluspro.common.ParserManager;
import mdn.vtvpluspro.fragment.HomeFragment;
import mdn.vtvpluspro.gcm.ServerUtilities;
import mdn.vtvpluspro.network.ApiManager;
import mdn.vtvpluspro.object.GCMInfo;
import mdn.vtvpluspro.util.DeviceUtil;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	public static final String GCMBUNDLE_ID = "GCMBUNDLE_ID";
	public static final String GCMBUNDLE_TYPE = "GCMBUNDLE_TYPE";

	public GCMIntentService() {
		super(ServerUtilities.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		ServerUtilities.register(context, registrationId);

	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {

		Log.i(TAG, "Received message");
		// String message = getString(R.string.gcm_message);
		String message = intent.getExtras().getString("message");
		GCMInfo info = ParserManager.parserGCMResponse(message);
		if(TextUtils.isEmpty(info.getMessage())){
			return;
		}
		// notifies user
		generateNotification(context, info);

		callLogPush(context, DeviceUtil.getDeviceId(context), info.getMessageId());
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, GCMInfo info) {
		/*
		int icon = R.drawable.icon_small;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, info.getMessage(),
				when);
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context,
				BaseSlideMenuActivity.class);
		notificationIntent.putExtra(GCMBUNDLE_ID, info.getItemId());
		notificationIntent.putExtra(GCMBUNDLE_TYPE, info.getType());
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, title, info.getMessage(),
				intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
		*/
		
		String message = info.getMessage();
		Intent notificationIntent = new Intent(context,	BaseSlideMenuActivity.class);
		notificationIntent.putExtra(GCMBUNDLE_ID, info.getItemId());
		notificationIntent.putExtra(GCMBUNDLE_TYPE, info.getType());
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//FLAG_ACTIVITY_NEW_TASK
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(BaseSlideMenuActivity.class);
		stackBuilder.addNextIntent(notificationIntent);
		int id = -1;
		try {
			id = Integer.parseInt(info.getMessageId());
		} catch (Exception e) {
			// TODO: handle exception
		}
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(id,
				PendingIntent.FLAG_CANCEL_CURRENT);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
		.setTicker(message)
		.setSmallIcon(R.drawable.icon_small)
		.setContentTitle(HomeFragment.APP_NAME_1)
		.setContentText(message)
		.setAutoCancel(true)
		.setContentIntent(resultPendingIntent)
		.setStyle(new NotificationCompat.BigTextStyle().setSummaryText(message));
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = mBuilder.build();
		notification.flags |= Notification.FLAG_NO_CLEAR
				| Notification.FLAG_AUTO_CANCEL;
		mNotificationManager.notify(id, notification);
	}
	
	private void callLogPush(Context context, String deviceId, String messId) {
		ApiManager.callLogPush(context, null, deviceId, messId);
	}
}