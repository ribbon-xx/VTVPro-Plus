package mdn.vtvsport.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class DeviceUtil {

	 @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	 public static boolean isTablet(Context context) {
	  boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
	  boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
	  return (xlarge || large);
	 }
	
	/**
	 * Use to get height of device screen
	 * 
	 * @param activity
	 */
	public static int getHeightScreen(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int height = display.getHeight();

		return height;
	}

	/**
	 * Use to get width of device screen
	 * 
	 * @param activity
	 */
	public static int getWidthScreen(Activity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();

		return width;
	}

	/**
	 * Use to get height of status bar
	 * 
	 * @param activity
	 */
	public static int getHeightStatusBar(Activity activity) {

		return (int) Math
				.ceil(25 * activity.getResources().getDisplayMetrics().density);

	}

	/**
	 * Hide keyboard
	 * 
	 * @param activity
	 * @param myEditText
	 */
	public static void hideKeyboard(Activity activity, EditText myEditText) {

		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);

	}
	
	public static void showSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	public static String getDeviceId(Context context) {
		return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	}

	public static String getTelcoCode(Context context){
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return telephonyManager.getSimOperator();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("hieuth", "error: " + e.toString());
			return null;
		}
	}

}
