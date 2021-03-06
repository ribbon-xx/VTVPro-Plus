package mdn.vtvsport.fragment;

import java.util.ArrayList;

import mdn.vtvsport.BaseSlideMenuActivity;
import mdn.vtvsport.R;
import mdn.vtvsport.adapter.VtvPlusAdapter;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ImageUtility;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.AdsInfo;
import mdn.vtvsport.object.ChannelInfo;
import mdn.vtvsport.object.EpisodeInfo;
import mdn.vtvsport.object.ItemVtvPlusInfo;
import mdn.vtvsport.object.VodInfo;
import mdn.vtvsport.util.DeviceUtil;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefreshscrollview.library.PullToRefreshBase;
import com.handmark.pulltorefreshscrollview.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefreshscrollview.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class VtvPlusFragment extends BaseFragment {
	public static final String KEY_TYPE_ITEM_VTV = "_tvplus_type_item";
	public static final String KEY_ID_ITEM_VTV = "_tvplus_id_item";
	public static final String KEY_NAME_ITEM_VTV = "_tvplus_name_item";

	private PullToRefreshScrollView layChannelScreen;
	private GridView gridChannel;
	private TextView tvChannel;
	private TextView tvNoItem;

	private int startIndex;
	private int offset = 10;
	private ArrayList<ItemVtvPlusInfo> listVtvPlus;
	private VtvPlusAdapter mAdapter;

	private int mTypeCategory = 0; //0_channel,1_vod,2_series of vod, 3_search,
									// -2 favourist; -1 listnew; -3 listhot
	private String mNameCategory = "";
	private String mIdCategory = ""; // luu ca tu can search vao day

	private boolean isGetFullList = false;

	private Animation mAnimation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startIndex = 0;
		isGetFullList = false;
		Bundle bundle = getArguments();
		if (bundle != null) {
			mTypeCategory = bundle.getInt(KEY_TYPE_ITEM_VTV);
			mIdCategory = bundle.getString(KEY_ID_ITEM_VTV);
			mNameCategory = bundle.getString(KEY_NAME_ITEM_VTV);
		}
		this.mAnimation = AnimationUtils.loadAnimation(baseSlideMenuActivity,
				R.anim.slide_in_scale);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (DeviceUtil.isTablet(baseSlideMenuActivity)) {
			baseSlideMenuActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		} else {
			baseSlideMenuActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		baseSlideMenuActivity.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		baseSlideMenuActivity.getLayoutTitle().setVisibility(View.VISIBLE);

		View view = inflater.inflate(R.layout.fragment_vtvplus, container,
				false);

		bindView(view);
		bindAction();

		baseSlideMenuActivity.trackEvent(baseSlideMenuActivity.getResources()
				.getString(R.string.app_name), baseSlideMenuActivity
				.getResources().getString(R.string.screen_category), "");
		baseSlideMenuActivity.trackView(baseSlideMenuActivity.getResources()
				.getString(R.string.screen_category));

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (listVtvPlus != null) {
			listVtvPlus.clear();
			listVtvPlus = null;
			mAdapter = null;
		}

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	protected void initUiTabbar() {
		// TODO Auto-generated method stub
		super.initUiTabbar();

//		baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
		baseSlideMenuActivity.iconSetting.setVisibility(View.GONE);
		baseSlideMenuActivity.iconBack.setVisibility(View.VISIBLE);
		baseSlideMenuActivity.iconVtvPlus.setVisibility(View.GONE);
		if (mTypeCategory != 3) {
			baseSlideMenuActivity.closeViewSearch();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

		int count = getNumberRowGridView();
		if (mAdapter != null) {
			gridChannel.removeAllViewsInLayout();
			mAdapter.updateSizeItem(count);
			gridChannel.setNumColumns(count);
		}
		expandGridView(count);
	}

	private void bindView(View view) {
		layChannelScreen = (PullToRefreshScrollView) view
				.findViewById(R.id.layListItem);
		layChannelScreen
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						layChannelScreen.onRefreshComplete();
						startIndex = 0;
						if (listVtvPlus != null) {
							listVtvPlus.clear();
						}
						gridChannel.setVisibility(View.GONE);
						isGetFullList = false;
						try {
							if (ImageLoader.getInstance().isInited()) {
								ImageLoader.getInstance().stop();
								ImageLoader.getInstance().clearDiscCache();
								ImageLoader.getInstance().getDiscCache()
										.clear();
								ImageLoader.getInstance().clearMemoryCache();
								ImageLoader.getInstance().getMemoryCache()
										.clear();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						callListItemVtvPlus();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						layChannelScreen.onRefreshComplete();
						if (!isGetFullList) {
							callListItemVtvPlus();
						}
					}
				});
		gridChannel = (GridView) view.findViewById(R.id.grChannel);
		tvChannel = (TextView) view.findViewById(R.id.tvChannel);
		tvChannel.setText(mNameCategory);
		baseSlideMenuActivity.setTextCategory(mNameCategory);
		tvNoItem = (TextView) view.findViewById(R.id.tvNoItemSearch);
		tvNoItem.setVisibility(View.GONE);
	}

	private void bindAction() {
		getListChannel();
	}

	public void getListChannel() {
		if (mTypeCategory == 3) {
			tvChannel.setText(getString(R.string.item_search) +" " + mIdCategory
					+ "\"");
		}

		int count = getNumberRowGridView();
		gridChannel.setNumColumns(count);
		if (listVtvPlus != null && listVtvPlus.size() > 0) {
			if (mAdapter == null) {
				mAdapter = new VtvPlusAdapter(baseSlideMenuActivity,
						itemRecipesClickListener, listVtvPlus);
				mAdapter.updateSizeItem(count);
				gridChannel.setAdapter(mAdapter);
				expandGridView(count);
				return;
			} else {
				mAdapter.updateSizeItem(count);
				gridChannel.setAdapter(mAdapter);
				expandGridView(count);
				return;
			}
		}
		callListItemVtvPlus();
	}

	public void clearData(String keyword) {
		isGetFullList = false;
		startIndex = 0;
		if (listVtvPlus != null) {
			listVtvPlus.clear();
		}
		mTypeCategory = 3;
		mIdCategory = keyword;
		tvChannel.setText(getString(R.string.item_search) + " " + mIdCategory
				+ "\"");
	}

	/****************************************************************************
	 * 
	 * Call data
	 * 
	 ***************************************************************************/
	private void callListItemVtvPlus() {
		switch (mTypeCategory) {
		case 1:
			callListItemOfCategory();
			break;
		case 2:
			callListEposideOfVod();
			break;
		case 3:
			callListSearch();
			break;
		case -2:
//			callListItemOfFavourist();
			break;
		case -1:
//			callListItemOfListNew();
			break;
		case -3:
//			callListItemOfListHot();
			break;
		case 0:
			callListItemOfChannel();
		default:
			break;
		}
	}

	IApiCallback mCallBack = new IApiCallback() {

		@Override
		public void responseSuccess(String response) {
			// TODO Auto-generated method stub
			gridChannel.setVisibility(View.VISIBLE);
			if (listVtvPlus == null) {
				listVtvPlus = new ArrayList<ItemVtvPlusInfo>();
			}
			ArrayList<ItemVtvPlusInfo> arr = null;
			arr = ParserManager.parserListItemOfCategory(response);
			listVtvPlus.addAll(arr);
			startIndex += arr.size();
			if (arr.size() < offset) {
				isGetFullList = true;
			}
			if (listVtvPlus.size() == 0) {
				layChannelScreen.setVisibility(View.GONE);
				tvNoItem.setVisibility(View.VISIBLE);
			} else {
				layChannelScreen.setVisibility(View.VISIBLE);
				tvNoItem.setVisibility(View.GONE);
			}

			int count = getNumberRowGridView();
			gridChannel.setNumColumns(count);
			if (mAdapter == null || gridChannel.getAdapter() == null) {
				mAdapter = new VtvPlusAdapter(baseSlideMenuActivity,
						itemRecipesClickListener, listVtvPlus);
				mAdapter.updateSizeItem(count);
				gridChannel.setAdapter(mAdapter);
			} else {
				mAdapter.updateSizeItem(count);
				mAdapter.notifyDataSetChanged();
			}
			expandGridView(count);
		}

		@Override
		public void responseFailWithCode(int statusCode) {
			DialogManager.alert(baseSlideMenuActivity,
					getString(R.string.network_fail));
		}
	};

	public void callListItemOfFavourist() {
		ApiManager.callListItemOfFavourist(baseSlideMenuActivity, mCallBack,
				startIndex, baseSlideMenuActivity.pProfile.pUserName, true);
	}

	public void callListItemOfListNew() {
		ApiManager.callListItemOfListNew(baseSlideMenuActivity, mCallBack,
				startIndex, baseSlideMenuActivity.pProfile.pUserName, true);
	}

	public void callListItemOfChannel() {
		ApiManager.callListItemOfChannel(baseSlideMenuActivity, mCallBack,
				startIndex, baseSlideMenuActivity.pProfile.pUserName, true);
	}
	
	public void callListItemOfListHot() {
		ApiManager.callListItemOfListHot(baseSlideMenuActivity, mCallBack,
				startIndex, baseSlideMenuActivity.pProfile.pUserName, true);
	}

	public void callListItemOfCategory() {

		ApiManager.callListItemOfCategory(baseSlideMenuActivity, mCallBack,
				mIdCategory, startIndex,
				baseSlideMenuActivity.pProfile.pUserName, true);
	}

	private void callListEposideOfVod() {
		ApiManager.callListEposideOfVod(baseSlideMenuActivity,
				new IApiCallback() {

					@Override
					public void responseSuccess(String response) {
						// TODO Auto-generated method stub
						gridChannel.setVisibility(View.VISIBLE);
						if (listVtvPlus == null) {
							listVtvPlus = new ArrayList<ItemVtvPlusInfo>();
						}
						ArrayList<ItemVtvPlusInfo> arr = null;
						arr = ParserManager.parserListEposide(response);
						if (arr.size() < offset) {
							isGetFullList = true;
						}
						startIndex += arr.size();
						listVtvPlus.addAll(arr);

						int count = getNumberRowGridView();
						gridChannel.setNumColumns(count);
						if (mAdapter == null
								|| gridChannel.getAdapter() == null) {
							mAdapter = new VtvPlusAdapter(
									baseSlideMenuActivity,
									itemRecipesClickListener, listVtvPlus);
							mAdapter.updateSizeItem(count);
							gridChannel.setAdapter(mAdapter);
						} else {
							mAdapter.updateSizeItem(count);
							mAdapter.notifyDataSetChanged();
						}
						expandGridView(count);
					}

					@Override
					public void responseFailWithCode(int statusCode) {
						DialogManager.alert(baseSlideMenuActivity,
								getString(R.string.network_fail));
					}
				}, Secure.getString(baseSlideMenuActivity.getContentResolver(),
						Secure.ANDROID_ID), mIdCategory, startIndex,
				baseSlideMenuActivity.pProfile.pUserName);
	}

	private void callListSearch() {
		ApiManager.callListSearch(baseSlideMenuActivity, mCallBack,
				mIdCategory, startIndex,
				baseSlideMenuActivity.pProfile.pUserName);
	}

	private void expandGridView(int count) {
		if (mAdapter == null) {
			return;
		}

		int countRow = (mAdapter.getCount() - 1 + count) / count;
		int totalHeight = (int) (countRow * (mAdapter.getHeightHomeRow() + mAdapter
				.getPadding()));
		ViewGroup.LayoutParams params = gridChannel.getLayoutParams();
		params.height = totalHeight;
		gridChannel.setLayoutParams(params);
		gridChannel.setFocusable(true);
	}

	private int getNumberRowGridView() {
		int count = 0;
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			count = 2;
		} else {
			count = 4;
		}
		return count;
	}

	/****************************************************************************
	 * 
	 * Click item
	 * 
	 ***************************************************************************/
	private OnClickListener itemRecipesClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int pos = gridChannel.getPositionForView(v);
			ItemVtvPlusInfo info = listVtvPlus.get(pos);

			if (info.getTypeItem() == 0) {
				actionChannel(info);
			} else if (info.getTypeItem() == 2) {
				actionEposide(info);
			} else if (info.getTypeItem() == 3) {
				actionAds(info);
			} else {
				actionVod(info);
			}
		}
	};

	private void actionChannel(ItemVtvPlusInfo info) {
		ChannelInfo channelInfo = (ChannelInfo) info;
		Bundle bundle = new Bundle();
		bundle.putInt(HomeFragment.TYPE, info.getTypeItem());
		bundle.putParcelable(HomeFragment.TYPE_CHANNEL, channelInfo);
		PlayerFragment playerFragment = new PlayerFragment();
		playerFragment.setArguments(bundle);
		baseSlideMenuActivity.switchContent(playerFragment, false);

//		ApiManager.callUpdateViewChannel(baseSlideMenuActivity,
//				channelInfo.getId());

		baseSlideMenuActivity.trackEvent(
				baseSlideMenuActivity.getString(R.string.track_channel),
				channelInfo.getName(), "");
		baseSlideMenuActivity.trackView("Channel " + channelInfo.getName());
	}

	private void actionEposide(ItemVtvPlusInfo info) {
		EpisodeInfo episodeInfo = (EpisodeInfo) info;
		Bundle bundle = new Bundle();
		bundle.putInt(HomeFragment.TYPE, info.getTypeItem());
		bundle.putParcelable(HomeFragment.TYPE_EPISODE, episodeInfo);
		PlayerFragment playerFragment = new PlayerFragment();
		playerFragment.setArguments(bundle);
		baseSlideMenuActivity.switchContent(playerFragment, false);

		ApiManager.callUpdateViewEpisode(baseSlideMenuActivity,
				episodeInfo.getId());
	}

	private void actionVod(ItemVtvPlusInfo info) {
		VodInfo vodInfo = (VodInfo) info;
		baseSlideMenuActivity.trackEvent(
				baseSlideMenuActivity.getString(R.string.track_vod),
				vodInfo.getName(), "");
		baseSlideMenuActivity.trackView("Video " + vodInfo.getName());

		if (vodInfo.isSeries()) {
			Bundle bundle = new Bundle();
			bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 2);
			bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV, info.getId());
			bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV, info.getName());
			VtvPlusFragment mVtvFragment = new VtvPlusFragment();
			mVtvFragment.setArguments(bundle);
			baseSlideMenuActivity.switchContent(mVtvFragment, false);
		} else {
			Bundle bundle = new Bundle();
			bundle.putInt(HomeFragment.TYPE, info.getTypeItem());
			bundle.putParcelable(HomeFragment.TYPE_VOD, vodInfo);
			PlayerFragment playerFragment = new PlayerFragment();
			playerFragment.setArguments(bundle);
			baseSlideMenuActivity.switchContent(playerFragment, false);

			ApiManager
					.callUpdateViewVod(baseSlideMenuActivity, vodInfo.getId());
		}
	}

	private void actionAds(ItemVtvPlusInfo info) {
		AdsInfo adsInfo = (AdsInfo) info;
		if (adsInfo.getTypeAds() == 0) {
			actionAdsBannerOrAdver(adsInfo.getUrl(), adsInfo.getLink());
		} else if (adsInfo.getTypeAds() == 1) {
			actionAdsBannerOrAdver(adsInfo.getPoster(), adsInfo.getLink());
		} else if (adsInfo.getTypeAds() == 2) {
			actionAdsVideo(adsInfo);
		}
	}

	private void actionAdsBannerOrAdver(String urlPoster, String urlLink) {
		if (urlPoster == null || urlPoster.length() == 0) {
			return;
		}
		DialogManager.showSimpleProgressDialog(baseSlideMenuActivity);
		BaseSlideMenuActivity.linkUrlAds = urlLink;
		ImageUtility.loadBitmapFromUrl(baseSlideMenuActivity, urlPoster,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub
						Toast.makeText(baseSlideMenuActivity,
								getString(R.string.connect_fail),
								Toast.LENGTH_SHORT).show();
						DialogManager.closeProgressDialog();
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						baseSlideMenuActivity.getImageAds().setVisibility(
								View.VISIBLE);
						baseSlideMenuActivity.getImageAds().startAnimation(
								mAnimation);
						DialogManager.closeProgressDialog();
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						Toast.makeText(baseSlideMenuActivity,
								getString(R.string.connect_fail),
								Toast.LENGTH_SHORT).show();
						DialogManager.closeProgressDialog();
					}
				}, baseSlideMenuActivity.getImageAds());
	}

	private void actionAdsVideo(AdsInfo info) {
		AdsVideoFragment fr = new AdsVideoFragment();
		Bundle bundle = new Bundle();
		bundle.putString(AdsVideoFragment.XURL_VIDEO, info.getUrl());
		fr.setArguments(bundle);
		baseSlideMenuActivity.switchContent(fr, true);
	}

}
