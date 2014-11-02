package mdn.vtvpluspro.util;

import android.util.Log;

public class HLog {
	public static boolean DEBUG = true;
	public static String TAG = "hieuth";

	public static void d(String tag, String msg) {
		if (DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (DEBUG) {
			Log.i(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (DEBUG) {
			Log.v(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (DEBUG) {
			Log.w(tag, msg);
		}
	}

	public static void enableLog(boolean enable) {
		DEBUG = enable;
	}

	public static void d(String msg) {
		HLog.d(TAG, msg);
	}

	public static void e(String msg) {
		HLog.e(TAG, msg);
	}

	public static void i(String msg) {
		HLog.i(TAG, msg);
	}

	public static void v(String msg) {
		HLog.v(TAG, msg);
	}

	public static void w(String msg) {
		HLog.w(TAG, msg);
	}

	public static void assertCondition(boolean condition, String failedMessage) {
//		if (DEBUG) {
//			Assert.assertTrue(failedMessage, condition);
//		} else {
//			if (!condition) {
//				e("Assert failed here !");
//			}
//		}

	}

	public static void assertCondition(boolean condition) {
//		if (DEBUG) {
//			Assert.assertTrue("ASSERT", condition);
//		}
	}
}
