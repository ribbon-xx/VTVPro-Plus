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
import mdn.vtvsport.object.home.HomeItemLandListview;
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

public class HomeListViewLandAdapter extends BaseAdapter {

	private Context mContext;
	private OnClickListener cateClickListener;
	private OnClickListener channelFirstListenner;
	private OnClickListener channelSecondListenner;
	private OnClickListener channelThirdListenner;
	private OnClickListener channelFourthListenner;
	private ArrayList<HomeObject> channelInfos;
	private int width, height;
	private int widthSmall, heightSmall;
	private float paddingChannel;
	private float heightCate;

	public HomeListViewLandAdapter(Context ctx, ArrayList<HomeObject> channelInfos, OnClickListener cateClickListener, OnClickListener channelFirstClickListener, 
			OnClickListener channelSecondClickListener, OnClickListener channelThirdClickListener, OnClickListener channelFourClickListener) {
		mContext = ctx;
		this.cateClickListener = cateClickListener;
		this.channelFirstListenner = channelFirstClickListener;
		this.channelSecondListenner = channelSecondClickListener;
		this.channelThirdListenner = channelThirdClickListener;
		this.channelFourthListenner = channelFourClickListener;
		this.channelInfos = channelInfos;

		paddingChannel = mContext.getResources().getDimension(R.dimen.padding_channel);
		heightCate = mContext.getResources().getDimension(R.dimen.home_textview_height);
		
		int tmpWidth = 0;
		if (ctx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			tmpWidth = DeviceUtil.getWidthScreen((Activity) ctx);
		} else {
			tmpWidth = DeviceUtil.getHeightScreen((Activity) ctx);
		}
		
		width = (int) ((tmpWidth - 5 * mContext.getResources().getDisplayMetrics().density) / 4);
		height = width * 5 / 8;
		
		heightSmall = height/3;//(int)(height/3 - 2*paddingChannel);
		widthSmall = heightSmall;//8*heightSmall/5;
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
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_home_listview_land_item, null);
			helper = new HomeItemHolder();
			
			helper.layChannel = (LinearLayout) view.findViewById(R.id.layChannel);
			
			helper.layChannelFirst = (RelativeLayout) view.findViewById(R.id.layChannelFirst);
			helper.layChannelFirst.setOnClickListener(channelFirstListenner);
			helper.imgIconFirst = (ImageView) view.findViewById(R.id.recipesIconFirst);
			helper.imgIconFirst.getLayoutParams().width = width;
			helper.imgIconFirst.getLayoutParams().height = height;
			helper.layBottomFirst = (LinearLayout) view.findViewById(R.id.homeItemLayBottomFirst);
			helper.layBottomFirst.getLayoutParams().height = (height/3);
			helper.imgIconSmallFirst = (ImageView) view.findViewById(R.id.homeItemIconSmallFirst);
			helper.imgIconSmallFirst.getLayoutParams().width = widthSmall;
			helper.imgIconSmallFirst.getLayoutParams().height = heightSmall;
			helper.tvNameFirst = (TextView) view.findViewById(R.id.homeItemTvNameFirst);
			helper.tvViewFirst = (TextView) view.findViewById(R.id.homeItemTvViewFirst);
//			helper.tvLikeFirst = (TextView) view.findViewById(R.id.homeItemTvLikeFirst);
//			helper.imgLockFirst = (ImageView) view.findViewById(R.id.homeItemLockFirst);
//			helper.imgLockFirst.getLayoutParams().width = widthSmall;
//			helper.imgLockFirst.getLayoutParams().height = heightSmall;
			
			helper.layChannelSecond = (RelativeLayout) view.findViewById(R.id.layChannelSecond);
			helper.layChannelSecond.setOnClickListener(channelSecondListenner);
			helper.imgIconSecond = (ImageView) view.findViewById(R.id.recipesIconSecond);
			helper.imgIconSecond.getLayoutParams().width = width;
			helper.imgIconSecond.getLayoutParams().height = height;
			helper.layBottomSecond = (LinearLayout) view.findViewById(R.id.homeItemLayBottomSecond);
			helper.layBottomSecond.getLayoutParams().height = (height/3);
			helper.imgIconSmallSecond = (ImageView) view.findViewById(R.id.homeItemIconSmallSecond);
			helper.imgIconSmallSecond.getLayoutParams().width = widthSmall;
			helper.imgIconSmallSecond.getLayoutParams().height = heightSmall;
			helper.tvNameSecond = (TextView) view.findViewById(R.id.homeItemTvNameSecond);
			helper.tvViewSecond = (TextView) view.findViewById(R.id.homeItemTvViewSecond);
//			helper.tvLikeSecond = (TextView) view.findViewById(R.id.homeItemTvLikeSecond);
//			helper.imgLockSecond = (ImageView) view.findViewById(R.id.homeItemLockSecond);
//			helper.imgLockSecond.getLayoutParams().width = widthSmall;
//			helper.imgLockSecond.getLayoutParams().height = heightSmall;
			
