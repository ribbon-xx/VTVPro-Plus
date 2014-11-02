package mdn.vtvpluspro.fragment;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import mdn.vtvplus.R;
import mdn.vtvpluspro.common.DialogManager;
import mdn.vtvpluspro.util.DeviceUtil;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AdsVideoFragment extends ChargingFragment implements OnInfoListener, OnBufferingUpdateListener, OnTouchListener{
	public static final String XURL_VIDEO = "_xurl_video_";

	private RelativeLayout layoutPlayer;
	public VideoView mVideoView;
	private ProgressBar pb;
	private TextView downloadRateView;
	private TextView loadRateView;
	private RelativeLayout layVolumeBrightness;
	private SeekBar seekBarVolumeBrightness;
	private ImageView iconVolumeBrightness;

	private String urlVideo = null;
	private boolean isStart = false;
	
	private AudioManager audioManager;
	private boolean isControlVolume = true;
	private int brightness;
	private ContentResolver cResolver;
	private Window window;
	private float mLastPositionY;
	private long mTimeTouchUp = 0;
	private Thread mThreadAudioBrightness;
	private boolean isRunAudioBrightness = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		baseSlideMenuActivity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		View view = inflater.inflate(R.layout.fragment_ads_video, null);
		baseSlideMenuActivity.getSlidingMenu().getmViewBehind().setMarginThreshold(0);
		bindPlayerView(view);
		playVideoView();
		return view;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		
		baseSlideMenuActivity.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		baseSlideMenuActivity.getSlidingMenu().getmViewBehind().setMarginThreshold(baseSlideMenuActivity.widScrollMenu);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		try {
			mThreadAudioBrightness.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected boolean needCheckCharging() {
		return true;
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

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		loadRateView.setText(percent + "%");
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
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
	
	private void initData() {
		Bundle bundle = getArguments();
		urlVideo = bundle.getString(XURL_VIDEO);
	}
	
	private void playVideoView() {
		if (mVideoView != null) {
			mVideoView.stopPlayback();
		}

		Uri uri = Uri.parse(urlVideo);
		mVideoView.setVideoURI(uri);
		mVideoView.requestFocus();
		mVideoView.setIsChannel(false);
//		mVideoView.setTitle(HomeFragment.APP_NAME);
	}
	
	private void showDialogError() {
		DialogManager.alert(baseSlideMenuActivity, baseSlideMenuActivity
				.getString(R.string.details_info_player_stream_error));
	}
	
	private View buildVideoViewLayout() {
		RelativeLayout layVideoView = (RelativeLayout) baseSlideMenuActivity
				.getLayoutInflater().inflate(R.layout.layout_videoview, null,
						true);
		View viewFunction = layVideoView.findViewById(R.id.layFunction);
		viewFunction.setVisibility(View.GONE);
		
		View layoutLogin = layVideoView.findViewById(R.id.layLogin);
		layoutLogin.setVisibility(View.GONE);

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
		mVideoView.setMediaFunctionExtra(null);

		layVideoView.setOnTouchListener(this);

		return layVideoView;
	}

	/***********************************************************
	 * 
	 * Player view
	 * 
	 **********************************************************/
	private void bindPlayerView(View view) {
		baseSlideMenuActivity.getLayoutTitle().setVisibility(View.GONE);
		baseSlideMenuActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		layoutPlayer = (RelativeLayout) view.findViewById(R.id.layoutPlayer);
		layoutPlayer.getLayoutParams().height = DeviceUtil
				.getWidthScreen(baseSlideMenuActivity);
		layoutPlayer.removeAllViews();
		layoutPlayer.addView(buildVideoViewLayout());
		mVideoView
				.setMediaController(new MediaController(baseSlideMenuActivity));
		mVideoView.setOnInfoListener(this);
		mVideoView.setOnBufferingUpdateListener(this);
		mVideoView.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				showDialogError();
				return false;
			}
		});
		mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mediaPlayer) {
				// optional need Vitamio 4.0
				mediaPlayer.setPlaybackSpeed(1.0f);
			}
		});
		
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

}
