package mdn.vtvsport.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import mdn.vtvsport.BaseSlideMenuActivity;
import mdn.vtvsport.R;
import mdn.vtvsport.alarm.AlarmManagerBroadcastReceiver;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ImageUtility;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.dialogs.DateTimePickerDialog;
import mdn.vtvsport.dialogs.DateTimePickerDialog.DateTimePickerDialogListener;
import mdn.vtvsport.facebook.FacebookUtil;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.network.WebServiceConfig;
import mdn.vtvsport.object.ChannelInfo;
import mdn.vtvsport.object.EpisodeInfo;
import mdn.vtvsport.object.StreamInfo;
import mdn.vtvsport.object.VodInfo;
import mdn.vtvsport.util.StringUtil;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerDetailsAboutFragment extends BaseFragmentPlayerDetail
		implements OnClickListener {
	private LinearLayout playerDetailsSchedule;
	private ImageView detailsLikeChannel;
	private ImageView detailsAlarmChannel;
	private ImageView detailsFavoriteChannel;
	private ImageView detailsShareChannel;
	private ImageView detailsChannelIcon;
	private TextView detailsChannelView;
	private TextView detailsChannelLike;
	private TextView detailsChannelName;
	private TextView detailsTextShedule;
	private TextView[] listStreamText;
	private DateTimePickerDialog dateTimePickerDialog;
	private AlarmManagerBroadcastReceiver alarm;
	public LinearLayout layoutListStream;
	public final static int temp = 1000;

	private FacebookUtil mFacebookUtil;
	private boolean isView = true;
	
	public PlayerDetailsAboutFragment() {
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		alarm = new AlarmManagerBroadcastReceiver();
		mFacebookUtil = FacebookUtil.newInstance(baseSlideMenuActivity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		isView = true;
		playerDetailsSchedule = (LinearLayout) inflater.inflate(
				R.layout.fragment_player_details_about, null);
		layoutListStream = (LinearLayout) playerDetailsSchedule
				.findViewById(R.id.layoutListStream);
		detailsAlarmChannel = (ImageView) playerDetailsSchedule
				.findViewById(R.id.detailsAlarmChannel);
		detailsFavoriteChannel = (ImageView) playerDetailsSchedule
				.findViewById(R.id.detailsFavoriteChannel);
		detailsShareChannel = (ImageView) playerDetailsSchedule
				.findViewById(R.id.detailsShareChannel);
		detailsChannelIcon = (ImageView) playerDetailsSchedule
				.findViewById(R.id.detailsChannelIcon);
		detailsChannelView = (TextView) playerDetailsSchedule
				.findViewById(R.id.detailsChannelView);
		detailsChannelLike = (TextView) playerDetailsSchedule
				.findViewById(R.id.detailsChannelLike);
		detailsChannelName = (TextView) playerDetailsSchedule
				.findViewById(R.id.detailsChannelName);
		detailsTextShedule = (TextView) playerDetailsSchedule
				.findViewById(R.id.detailsTextShedule);
		detailsLikeChannel = (ImageView) playerDetailsSchedule
				.findViewById(R.id.detailsLikeChannel);
		detailsLikeChannel.setOnClickListener(this);
		detailsAlarmChannel.setOnClickListener(this);
		detailsFavoriteChannel.setOnClickListener(this);
		detailsShareChannel.setOnClickListener(this);

		if (mPlayerFragment.arrStream != null && mPlayerFragment.arrStream.size() > 0) {
			buildLayoutListStream(mPlayerFragment.arrStream);
		}
		
		if (mPlayerFragment.itemInfo != null) {
			getDetailsInfoByType();
		} else if (BaseSlideMenuActivity.gcmBundleId != null && BaseSlideMenuActivity.gcmBundleId.length() > 0) {
			callDetailsInfo(BaseSlideMenuActivity.gcmBundleId, mPlayerFragment.type);
		}

		return playerDetailsSchedule;
	}

	private void callDetailsInfo(String itemId, final int type) {

		ApiManager.callRelationItemVtvPlus(
				baseSlideMenuActivity,
				new IApiCallback() {

					@Override
					public void responseSuccess(String response) {
						// TODO Auto-generated method stub
						if (!isView) {
							return;
						}
						BaseSlideMenuActivity.gcmBundleId = "";
						mPlayerFragment.itemInfo = ParserManager.parserItemVtvPlusDetailsInfo(response, type);
						getDetailsInfoByType();
						mPlayerFragment.updateViewPlayer();
					}

					@Override
					public void responseFailWithCode(int statusCode) {
						if (!isView) {
							return;
						}
						DialogManager.alert(baseSlideMenuActivity,
								baseSlideMenuActivity
										.getString(R.string.network_fail));
					}
				}, Secure.getString(baseSlideMenuActivity.getContentResolver(),
						Secure.ANDROID_ID), itemId, HomeFragment.APP_NAME, 0,
				0, type, baseSlideMenuActivity.pProfile.pUserName, false);
	}
	
	public void getDetailsInfoByType() {
		detailsChannelName.setText(mPlayerFragment.itemInfo.getName());
		detailsChannelView.setText(mPlayerFragment.itemInfo.getView() + " ");
		detailsChannelLike.setText(" | " + mPlayerFragment.itemInfo.getCountLike() + " ");

		if (mPlayerFragment.type == 0) {
			ImageUtility.loadBitmapFromUrl(baseSlideMenuActivity,
					((ChannelInfo) mPlayerFragment.itemInfo)
							.getIconSmall(), detailsChannelIcon);
		} else if (mPlayerFragment.type == 1) {
			ImageUtility.loadBitmapFromUrl(baseSlideMenuActivity,
					((VodInfo) mPlayerFragment.itemInfo).getImage(),
					detailsChannelIcon);
		} else {
			ImageUtility
					.loadBitmapFromUrl(baseSlideMenuActivity,
							((EpisodeInfo) mPlayerFragment.itemInfo)
									.getImage(), detailsChannelIcon);
		}

		boolean isFavorite = mPlayerFragment.itemInfo.isFavorites();
		if (isFavorite) {
			detailsFavoriteChannel
					.setBackgroundResource(R.drawable.icon_favorite_active);
		} else {
			detailsFavoriteChannel
					.setBackgroundResource(R.drawable.icon_favorite);
		}
		
		boolean isLike = mPlayerFragment.itemInfo.isLike();
		if (isLike) {
			detailsLikeChannel
					.setBackgroundResource(R.drawable.icon_like_active);
		} else {
			detailsLikeChannel
					.setBackgroundResource(R.drawable.icon_like);
		}

		String scheduleText = "";
		try {
			scheduleText = sharedPreferenceManager.getScheduleText(temp
					+ getItemVtvPlusId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (scheduleText.equals("")) {
			detailsTextShedule.setVisibility(View.GONE);
			detailsAlarmChannel.setBackgroundResource(R.drawable.icon_clock);
		} else {
			if (StringUtil.convertStringToDate(
					StringUtil.TIMESTAMP_DATE_FORMAT_CONVERT, scheduleText)
					.after(new Date())) {
				detailsTextShedule.setVisibility(View.VISIBLE);
				detailsAlarmChannel
						.setBackgroundResource(R.drawable.icon_clock_active);
				detailsTextShedule.setText(String.format(baseSlideMenuActivity
						.getString(R.string.details_info_schedule_text),
						scheduleText));
			}

		}
	}

	public void buildLayoutListStream(final ArrayList<StreamInfo> arrStream) {
		final int size = arrStream.size();
		listStreamText = new TextView[size];
		for (int i = 0; i < size; i++) {
			TextView server = (TextView) baseSlideMenuActivity
					.getLayoutInflater().inflate(
							R.layout.layout_player_details_list_server, null,
							true);

			layoutListStream.addView(server);
			server.setText("Server " + (i + 1));
			if (i == 0) {
				server.setTextColor(baseSlideMenuActivity.getResources()
						.getColor(R.color.baset_title_divider_color));
			} else {
				server.setTextColor(baseSlideMenuActivity.getResources()
						.getColor(android.R.color.black));
			}
			server.setId(i);
			listStreamText[i] = server;
			server.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PlayerFragment.isServerClick = true;
					mPlayerFragment.serverPosition = v.getId();
					// rebind a new player view
					int btnCharge = mPlayerFragment.btChargePackage
							.getVisibility();
					mPlayerFragment
							.bindPlayerView(mPlayerFragment.playerDetails);
					mPlayerFragment.btChargePackage.setVisibility(btnCharge);
					mPlayerFragment.playVideoView(arrStream.get(
							mPlayerFragment.serverPosition).getUrl(), mPlayerFragment.serverPosition);
					changeServer(v.getId(), size);
				}
			});
		}
	}

	public void changeServer(int id, int size) {
		for (int i = 0; i < size; i++) {
			if (i == id) {
				listStreamText[i].setTextColor(baseSlideMenuActivity
						.getResources().getColor(
								R.color.baset_title_divider_color));
			} else {
				listStreamText[i].setTextColor(baseSlideMenuActivity
						.getResources().getColor(android.R.color.black));
			}
		}
	}

	public int getItemVtvPlusId() {
		String id = "0";
		id = mPlayerFragment.itemInfo.getId();
		return Integer.parseInt(id);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.detailsShareChannel:
			String name = "";
			String desc = "";
			String icon = "";
			name = mPlayerFragment.itemInfo.getName();
			if (mPlayerFragment.type == 0) {
				desc = ((ChannelInfo) mPlayerFragment.itemInfo)
						.getDes();
				icon = ((ChannelInfo) mPlayerFragment.itemInfo)
						.getIcon();
			} else if (mPlayerFragment.type == 1) {
				desc = ((VodInfo) mPlayerFragment.itemInfo).getDes();
				icon = ((VodInfo) mPlayerFragment.itemInfo).getImage();
			} else {
				desc = ((EpisodeInfo) mPlayerFragment.itemInfo)
						.getName();
				icon = ((EpisodeInfo) mPlayerFragment.itemInfo)
						.getImage();
			}
			if (name.equals("")) {
				name = HomeFragment.APP_NAME_1;
			} else if (desc.equals("")) {
				desc = HomeFragment.APP_NAME_1;
			} else if (icon.equals("")) {
				icon = WebServiceConfig.urlImageDefault;
			}
			mFacebookUtil.requestPostFeed(name, "Tải "
					+ HomeFragment.APP_NAME_1 + " trên Android tại: "
					+ "https://play.google.com/store/apps/details?id="
					+ baseSlideMenuActivity.getPackageName(), desc,
					"https://play.google.com/store/apps/details?id="
							+ baseSlideMenuActivity.getPackageName(), icon);

			break;

		case R.id.detailsAlarmChannel:
			showDialogDatePicker();
			break;

		case R.id.detailsFavoriteChannel:
			actionFavourist();			
			break;
		case R.id.detailsLikeChannel:
			actionLike();
			break;

		default:
			break;
		}
	}

	private void showDialogDatePicker() {
		removePreviousDialog("date_picker");
		dateTimePickerDialog = DateTimePickerDialog.newInstance(
				baseSlideMenuActivity, new DateTimePickerDialogListener() {

					@Override
					public void onDone(String dateTime) {
						String dateTimeConvert = StringUtil.convertDateFormat(
								dateTime, StringUtil.TIMESTAMP_DATE_FORMAT,
								StringUtil.TIMESTAMP_DATE_FORMAT_CONVERT);
						String schedule = String.format(
								baseSlideMenuActivity
										.getString(R.string.details_info_schedule_text),
								dateTimeConvert);
						detailsTextShedule.setText(schedule);
						detailsTextShedule.setVisibility(View.VISIBLE);
						detailsAlarmChannel
								.setBackgroundResource(R.drawable.icon_clock_active);
						Calendar c = StringUtil.convertStringToCalendar(
								dateTime, StringUtil.TIMESTAMP_DATE_FORMAT);
						if (alarm != null) {
							alarm.setOnetimeTimer(baseSlideMenuActivity,
									c.getTimeInMillis(),
									saveDetailsSchedule(dateTimeConvert));
						}
					}
				});
		dateTimePickerDialog.show(mFragmentManager, "date_picker");
	}

	private int saveDetailsSchedule(String schedule) {
		int id = -1;
		id = Integer.parseInt(mPlayerFragment.itemInfo.getId());
		sharedPreferenceManager.setItemVtvPlusInfo(
				mPlayerFragment.itemInfo, temp + id,
				mPlayerFragment.type);
		sharedPreferenceManager.setScheduleName(
				mPlayerFragment.itemInfo.getName(), temp + id);
		sharedPreferenceManager.setVtvType(mPlayerFragment.type, temp
				+ id);
		sharedPreferenceManager.setScheduleText(schedule, temp + id);

		return id;
	}

	private void actionLike() {
		if (baseSlideMenuActivity.pProfile.pUserName == null
			|| baseSlideMenuActivity.pProfile.pUserName.length() == 0) {
			Toast.makeText(baseSlideMenuActivity, getString(R.string.no_login), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (mPlayerFragment.itemInfo.isLike()) {
			Toast.makeText(baseSlideMenuActivity, getString(R.string.liked), Toast.LENGTH_SHORT).show();
			return;
		}
		
		ApiManager.callActionLike(baseSlideMenuActivity, new IApiCallback() {
			
			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				boolean isSuccess = ParserManager.parserLike(response);
				if (isSuccess) {
					detailsLikeChannel.setBackgroundResource(R.drawable.icon_like_active);
					HomeFragment.IS_CHANGE_DATA = true;
				} else {
					Toast.makeText(baseSlideMenuActivity, getString(R.string.not_like_sucess), Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				DialogManager.alert(baseSlideMenuActivity,
						getString(R.string.network_fail));
			}
		}, mPlayerFragment.type, mPlayerFragment.itemInfo.getId(), baseSlideMenuActivity.pProfile.pUserName);
	}
	
	private void actionFavourist() {
		if (baseSlideMenuActivity.pProfile.pUserName == null
			|| baseSlideMenuActivity.pProfile.pUserName.length() == 0) {
			Toast.makeText(baseSlideMenuActivity, getString(R.string.no_login), Toast.LENGTH_SHORT).show();
			return;
		}
		ApiManager.callActionFavourist(baseSlideMenuActivity, new IApiCallback() {
			
			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				int result = ParserManager.parserFavourist(response);
				if (result == 1) {
					detailsFavoriteChannel.setBackgroundResource(R.drawable.icon_favorite_active);
					HomeFragment.IS_CHANGE_DATA = true;
				} else if (result == 2) {
					detailsFavoriteChannel.setBackgroundResource(R.drawable.icon_favorite);
					HomeFragment.IS_CHANGE_DATA = true;
				} else {
					Toast.makeText(baseSlideMenuActivity, getString(R.string.not_like_sucess), Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				DialogManager.alert(baseSlideMenuActivity,
						getString(R.string.network_fail));
			}
		}, mPlayerFragment.type, mPlayerFragment.itemInfo.getId(), baseSlideMenuActivity.pProfile.pUserName);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		isView = false;
		listStreamText = null;
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		isView = false;
	}

}