			helper.layChannelThird = (RelativeLayout) view.findViewById(R.id.layChannelThird);
			helper.layChannelThird.setOnClickListener(channelThirdListenner);
			helper.imgIconThird = (ImageView) view.findViewById(R.id.recipesIconThird);
			helper.imgIconThird.getLayoutParams().width = width;
			helper.imgIconThird.getLayoutParams().height = height;
			helper.layBottomThird = (LinearLayout) view.findViewById(R.id.homeItemLayBottomThird);
			helper.layBottomThird.getLayoutParams().height = (height/3);
			helper.imgIconSmallThird = (ImageView) view.findViewById(R.id.homeItemIconSmallThird);
			helper.imgIconSmallThird.getLayoutParams().width = widthSmall;
			helper.imgIconSmallThird.getLayoutParams().height = heightSmall;
			helper.tvNameThird = (TextView) view.findViewById(R.id.homeItemTvNameThird);
			helper.tvViewThird = (TextView) view.findViewById(R.id.homeItemTvViewThird);
//			helper.tvLikeThird = (TextView) view.findViewById(R.id.homeItemTvLikeThird);
//			helper.imgLockThird = (ImageView) view.findViewById(R.id.homeItemLockThird);
//			helper.imgLockThird.getLayoutParams().width = widthSmall;
//			helper.imgLockThird.getLayoutParams().height = heightSmall;
			
			helper.layChannelFourth = (RelativeLayout) view.findViewById(R.id.layChannelFourth);
			helper.layChannelFourth.setOnClickListener(channelFourthListenner);
			helper.imgIconFourth = (ImageView) view.findViewById(R.id.recipesIconFourth);
			helper.imgIconFourth.getLayoutParams().width = width;
			helper.imgIconFourth.getLayoutParams().height = height;
			helper.layBottomFourth = (LinearLayout) view.findViewById(R.id.homeItemLayBottomFourth);
			helper.layBottomFourth.getLayoutParams().height = (height/3);
			helper.imgIconSmallFourth = (ImageView) view.findViewById(R.id.homeItemIconSmallFourth);
			helper.imgIconSmallFourth.getLayoutParams().width = widthSmall;
			helper.imgIconSmallFourth.getLayoutParams().height = heightSmall;
			helper.tvNameFourth = (TextView) view.findViewById(R.id.homeItemTvNameFourth);
			helper.tvViewFourth = (TextView) view.findViewById(R.id.homeItemTvViewFourth);
//			helper.tvLikeFourth = (TextView) view.findViewById(R.id.homeItemTvLikeFourth);
//			helper.imgLockFourth = (ImageView) view.findViewById(R.id.homeItemLockFourth);
//			helper.imgLockFourth.getLayoutParams().width = widthSmall;
//			helper.imgLockFourth.getLayoutParams().height = heightSmall;
			
			helper.tvCateName = (TextView) view.findViewById(R.id.homeItemTvNameCate);
			helper.tvCateName.setOnClickListener(cateClickListener);
			
