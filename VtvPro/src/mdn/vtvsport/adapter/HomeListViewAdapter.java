package mdn.vtvsport.adapter;

import java.util.ArrayList;

import mdn.vtvsport.R;
import mdn.vtvsport.common.ImageUtility;
import mdn.vtvsport.object.AdsInfo;
import mdn.vtvsport.object.ChannelInfo;
import mdn.vtvsport.object.EpisodeInfo;
import mdn.vtvsport.object.ItemVtvPlusInfo;
import mdn.vtvsport.object.VodInfo;
import mdn.vtvsport.object.home.HomeCategory;
import mdn.vtvsport.object.home.HomeItemListview;
import mdn.vtvsport.object.home.HomeObject;
import mdn.vtvsport.util.DeviceUtil;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeListViewAdapter extends BaseAdapter {

	private Context mContext;
	private OnClickListener cateClickListener;
	private OnClickListener channelLeftListenner;
	private OnClickListener channelRightListenner;
	private ArrayList<HomeObject> channelInfos;
	private int width, height;
	private int widthSmall, heightSmall;
	private float paddingChannel;
	private float heightCate;

	public HomeListViewAdapter(Context ctx, ArrayList<HomeObject> channelInfos,
			OnClickListener cateClickListener,
			OnClickListener channelLeftClickListener,
			OnClickListener channelRightClickListener) {
		mContext = ctx;
		this.cateClickListener = cateClickListener;
		this.channelLeftListenner = channelLeftClickListener;
		this.channelRightListenner = channelRightClickListener;
		this.channelInfos = channelInfos;

		paddingChannel = mContext.getResources().getDimension(
				R.dimen.padding_channel);// mContext.getResources().getDimension(R.dimen.padding_channel);
		heightCate = mContext.getResources().getDimension(
				R.dimen.home_textview_height);

		int tmpWidth = 0;
		if (ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			tmpWidth = DeviceUtil.getWidthScreen((Activity) ctx);
		} else {
			tmpWidth = DeviceUtil.getHeightScreen((Activity) ctx);
		}

		width = (int) ((tmpWidth - 3 * mContext.getResources()
				.getDisplayMetrics().density) / 2);
		height = width * 5 / 8;

		heightSmall = height / 3;// (int)(height/3 - 2*paddingChannel);
		widthSmall = heightSmall;// 8*heightSmall/5;
	}

	@Override
	public int getCount() {
		return channelInfos.size();

	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return channelInfos.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		HomeItemHolder helper = null;

		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_home_listview_item, null);
			helper = new HomeItemHolder();

			helper.layChannel = (LinearLayout) view
					.findViewById(R.id.layChannel);

			helper.layChannelLeft = (RelativeLayout) view
					.findViewById(R.id.layChannelLeft);
			helper.layChannelLeft.setOnClickListener(channelLeftListenner);
			helper.imgIconLeft = (ImageView) view
					.findViewById(R.id.recipesIconLeft);
			helper.imgIconLeft.getLayoutParams().width = width;
			helper.imgIconLeft.getLayoutParams().height = height;
			helper.layBottomLeft = (LinearLayout) view
					.findViewById(R.id.homeItemLayBottomLeft);
			helper.layBottomLeft.getLayoutParams().height = (height / 3);
//			helper.imgIconSmallLeft = (ImageView) view
//					.findViewById(R.id.homeItemIconSmallLeft);
//			helper.imgIconSmallLeft.getLayoutParams().width = widthSmall;
//			helper.imgIconSmallLeft.getLayoutParams().height = heightSmall;
			helper.tvNameLeft = (TextView) view
					.findViewById(R.id.homeItemTvNameLeft);
			helper.tvViewLeft = (TextView) view
					.findViewById(R.id.homeItemTvViewLeft);
//			helper.tvLikeLeft = (TextView) view
//					.findViewById(R.id.homeItemTvLikeLeft);
//			helper.imgLockLeft = (ImageView) view
//					.findViewById(R.id.homeItemLockLeft);
//			helper.imgLockLeft.getLayoutParams().width = widthSmall;
//			helper.imgLockLeft.getLayoutParams().height = heightSmall;

			helper.layChannelRight = (RelativeLayout) view
					.findViewById(R.id.layChannelRight);
			helper.layChannelRight.setOnClickListener(channelRightListenner);
			helper.imgIconRight = (ImageView) view
					.findViewById(R.id.recipesIconRight);
			helper.imgIconRight.getLayoutParams().width = width;
			helper.imgIconRight.getLayoutParams().height = height;
			helper.layBottomRight = (LinearLayout) view
					.findViewById(R.id.homeItemLayBottomRight);
			helper.layBottomRight.getLayoutParams().height = (height / 3);
//			helper.imgIconSmallRight = (ImageView) view
//					.findViewById(R.id.homeItemIconSmallRight);
//			helper.imgIconSmallRight.getLayoutParams().width = widthSmall;
//			helper.imgIconSmallRight.getLayoutParams().height = heightSmall;
			helper.tvNameRight = (TextView) view
					.findViewById(R.id.homeItemTvNameRight);
			helper.tvViewRight = (TextView) view
					.findViewById(R.id.homeItemTvViewRight);
//			helper.tvLikeRight = (TextView) view
//					.findViewById(R.id.homeItemTvLikeRight);
//			helper.imgLockRight = (ImageView) view
//					.findViewById(R.id.homeItemLockRight);
//			helper.imgLockRight.getLayoutParams().width = widthSmall;
//			helper.imgLockRight.getLayoutParams().height = heightSmall;

			helper.tvCateName = (TextView) view
					.findViewById(R.id.homeItemTvNameCate);
			helper.mLayoutCateName = (LinearLayout) view
					.findViewById(R.id.homeLayoutItemTvNameCate);
			helper.mLayoutCateName.setOnClickListener(cateClickListener);

			view.setTag(helper);
		} else {
			helper = (HomeItemHolder) view.getTag();
		}

		HomeObject info = channelInfos.get(position);
		if (info.getIsCategory()) {
			helper.layChannel.setVisibility(View.GONE);
			helper.mLayoutCateName.setVisibility(View.VISIBLE);
			helper.tvCateName.setText(((HomeCategory) info).getNameCategory());
		} else {
			helper.layChannel.setVisibility(View.VISIBLE);
			helper.mLayoutCateName.setVisibility(View.GONE);
			ItemVtvPlusInfo item = ((HomeItemListview) info).getItemLeft();
			if (item != null) {
				helper.tvNameLeft.setText(item.getName());
				if (item.getView().length() > 0) {
					helper.tvViewLeft.setVisibility(View.VISIBLE);
					helper.tvViewLeft.setText(mContext.getString(R.string.home_views) + item.getView());
				} else {
					helper.tvViewLeft.setVisibility(View.GONE);
				}

//				if (item.getCountLike().length() > 0) {
//					helper.tvLikeLeft.setVisibility(View.VISIBLE);
//					helper.tvLikeLeft
//							.setText(" | " + item.getCountLike() + " ");
//				} else {
//					helper.tvLikeLeft.setVisibility(View.GONE);
//				}
				helper.imgIconLeft.setImageBitmap(null);
//				helper.imgIconSmallLeft.setImageBitmap(null);
				switch (item.getTypeItem()) {
				case 0:
					helper.layBottomLeft.setVisibility(View.GONE);
//					helper.imgIconSmallLeft.setVisibility(View.VISIBLE);
//					ImageUtility.loadBitmapFromUrl(mContext,
//							((ChannelInfo) item).getIcon(), helper.imgIconLeft);
					
					ImageUtility.loadBitmapFromUrl(mContext,
							((ChannelInfo) item).getIconSmall(),
							helper.imgIconLeft);
					
					// ignore
//					if (((ChannelInfo) item).isFree()) {
//						helper.imgLockLeft.setVisibility(View.GONE);
//					} else {
//						helper.imgLockLeft.setVisibility(View.VISIBLE);
//						StringBuilder tmpId = new StringBuilder(",").append(
//								item.getId()).append(",");
//						if (((BaseSlideMenuActivity) mContext).pProfile.pIdChannel
//								.contains(tmpId.toString())) {
//							helper.imgLockLeft
//									.setImageResource(R.drawable.unlook);
//						} else {
//							helper.imgLockLeft
//									.setImageResource(R.drawable.look);
//						}
//						tmpId = null;
//					}
					break;
				case 1:
					helper.layBottomLeft.setVisibility(View.VISIBLE);
//					helper.imgIconSmallLeft.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext,
							((VodInfo) item).getImage(), helper.imgIconLeft);
					//ignore
//					if (((VodInfo) item).isFree()) {
//						helper.imgLockLeft.setVisibility(View.GONE);
//					} else {
//						helper.imgLockLeft.setVisibility(View.VISIBLE);
//					}
					break;
				case 2:
					helper.layBottomLeft.setVisibility(View.VISIBLE);
					// ignore
//					helper.imgIconSmallLeft.setVisibility(View.GONE);
//					ImageUtility
//							.loadBitmapFromUrl(mContext,
//									((EpisodeInfo) item).getImage(),
//									helper.imgIconLeft);
//					helper.imgLockLeft.setVisibility(View.GONE);
					break;
				case 3:
					helper.layBottomLeft.setVisibility(View.GONE);
					// helper.imgIconSmallLeft.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext,
							((AdsInfo) item).getImage(), helper.imgIconLeft);
					// helper.imgLockLeft.setVisibility(View.GONE);
					break;
				default:
					break;
				}
			}

			item = ((HomeItemListview) info).getItemRight();
			if (item != null) {
				helper.layChannelRight.setVisibility(View.VISIBLE);
				helper.tvNameRight.setText(item.getName());
				if (item.getView().length() > 0) {
					helper.tvViewRight.setVisibility(View.VISIBLE);
					helper.tvViewRight.setText(mContext.getString(R.string.home_views) + item.getView());
				} else {
					helper.tvViewRight.setVisibility(View.GONE);
				}

//				if (item.getCountLike().length() > 0) {
//					helper.tvLikeRight.setVisibility(View.VISIBLE);
//					helper.tvLikeRight.setText(" | " + item.getCountLike()
//							+ " ");
//				} else {
//					helper.tvLikeRight.setVisibility(View.GONE);
//				}
				helper.imgIconRight.setImageBitmap(null);
//				helper.imgIconSmallRight.setImageBitmap(null);
				switch (item.getTypeItem()) {
				case 0:
					helper.layBottomRight.setVisibility(View.GONE);
//					helper.imgIconSmallRight.setVisibility(View.VISIBLE);
//					ImageUtility
//							.loadBitmapFromUrl(mContext,
//									((ChannelInfo) item).getIcon(),
//									helper.imgIconRight);
					
					ImageUtility.loadBitmapFromUrl(mContext,
					((ChannelInfo) item).getIconSmall(),
					helper.imgIconRight);
					
//					ImageUtility.loadBitmapFromUrl(mContext,
//							((ChannelInfo) item).getIconSmall(),
//							helper.imgIconSmallRight);
					// ignore
//					if (((ChannelInfo) item).isFree()) {
//						helper.imgLockRight.setVisibility(View.GONE);
//					} else {
//						helper.imgLockRight.setVisibility(View.VISIBLE);
//						StringBuilder tmpId = new StringBuilder(",").append(
//								item.getId()).append(",");
//						if (((BaseSlideMenuActivity) mContext).pProfile.pIdChannel
//								.contains(tmpId.toString())) {
//							helper.imgLockRight
//									.setImageResource(R.drawable.unlook);
//						} else {
//							helper.imgLockRight
//									.setImageResource(R.drawable.look);
//						}
//						tmpId = null;
//					}
					break;
				case 1:
					helper.layBottomRight.setVisibility(View.VISIBLE);
//					helper.imgIconSmallRight.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext,
							((VodInfo) item).getImage(), helper.imgIconRight);
					// ignore
//					if (((VodInfo) item).isFree()) {
//						helper.imgLockRight.setVisibility(View.GONE);
//					} else {
//						helper.imgLockRight.setVisibility(View.VISIBLE);
//					}
					break;
				case 2:
					helper.layBottomRight.setVisibility(View.VISIBLE);
//					helper.imgIconSmallRight.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext,
							((EpisodeInfo) item).getImage(),
							helper.imgIconRight);
					// ignore
