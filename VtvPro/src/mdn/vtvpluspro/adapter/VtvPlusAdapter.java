package mdn.vtvpluspro.adapter;

import java.util.ArrayList;

import mdn.vtvpluspro.BaseSlideMenuActivity;
import mdn.vtvplus.R;
import mdn.vtvpluspro.common.ImageUtility;
import mdn.vtvpluspro.object.AdsInfo;
import mdn.vtvpluspro.object.ChannelInfo;
import mdn.vtvpluspro.object.EpisodeInfo;
import mdn.vtvpluspro.object.ItemVtvPlusInfo;
import mdn.vtvpluspro.object.VodInfo;
import mdn.vtvpluspro.util.DeviceUtil;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VtvPlusAdapter extends BaseAdapter {

	private Context mContext;
	private OnClickListener itemClickListener;
	private ArrayList<ItemVtvPlusInfo> vtvPlusInfos;
	private int width, height;
	private int widthSmall, heightSmall;
	private float paddingChannel;

	public VtvPlusAdapter(Context ctx, OnClickListener itemClickListener,
			ArrayList<ItemVtvPlusInfo> channelInfos) {
		mContext = ctx;
		this.itemClickListener = itemClickListener;
		this.vtvPlusInfos = channelInfos;

		paddingChannel = mContext.getResources().getDimension(R.dimen.padding_channel);
	}
	
	public void updateSizeItem(int count) {
		width = (int) ((DeviceUtil.getWidthScreen((Activity) mContext) - (count+1) * mContext
				.getResources().getDisplayMetrics().density) / count);
		height = width * 5 / 8;

		heightSmall = height / 3;// (int)(height/3 - 2*paddingChannel);
		widthSmall = heightSmall;// 3*heightSmall/2;//8*heightSmall/5;
	}

	@Override
	public int getCount() {
		return vtvPlusInfos.size();

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return vtvPlusInfos.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		HomeItemHolder helper = null;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_home_item, null);
			helper = new HomeItemHolder();
			helper.layBottom = (LinearLayout) view
					.findViewById(R.id.homeItemLayBottom);
			helper.layBottom.getLayoutParams().height = (height / 3);

			helper.imgIcon = (ImageView) view.findViewById(R.id.recipesIcon);
			helper.imgIcon.getLayoutParams().width = width;
			helper.imgIcon.getLayoutParams().height = height;

			helper.imgIconSmall = (ImageView) view
					.findViewById(R.id.homeItemIconSmall);
			helper.imgIconSmall.getLayoutParams().width = widthSmall;
			helper.imgIconSmall.getLayoutParams().height = heightSmall;

			helper.tvName = (TextView) view.findViewById(R.id.homeItemTvName);
			helper.tvView = (TextView) view.findViewById(R.id.homeItemTvView);
//			helper.tvLike = (TextView) view.findViewById(R.id.homeItemTvLike);
//			helper.imgLock = (ImageView) view.findViewById(R.id.homeItemLock);
//			helper.imgLock.getLayoutParams().width = widthSmall;
//			helper.imgLock.getLayoutParams().height = heightSmall;
			view.setOnClickListener(itemClickListener);
			view.setTag(helper);
		} else {
			helper = (HomeItemHolder) view.getTag();
		}

		ItemVtvPlusInfo info = vtvPlusInfos.get(position);
		helper.tvName.setText(info.getName());
		
		if (info.getView().length() > 0) {
			helper.tvView.setVisibility(View.VISIBLE);
			helper.tvView.setText(mContext.getString(R.string.home_views) + info.getView());
		} else {
			helper.tvView.setVisibility(View.GONE);
		}
		
//		if (info.getCountLike().length() > 0) {
//			helper.tvLike.setVisibility(View.VISIBLE);
//			helper.tvLike.setText(" | " + info.getCountLike() + " ");
//		} else {
//			helper.tvLike.setVisibility(View.GONE);
//		}

		helper.imgIcon.setImageBitmap(null);
		helper.imgIconSmall.setImageBitmap(null);
		switch (info.getTypeItem()) {
		case 0:
			helper.layBottom.setVisibility(View.VISIBLE);
			helper.imgIconSmall.setVisibility(View.VISIBLE);
			ImageUtility.loadBitmapFromUrl(mContext,
					((ChannelInfo) info).getIcon(), helper.imgIcon);
			ImageUtility.loadBitmapFromUrl(mContext,
					((ChannelInfo) info).getIconSmall(), helper.imgIconSmall);
//			if (((ChannelInfo)info).isFree()) {
//				helper.imgLock.setVisibility(View.GONE);
//			} else {
//				helper.imgLock.setVisibility(View.VISIBLE);
//				StringBuilder strBuilder = new StringBuilder(",").append(info.getId()).append(",");
//				if (((BaseSlideMenuActivity)mContext).pProfile.pIdChannel.contains(strBuilder.toString())) {
//					helper.imgLock.setImageResource(R.drawable.unlook);
//				} else {
//					helper.imgLock.setImageResource(R.drawable.look);
//				}
//				strBuilder = null;
//			}
			break;
		case 1:
			helper.layBottom.setVisibility(View.VISIBLE);
			helper.imgIconSmall.setVisibility(View.GONE);
			ImageUtility.loadBitmapFromUrl(mContext,
					((VodInfo) info).getImage(), helper.imgIcon);
//			if (((VodInfo)info).isFree()) {
//				helper.imgLock.setVisibility(View.GONE);
//			} else {
//				helper.imgLock.setVisibility(View.VISIBLE);
//			}
			break;
		case 2:
			helper.layBottom.setVisibility(View.VISIBLE);
			helper.imgIconSmall.setVisibility(View.GONE);
			ImageUtility.loadBitmapFromUrl(mContext,
					((EpisodeInfo) info).getImage(), helper.imgIcon);
//			helper.imgLock.setVisibility(View.GONE);
			break;
		case 3:
			helper.layBottom.setVisibility(View.GONE);
			ImageUtility.loadBitmapFromUrl(mContext,
					((AdsInfo) info).getImage(), helper.imgIcon);
			break;
		}
		view.setId(position);

		return view;
	}

	public int getHeightHomeRow() {
		return height;
	}

	public float getPadding() {
		return paddingChannel;
	}

	private class HomeItemHolder {
		public LinearLayout layBottom;
		public ImageView imgIcon;
		public ImageView imgIconSmall;
		public TextView tvName;
		public TextView tvView;
//		public TextView tvLike;
//		public ImageView imgLock;
	}

}