			view.setTag(helper);
		} else {
			helper = (HomeItemHolder) view.getTag();
		}
		
		HomeObject info = channelInfos.get(position);
		if (info.getIsCategory()) {
			helper.layChannel.setVisibility(View.GONE);
			helper.tvCateName.setVisibility(View.VISIBLE);
			helper.tvCateName.setText(((HomeCategory)info).getNameCategory());
		} else {
			helper.layChannel.setVisibility(View.VISIBLE);
			helper.tvCateName.setVisibility(View.GONE);
			ItemVtvPlusInfo item = ((HomeItemLandListview)info).getItemFirst();
			if (item != null) {
				helper.tvNameFirst.setText(item.getName());
				if (item.getView().length() > 0) {
					helper.tvViewFirst.setVisibility(View.VISIBLE);
					helper.tvViewFirst.setText(mContext.getString(R.string.home_views) + " " + item.getView());
				} else {
					helper.tvViewFirst.setVisibility(View.GONE);
				}
				
//				if (item.getCountLike().length() > 0) {
//					helper.tvLikeFirst.setVisibility(View.VISIBLE);
//					helper.tvLikeFirst.setText(" | " + item.getCountLike() + " ");
//				} else {
//					helper.tvLikeFirst.setVisibility(View.GONE);
//				}
				helper.imgIconFirst.setImageBitmap(null);
				helper.imgIconSmallFirst.setImageBitmap(null);
				switch (item.getTypeItem()) {
				case 0:
					helper.layBottomFirst.setVisibility(View.VISIBLE);
					helper.imgIconSmallFirst.setVisibility(View.VISIBLE);
//					ImageUtility.loadBitmapFromUrl(mContext, ((ChannelInfo)item).getIcon(), helper.imgIconFirst);
					ImageUtility.loadBitmapFromUrl(mContext, ((ChannelInfo)item).getIconSmall(), helper.imgIconFirst);
					
//					if (((ChannelInfo)item).isFree()) {
//						helper.imgLockFirst.setVisibility(View.GONE);
//					} else {
//						helper.imgLockFirst.setVisibility(View.VISIBLE);
//						StringBuilder tmpId = new StringBuilder(",").append(item.getId()).append(",");
//						if (((BaseSlideMenuActivity)mContext).pProfile.pIdChannel.contains(tmpId.toString())) {
//							helper.imgLockFirst.setImageResource(R.drawable.unlook);
//						} else {
//							helper.imgLockFirst.setImageResource(R.drawable.look);
//						}
//						tmpId = null;
//					}
					break;
				case 1:
					helper.layBottomFirst.setVisibility(View.VISIBLE);
					helper.imgIconSmallFirst.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((VodInfo)item).getImage(), helper.imgIconFirst);
//					if (((VodInfo)item).isFree()) {
//						helper.imgLockFirst.setVisibility(View.GONE);
//					} else {
//						helper.imgLockFirst.setVisibility(View.VISIBLE);
//					}
					break;
				case 2:
					helper.layBottomFirst.setVisibility(View.VISIBLE);
					helper.imgIconSmallFirst.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((EpisodeInfo)item).getImage(), helper.imgIconFirst);
//					helper.imgLockFirst.setVisibility(View.GONE);
					break;
				case 3:
					helper.layBottomFirst.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((AdsInfo)item).getImage(), helper.imgIconFirst);
					break;
				default:
					break;
				}
			}
			
			item = ((HomeItemLandListview)info).getItemSecond();
			if (item != null) {
				helper.layChannelSecond.setVisibility(View.VISIBLE);
				helper.tvNameSecond.setText(item.getName());
				if (item.getView().length() > 0) {
					helper.tvViewSecond.setVisibility(View.VISIBLE);
					helper.tvViewSecond.setText(mContext.getString(R.string.home_views) + " " + item.getView() );
				} else {
					helper.tvViewSecond.setVisibility(View.GONE);
				}
				
//				if (item.getCountLike().length() > 0) {
//					helper.tvLikeSecond.setVisibility(View.VISIBLE);
//					helper.tvLikeSecond.setText(" | " + item.getCountLike() + " ");
//				} else {
//					helper.tvLikeSecond.setVisibility(View.GONE);
//				}
				helper.imgIconSecond.setImageBitmap(null);
				helper.imgIconSmallSecond.setImageBitmap(null);
				switch (item.getTypeItem()) {
				case 0:
					helper.layBottomSecond.setVisibility(View.VISIBLE);
					helper.imgIconSmallSecond.setVisibility(View.VISIBLE);
//					ImageUtility.loadBitmapFromUrl(mContext, ((ChannelInfo)item).getIcon(), helper.imgIconSecond);
					ImageUtility.loadBitmapFromUrl(mContext, ((ChannelInfo)item).getIconSmall(), helper.imgIconSecond);
//					if (((ChannelInfo)item).isFree()) {
//						helper.imgLockSecond.setVisibility(View.GONE);
//					} else {
//						helper.imgLockSecond.setVisibility(View.VISIBLE);
//						StringBuilder tmpId = new StringBuilder(",").append(item.getId()).append(",");
//						if (((BaseSlideMenuActivity)mContext).pProfile.pIdChannel.contains(tmpId.toString())) {
//							helper.imgLockSecond.setImageResource(R.drawable.unlook);
//						} else {
//							helper.imgLockSecond.setImageResource(R.drawable.look);
//						}
//						tmpId = null;
//					}
					break;
				case 1:
					helper.layBottomSecond.setVisibility(View.VISIBLE);
					helper.imgIconSmallSecond.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((VodInfo)item).getImage(), helper.imgIconSecond);
//					if (((VodInfo)item).isFree()) {
//						helper.imgLockSecond.setVisibility(View.GONE);
//					} else {
//						helper.imgLockSecond.setVisibility(View.VISIBLE);
//					}
					break;
				case 2:
					helper.layBottomSecond.setVisibility(View.VISIBLE);
					helper.imgIconSmallSecond.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((EpisodeInfo)item).getImage(), helper.imgIconSecond);
