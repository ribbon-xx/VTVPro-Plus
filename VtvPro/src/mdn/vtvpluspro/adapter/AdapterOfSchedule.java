package mdn.vtvpluspro.adapter;

import java.util.ArrayList;

import mdn.vtvplus.R;
import mdn.vtvpluspro.object.ScheduleInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterOfSchedule extends ArrayAdapter<ScheduleInfo> {
	private Context mContext;
	private ArrayList<ScheduleInfo> arrInfo;
	
	public AdapterOfSchedule(Context context, ArrayList<ScheduleInfo> arrInfo) {
		super(context, -1, arrInfo);
		this.mContext = context;
		this.arrInfo = arrInfo;
	}

	@Override
	public int getCount() {
		return arrInfo.size();

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public ScheduleInfo getItem(int position) {
		// TODO Auto-generated method stub
		return arrInfo.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ScheduleItemHolder helper = null;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_item_schedule, null);
			helper = new ScheduleItemHolder();
			helper.tvTitle = (TextView) view.findViewById(R.id.itemScheduleTitle);
			helper.tvTime = (TextView) view.findViewById(R.id.itemScheduleTime);
			helper.tvContent = (TextView) view.findViewById(R.id.itemScheduleContent);
			view.setTag(helper);
		} else {
			helper = (ScheduleItemHolder)view.getTag();
		}
		ScheduleInfo item = getItem(position);
		if (item.getTitle() != null && item.getTitle().length() > 0) {
			helper.tvTitle.setText(item.getTitle());
		}
		if (item.getTime() != null && item.getTime().length() > 0) {
			helper.tvTime.setText(item.getTime());
		}
		if (item.getContent() != null && item.getContent().length() > 0) {
			helper.tvContent.setText(item.getContent());
		}
		return view;
	}
	
	private class ScheduleItemHolder {
		public TextView tvTitle;
		public TextView tvTime;
		public TextView tvContent;
	}
}
