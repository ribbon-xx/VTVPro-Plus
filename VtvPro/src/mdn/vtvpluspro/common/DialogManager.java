package mdn.vtvpluspro.common;

import mdn.vtvplus.R;
import mdn.vtvpluspro.util.UiUtil;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.view.Window;

/**
 * 
 * @author MC
 * 
 */
public class DialogManager {
	// ======================= ALERT DIALOG ======================

	private static Dialog simpleProgressDialog = null;

	/**
	 * Show an alert dialog
	 * 
	 * @param context
	 * @param message
	 */
	public static void alert(Context context, String message) {
		if (context != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getString(R.string.app_name));
			builder.setMessage(message);
			builder.setPositiveButton(context.getString(android.R.string.ok),
					null);
			Dialog dialog = builder.create();
			dialog.show();
		}
	}

	/**
	 * Show an alert dialog
	 * 
	 * @param context
	 * @param message
	 */
	public static void alertWith2Status(Context context, String message,
			OnClickListener onClickPositiveButtonListener) {
		if (context != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getString(R.string.app_name));
			builder.setMessage(message);
			builder.setNegativeButton(
					context.getString(android.R.string.cancel), null);
			builder.setPositiveButton(context.getString(android.R.string.ok),
					onClickPositiveButtonListener);
			Dialog dialog = builder.create();
			dialog.show();
		}
	}

	/**
	 * Show an alert dialog
	 * 
	 * @param context
	 * @param message
	 */
	public static void alertWith2Status(Context context, String title,
			String message, OnClickListener onClickNagetiveButtonListener,
			OnClickListener onClickPositiveButtonListener) {
		if (context != null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(false);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.setNegativeButton(
					context.getString(android.R.string.cancel),
					onClickNagetiveButtonListener);
			builder.setPositiveButton(context.getString(android.R.string.ok),
					onClickPositiveButtonListener);
			Dialog dialog = builder.create();
			dialog.show();
		}
	}

	/**
	 * Show simple progress dialog
	 * 
	 * @param context
	 */
	public static void showSimpleProgressDialog(Context context) {
		/** Initialize custom dialog */
		if (simpleProgressDialog != null) {
			closeProgressDialog();
		}

		/** Show progress dialog */
		if (context != null) {
			simpleProgressDialog = new Dialog(context);
			simpleProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			simpleProgressDialog
					.setContentView(R.layout.dialog_progress_simple);
			// simpleProgressDialog.getWindow().setBackgroundDrawable(null);
			UiUtil.setDialogOpacity(simpleProgressDialog, Color.WHITE, 0);
			simpleProgressDialog.setCancelable(false);
			simpleProgressDialog.show();
		}
	}

	/**
	 * Close progress dialog
	 */
	public static void closeProgressDialog() {

		if (simpleProgressDialog != null) {
			try {
				simpleProgressDialog.cancel();
				simpleProgressDialog = null;
			} catch (Exception e) {
				// Handle exception: do nothing here
			}
		}
	}

	public static boolean isProgressShowing() {
		return (simpleProgressDialog != null && simpleProgressDialog
				.isShowing());
	}

}