//					helper.imgLockSecond.setVisibility(View.GONE);
					break;
				case 3:
					helper.layBottomSecond.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((AdsInfo)item).getImage(), helper.imgIconSecond);
					break;
				default:
					break;
				}
			} else {
				helper.layChannelSecond.setVisibility(View.INVISIBLE);
			}
			
			item = ((HomeItemLandListview)info).getItemThird();
			if (item != null) {
				helper.layChannelThird.setVisibility(View.VISIBLE);
				helper.tvNameThird.setText(item.getName());
				if (item.getView().length() > 0) {
					helper.tvViewThird.setVisibility(View.VISIBLE);
					helper.tvViewThird.setText(mContext.getString(R.string.home_views) + " " + item.getView());
				} else {
					helper.tvViewThird.setVisibility(View.GONE);
				}
				
//				if (item.getCountLike().length() > 0) {
//					helper.tvLikeThird.setVisibility(View.VISIBLE);
//					helper.tvLikeThird.setText(" | " + item.getCountLike() + " ");
//				} else {
//					helper.tvLikeThird.setVisibility(View.GONE);
//				}
				helper.imgIconThird.setImageBitmap(null);
				helper.imgIconSmallThird.setImageBitmap(null);
				switch (item.getTypeItem()) {
				case 0:
					helper.layBottomThird.setVisibility(View.VISIBLE);
					helper.imgIconSmallThird.setVisibility(View.VISIBLE);
//					ImageUtility.loadBitmapFromUrl(mContext, ((ChannelInfo)item).getIcon(), helper.imgIconThird);
					ImageUtility.loadBitmapFromUrl(mContext, ((ChannelInfo)item).getIconSmall(), helper.imgIconThird);
//					if (((ChannelInfo)item).isFree()) {
//						helper.imgLockThird.setVisibility(View.GONE);
//					} else {
//						helper.imgLockThird.setVisibility(View.VISIBLE);
//						StringBuilder tmpId = new StringBuilder(",").append(item.getId()).append(",");
//						if (((BaseSlideMenuActivity)mContext).pProfile.pIdChannel.contains(tmpId.toString())) {
//							helper.imgLockThird.setImageResource(R.drawable.unlook);
//						} else {
//							helper.imgLockThird.setImageResource(R.drawable.look);
//						}
//						tmpId = null;
//					}
					break;
				case 1:
					helper.layBottomThird.setVisibility(View.VISIBLE);
					helper.imgIconSmallThird.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((VodInfo)item).getImage(), helper.imgIconThird);
//					if (((VodInfo)item).isFree()) {
//						helper.imgLockThird.setVisibility(View.GONE);
//					} else {
//						helper.imgLockThird.setVisibility(View.VISIBLE);
//					}
					break;
				case 2:
					helper.layBottomThird.setVisibility(View.VISIBLE);
					helper.imgIconSmallThird.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((EpisodeInfo)item).getImage(), helper.imgIconThird);
