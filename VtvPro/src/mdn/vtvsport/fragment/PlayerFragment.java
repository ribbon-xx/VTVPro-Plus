package mdn.vtvsport.fragment;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.widget.VideoView.MediaFunctionExtra;

import java.util.ArrayList;

import mdn.vtvsport.BaseSlideMenuActivity;
import mdn.vtvsport.R;
import mdn.vtvsport.adapter.PlayerDetailsPagerAdapter;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ImageUtility;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.fragment.account.LoginFragment;
import mdn.vtvsport.fragment.account.RegisterFragment;
import mdn.vtvsport.fragment.account.WebviewChargeFragment;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.ChannelInfo;
import mdn.vtvsport.object.EpisodeInfo;
import mdn.vtvsport.object.ItemInteraction;
import mdn.vtvsport.object.ItemVtvPlusInfo;
import mdn.vtvsport.object.StreamInfo;
import mdn.vtvsport.object.VodInfo;
import mdn.vtvsport.object.account.UserInfo;
import mdn.vtvsport.util.DeviceUtil;
import mdn.vtvsport.util.HLog;
import mdn.vtvsport.util.Utils;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class PlayerFragment extends ChargingFragment implements OnInfoListener,
		OnBufferingUpdateListener, OnClickListener, OnPageChangeListener,
		OnTouchListener {

	public RelativeLayout playerDetails;
	private LinearLayout layoutInfo;
	private RelativeLayout tabRelated;
//	private RelativeLayout tabAbout;
//	private RelativeLayout tabSchedule;
	private View tabRelatedFocus;
//	private View tabAboutFocus;
//	private View tabScheduleFocus;
	private TextView tabRelatedText;
	private PlayerDetailsPagerAdapter mAdapter;

	private RelativeLayout layoutPlayer;
	public VideoView mVideoView;
	private ProgressBar pb;
	private TextView downloadRateView;
	private TextView loadRateView;
	private RelativeLayout layVolumeBrightness;
	private SeekBar seekBarVolumeBrightness;
	private ImageView iconVolumeBrightness;
	public boolean isStart = false;
	public int serverPosition = 0;
	public ArrayList<StreamInfo> arrStream = null;
	public static boolean isServerClick = false;

	public LinearLayout layoutLogin;

	public ViewPager layInfoPager;
	public int type = -1; // 0: channel; 1: vod; 2: eposide;
	public ItemVtvPlusInfo itemInfo = null;

	private AudioManager audioManager;
	private boolean isControlVolume = true;
	private int brightness;
	private ContentResolver cResolver;
	private Window window;
	private float mLastPositionY;
	private long mTimeTouchUp = 0;
	private Thread mThreadAudioBrightness;
	private boolean isRunAudioBrightness = false;

	private boolean isFullScreen = false;
	private boolean isLockScreen = true;

	public ArrayList<Fragment> listFragments = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		Utils.lookUp(getArguments());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isFullScreen = false;
		isLockScreen = true;

		// BaseSlideMenuActivity.backFromPlayerToHome = true;
		baseSlideMenuActivity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		playerDetails = (RelativeLayout) inflater.inflate(
				R.layout.fragment_player_details, null);
		baseSlideMenuActivity.getSlidingMenu().getmViewBehind()
				.setMarginThreshold(0);
		baseSlideMenuActivity.getLayoutTitle().setVisibility(View.VISIBLE);

		bindSecondScreen(playerDetails);
		bindAllViewExtra(playerDetails);
		bindPlayerView(playerDetails);
		initDataInfoPager();

		if (BaseSlideMenuActivity.gcmBundleId.equals("")
				|| (BaseSlideMenuActivity.gcmBundleId == null)) {
			updateViewPlayer();
		}

		// Google analytics
		baseSlideMenuActivity.trackEvent(baseSlideMenuActivity.getResources()
				.getString(R.string.app_name), baseSlideMenuActivity
				.getResources().getString(R.string.screen_details), "");
		baseSlideMenuActivity.trackView(baseSlideMenuActivity.getResources()
				.getString(R.string.screen_details));

		return playerDetails;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

		baseSlideMenuActivity.getSlidingMenu().getmViewBehind()
				.setMarginThreshold(baseSlideMenuActivity.widScrollMenu);

		// removeAllPager();
		// listFragments.clear();
		// listFragments = null;
		// mAdapter = null;
	}

	@Override
	public void actionInteract() {
		// TODO Auto-generated method stub
		super.actionInteract();

		callInteraction();
	}

	@Override
	protected void initUiTabbar() {
		// TODO Auto-generated method stub
		super.initUiTabbar();

		baseSlideMenuActivity.iconSetting.setVisibility(View.GONE);
		baseSlideMenuActivity.iconBack.setVisibility(View.VISIBLE);
		baseSlideMenuActivity.iconVtvPlus.setVisibility(View.GONE);
		baseSlideMenuActivity.closeViewSearch();
	}

	private void initData() {
		if (BaseSlideMenuActivity.scheduleId == 0) {
			if ((BaseSlideMenuActivity.gcmBundleId.equals(""))
					|| (BaseSlideMenuActivity.gcmBundleId == null)) {
				Bundle bundle = getArguments();
				if (bundle != null) {
					type = bundle.getInt(HomeFragment.TYPE);
					if (type == 0) {
						itemInfo = (ChannelInfo) bundle
								.getParcelable(HomeFragment.TYPE_CHANNEL);

					} else if (type == 1) {
						itemInfo = (VodInfo) bundle
								.getParcelable(HomeFragment.TYPE_VOD);
					} else {
						itemInfo = (EpisodeInfo) bundle
								.getParcelable(HomeFragment.TYPE_EPISODE);
					}
					HLog.d("Item info: " + itemInfo);
				}
			} else {
				if (BaseSlideMenuActivity.gcmBundleType
						.equalsIgnoreCase("channel")) {
					type = 0;
				} else {
					type = 2;
				}
			}
		} else {
			type = sharedPreferenceManager
					.getVtvType(PlayerDetailsAboutFragment.temp
							+ BaseSlideMenuActivity.scheduleId);
			int index = PlayerDetailsAboutFragment.temp
					+ BaseSlideMenuActivity.scheduleId;
			itemInfo = sharedPreferenceManager.getItemVtvPlusInfo(index, type);
			sharedPreferenceManager.removeScheduleText(index);
			BaseSlideMenuActivity.scheduleId = 0;
		}
	}

	public void playVideoView(String path, int pos) {
		postitionPlayStream = pos;
		if (mVideoView != null) {
			mVideoView.stopPlayback();
		}

		Uri uri = Uri.parse(path);
		mVideoView.setVideoURI(uri);
		mVideoView.requestFocus();

		if (type == 0) {
			mVideoView.setIsChannel(true);
		} else {
			mVideoView.setIsChannel(false);
		}
		mVideoView.setTitle(HomeFragment.APP_NAME);
	}

	private void showDialogError() {
		DialogManager.alert(baseSlideMenuActivity, baseSlideMenuActivity
				.getString(R.string.details_info_player_stream_error));
	}

	private View buildVideoViewLayout() {
		RelativeLayout layVideoView = (RelativeLayout) baseSlideMenuActivity
				.getLayoutInflater().inflate(R.layout.layout_videoview, null,
						true);
		viewFunction = layVideoView.findViewById(R.id.layFunction);
		initFunctionPlayer(viewFunction);
		updateUiFunctionPlayer(true, isLockScreen, isFullScreen);
		viewFunction.setVisibility(View.GONE);

		layVolumeBrightness = (RelativeLayout) layVideoView
				.findViewById(R.id.layVolumeBrightness);
		seekBarVolumeBrightness = (SeekBar) layVideoView
				.findViewById(R.id.seekBarVolumeBrightness);
		iconVolumeBrightness = (ImageView) layVideoView
				.findViewById(R.id.iconVolumeBrightness);
		pb = (ProgressBar) layVideoView.findViewById(R.id.probar);
		downloadRateView = (TextView) layVideoView
				.findViewById(R.id.download_rate);
		loadRateView = (TextView) layVideoView.findViewById(R.id.load_rate);
		mVideoView = (VideoView) layVideoView.findViewById(R.id.buffer);
		mVideoView.setMediaFunctionExtra(mMediaFunctionExtra);

		layVideoView.setOnTouchListener(this);

		return layVideoView;
	}

	private void doLayout() {
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			doLayoutSmallScreen();
		} else {
			doLayoutFullScreen();
		}
	}

	private void doLayoutFullScreen() {
		if (isFullScreen) {
			return;
		}
		isFullScreen = true;
		updateUiFunctionPlayer(true, isLockScreen, isFullScreen);
		baseSlideMenuActivity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		layoutPlayer.getLayoutParams().height = DeviceUtil
				.getWidthScreen(baseSlideMenuActivity);
		layoutInfo.setVisibility(View.GONE);
		if (BaseSlideMenuActivity.isInSecondScreen) {
			baseSlideMenuActivity.getLayoutTitle().setVisibility(View.VISIBLE);
		} else {
			baseSlideMenuActivity.getLayoutTitle().setVisibility(View.GONE);
		}

		try {
			mVideoView.getLayoutParams().width = LayoutParams.MATCH_PARENT;
			mVideoView.getLayoutParams().height = DeviceUtil
					.getWidthScreen(baseSlideMenuActivity);

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doLayoutSmallScreen() {
		if (!isFullScreen) {
			return;
		}
		isFullScreen = false;
		updateUiFunctionPlayer(true, isLockScreen, isFullScreen);
		baseSlideMenuActivity.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		layoutPlayer.getLayoutParams().height = DeviceUtil
				.getWidthScreen(baseSlideMenuActivity) * 4 / 5;
		layoutInfo.setVisibility(View.VISIBLE);
		baseSlideMenuActivity.getLayoutTitle().setVisibility(View.VISIBLE);

		try {
			mVideoView.getLayoutParams().width = DeviceUtil
					.getWidthScreen(baseSlideMenuActivity);
			mVideoView.getLayoutParams().height = LayoutParams.WRAP_CONTENT;

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

//		case R.id.tabAbout:
//			if (layInfoPager.getCurrentItem() != 0) {
//				layInfoPager.setCurrentItem(0);
//				showDetailsInfo(DetailsInfo.ABOUT);
//			}
//			break;
//		case R.id.tabSchedule:
//			if (layInfoPager.getCurrentItem() != 1) {
//				layInfoPager.setCurrentItem(1);
//				showDetailsInfo(DetailsInfo.SCHEDULE);
//			}
//			break;
		case R.id.tabRelated:
//			if (layInfoPager.getCurrentItem() != 2) {
//				layInfoPager.setCurrentItem(0);
//				showDetailsInfo(DetailsInfo.RELATED);
//			}
			layInfoPager.setCurrentItem(0);
			showDetailsInfo(DetailsInfo.RELATED);
			
			break;
		case R.id.imvLockPlayer:
			actionLockPlayer();
			break;
		case R.id.imvFullScreenPlayer:
			actionFullScreenPlayer();
			break;
		case R.id.btLogin:
			actionAccount(0);
//			checkChargingAndPopupIfNeeded();
			break;
		case R.id.btRegis:
//			actionAccount(1);
			checkChargingAndPopupIfNeeded(true);
			break;
		case R.id.btRegCharge:
			actionAccount(2);
			break;
		default:
			break;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		doLayout();
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		isServerClick = false;
		switch (what) {
		case MediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (mVideoView.isPlaying()) {
				mVideoView.pause();
				isStart = true;
				pb.setVisibility(View.VISIBLE);
				downloadRateView.setVisibility(View.VISIBLE);
				loadRateView.setVisibility(View.VISIBLE);

			}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_END:
			if (isStart) {
				mVideoView.start();
				pb.setVisibility(View.GONE);
				downloadRateView.setVisibility(View.GONE);
				loadRateView.setVisibility(View.GONE);
			}
			break;
		case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
			downloadRateView.setText("" + extra + "kb/s" + "  ");
			break;
		}
		return true;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		loadRateView.setText(percent + "%");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		try {
			if (mThreadAudioBrightness!= null) {
				mThreadAudioBrightness.interrupt();	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (listFragments != null) {
			listFragments.clear();
			listFragments = null;
		}
		if (arrStream != null) {
			arrStream.clear();
			arrStream = null;
		}
	}

	@Override
	protected boolean needCheckCharging() {
//		if(ChannelInfo.class.isInstance(itemInfo)){
//			if(((ChannelInfo)itemInfo).isFree()){
//				return false;
//			}
//		}
//		if(VodInfo.class.isInstance(itemInfo)){
//			if(((VodInfo)itemInfo).isFree()){
//				return false;
//			}
//		}
//		ProfileInfo profile = getProfile();
//		if(profile != null && !TextUtils.isEmpty(profile.pUserName)){
//			return true;
//		}
//		return false;
		
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		final int action = event.getAction();
		switch (action) {

		case MotionEvent.ACTION_DOWN:
			mLastPositionY = event.getY();
			if (layVolumeBrightness.getVisibility() == View.VISIBLE) {
				isRunAudioBrightness = false;
				layVolumeBrightness.setVisibility(View.GONE);
			}
			break;

		case MotionEvent.ACTION_MOVE:
			float xPosition = event.getX();
			float yPosition = event.getY();
			float diff = Math.abs(mLastPositionY - yPosition);
			if ((diff > 15)
					&& (layVolumeBrightness.getVisibility() == View.GONE)) {
				layVolumeBrightness.setVisibility(View.VISIBLE);
				isRunAudioBrightness = true;
				mTimeTouchUp = SystemClock.uptimeMillis();
				try {
					mThreadAudioBrightness = new Thread(mRunAudioBrightness);
					mThreadAudioBrightness.start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (xPosition > v.getWidth() / 2) {
				// Brightness
				isControlVolume = false;
				initControlsBrightness();
			} else {
				// Volume
				isControlVolume = true;
				initControlsVolume();
			}
			mLastPositionY = yPosition;
			break;

		case MotionEvent.ACTION_UP:
			break;

		default:
			break;
		}

		return true;
	}

	/***********************************************************
	 * 
	 * Call api
	 * 
	 **********************************************************/
	public void callIpAddress() {
		ApiManager.callIpAdress(baseSlideMenuActivity, new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				String ip = ParserManager.parserIpAddress(response);
				callListStream(ip);
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				DialogManager.alert(baseSlideMenuActivity,
						baseSlideMenuActivity.getString(R.string.network_fail));
			}
		});
	}

	private void callListStream(String ip) {
		ApiManager.callListStream(
				baseSlideMenuActivity,
				new IApiCallback() {

					@Override
					public void responseSuccess(String response) {
						// TODO Auto-generated method stub
						serverPosition = 0;
						if (arrStream != null) {
							arrStream.clear();
						} else {
							arrStream = new ArrayList<StreamInfo>();
						}
						int result = ParserManager.parserListStreams(response,
								arrStream);
					
					if (result == 0) {
//						PlayerDetailsAboutFragment fr = getFragmentAbout();
//						if (fr != null) {
//							fr.layoutListStream.removeAllViews();
//						}
						if ((arrStream != null)
								&& (arrStream.size() > 0)) {
							playVideoView(arrStream.get(0).getUrl(), 0);
//							if (fr != null) {
//								fr.buildLayoutListStream(arrStream);
//							}
						}
					} else if (result == 1) {
						// if (baseSlideMenuActivity.pProfile.pUserName
						// .length() > 0) {
						// baseSlideMenuActivity
						// .callLoginApi(callBackLogin);
						// }
						
						DialogManager
						.alert(baseSlideMenuActivity,
								baseSlideMenuActivity
										.getString(R.string.not_stream));
					} else {
						DialogManager
								.alert(baseSlideMenuActivity,
										baseSlideMenuActivity
												.getString(R.string.not_stream));
					}
				}

					@Override
					public void responseFailWithCode(int statusCode) {
						DialogManager.alert(baseSlideMenuActivity,
								baseSlideMenuActivity
										.getString(R.string.network_fail));
					}
				}, itemInfo.getId(), type,
				baseSlideMenuActivity.pProfile.pUserName,
				baseSlideMenuActivity.pProfile.pSession, ip);
	}

	private void callInteraction() {
		ApiManager.callInteraction(baseSlideMenuActivity, new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				ItemInteraction info = ParserManager
						.parserInteraction(response);
				if (info != null) {
					mVideoView.hideMediaController();
					if (getResources().getConfiguration().orientation == 2) {
						baseSlideMenuActivity.getLayoutTitle().setVisibility(
								View.VISIBLE);
					}
					processActionInteraction(info);
				} else {
					Toast.makeText(baseSlideMenuActivity,
							getString(R.string.not_interaciont),
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				DialogManager.alert(baseSlideMenuActivity,
						getString(R.string.network_fail));
			}
		}, itemInfo.getId());
	}

	private void processActionInteraction(ItemInteraction info) {
		if (info.getType() == 0) {
			mVideoView.setShowMediaController(false);
			baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
			baseSlideMenuActivity.showSecondScreen(info.getUrl());
		} else if (info.getType() == 1) {
			actionInteractionImage(info.getUrl());
		} else {
			actionInteractionVideo(info.getUrl());
		}
	}

	private void actionInteractionImage(String urlLink) {
		if (urlLink == null || urlLink.length() == 0) {
			return;
		}
		DialogManager.showSimpleProgressDialog(baseSlideMenuActivity);
		ImageUtility.loadBitmapFromUrl(baseSlideMenuActivity, urlLink,
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
						DialogManager.closeProgressDialog();
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						Toast.makeText(baseSlideMenuActivity,
								"Ä�Ã£ xáº£y ra lá»—i. Vui lÃ²ng thá»­ láº¡i.",
								Toast.LENGTH_SHORT).show();
						DialogManager.closeProgressDialog();
					}
				}, baseSlideMenuActivity.getImageAds());
	}

	private void actionInteractionVideo(String urlLink) {
		if (urlLink == null || urlLink.length() == 0) {
			return;
		}
		AdsVideoFragment fr = new AdsVideoFragment();
		Bundle bundle = new Bundle();
		bundle.putString(AdsVideoFragment.XURL_VIDEO, urlLink);
		fr.setArguments(bundle);
		baseSlideMenuActivity.switchContent(fr, true);
	}

	private IApiCallback callBackLogin = new IApiCallback() {

		@Override
		public void responseSuccess(String response) {
			// TODO Auto-generated method stub
			UserInfo info = ParserManager.parserLoginInfo(response);
			if (info.isGetSucc()) {
				baseSlideMenuActivity.pProfile.pSession = info.getSession();
				callIpAddress();
			} else {
				DialogManager.alert(baseSlideMenuActivity, info.getMessage());
			}
		}

		@Override
		public void responseFailWithCode(int statusCode) {
			// TODO Auto-generated method stub
			DialogManager.alert(baseSlideMenuActivity,
					getString(R.string.network_fail));
		}
	};

	/***********************************************************
	 * 
	 * Player view
	 * 
	 **********************************************************/
	private int postitionPlayStream = -1;

	public void bindPlayerView(View view) {
		layoutPlayer = (RelativeLayout) view.findViewById(R.id.layoutPlayer);
		layoutPlayer.getLayoutParams().height = DeviceUtil
				.getWidthScreen(baseSlideMenuActivity) * 4 / 5;
		layoutPlayer.removeAllViews();
		layoutPlayer.addView(buildVideoViewLayout());
		mVideoView
				.setMediaController(new MediaController(baseSlideMenuActivity));
		mVideoView.setOnInfoListener(this);
		mVideoView.setOnBufferingUpdateListener(this);
		mVideoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				try {
//					if ((arrStream.size() > 1)
//							&& (serverPosition < arrStream.size())
//							&& !isServerClick) {
//						playVideoView(arrStream.get(serverPosition).getUrl(),
//								serverPosition);
//						PlayerDetailsAboutFragment fr = getFragmentAbout();
//						if (fr != null) {
//							fr.changeServer(serverPosition, arrStream.size());
//						}
//						serverPosition++;
//					} else {
//						showDialogError();
//					}
				} catch (Exception e) {
					e.printStackTrace();
					showDialogError();
				}

				isServerClick = false;
				return false;
			}
		});
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				// optional need Vitamio 4.0
				mediaPlayer.setPlaybackSpeed(1.0f);
//				if (postitionPlayStream >= 0
//						&& postitionPlayStream < arrStream.size()) {
//					ApiManager.callUpdateRank(baseSlideMenuActivity, null,
//							arrStream.get(postitionPlayStream).getId());
//				}
			}
		});

		bindLayoutLogin(view);
	}

	private void initControlsVolume() {
		try {
			iconVolumeBrightness.setBackgroundResource(R.drawable.icon_volume);
			audioManager = (AudioManager) baseSlideMenuActivity
					.getSystemService(Context.AUDIO_SERVICE);
			seekBarVolumeBrightness.setMax(audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
			seekBarVolumeBrightness.setProgress(audioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC));
			seekBarVolumeBrightness
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						@Override
						public void onStopTrackingTouch(SeekBar arg0) {
						}

						@Override
						public void onStartTrackingTouch(SeekBar arg0) {
						}

						@Override
						public void onProgressChanged(SeekBar arg0,
								int progress, boolean arg2) {
							mTimeTouchUp = SystemClock.uptimeMillis();
							if (isControlVolume) {
								audioManager.setStreamVolume(
										AudioManager.STREAM_MUSIC, progress, 0);
							}

						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initControlsBrightness() {

		iconVolumeBrightness.setBackgroundResource(R.drawable.icon_brightness);
		cResolver = baseSlideMenuActivity.getContentResolver();
		window = baseSlideMenuActivity.getWindow();
		seekBarVolumeBrightness.setMax(255);
		seekBarVolumeBrightness.setKeyProgressIncrement(1);

		try {
			brightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		seekBarVolumeBrightness.setProgress(brightness);

		seekBarVolumeBrightness
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						mTimeTouchUp = SystemClock.uptimeMillis();
						if (!isControlVolume) {
							if (progress <= 20) {
								brightness = 20;
							} else {
								brightness = progress;
							}

							System.putInt(cResolver, System.SCREEN_BRIGHTNESS,
									brightness);
							LayoutParams layoutpars = window.getAttributes();
							layoutpars.screenBrightness = brightness
									/ (float) 255;
							window.setAttributes(layoutpars);
						}
					}
				});
	}

	private Runnable mRunAudioBrightness = new Runnable() {

		@Override
		public void run() {
			while (isRunAudioBrightness) {
				long now = SystemClock.uptimeMillis();
				if (now - mTimeTouchUp >= 3000) {
					isRunAudioBrightness = false;
					baseSlideMenuActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							try {
								layVolumeBrightness.setVisibility(View.GONE);
								mThreadAudioBrightness.interrupt();
							} catch (Exception e) {
								e.printStackTrace();
							}
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

	public void updateViewPlayer() {
		if ((itemInfo instanceof ChannelInfo)
				&& (!((ChannelInfo) itemInfo).isFree())) {
			if (baseSlideMenuActivity.pProfile.pUserName == null
					|| baseSlideMenuActivity.pProfile.pUserName.length() == 0) {
				layoutLogin.setVisibility(View.VISIBLE);
				mVideoView.setVisibility(View.GONE);
				return;
			} else {
				StringBuilder strBuilder = new StringBuilder(",").append(
						itemInfo.getId()).append(",");
				if (!(baseSlideMenuActivity.pProfile.pIdChannel
						.contains(strBuilder.toString()))) {
					viewNotCharge.setVisibility(View.GONE);
					btChargePackage.setVisibility(View.GONE);
				} else {
					viewNotCharge.setVisibility(View.VISIBLE);
					btChargePackage.setVisibility(View.GONE);
				}
				strBuilder = null;
			}
		} else if ((itemInfo instanceof VodInfo)
				&& (!((VodInfo) itemInfo).isFree())) {
			if (baseSlideMenuActivity.pProfile.pUserName == null
					|| baseSlideMenuActivity.pProfile.pUserName.length() == 0) {
				layoutLogin.setVisibility(View.VISIBLE);
				mVideoView.setVisibility(View.GONE);
				return;
			} else {
				StringBuilder strBuilder = new StringBuilder(",").append(
						itemInfo.getId()).append(",");
				if (!(baseSlideMenuActivity.pProfile.pIdChannel
						.contains(strBuilder.toString()))) {
					viewNotCharge.setVisibility(View.GONE);
					btChargePackage.setVisibility(View.GONE);
				} else {
					viewNotCharge.setVisibility(View.VISIBLE);
					btChargePackage.setVisibility(View.GONE);
				}
				strBuilder = null;
			}
		} else {
			viewNotCharge.setVisibility(View.VISIBLE);
			btChargePackage.setVisibility(View.GONE);
		}

		layoutLogin.setVisibility(View.GONE);
		mVideoView.setVisibility(View.VISIBLE);
		callIpAddress();
	}

	/***********************************************************
	 * 
	 * Function player
	 * 
	 **********************************************************/
	private View viewFunction;
	private ImageView imvLockPlayer;
	private ImageView imvFullScreenPlayer;
	public Button btChargePackage;
	private View viewNotCharge;

	private void initFunctionPlayer(View view) {
		viewNotCharge = view.findViewById(R.id.viewNotCharge);
		imvLockPlayer = (ImageView) view.findViewById(R.id.imvLockPlayer);
		imvLockPlayer.setOnClickListener(this);
		imvFullScreenPlayer = (ImageView) view
				.findViewById(R.id.imvFullScreenPlayer);
		imvFullScreenPlayer.setOnClickListener(this);
		btChargePackage = (Button) view.findViewById(R.id.btRegCharge);
		btChargePackage.setOnClickListener(this);
	}

	private void actionLockPlayer() {
		if (isLockScreen) {
			isLockScreen = false;
			baseSlideMenuActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		} else {
			isLockScreen = true;
			if (isFullScreen) {
				baseSlideMenuActivity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
				baseSlideMenuActivity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				baseSlideMenuActivity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
			}
		}
		updateUiFunctionPlayer(true, isLockScreen, isFullScreen);
	}

	private void actionFullScreenPlayer() {
		if (!isFullScreen) {
			doLayoutFullScreenBt();
		} else {
			doLayoutSmallScreenBt();
		}
	}

	private void doLayoutFullScreenBt() {
		baseSlideMenuActivity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	private void doLayoutSmallScreenBt() {
		baseSlideMenuActivity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private void updateUiFunctionPlayer(boolean isLike, boolean isLock,
			boolean isFull) {
		if (isLock) {
			imvLockPlayer.setImageResource(R.drawable.icon_lock_screen_player);
			imvFullScreenPlayer.setVisibility(View.VISIBLE);
			if (isFull) {
				imvFullScreenPlayer
						.setImageResource(R.drawable.icon_small_screen_player);
			} else {
				imvFullScreenPlayer
						.setImageResource(R.drawable.icon_full_screen_player);
			}
		} else {
			imvFullScreenPlayer.setVisibility(View.GONE);
			imvLockPlayer.setImageResource(R.drawable.icon_auto_screen_player);
		}

	}

	private MediaFunctionExtra mMediaFunctionExtra = new MediaFunctionExtra() {

		@Override
		public void show() {
			// TODO Auto-generated method stub
			viewFunction.setVisibility(View.VISIBLE);
		}

		@Override
		public void hide() {
			// TODO Auto-generated method stub
			viewFunction.setVisibility(View.GONE);
		}
	};

	/*********************************************************************
	 * 
	 * Second screen
	 * 
	 ********************************************************************/
	private void bindSecondScreen(View view) {
		if (this.type == 0) {
			baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
		} else {
			baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
		}
	}

	/**********************************************************************
	 * 
	 * All view extra
	 * 
	 *********************************************************************/
	public enum DetailsInfo {
		ABOUT, SCHEDULE, RELATED
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		switch (position) {
		case 0:
			showDetailsInfo(DetailsInfo.RELATED);
//			showDetailsInfo(DetailsInfo.ABOUT);
			break;
		case 1:
//			if (type == 0) {
//				showDetailsInfo(DetailsInfo.SCHEDULE);
//			} else {
//				showDetailsInfo(DetailsInfo.RELATED);
//			}
			break;
		case 2:
//			showDetailsInfo(DetailsInfo.RELATED);
			break;
		default:
			break;
		}
	}

	private void bindAllViewExtra(View view) {
		layoutInfo = (LinearLayout) view.findViewById(R.id.layoutInfo);
		layInfoPager = (ViewPager) view.findViewById(R.id.layInfoPager);
//		layInfoPager.setOnPageChangeListener(this);

//		tabAbout = (RelativeLayout) view.findViewById(R.id.tabAbout);
//		tabSchedule = (RelativeLayout) view.findViewById(R.id.tabSchedule);
		tabRelated = (RelativeLayout) view.findViewById(R.id.tabRelated);
//		tabAboutFocus = (View) view.findViewById(R.id.tabAboutFocus);
//		tabScheduleFocus = (View) view.findViewById(R.id.tabScheduleFocus);
		tabRelatedFocus = (View) view.findViewById(R.id.tabRelatedFocus);
		tabRelatedText = (TextView) view.findViewById(R.id.tabRelatedText);
		tabRelated.setOnClickListener(this);
//		tabAbout.setOnClickListener(this);
//		tabSchedule.setOnClickListener(this);

		if (type == 0) {
			tabRelatedText
					.setText(getString(R.string.details_info_related_channel));
		} else {
			tabRelatedText
					.setText(getString(R.string.details_info_related_video));
		}
		if (layInfoPager != null) {
			layInfoPager.setOffscreenPageLimit(1);
			layInfoPager.setCurrentItem(0);
		}
	}

	private void initDataInfoPager() {
		if (listFragments == null) {
			listFragments = new ArrayList<Fragment>();
//			listFragments.add(new PlayerDetailsAboutFragment());
//			if (type == 0) {
//				tabSchedule.setVisibility(View.VISIBLE);
//				listFragments.add(new PlayerDetailsScheduleFragment());
//			} else {
//				tabSchedule.setVisibility(View.GONE);
//			}

			listFragments.add(new PlayerDetailsRelatedFragment());
		}

		if (mAdapter == null) {
			mAdapter = new PlayerDetailsPagerAdapter(baseSlideMenuActivity,
					baseSlideMenuActivity.getSupportFragmentManager(),
					listFragments);
		}
		layInfoPager.setAdapter(mAdapter);
		layInfoPager.setCurrentItem(0);
	}

	public void showDetailsInfo(DetailsInfo info) {
		switch (info) {
//
//		case ABOUT:
//			tabAboutFocus.setVisibility(View.VISIBLE);
//			tabScheduleFocus.setVisibility(View.GONE);
//			tabRelatedFocus.setVisibility(View.GONE);
//			break;
//		case SCHEDULE:
//			tabAboutFocus.setVisibility(View.GONE);
//			tabScheduleFocus.setVisibility(View.VISIBLE);
//			tabRelatedFocus.setVisibility(View.GONE);
//			break;
		case RELATED:
//			tabAboutFocus.setVisibility(View.GONE);
//			tabScheduleFocus.setVisibility(View.GONE);
			tabRelatedFocus.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	/**********************************************************************
	 * 
	 * Layout login
	 * 
	 *********************************************************************/
	private void bindLayoutLogin(View view) {
		layoutLogin = (LinearLayout) view.findViewById(R.id.layLogin);
		Button btLogin = (Button) layoutLogin.findViewById(R.id.btLogin);
		btLogin.setOnClickListener(this);
		Button btReg = (Button) layoutLogin.findViewById(R.id.btRegis);
		btReg.setOnClickListener(this);
		layoutLogin.setVisibility(View.GONE);
	}

	private void actionAccount(int typeAcc) {
		// type: 0: login; 1: reg; 2: charge money
		Fragment fr = null;
		if (typeAcc == 0) {
			fr = new LoginFragment();
			Bundle bundle = new Bundle();
			bundle.putBoolean("from_player", true);
			fr.setArguments(bundle);
		} else if (typeAcc == 1) {
			fr = new RegisterFragment();
		} else if (typeAcc == 2) {
			fr = new WebviewChargeFragment();
		}
		baseSlideMenuActivity.switchContent(fr, true);
	}

	/*********************************************************************
	 * 
	 * Process fragment
	 * 
	 ********************************************************************/
	private String getFragmentTag(int pos) {
		return "android:switcher:" + R.id.layInfoPager + ":" + pos;
	}

	public PlayerDetailsAboutFragment getFragmentAbout() {
		return (PlayerDetailsAboutFragment) baseSlideMenuActivity
				.getSupportFragmentManager().findFragmentByTag(
						getFragmentTag(0));
	}

	public PlayerDetailsScheduleFragment getFragmentSchedule() {
		return (PlayerDetailsScheduleFragment) baseSlideMenuActivity
				.getSupportFragmentManager().findFragmentByTag(
						getFragmentTag(1));
	}

	public PlayerDetailsRelatedFragment getFragmentRelate() {
		int pos = 1;
		if (type == 0) {
			pos = 2;
		}
		return (PlayerDetailsRelatedFragment) baseSlideMenuActivity
				.getSupportFragmentManager().findFragmentByTag(
						getFragmentTag(pos));
	}

	public void removeAllPager() {
		FragmentTransaction trans = getFragmentManager().beginTransaction();
		for (int i = 0; i < listFragments.size(); i++) {
			Fragment f = getActivity().getSupportFragmentManager()
					.findFragmentByTag(getFragmentTag(i));
			if (f != null) {
				trans.remove(f);
			}
		}
		trans.commit();
	}
}
