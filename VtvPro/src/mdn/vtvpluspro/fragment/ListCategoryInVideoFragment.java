package mdn.vtvpluspro.fragment;

import java.util.ArrayList;

import com.handmark.pulltorefreshscrollview.library.PullToRefreshBase;
import com.handmark.pulltorefreshscrollview.library.PullToRefreshScrollView;
import com.handmark.pulltorefreshscrollview.library.PullToRefreshBase.OnRefreshListener2;
import com.nostra13.universalimageloader.core.ImageLoader;

import mdn.vtvplus.R;
import mdn.vtvpluspro.adapter.VtvPlusAdapter;
import mdn.vtvpluspro.common.DialogManager;
import mdn.vtvpluspro.common.ParserManager;
import mdn.vtvpluspro.network.ApiManager;
import mdn.vtvpluspro.network.IApiCallback;
import mdn.vtvpluspro.object.ItemVtvPlusInfo;
import mdn.vtvpluspro.object.VodInfo;
import mdn.vtvpluspro.util.DeviceUtil;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

public class ListCategoryInVideoFragment extends BaseFragment {

	public static final String KEY_ID_ITEM_VTV = "_tvplus_id_item";
	private int mTypeCategory = 0; //0_channel,1_vod, 2_series of vod, 3_search,
	// -2 favourist; -1 listnew; -3 listhot
	
	private String mNameCategory = "";
	private String mIdCategory = ""; // luu ca tu can search vao day
	private boolean isGetFullList = false;

	private Animation mAnimation;
	private int startIndex;
	private int offset = 10;
	private ArrayList<ItemVtvPlusInfo> listVtvPlus;
	private VtvPlusAdapter mAdapter;
	private PullToRefreshScrollView layChannelScreen;
	private GridView gridChannel;
	private TextView tvChannel;
	private TextView tvNoItem;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startIndex = 0;
		isGetFullList = false;
		Bundle bundle = getArguments();
		if (bundle != null) {
//			mTypeCategory = bundle.getInt(KEY_TYPE_ITEM_VTV);
			mIdCategory = bundle.getString(KEY_ID_ITEM_VTV);
//			mNameCategory = bundle.getString(KEY_NAME_ITEM_VTV);
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

		View view = inflater.inflate(R.layout.fragment_list_category_in_video, container,
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
		tvNoItem = (TextView) view.findViewById(R.id.tvNoItemSearch);
		tvNoItem.setVisibility(View.GONE);
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

		baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
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

	private int getNumberRowGridView() {
		int count = 0;
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			count = 2;
		} else {
			count = 4;
		}
		return count;
	}

	private void bindAction() {
		getListChannel();
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

	/****************************************************************************
	 * 
	 * Call data
	 * 
	 ***************************************************************************/
	private void callListItemVtvPlus() {
			callListItemOfCategory();
	}
	
	public void callListItemOfCategory() {

		ApiManager.callListItemOfCategory(baseSlideMenuActivity, mCallBack,
				mIdCategory, startIndex,
				baseSlideMenuActivity.pProfile.pUserName, true);
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
			
			actionVod(info);
			
		}
	};
	
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
	
}
