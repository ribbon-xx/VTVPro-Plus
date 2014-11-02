package mdn.vtvpluspro.dialogs;

import java.util.Calendar;
import java.util.Date;

import mdn.vtvplus.R;
import mdn.vtvpluspro.util.StringUtil;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class DateTimePickerDialog extends DialogFragment implements
		OnDateChangedListener, OnTimeChangedListener, OnClickListener {

	Context mContext;
	LayoutInflater mInflater;
	DateTimePickerDialogListener dateTimeListener;

	private DatePicker datePicker;
	private TimePicker timePicker;
	private TextView btnOk;
	private TextView btnCancel;
	private Dialog dialog;

	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	public static DateTimePickerDialog mInstance;

	private DateTimePickerDialog(Context context,
			DateTimePickerDialogListener dateTimeListener) {
		mContext = context;
		this.dateTimeListener = dateTimeListener;
		mInflater = LayoutInflater.from(context);
	}

	public static DateTimePickerDialog newInstance(Context context,
			DateTimePickerDialogListener dateTimeListener) {
		mInstance = new DateTimePickerDialog(context, dateTimeListener);
		return mInstance;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		mYear = year;
		mMonth = month + 1;
		mDay = day;
		mHour = hour;
		mMinute = minute;

		dialog = new Dialog(mContext, android.R.style.Theme_Holo_Light_Dialog);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = mInflater.inflate(R.layout.dialog_datetime, null);

		datePicker = (DatePicker) view.findViewById(R.id.datePicker);
		timePicker = (TimePicker) view.findViewById(R.id.timePicker);
		btnOk = (TextView) view.findViewById(R.id.btnOk);
		btnCancel = (TextView) view.findViewById(R.id.btnCancel);
		datePicker.init(year, month, day, this);
		timePicker.setOnTimeChangedListener(this);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		dialog.setContentView(view);
		return dialog;

	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		mHour = hourOfDay;
		mMinute = minute;
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		mYear = year;
		mMonth = monthOfYear + 1;
		mDay = dayOfMonth;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCancel:
			dialog.dismiss();
			break;

		case R.id.btnOk:
			String datetime = mYear + "-" + mMonth + "-" + mDay + " " + mHour
					+ ":" + mMinute;
			if (StringUtil.convertStringToDate(
					StringUtil.TIMESTAMP_DATE_FORMAT, datetime).after(
					new Date())) {
				dialog.dismiss();
				dateTimeListener.onDone(datetime);
			} else {
				Toast.makeText(
						mContext,
						mContext.getString(R.string.details_info_schedule_settime),
						Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	public interface DateTimePickerDialogListener {

		public void onDone(String dateTime);

	}
}
