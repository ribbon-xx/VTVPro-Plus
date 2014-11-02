package mdn.vtvpluspro.alarm;

import mdn.vtvpluspro.BaseSlideMenuActivity;
import mdn.vtvplus.R;
import mdn.vtvpluspro.common.SharedPreferenceManager;
import mdn.vtvpluspro.fragment.HomeFragment;
import mdn.vtvpluspro.fragment.PlayerDetailsAboutFragment;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	final public static String ONE_TIME = "onetime";
	final public static String SCHEDULE_ID = "SCHEDULE_ID";

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		// Acquire the lock
		wl.acquire();

		// You can do the processing here update the widget/remote views.
		Bundle extras = intent.getExtras();
		int id = -1;
		if (extras != null) {
			id = extras.getInt(SCHEDULE_ID);
		}
		generateNotification(context, id);

		// Release the lock
		wl.release();

	}

	public void SetAlarm(Context context) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		intent.putExtra(ONE_TIME, Boolean.FALSE);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		// After after 30 seconds
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				1000 * 5, pi);
	}

	public void CancelAlarm(Context context) {
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

	public void setOnetimeTimer(Context context, long time, int id) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		intent.putExtra(ONE_TIME, Boolean.TRUE);
		intent.putExtra(SCHEDULE_ID, id);
		PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, 0);
		am.set(AlarmManager.RTC_WAKEUP, time, pi);
	}

	private void generateNotification(Context context, int id) {
		/*
		int icon = R.drawable.icon_small;
		long when = System.currentTimeMillis();
		String nameChannel = SharedPreferenceManager.getInstance(context)
				.getScheduleName(PlayerDetailsAboutFragment.temp + id);
		String message = String.format(
				context.getString(R.string.details_info_schedule_notification),
				nameChannel);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		
		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context,
				BaseSlideMenuActivity.class);
		notificationIntent.putExtra(SCHEDULE_ID, id);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent intent = PendingIntent.getActivity(context, id,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = null;
		notification.setLatestEventInfo(context, title, message, intent);
		
//		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//                R.layout.layout_custom_notification);
//		remoteViews.setTextViewText(R.id.text, message);
//		notification.contentView = remoteViews;
//		notification.contentIntent = intent;
		
		notification.flags |= Notification.FLAG_NO_CLEAR
				| Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(id, notification);
		*/
		
		String nameChannel = SharedPreferenceManager.getInstance(context)
				.getScheduleName(PlayerDetailsAboutFragment.temp + id);
		String message = String.format(
				context.getString(R.string.details_info_schedule_notification),
				nameChannel);
		
		Intent notificationIntent = new Intent(context,	BaseSlideMenuActivity.class);
		notificationIntent.putExtra(SCHEDULE_ID, id);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//FLAG_ACTIVITY_NEW_TASK
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(BaseSlideMenuActivity.class);
		stackBuilder.addNextIntent(notificationIntent);
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