//					helper.imgLockRight.setVisibility(View.GONE);
					break;
				case 3:
					helper.layBottomRight.setVisibility(View.GONE);
					// helper.imgIconSmallRight.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext,
							((AdsInfo) item).getImage(), helper.imgIconRight);
					// helper.imgLockRight.setVisibility(View.GONE);
					break;
				default:
					break;
				}
			} else {
				helper.layChannelRight.setVisibility(View.INVISIBLE);
			}
		}

		return view;
	}

	public int getHeightHomeRow() {
		return height;
	}

	public float getPadding() {
		return paddingChannel;
	}

	public float getHeightCate() {
		return heightCate;
	}

	private class HomeItemHolder {
		public LinearLayout layChannel;

		public RelativeLayout layChannelLeft;
		public ImageView imgIconLeft;
		public LinearLayout layBottomLeft;
//		public ImageView imgIconSmallLeft;
		public TextView tvNameLeft;
		public TextView tvViewLeft;
//		public TextView tvLikeLeft;
//		public ImageView imgLockLeft;

		public RelativeLayout layChannelRight;
		public ImageView imgIconRight;
		public LinearLayout layBottomRight;
//		public ImageView imgIconSmallRight;
		public TextView tvNameRight;
		public TextView tvViewRight;
//		public TextView tvLikeRight;
//		public ImageView imgLockRight;

		public LinearLayout mLayoutCateName;
		public TextView tvCateName;
	}

}
