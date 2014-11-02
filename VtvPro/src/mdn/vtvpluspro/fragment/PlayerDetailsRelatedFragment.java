package mdn.vtvpluspro.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import mdn.vtvplus.R;
import mdn.vtvpluspro.BaseSlideMenuActivity;
import mdn.vtvpluspro.adapter.VtvPlusAdapter;
import mdn.vtvpluspro.common.DialogManager;
import mdn.vtvpluspro.common.ParserManager;
import mdn.vtvpluspro.fragment.PlayerFragment.DetailsInfo;
import mdn.vtvpluspro.network.ApiManager;
import mdn.vtvpluspro.network.IApiCallback;
import mdn.vtvpluspro.object.ItemVtvPlusInfo;
import mdn.vtvpluspro.util.DeviceUtil;

import java.util.ArrayList;

public class PlayerDetailsRelatedFragment extends BaseFragmentPlayerDetail
		implements OnClickListener {

	private LinearLayout layLoadding;
	private GridView grdRelatedChannel;
	private ArrayList<ItemVtvPlusInfo> listItemVtvPlusInfo;
	private VtvPlusAdapter mAdapter;
	private int startIndex = 0;
	private int limit = 20;

	private boolean isShow = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isShow = true;

		View view = inflater.inflate(R.layout.fragment_player_details_related,
				null);
		grdRelatedChannel = (GridView) view
				.findViewById(R.id.grdRelatedChannel);
		layLoadding = (LinearLayout) view.findViewById(R.id.layLoading);
		layLoadding.setVisibility(View.GONE);
		initData();

		return view;
	}

	private void initData() {
		if (listItemVtvPlusInfo != null && listItemVtvPlusInfo.size() > 0) {
			if (mAdapter == null) {
				mAdapter = new VtvPlusAdapter(baseSlideMenuActivity,
						itemClickListener, listItemVtvPlusInfo);
				mAdapter.updateSizeItem(2);
			}

			if (grdRelatedChannel.getAdapter() == null) {
				grdRelatedChannel.setAdapter(mAdapter);
			} else {
				mAdapter.notifyDataSetChanged();
			}
		} else {
			callRelatedChannel();
		}
	}

	public void callRelatedChannel() {
		String idChannel = null;
		if (mPlayerFragment.itemInfo != null) {
			idChannel = mPlayerFragment.itemInfo.getId();
		} else {
			idChannel = BaseSlideMenuActivity.gcmBundleId;
		}

		ApiManager.callRelationItemVtvPlus(
				baseSlideMenuActivity,
				new IApiCallback() {

					@Override
					public void responseSuccess(String response) {
						// TODO Auto-generated method stub
						if (!isShow) {
							return;
						}
						if (listItemVtvPlusInfo == null) {
							listItemVtvPlusInfo = new ArrayList<ItemVtvPlusInfo>();
						}
						ArrayList<ItemVtvPlusInfo> arr = null;
						arr = ParserManager.parserListRelation(response,
								mPlayerFragment.type);
						listItemVtvPlusInfo.addAll(arr);

						if (mAdapter == null
								|| grdRelatedChannel.getAdapter() == null) {
							mAdapter = new VtvPlusAdapter(
									baseSlideMenuActivity, itemClickListener,
									listItemVtvPlusInfo);
							mAdapter.updateSizeItem(2);
							grdRelatedChannel.setAdapter(mAdapter);
						} else {
							mAdapter.notifyDataSetChanged();
						}
						startIndex += arr.size();
					}

					@Override
					public void responseFailWithCode(int statusCode) {
						if (!isShow) {
							return;
						}
						DialogManager.alert(baseSlideMenuActivity,
								baseSlideMenuActivity
										.getString(R.string.network_fail));
					}
				}, DeviceUtil.getDeviceId(baseSlideMenuActivity), idChannel,
				HomeFragment.APP_NAME, limit, startIndex, mPlayerFragment.type,
				baseSlideMenuActivity.pProfile.pUserName, false);
	}

	public void updateView() {
		if (mAdapter == null || grdRelatedChannel.getAdapter() == null) {
			mAdapter = new VtvPlusAdapter(baseSlideMenuActivity,
					itemClickListener, listItemVtvPlusInfo);
			mAdapter.updateSizeItem(2);
			grdRelatedChannel.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private OnClickListener itemClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int pos = grdRelatedChannel.getPositionForView(v);
			ItemVtvPlusInfo tmpItemInfo = listItemVtvPlusInfo.get(pos);
			if (mPlayerFragment.type == 0) {
				mPlayerFragment.itemInfo = tmpItemInfo;

				// Update view channel
				ApiManager.callUpdateViewChannel(baseSlideMenuActivity,
						mPlayerFragment.itemInfo.getId());

				// Google analytics track channel
				baseSlideMenuActivity
						.trackEvent(baseSlideMenuActivity
								.getString(R.string.track_channel),
								mPlayerFragment.itemInfo.getName(), "");
				baseSlideMenuActivity.trackView("Channel "
						+ mPlayerFragment.itemInfo.getName());
			} else if (mPlayerFragment.type == 1) {
				mPlayerFragment.itemInfo = tmpItemInfo;

				// Update view vod
				ApiManager.callUpdateViewVod(baseSlideMenuActivity,
						mPlayerFragment.itemInfo.getId());

				// Google analytics track vod
				baseSlideMenuActivity.trackEvent(
						baseSlideMenuActivity.getString(R.string.track_vod),
						mPlayerFragment.itemInfo.getName(), "");
				baseSlideMenuActivity.trackView("Video "
						+ mPlayerFragment.itemInfo.getName());
			} else {
				mPlayerFragment.itemInfo = tmpItemInfo;

				// Update view Episode
				ApiManager.callUpdateViewEpisode(baseSlideMenuActivity,
						mPlayerFragment.itemInfo.getId());
			}
			if (mPlayerFragment.mVideoView != null) {
				mPlayerFragment.mVideoView.stopPlayback();
			}
			mPlayerFragment.updateViewPlayer();
			if (mPlayerFragment.layoutLogin.getVisibility() == View.GONE) {
				// layout login gone -> layout player visible -> bind new player
				int btnCharge = mPlayerFragment.btChargePackage.getVisibility();
				mPlayerFragment.bindPlayerView(mPlayerFragment.playerDetails);
				mPlayerFragment.btChargePackage.setVisibility(btnCharge);
			}
//			PlayerDetailsAboutFragment fr = mPlayerFragment.getFragmentAbout();
//			if (fr != null) {
//				fr.getDetailsInfoByType();
//			}

			
//			if (mPlayerFragment.type == 0) {
//				PlayerDetailsScheduleFragment frSche = mPlayerFragment
//						.getFragmentSchedule();
//				if (frSche != null) {
//					if (frSche.adapter != null) {
//						frSche.adapter.clear();
//						frSche.adapter = null;
//					}
//					frSche.callListSchedule();
//				}
//			}

			listItemVtvPlusInfo.clear();
			startIndex = 0;
			callRelatedChannel();
			
			mPlayerFragment.layInfoPager.setCurrentItem(0);
			mPlayerFragment.showDetailsInfo(DetailsInfo.RELATED);
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		isShow = false;
		if (listItemVtvPlusInfo != null) {
			listItemVtvPlusInfo.clear();
		}
		startIndex = 0;
		listItemVtvPlusInfo = null;
		mAdapter = null;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

		isShow = false;
	}

}
