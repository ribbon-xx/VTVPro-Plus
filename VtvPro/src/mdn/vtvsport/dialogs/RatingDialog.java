package mdn.vtvsport.dialogs;

import mdn.vtvsport.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class RatingDialog implements OnClickListener {

	TextView rate;
	TextView remindLater;
	TextView dismiss;
	MyClickListener mListener;

	Context mContext;
	Dialog myDialog;

	public static RatingDialog mInstance;

	public RatingDialog(Context context, MyClickListener listener) {
		mContext = context;
		mListener = listener;

//		myDialog = new Dialog(mContext, android.R.style.Theme_Dialog);

		myDialog = new Dialog(mContext);
		myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.setContentView(R.layout.dialog_rating_confirm);
		myDialog.setCancelable(true);
		myDialog.show();

		rate = (TextView) myDialog.findViewById(R.id.btnRating);
		remindLater = (TextView) myDialog.findViewById(R.id.btnRemindLater);
		dismiss = (TextView) myDialog.findViewById(R.id.btnDismiss);
		rate.setOnClickListener(this);
		remindLater.setOnClickListener(this);
		dismiss.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnRating:
			mListener.onRating(myDialog);
			break;
		case R.id.btnRemindLater:
			mListener.onRemindLater(myDialog);
			break;
		case R.id.btnDismiss:
			mListener.onDimiss(myDialog);
			break;
		default:
			break;
		}
	}

	public interface MyClickListener {

		public void onRating(Dialog dialog);

		public void onRemindLater(Dialog dialog);

		public void onDimiss(Dialog dialog);
	}
}