//					helper.imgLockThird.setVisibility(View.GONE);
					break;
				case 3:
					helper.layBottomThird.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((AdsInfo)item).getImage(), helper.imgIconThird);
					break;
				default:
					break;
				}
			} else {
				helper.layChannelThird.setVisibility(View.INVISIBLE);
			}
			
			item = ((HomeItemLandListview)info).getItemFourth();
			if (item != null) {
				helper.layChannelFourth.setVisibility(View.VISIBLE);
				helper.tvNameFourth.setText(item.getName());
				if (item.getView().length() > 0) {
					helper.tvViewFourth.setVisibility(View.VISIBLE);
					helper.tvViewFourth.setText(mContext.getString(R.string.home_views) + " " + item.getView());
				} else {
					helper.tvViewFourth.setVisibility(View.GONE);
				}
				
//				if (item.getCountLike().length() > 0) {
//					helper.tvLikeFourth.setVisibility(View.VISIBLE);
//					helper.tvLikeFourth.setText(" | " + item.getCountLike() + " ");
//				} else {
//					helper.tvLikeFourth.setVisibility(View.GONE);
//				}
				helper.imgIconFourth.setImageBitmap(null);
				helper.imgIconSmallFourth.setImageBitmap(null);
				switch (item.getTypeItem()) {
				case 0:
					helper.layBottomFourth.setVisibility(View.VISIBLE);
					helper.imgIconSmallFourth.setVisibility(View.VISIBLE);
//					ImageUtility.loadBitmapFromUrl(mContext, ((ChannelInfo)item).getIcon(), helper.imgIconFourth);
					ImageUtility.loadBitmapFromUrl(mContext, ((ChannelInfo)item).getIconSmall(), helper.imgIconFourth);
//					if (((ChannelInfo)item).isFree()) {
//						helper.imgLockFourth.setVisibility(View.GONE);
//					} else {
//						helper.imgLockFourth.setVisibility(View.VISIBLE);
//						StringBuilder tmpId = new StringBuilder(",").append(item.getId()).append(",");
//						if (((BaseSlideMenuActivity)mContext).pProfile.pIdChannel.contains(tmpId.toString())) {
//							helper.imgLockFourth.setImageResource(R.drawable.unlook);
//						} else {
//							helper.imgLockFourth.setImageResource(R.drawable.look);
//						}
//						tmpId = null;
//					}
					break;
				case 1:
					helper.layBottomFourth.setVisibility(View.VISIBLE);
					helper.imgIconSmallFourth.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((VodInfo)item).getImage(), helper.imgIconFourth);
//					if (((VodInfo)item).isFree()) {
//						helper.imgLockFourth.setVisibility(View.GONE);
//					} else {
//						helper.imgLockFourth.setVisibility(View.VISIBLE);
//					}
					break;
				case 2:
					helper.layBottomFourth.setVisibility(View.VISIBLE);
					helper.imgIconSmallFourth.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((EpisodeInfo)item).getImage(), helper.imgIconFourth);
//					helper.imgLockFourth.setVisibility(View.GONE);
					break;
				case 3:
					helper.layBottomFourth.setVisibility(View.GONE);
					ImageUtility.loadBitmapFromUrl(mContext, ((AdsInfo)item).getImage(), helper.imgIconFourth);
					break;
				default:
					break;
				}
			} else {
				helper.layChannelFourth.setVisibility(View.INVISIBLE);
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
		
		public RelativeLayout layChannelFirst;
		public ImageView imgIconFirst;
		public LinearLayout layBottomFirst;
		public ImageView imgIconSmallFirst;
		public TextView tvNameFirst;
		public TextView tvViewFirst;
//		public TextView tvLikeFirst;
//		public ImageView imgLockFirst;
		
		public RelativeLayout layChannelSecond;
		public ImageView imgIconSecond;
		public LinearLayout layBottomSecond;
		public ImageView imgIconSmallSecond;
		public TextView tvNameSecond;
		public TextView tvViewSecond;
//		public TextView tvLikeSecond;
//		public ImageView imgLockSecond;
		
		public RelativeLayout layChannelThird;
		public ImageView imgIconThird;
		public LinearLayout layBottomThird;
		public ImageView imgIconSmallThird;
		public TextView tvNameThird;
		public TextView tvViewThird;
//		public TextView tvLikeThird;
//		public ImageView imgLockThird;
		
		public RelativeLayout layChannelFourth;
		public ImageView imgIconFourth;
		public LinearLayout layBottomFourth;
		public ImageView imgIconSmallFourth;
		public TextView tvNameFourth;
		public TextView tvViewFourth;
//		public TextView tvLikeFourth;
//		public ImageView imgLockFourth;
		
		public TextView tvCateName;
	}

}
