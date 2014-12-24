package mdn.vtvsport.fragment;

import java.util.ArrayList;

import mdn.vtvsport.BaseSlideMenuActivity;
import mdn.vtvsport.R;
import mdn.vtvsport.adapter.BannerHomeAdapter;
import mdn.vtvsport.adapter.HomeListViewAdapter;
import mdn.vtvsport.adapter.HomeListViewLandAdapter;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ImageUtility;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.common.Timer;
import mdn.vtvsport.library.viewflow.CircleFlowIndicator;
import mdn.vtvsport.library.viewflow.ViewFlow;
import mdn.vtvsport.library.viewflow.ViewFlow.ViewSwitchListener;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.AdsInfo;
import mdn.vtvsport.object.BannerInfo;
import mdn.vtvsport.object.ChannelInfo;
import mdn.vtvsport.object.EpisodeInfo;
import mdn.vtvsport.object.ItemVtvPlusInfo;
import mdn.vtvsport.object.VodInfo;
import mdn.vtvsport.object.account.UserInfo;
import mdn.vtvsport.object.home.HomeCategory;
import mdn.vtvsport.object.home.HomeItemLandListview;
import mdn.vtvsport.object.home.HomeItemListview;
import mdn.vtvsport.object.home.HomeObject;
import mdn.vtvsport.util.DeviceUtil;
import mdn.vtvsport.util.HLog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.handmark.pulltorefreshscrollview.library.PullToRefreshBase;
import com.handmark.pulltorefreshscrollview.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefreshscrollview.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class HomeFragment extends BaseFragment implements OnTouchListener, OnClickListener {
	public static int HEIGHT_BANNER = 0;
	public static int COUNT_CATEGORY = 0;

	private PullToRefreshScrollView layHomeScreen;
	private RelativeLayout layGallery;
	private CircleFlowIndicator galleryIndicator;
	private ViewFlow galHomeBanner;
	private ListView lvHomeObject;

	public static boolean isHomeRunning;

	private ArrayList<HomeObject> mListHomeObject;
	private HomeListViewAdapter mAdapter;
	private ArrayList<HomeObject> mListHomeObjectLand;
	private HomeListViewLandAdapter mAdapterLand;

	private ArrayList<BannerInfo> mListBanner;
	private BannerHomeAdapter mBannerAdapter;

	private int indicator = 0;
	private LoadBannerTimer loadBannerTimer;

	private Thread mThreadBanner;
	private boolean isRunBanner = false;
	private long mTimeBanner = 0;

	public final static String TYPE = "TYPE";
	public final static String TYPE_CHANNEL = "TYPE_CHANNEL";
	public final static String TYPE_VOD = "TYPE_VOD"; // isSerer= true
	public final static String TYPE_EPISODE = "TYPE_EPISODE";
	public static boolean IS_CHANGE_DATA = false;

	public static String APP_NAME = "VTVPlus";
	public static String APP_NAME_1 = "VTV Plus";

	private Animation mAnimation;
	private boolean isTablet = false;
	private boolean isLandscape = false; // dang o man hinh doc hay ngang
	private View view;
	private LinearLayout tabVideo;
	private LinearLayout tabChannel;
	private LinearLayout tabEvent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		HEIGHT_BANNER = 0;
		COUNT_CATEGORY = 0;
		loadBannerTimer = new LoadBannerTimer();

		getInfoApp();
		this.mAnimation = AnimationUtils.loadAnimation(baseSlideMenuActivity,
				R.anim.slide_in_scale);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {

		isTablet = DeviceUtil.isTablet(baseSlideMenuActivity);
		if (isTablet) {
			baseSlideMenuActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		} else {
			isLandscape = false;
			baseSlideMenuActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		baseSlideMenuActivity.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		view = inflater.inflate(R.layout.fragment_home, container, false);
		
		layHomeScreen = (PullToRefreshScrollView) view.findViewById(R.id.pull_home_content);
		baseSlideMenuActivity.getLayoutTitle().setVisibility(View.VISIBLE);

		layHomeScreen
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						refreshListHome();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {

					}
				});

		indicator = 0;

		bindView();
		bindAction();

		mThreadBanner = new Thread(mRunBanner);

		// Google analytics
		baseSlideMenuActivity.trackEvent(baseSlideMenuActivity.getResources()
				.getString(R.string.app_name), baseSlideMenuActivity
				.getResources().getString(R.string.screen_home), "");
		baseSlideMenuActivity.trackView(baseSlideMenuActivity.getResources()
				.getString(R.string.screen_home));

		return view;
	}

	@Override
	public void onPause() {
		isHomeRunning = false;
		super.onPause();
	}

	@Override
	public void onResume() {
		isHomeRunning = true;
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		clearData();
	}

	@Override
	protected void initUiTabbar() {
		// TODO Auto-generated method stub
		super.initUiTabbar();
//		baseSlideMenuActivity.iconInteract.setVisibility(View.VISIBLE);
	}

	@Override
	public void actionInteract() {
		// TODO Auto-generated method stub
		super.actionInteract();

		baseSlideMenuActivity.switchContent(new AllInteractionFragment(), true);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

		int count = getNumberRowGridView();
		lvHomeObject.removeAllViewsInLayout();
		if (!isLandscape) {
			lvHomeObject.setAdapter(mAdapter);
		} else {
			lvHomeObject.setAdapter(mAdapterLand);
		}
		if ((mBannerAdapter != null) && (HEIGHT_BANNER != 0)) {
			galHomeBanner.getLayoutParams().width = DeviceUtil
					.getWidthScreen(baseSlideMenuActivity);
			if (!isLandscape) {
				HEIGHT_BANNER = mBannerAdapter.heightPortrait;
			} else {
				HEIGHT_BANNER = mBannerAdapter.heightLand;
			}
			galHomeBanner.getLayoutParams().height = HEIGHT_BANNER;
		}
		expandGridView(count);
	}

	private int getNumberRowGridView() {
		int count = 0;
		if (!isTablet) {
			isLandscape = false;
			return 2;
		}
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			isLandscape = false;
			count = 2;
		} else {
			isLandscape = true;
			count = 4;
		}
		return count;
	}

	private void refreshListHome() {
		// Clear data
		clearData();
		clearDataBanner();
		try {
			if (ImageLoader.getInstance().isInited()) {
				ImageLoader.getInstance().stop();
				ImageLoader.getInstance().clearDiscCache();
				ImageLoader.getInstance().getDiscCache().clear();
				ImageLoader.getInstance().clearMemoryCache();
				ImageLoader.getInstance().getMemoryCache().clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		mThreadBanner = new Thread(mRunBanner);
		layHomeScreen.onRefreshComplete();
		layHomeScreen.setVisibility(View.GONE);
		callHomeData();
	}

	public void clearData() {
		if (mListHomeObject != null) {
			mListHomeObject.clear();
			mListHomeObject = null;
			mAdapter = null;
		}

		if (mListHomeObjectLand != null) {
			mListHomeObjectLand.clear();
			mListHomeObjectLand = null;
			mAdapterLand = null;
		}

		if (mListBanner != null) {
			HEIGHT_BANNER = 0;
			COUNT_CATEGORY = 0;
			indicator = 0;
			mListBanner.clear();
			mListBanner = null;
			mBannerAdapter = null;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		clearDataBanner();

	}

	public void clearDataBanner() {

		try {
			loadBannerTimer.stop();
			isRunBanner = false;
			mThreadBanner.interrupt();
			galHomeBanner.removeAllViews();
			galHomeBanner.removeAllViewsInLayout();
		} catch (Exception e) {
			// TODO: handle exception
		}
		HEIGHT_BANNER = 0;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		layHomeScreen.getRefreshableView().requestDisallowInterceptTouchEvent(
				true);
		return false;
	}

	private void bindView() {
		baseSlideMenuActivity.iconSetting.setVisibility(View.VISIBLE);
		baseSlideMenuActivity.iconBack.setVisibility(View.GONE);
		baseSlideMenuActivity.iconVtvPlus.setVisibility(View.GONE);
		layGallery = (RelativeLayout) view
				.findViewById(R.id.layoutGallery);
		layGallery.setVisibility(View.GONE);

		galleryIndicator = (CircleFlowIndicator) view
				.findViewById(R.id.indHomeGallery);
		galHomeBanner = (ViewFlow) view
				.findViewById(R.id.galHomeBanner);
		galHomeBanner.setOnTouchListener(this);
		lvHomeObject = (ListView) view.findViewById(R.id.grChannel);
		
		tabEvent = (LinearLayout) view.findViewById(R.id.tab_event);
		tabEvent.setOnClickListener(this);
		tabChannel = (LinearLayout) view.findViewById(R.id.tab_channel);
		tabChannel.setOnClickListener(this);
		tabVideo = (LinearLayout) view.findViewById(R.id.tab_video);
		tabVideo.setOnClickListener(this);
		
		baseSlideMenuActivity.closeViewSearch();
	}
	
	private void bindAction() {
		if (baseSlideMenuActivity.pProfile.pUserName.length() > 0
				&& baseSlideMenuActivity.pProfile.pSession.length() == 0) {
			baseSlideMenuActivity.callLoginApi(callbackLogin);
			callListHomeObject();
		} else {
			callListHomeObject();
		}
	}

	/**
	 * *************************************************************************
	 * <p/>
	 * Call data
	 * <p/>
	 * *************************************************************************
	 */
	public void callListHomeObject() {
		if (IS_CHANGE_DATA) {
			IS_CHANGE_DATA = false;
			refreshListHome();
			return;
		}

		if (!checkCallApi()) {
			actionReturn();
			return;
		}

		callHomeData();

	}

	public void callHomeData() {
		DialogManager.showSimpleProgressDialog(baseSlideMenuActivity);

		ApiManager.callListHome(baseSlideMenuActivity, new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				parserData(response);
				if (BaseSlideMenuActivity.gcmBundleId != null
						&& BaseSlideMenuActivity.gcmBundleId.length() > 0) {
					callPushAds();
				}
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				DialogManager.alert(baseSlideMenuActivity,
						getString(R.string.network_fail));

				DialogManager.closeProgressDialog();
			}
		}, baseSlideMenuActivity.pProfile.pUserName);
	}

	private void parserData(String response) {
		new AsyncTask<String, Void, Void>() {

			@Override
			protected Void doInBackground(String... params) {
				// TODO Auto-generated method stub
				if (mListHomeObject == null) {
					mListHomeObject = new ArrayList<HomeObject>();
				}
				if (mListBanner == null) {
					mListBanner = new ArrayList<BannerInfo>();
				}
				if ((mListHomeObjectLand == null) && isTablet) {
					mListHomeObjectLand = new ArrayList<HomeObject>();
				}

				ParserManager.parserListHome(params[0], mListBanner,
						mListHomeObject, mListHomeObjectLand,
						baseSlideMenuActivity);
				mBannerAdapter = new BannerHomeAdapter(baseSlideMenuActivity,
						mListBanner, mItemBannerClickListener);
				mAdapter = new HomeListViewAdapter(baseSlideMenuActivity,
						mListHomeObject, cateClickListener, firstClickListener,
						secondClickListener);
				if (isTablet) {
					mAdapterLand = new HomeListViewLandAdapter(
							baseSlideMenuActivity, mListHomeObjectLand,
							cateClickListener, firstClickListener,
							secondClickListener, thirdClickListener,
							fourthClickListener);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				int count = getNumberRowGridView();
				if (!isLandscape) {
					lvHomeObject.setAdapter(mAdapter);
				} else {
					lvHomeObject.setAdapter(mAdapterLand);
				}
				expandGridView(count);

				galHomeBanner.setAdapter(mBannerAdapter);
				initBanner();

				layHomeScreen.setVisibility(View.VISIBLE);
				DialogManager.closeProgressDialog();

			}

		}.execute(response);
	}

	private void callPushAds() {
		ApiManager.callDetailAds(baseSlideMenuActivity, new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				AdsInfo item = ParserManager.parserPushAds(response);
				if (item != null) {
					actionAds(item);
				}
				BaseSlideMenuActivity.gcmBundleId = "";
				BaseSlideMenuActivity.gcmBundleType = "";
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub

			}
		}, BaseSlideMenuActivity.gcmBundleId);
	}

	private void actionReturn() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				if (mAdapter == null) {
					mAdapter = new HomeListViewAdapter(baseSlideMenuActivity,
							mListHomeObject, cateClickListener,
							firstClickListener, secondClickListener);

				}

				if ((mAdapterLand == null) && isTablet) {
					mAdapterLand = new HomeListViewLandAdapter(
							baseSlideMenuActivity, mListHomeObjectLand,
							cateClickListener, firstClickListener,
							secondClickListener, thirdClickListener,
							fourthClickListener);
				}

				if (mBannerAdapter == null) {
					mBannerAdapter = new BannerHomeAdapter(
							baseSlideMenuActivity, mListBanner,
							mItemBannerClickListener);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				int count = getNumberRowGridView();
				if (!isLandscape) {
					lvHomeObject.setAdapter(mAdapter);
				} else {
					lvHomeObject.setAdapter(mAdapterLand);
				}
				expandGridView(count);

				galHomeBanner.setAdapter(mBannerAdapter);
				initBanner();

				DialogManager.closeProgressDialog();
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				DialogManager.showSimpleProgressDialog(baseSlideMenuActivity);
			}

		}.execute();
	}

	public boolean checkCallApi() {
		if ((mListBanner == null) /* || (mListBanner.size() == 0) */) {
			mListBanner = null;
			mBannerAdapter = null;
			if (mListHomeObject != null) {
				mListHomeObject.clear();
			}
			mListHomeObject = null;
			mAdapter = null;
			if (mListHomeObjectLand != null) {
				mListHomeObjectLand.clear();
				mListHomeObjectLand = null;
			}
			mAdapterLand = null;
			return true;
		}

		if ((mListHomeObject == null) || (mListHomeObject.size() == 0)) {
			mListHomeObject = null;
			mAdapter = null;
			mListHomeObjectLand = null;
			mAdapterLand = null;
			if (mListBanner != null) {
				mListBanner.clear();
			}
			mListBanner = null;
			mBannerAdapter = null;
			return true;
		}

		if (isTablet
				&& ((mListHomeObjectLand == null) || (mListHomeObjectLand
				.size() == 0))) {
			mListHomeObject = null;
			mAdapter = null;
			mListHomeObjectLand = null;
			mAdapterLand = null;
			if (mListBanner != null) {
				mListBanner.clear();
			}
			mListBanner = null;
			mBannerAdapter = null;
			return true;
		}

		return false;
	}

	private void initBanner() {
		galHomeBanner.setFlowIndicator(galleryIndicator);
		galHomeBanner.getLayoutParams().width = DeviceUtil
				.getWidthScreen(baseSlideMenuActivity);
		galHomeBanner.getLayoutParams().height = HEIGHT_BANNER;

		layGallery.setVisibility(View.VISIBLE);
		galHomeBanner.setOnViewSwitchListener(new ViewSwitchListener() {

			@Override
			public void onSwitched(View view, int position) {
				// TODO Auto-generated method stub
				indicator = position;
				mTimeBanner = SystemClock.uptimeMillis();
			}
		});
		loadBannerTimer.start();
	}

	private void expandGridView(int count) {
		if (mAdapter == null && mAdapterLand == null) {
			return;
		}

		int countRow = 0;
		int totalHeight = 0;

		if (count == 2) {
			countRow = mAdapter.getCount();
			totalHeight = (int) ((countRow - COUNT_CATEGORY)
					* mAdapter.getHeightHomeRow() + COUNT_CATEGORY
					* mAdapter.getHeightCate() + (countRow + 1)
					* mAdapter.getPadding());
		} else {
			countRow = mAdapterLand.getCount();
			totalHeight = (int) ((countRow - COUNT_CATEGORY)
					* mAdapterLand.getHeightHomeRow() + COUNT_CATEGORY
					* mAdapterLand.getHeightCate() + (countRow + 1)
					* mAdapterLand.getPadding());
		}

		// int countRow = mAdapter.getCount();
		// int totalHeight = (int) ((countRow - COUNT_CATEGORY)
		// * mAdapter.getHeightHomeRow() + COUNT_CATEGORY
		// * mAdapter.getHeightCate() + (countRow + 1)
		// * mAdapter.getPadding());
		ViewGroup.LayoutParams params = lvHomeObject.getLayoutParams();
		params.height = totalHeight;
		lvHomeObject.setLayoutParams(params);
		lvHomeObject.setFocusable(true);
	}

	/**
	 * *************************************************************************
	 * <p/>
	 * Click item
	 * <p/>
	 * *************************************************************************
	 */
	private OnClickListener cateClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Bundle bundle = null;
			VtvPlusFragment mVtvFragment = null;

			int pos = lvHomeObject.getPositionForView(v);

			HomeCategory item = null;
			if (!isLandscape) {
				item = (HomeCategory) mListHomeObject.get(pos);
			} else {
				item = (HomeCategory) mListHomeObjectLand.get(pos);
			}
			switch (item.getType()) {
				case -2:
					bundle = new Bundle();
					bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, -2);
					bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV, item.getId());
					bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV,
							item.getNameCategory());
					mVtvFragment = new VtvPlusFragment();
					mVtvFragment.setArguments(bundle);
					baseSlideMenuActivity.switchContent(mVtvFragment, true);
					break;
				case 0:
					// channel
					bundle = new Bundle();
					bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 0);
					bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV, item.getId());
					bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV,
							item.getNameCategory());
					mVtvFragment = new VtvPlusFragment();
					mVtvFragment.setArguments(bundle);
					baseSlideMenuActivity.switchContent(mVtvFragment, true);
					break;
				case 1:
					// vod
					bundle = new Bundle();
					bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 1);
					bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV, item.getId());
					bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV,
							item.getNameCategory());
					mVtvFragment = new VtvPlusFragment();
					mVtvFragment.setArguments(bundle);
					baseSlideMenuActivity.switchContent(mVtvFragment, true);
					break;
				case -1:
					bundle = new Bundle();
					bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, -1);
					bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV, item.getId());
					bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV,
							item.getNameCategory());
					mVtvFragment = new VtvPlusFragment();
					mVtvFragment.setArguments(bundle);
					baseSlideMenuActivity.switchContent(mVtvFragment, true);
					break;
				case -3:
					bundle = new Bundle();
					bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, -3);
					bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV, item.getId());
					bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV,
							item.getNameCategory());
					mVtvFragment = new VtvPlusFragment();
					mVtvFragment.setArguments(bundle);
					baseSlideMenuActivity.switchContent(mVtvFragment, true);
					break;
				default:
					break;
			}
		}
	};

	private OnClickListener firstClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int pos = lvHomeObject.getPositionForView((View) v.getParent());
			ItemVtvPlusInfo info = null;
			if (!isLandscape) {
				info = ((HomeItemListview) mListHomeObject.get(pos))
						.getItemLeft();
			} else {
				info = ((HomeItemLandListview) mListHomeObjectLand.get(pos))
						.getItemFirst();
			}

			actionNextFragment(info);
		}
	};

	private OnClickListener secondClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int pos = lvHomeObject.getPositionForView((View) v.getParent());
			ItemVtvPlusInfo info = null;
			if (!isLandscape) {
				info = ((HomeItemListview) mListHomeObject.get(pos))
						.getItemRight();
			} else {
				info = ((HomeItemLandListview) mListHomeObjectLand.get(pos))
						.getItemSecond();
			}
			actionNextFragment(info);
		}
	};

	private OnClickListener thirdClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int pos = lvHomeObject.getPositionForView((View) v.getParent());
			ItemVtvPlusInfo info = null;
			info = ((HomeItemLandListview) mListHomeObjectLand.get(pos))
					.getItemThird();
			actionNextFragment(info);
		}
	};

	private OnClickListener fourthClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int pos = lvHomeObject.getPositionForView((View) v.getParent());
			ItemVtvPlusInfo info = null;
			info = ((HomeItemLandListview) mListHomeObjectLand.get(pos))
					.getItemFourth();
			actionNextFragment(info);
		}
	};

	private void actionNextFragment(ItemVtvPlusInfo info) {
		if (info.getTypeItem() == 0) {
			actionChannel(info);
		} else if (info.getTypeItem() == 2) {
			actionEpisode(info);
		} else if (info.getTypeItem() == 3) {
			actionAds(info);
		} else {
			actionVod(info);
		}
	}

	private void actionChannel(ItemVtvPlusInfo info) {
		ChannelInfo channelInfo = (ChannelInfo) info;
		Bundle bundle = new Bundle();
		bundle.putInt(TYPE, info.getTypeItem());
		bundle.putParcelable(TYPE_CHANNEL, channelInfo);
		PlayerFragment playerFragment = new PlayerFragment();
		playerFragment.setArguments(bundle);
		baseSlideMenuActivity.switchContent(playerFragment, true);

//		ApiManager.callUpdateViewChannel(baseSlideMenuActivity,
//				channelInfo.getId());

		// Google analytics track channel
		baseSlideMenuActivity.trackEvent(
				baseSlideMenuActivity.getString(R.string.track_channel),
				channelInfo.getName(), "");
		baseSlideMenuActivity.trackView("Channel " + channelInfo.getName());
	}

	private void actionEpisode(ItemVtvPlusInfo info) {
		EpisodeInfo episodeInfo = (EpisodeInfo) info;
		Bundle bundle = new Bundle();
		bundle.putInt(TYPE, info.getTypeItem());
		bundle.putParcelable(TYPE_EPISODE, episodeInfo);
		PlayerFragment playerFragment = new PlayerFragment();
		playerFragment.setArguments(bundle);
		baseSlideMenuActivity.switchContent(playerFragment, true);

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
			baseSlideMenuActivity.switchContent(mVtvFragment, true);
		} else {
			// play video vod
			Bundle bundle = new Bundle();
			bundle.putInt(TYPE, info.getTypeItem());
			bundle.putParcelable(TYPE_VOD, vodInfo);
			PlayerFragment playerFragment = new PlayerFragment();
			playerFragment.setArguments(bundle);
			baseSlideMenuActivity.switchContent(playerFragment, true);
		}

		ApiManager.callUpdateViewVod(baseSlideMenuActivity, vodInfo.getId());
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
		if (urlPoster.length() > 0) {
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
						public void onLoadingComplete(String imageUri,
						                              View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							baseSlideMenuActivity.getImageAds().setVisibility(
									View.VISIBLE);
							baseSlideMenuActivity.getImageAds().startAnimation(
									mAnimation);
							DialogManager.closeProgressDialog();
						}

						@Override
						public void onLoadingCancelled(String imageUri,
						                               View view) {
							// TODO Auto-generated method stub
							Toast.makeText(
									baseSlideMenuActivity,
									"Ä�Ã£ xáº£y ra lá»—i. Vui lÃ²ng thá»­ láº¡i.",
									Toast.LENGTH_SHORT).show();
							DialogManager.closeProgressDialog();
						}
					}, baseSlideMenuActivity.getImageAds());
		}
	}

	private void actionAdsVideo(AdsInfo info) {
		AdsVideoFragment fr = new AdsVideoFragment();
		Bundle bundle = new Bundle();
		bundle.putString(AdsVideoFragment.XURL_VIDEO, info.getUrl());
		fr.setArguments(bundle);
		baseSlideMenuActivity.switchContent(fr, true);
	}

	private OnClickListener mItemBannerClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int pos = galHomeBanner.getPositionForView(v);
			BannerInfo info = mListBanner.get(pos);
			if (info.getLinkBanner() != null
					&& info.getLinkBanner().length() > 0) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(info.getLinkBanner()));
				startActivity(i);
			}
		}
	};

	/**
	 * *************************************************************************
	 * <p/>
	 * Callback login, get info
	 * <p/>
	 * *************************************************************************
	 */
	private IApiCallback callbackLogin = new IApiCallback() {

		@Override
		public void responseSuccess(String response) {
			if (baseSlideMenuActivity == null) {
				return;
			}
			UserInfo info = ParserManager.parserLoginInfo(response);
			if (info.isGetSucc()) {
//				baseSlideMenuActivity.pProfile.pSession = info.getMessage();
				baseSlideMenuActivity.pProfile.pSession = info
						.getSession();
				
				if (info.isRegister()) {
					baseSlideMenuActivity.pProfile.pTypePack = 1;
					baseSlideMenuActivity.pProfile.pIdChannel = info
							.getLiveChannel();
					baseSlideMenuActivity.pProfile.pNamePack = info
							.getCurrentService();
					baseSlideMenuActivity.pProfile.pDayPack = info
							.getExpiryDate();
//					baseSlideMenuActivity.pProfile.pSession = info
//							.getSession();
				} else {
					baseSlideMenuActivity.pProfile.pTypePack = 2;
				}
				
				baseSlideMenuActivity.mMenuFragment.initLayout();
//				baseSlideMenuActivity.callAccountInfoApi(callBackGetInfoAccount);
//				if (TextUtils.isEmpty(baseSlideMenuActivity.pProfile.pSession)) {
//					HLog.d("callLoginApi in BaseSlideMenuActivity has empty login session");
//					return;
//				}
//				HLog.i("callbackLogin success " + baseSlideMenuActivity.pProfile.pSession);
//				String operatorCode = DeviceUtil.getTelcoCode(baseSlideMenuActivity);
//				if (!BaseSlideMenuActivity.DEBUG && TextUtils.isEmpty(operatorCode)) {
//					return;
//				}
//				baseSlideMenuActivity.callCheckChargeEnable();
			} else {
				baseSlideMenuActivity.logoutAccount();
				callListHomeObject();
			}
		}

		@Override
		public void responseFailWithCode(int statusCode) {
			// TODO Auto-generated method stub
			DialogManager.alert(baseSlideMenuActivity,
					getString(R.string.network_fail));
		}
	};

	private IApiCallback callBackGetInfoAccount = new IApiCallback() {

		@Override
		public void responseSuccess(String response) {
			// TODO Auto-generated method stub
			UserInfo info = ParserManager.parserUserInfo(response);
			if (info.isGetSucc()) {
				if (info.isRegister()) {
					baseSlideMenuActivity.pProfile.pTypePack = 1;
					baseSlideMenuActivity.pProfile.pIdChannel = info
							.getLiveChannel();
					baseSlideMenuActivity.pProfile.pNamePack = info
							.getCurrentService();
					baseSlideMenuActivity.pProfile.pDayPack = info
							.getExpiryDate();
					callListHomeObject();
				} else {
					baseSlideMenuActivity.pProfile.pTypePack = 2;
					callListHomeObject();
				}
			} else {
				callListHomeObject();
			}
		}

		@Override
		public void responseFailWithCode(int statusCode) {
			// TODO Auto-generated method stub
			DialogManager.alert(baseSlideMenuActivity,
					getString(R.string.network_fail));
		}
	};

	/**
	 * *************************************************************************
	 * <p/>
	 * Timer
	 * <p/>
	 * *************************************************************************
	 */
	private boolean goNext = true;

	public void scrollBanner() {
		try {
			if ((indicator == mListBanner.size() - 1) && goNext) {
				goNext = false;
			} else if ((indicator == 0) && !goNext) {
				goNext = true;
			}

			if (mListBanner.size() > 1) {
				if (goNext) {
					galHomeBanner
							.snapToScreen(galHomeBanner.getCurrentPage() + 1);
				} else {
					galHomeBanner
							.snapToScreen(galHomeBanner.getCurrentPage() - 1);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private final class LoadBannerTimer extends Timer {

		LoadBannerTimer() {
			super(200);
		}

		@Override
		protected boolean step(int count, long time) {
			loadBanner();
			return false;
		}

	}

	public void loadBanner() {
		if (HEIGHT_BANNER != 0) {
			loadBannerTimer.stop();

			ViewGroup.LayoutParams params = galHomeBanner.getLayoutParams();
			params.height = HEIGHT_BANNER;
			galHomeBanner.setLayoutParams(params);

			isRunBanner = true;
			mTimeBanner = SystemClock.uptimeMillis();
			try {

				mThreadBanner.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Runnable mRunBanner = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (isRunBanner) {
				long now = SystemClock.uptimeMillis();
				if (now - mTimeBanner >= 3500) {
					mTimeBanner = now;
					baseSlideMenuActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							scrollBanner();
						}
					});
				} else {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	};

	/**
	 * ****************************************************
	 * <p/>
	 * Version
	 * <p/>
	 * ****************************************************
	 */

	// TODO hieuxit checked code here to remove update dialog
	private void getInfoApp() {
		PackageInfo pInfo;
		String version = "0";
		try {
			pInfo = baseSlideMenuActivity.getPackageManager().getPackageInfo(
					baseSlideMenuActivity.getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ApiManager.callGetInfoApp(baseSlideMenuActivity, new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				HLog.i("info app: " + response);
				// TODO Auto-generated method stub
				ParserManager.parserGetInfoApp(response,
						baseSlideMenuActivity.pVersion);
//				if (baseSlideMenuActivity.pVersion.isStatus()) {
//					DialogManager.alertWith2Status(baseSlideMenuActivity,
//							baseSlideMenuActivity
//									.getString(R.string.dialog_update_version),
//							new DialogInterface.OnClickListener() {
//
//								@Override
//								public void onClick(DialogInterface dialog,
//								                    int which) {
//									IntentUtil.openAppStore(
//											baseSlideMenuActivity,
//											baseSlideMenuActivity
//													.getPackageName());
//								}
//							});
//				}

				if (baseSlideMenuActivity.pVersion.getLogoApp().length() > 0) {
					ImageUtility.loadBitmapFromUrl(baseSlideMenuActivity,
							baseSlideMenuActivity.pVersion.getLogoApp(),
							baseSlideMenuActivity.iconVtvPlus);
				}

				// show policy
//				if (sharedPreferenceManager.isShowPolicy()) {
//					new PolicyDialog(baseSlideMenuActivity,
//							baseSlideMenuActivity.pVersion.getPolicy());
//				}

			}

			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub

			}
		}, version);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tab_video:
			Fragment videoFragment = new CategoryVideoFragment();
			baseSlideMenuActivity.switchContent(videoFragment, true);
			break;

		case R.id.tab_channel:
			Bundle bundle = new Bundle();
			bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 0);
			bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV, getString(R.string.slidemenu_channel));
			VtvPlusFragment mVtvFragment = new VtvPlusFragment();
			mVtvFragment.setArguments(bundle);
			baseSlideMenuActivity.switchContent(mVtvFragment, true);
			break;
			
		case R.id.tab_event:
			Fragment eventFragment = new ListCategoryInEventFragment();
			baseSlideMenuActivity.switchContent(eventFragment, true);
		default:
			break;
		}
	}

}
