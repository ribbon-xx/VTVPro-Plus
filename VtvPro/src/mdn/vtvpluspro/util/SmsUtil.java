package mdn.vtvpluspro.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.SmsManager;

/**
 * Created by hieuxit on 8/11/14.
 */
public class SmsUtil {
	public static String SENT_SMS = "VTVPLUS_SMS_SENT";
	public static String DELIVERED_SMS = "VTVPLUS_SMS_DELIVERED";

	public static void sendSMS(Context context, final String gateway, String smsContent, final SMSListener listener) {
		String SENT = SENT_SMS;
		String DELIVERED = DELIVERED_SMS;

		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
				new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
				new Intent(DELIVERED), 0);
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(gateway, null, smsContent, sentPI, deliveredPI);
		HLog.i("SEND SMS HERE, gateway: " + gateway + ", message: " + smsContent);
		if (listener != null) {
			context.registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					switch (getResultCode()) {
						case Activity.RESULT_OK:
							HLog.d("Send SMS success");
							if (listener != null) {
								listener.onSuccess(gateway);
							}
							context.unregisterReceiver(this);
							break;
						case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
							HLog.e("RESULT_ERROR_GENERIC_FAILURE");
							if (listener != null) {
								listener.onFailure(gateway);
							}
							context.unregisterReceiver(this);
							break;
						case SmsManager.RESULT_ERROR_NO_SERVICE:
							HLog.e("RESULT_ERROR_NO_SERVICE");
							if (listener != null) {
								listener.onFailure(gateway);
							}
							context.unregisterReceiver(this);
							break;
						case SmsManager.RESULT_ERROR_NULL_PDU:
							HLog.e("RESULT_ERROR_NULL_PDU");
							if (listener != null) {
								listener.onFailure(gateway);
							}
							context.unregisterReceiver(this);
							break;
						case SmsManager.RESULT_ERROR_RADIO_OFF:
							HLog.e("RESULT_ERROR_RADIO_OFF");
							if (listener != null) {
								listener.onFailure(gateway);
							}
							context.unregisterReceiver(this);
							break;
						default:
							HLog.e("error not recognized");
							if (listener != null) {
								listener.onFailure(gateway);
							}
							context.unregisterReceiver(this);
							break;
					}
				}
			}, new IntentFilter(SENT_SMS));
		}
	}

	public static void sendSMSByBuildInApp(Context context, String gateway, String smsContent) {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);

		smsIntent.setData(Uri.parse("smsto:"));
		smsIntent.setType("vnd.android-dir/mms-sms");
		smsIntent.putExtra("address", gateway);
		smsIntent.putExtra("sms_body", smsContent);
		context.startActivity(smsIntent);
	}

	public static interface SMSListener {
		public void onSuccess(String gateway);

		public void onFailure(String gateway);
	}
}
