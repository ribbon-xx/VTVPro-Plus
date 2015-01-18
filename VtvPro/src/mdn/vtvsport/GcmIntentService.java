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
package mdn.vtvsport;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.fragment.HomeFragment;
import mdn.vtvsport.object.GCMInfo;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public static final String GCMBUNDLE_ID = "GCMBUNDLE_ID";
	public static final String GCMBUNDLE_TYPE = "GCMBUNDLE_TYPE";
	
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
//        	Toast.makeText(getApplicationContext(), "Received message gcm", Toast.LENGTH_LONG).show();
    		// String message = getString(R.string.gcm_message);
    		String message = intent.getExtras().getString("message");
    		GCMInfo info = ParserManager.parserGCMResponse(message);
    		if(TextUtils.isEmpty(info.getMessage())){
    			return;
    		}
    		// notifies user
    		generateNotification(getApplicationContext(), info);
    		
//            if (GoogleCloudMessaging.
//                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                sendNotification("Send error: " + extras.toString());
//            } else if (GoogleCloudMessaging.
//                    MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: " +
//                        extras.toString());
//            // If it's a regular GCM message, do some work.
//            } else if (GoogleCloudMessaging.
//                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//                // This loop represents the service doing some work.
//                for (int i=0; i<5; i++) {
//                    Log.i(TAG, "Working... " + (i+1)
//                            + "/5 @ " + SystemClock.elapsedRealtime());
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                    }
//                }
//                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
//                // Post notification of received message.
//                sendNotification("Received: " + extras.toString());
//                Log.i(TAG, "Received: " + extras.toString());
//            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

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
}