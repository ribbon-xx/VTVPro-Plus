package mdn.vtvsport.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by hieuxit on 8/20/14.
 */
public class Utils {
	public static final String md5(final String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2) {
					h = "0" + h;
				}
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void lookUp(Bundle bundle){
		if(bundle == null)return;
		Set<String> keys = bundle.keySet();
		HLog.i("Bundle information "+((Object)bundle).hashCode()+" ----------------------");
		for(String key : keys){
			Object obj = bundle.get(key);
			HLog.i(key+": "+obj);
			if(obj instanceof Bundle){
				lookUp((Bundle) obj);
			}else if(obj instanceof Intent){
				lookUp(((Intent)obj).getExtras());
			}
		}
		HLog.i("End Bundle "+((Object)bundle).hashCode()+" ----------------------");
	}

	public static <T> String array2String(T[] array) {
		if (array == null) {
			return "[]";
		}
		if (array.length == 0) {
			return "[]";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				builder.append("null");
			} else {
				builder.append(array[i].toString());
			}
			if (i < array.length - 1) {
				builder.append(",");
			}
		}
		builder.append("]");
		return builder.toString();
	}

	public static <T> boolean arrayContains(T element, T[] array) {
		if (element == null) {
			return false;
		}
		for (int i = 0; i < array.length; i++) {
			if (element.equals(array[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	public static boolean checkPlayServices(Activity context, final int request) {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, context,
	            		request).show();
	        } else {
//	            Log.i(TAG, "This device is not supported.");
//	            context.finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	public static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
}
