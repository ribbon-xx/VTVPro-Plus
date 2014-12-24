package mdn.vtvsport.dialogs;

import mdn.vtvsport.R;
import mdn.vtvsport.common.SharedPreferenceManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class PolicyDialog implements OnClickListener {

	TextView mBtnOk;
	TextView mBtnCancel;
	WebView mContent;

	Context mContext;
	Dialog myDialog;

	public static PolicyDialog mInstance;

	public PolicyDialog(Context context, String policy) {
		mContext = context;

		myDialog = new Dialog(mContext);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(R.layout.dialog_policy);
		myDialog.setCancelable(false);
		myDialog.show();

		mContent = (WebView) myDialog.findViewById(R.id.wvContent);
		mBtnCancel = (TextView) myDialog.findViewById(R.id.btnCancel);
		mBtnOk = (TextView) myDialog.findViewById(R.id.btnOk);
		mBtnCancel.setOnClickListener(this);
		mBtnOk.setOnClickListener(this);

		callWebview(mContent, policy);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnCancel:
			myDialog.dismiss();
			((Activity) mContext).finish();
			break;
		case R.id.btnOk:
			myDialog.dismiss();
			SharedPreferenceManager.getInstance(mContext).setShowPolicy(false);
			break;
		default:
			break;
		}
	}

	private void callWebview(WebView wv, String data) {
		StringBuilder tmp = new StringBuilder(
				"<body style=\"margin: 0; padding: 0\">");
		tmp.append(data);
		tmp.append("</body>");

		wv.loadDataWithBaseURL("fake://not/needed", tmp.toString(),
				"text/html", "UTF-8", "");
		tmp = null;
	}

}
