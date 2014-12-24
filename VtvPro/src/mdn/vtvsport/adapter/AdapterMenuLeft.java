package mdn.vtvsport.adapter;

import java.util.ArrayList;
import java.util.TreeSet;

import mdn.vtvsport.R;
import mdn.vtvsport.object.objMenu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterMenuLeft extends ArrayAdapter<objMenu> {

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SEPARATOR = 1;
	private LayoutInflater mInf;
	private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
	private ArrayList<objMenu> mData = new ArrayList<objMenu>();
	

	public AdapterMenuLeft(Context context, int textViewResourceId, ArrayList<objMenu> objects) {
		super(context, textViewResourceId, objects);
		mInf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = objects;
	}

	public void removeItem(int index) {
		mData.remove(index);
		notifyDataSetChanged();
	}
	public void addItem(final objMenu item) {
		mData.add(item);
		notifyDataSetChanged();
	}

	public void addSectionHeaderItem(objMenu item) {
		mData.add(item);
		sectionHeader.add(mData.size() - 1);
		notifyDataSetChanged();
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
	}
	
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}
	
	@Override
	public objMenu getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		View v = convertView;
//		v = mInf.inflate(R.layout.item_menu_left, null);
//		LinearLayout mFrame = (LinearLayout) v.findViewById(R.id.menu_left_frame);
//		ImageView img = (ImageView) v.findViewById(R.id.menu_left_icon);
//		UIFontText mTitle = (UIFontText) v.findViewById(R.id.menu_left_title);
//		objMenu ob = getItem(position);
//		if (ob != null) {
//			if (ob.id == -1) {
//				mFrame.setBackgroundColor(0x021e2c);
//				img.setBackgroundResource(ob.resIcon);
//				mTitle.setVisibility(View.GONE);
//			} else {
//				if (ob.isBanner) {
//					mFrame.setBackgroundResource(R.drawable.menu_hover);
//				}
//				img.setBackgroundResource(ob.resIcon);
//				mTitle.setText("" + ob.name);
//			}
//		}
//		return v;
//	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
//		return super.getView(position, convertView, parent);
		
		ViewHolder holder = null;
//		int rowType = getItemViewType(position);
		
		if (convertView == null) {
			holder = new ViewHolder();
//			switch (rowType) {
//			case TYPE_ITEM:
//				convertView = mInf.inflate(R.layout.item_menu_left, null);
//				holder.tvTitle = (TextView) convertView.findViewById(R.id.menu_left_title);
//				holder.ivIcon = (ImageView) convertView.findViewById(R.id.menu_left_icon);
//				break;
//				
//			case TYPE_SEPARATOR:
//				Log.d("Thinhdt", "Thinhdt");
//				convertView = mInf.inflate(R.layout.slidemenu_group_header_item, null);
//				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_groupTitle);
//				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_groupHeader);
//				break;
//
//			}
			
			if (mData.get(position).isGroupHeader() == false) {
				// type item
				convertView = mInf.inflate(R.layout.slidemenu_item_listview_video, null);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_item);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.menu_left_icon);
			} else {
				// type group header
				convertView = mInf.inflate(R.layout.slidemenu_group_header_item, null);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_groupTitle);
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_groupHeader);
			}
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvTitle.setText(mData.get(position).name);
		if (mData.get(position).resIcon > 0) {
			holder.ivIcon.setBackgroundResource(mData.get(position).resIcon);	
		}
		
		
		return convertView;
	}
	
	public static class ViewHolder {
		public TextView tvTitle;
		public ImageView ivIcon;
	}
}
