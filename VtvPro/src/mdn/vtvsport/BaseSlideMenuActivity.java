package mdn.vtvsport;

import java.io.IOException;

import io.vov.vitamio.LibsChecker;
import mdn.vtvsport.alarm.AlarmManagerBroadcastReceiver;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.common.SharedPreferenceManager;
import mdn.vtvsport.facebook.FacebookUtil;
import mdn.vtvsport.fragment.HomeFragment;
import mdn.vtvsport.fragment.ListMenuFragment;
import mdn.vtvsport.fragment.PlayerFragment;
import mdn.vtvsport.fragment.VtvPlusFragment;
import mdn.vtvsport.fragment.account.LoginFragment;
import mdn.vtvsport.fragment.account.WebviewChargeFragment;
import mdn.vtvsport.gcm.ServerUtilities;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.VersionVtv;
import mdn.vtvsport.object.VodInfo;
import mdn.vtvsport.object.account.ProfileInfo;
import mdn.vtvsport.object.account.UserInfo;
import mdn.vtvsport.slidingmenu.app.SlidingFragmentActivity;
import mdn.vtvsport.util.AnimationUtil;
import mdn.vtvsport.util.DeviceUtil;
import mdn.vtvsport.util.IntentUtil;
import mdn.vtvsport.util.Utils;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseSlideMenuActivity extends SlidingFragmentActivity implements
		OnClickListener, AnimationListener {
	public Fragment mContent;
	public ListMenuFragment mMenuFragment;

	public int widScrollMenu;

	protected SharedPreferenceManager sharedPreferenceManager;
	protected GoogleAnalytics GAtracker;
	protected Tracker tracker;

	private View viewBanner;
	private View viewSearch;

	private ImageView imvSmallSearch;
	private EditText edSearch;
	private Button btCancelSearch;

	private LinearLayout layoutTitle;
	public LinearLayout iconSetting;
	public LinearLayout iconBack;
	private LinearLayout iconSearch;
	public ImageView iconVtvPlus;
	public TextView tvCategory;
	private ImageView imgAds;
	public ImageView iconInteract;

	public FacebookUtil mFacebookUtil;
	public static String linkUrlAds = "";
	public static int scheduleId = 0;
	public static String gcmBundleId = "";
	public static String gcmBundleType = "";
	
	public static boolean isStartNomal = false; // phan biet bat dau vao app tu
	// home hay playvideo _ an icon
	// home
	public VersionVtv pVersion;
	public ProfileInfo pProfile;
	
	String regid;
	String SENDER_ID = "70351511992";
	GoogleCloudMessaging gcm;
	 
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		pProfile = new ProfileInfo();
		clearCookie();

		if (!LibsChecker.checkVitamioLibs(this)) {
			return;
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (DeviceUtil.isTablet(this)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		sharedPreferenceManager = SharedPreferenceManager.getInstance(this);
		getUserInfo();

//		// TODO hieuth implement code
//		if (TextUtils.isEmpty(pProfile.pUserName)) {
//			callCheckChargeEnable();
//		}

		pVersion = new VersionVtv();
		GAtracker = GoogleAnalytics.getInstance(this);
		tracker = GAtracker
				.getTracker(getResources().getString(R.string.ua_id));

		mFacebookUtil = FacebookUtil.newInstance(this);
		mFacebookUtil.onCreate(savedInstanceState);

		/* Set above view */
		setContentView(R.layout.activity_base);
		bindLayout();
		inputFragment();

		/* Set menu view */
		mMenuFragment = new ListMenuFragment();
		setBehindContentView(R.layout.frame_menu);
		FragmentTransaction t = this.getSupportFragmentManager()
				.beginTransaction();
		t.add(R.id.frameMenu, mMenuFragment);
		t.commit();

		/** Customize the SlidingMenu */
		this.setSlidingActionBarEnabled(true);
		Display display = getWindowManager().getDefaultDisplay();
		getSlidingMenu().setBehindOffset(display.getWidth() / 5);
		getSlidingMenu().setBehindScrollScale(0.25f);

		this.widScrollMenu = getSlidingMenu().getmViewBehind()
				.getMarginThreshold();

		 // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
		
		 // Check device for Play Services APK. If check succeeds, proceed with
        //  GCM registration.
		if (Utils.checkPlayServices(BaseSlideMenuActivity.this,
				PLAY_SERVICES_RESOLUTION_REQUEST)) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(getApplicationContext());

			if (regid.isEmpty()) {
				registerInBackground();
			}
		} else {
			Log.i("", "No valid Google Play Services APK found.");
			Toast.makeText(this,
					getString(R.string.gcm_not_valid_google_play_services),
					Toast.LENGTH_SHORT).show();
		}

//		registerInBackground();
//		checkNotNull(ServerUtilities.SENDER_ID, "SENDER_ID");
//		GCMRegistrar.checkDevice(this);
//		GCMRegistrar.checkManifest(this);
//		final String regId = GCMRegistrar.getRegistrationId(this);
//		if (regid.equals("")) {
//			GCMRegistrar.register(this, ServerUtilities.SENDER_ID);
//			registerInBackground();
//		} else {
//			ServerUtilities.postRegistrationId(this, regid);
//		}
	}

	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i("", "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = Utils.getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i("", "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(BaseSlideMenuActivity.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
	
	 // Persist the regID - no need to register again.
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = Utils.getAppVersion(context);
	    Log.i("", "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
    
	/**
	 * Registers the application with GCM servers asynchronously.
	 */
	private void registerInBackground() {
		
	    new AsyncTask<Void, Void, String>() {
	        @Override
	        protected String doInBackground(Void... params) {
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;
	                
	                ServerUtilities.postRegistrationId(getApplicationContext(), regid);
	                
	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
//	                sendRegistrationIdToBackend();

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the
	                // message using the 'from' address in the message.

	                // Persist the regID - no need to register again.
	                storeRegistrationId(getApplicationContext(), regid);
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	            }
	            return msg;
	        }

	        @Override
	        protected void onPostExecute(String msg) {
//	            mDisplay.append(msg + "\n");
	        }

	    }.execute(null, null, null);
	}
	
	public ListMenuFragment getMenuFragment() {
		return this.mMenuFragment;
	}

	@Override
	public void showMenu() {
		// TODO Auto-generated method stub
		super.showMenu();
	}

	private void clearCookie() {
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		cookieManager.setAcceptCookie(false);
	}

	private void getUserInfo() {
		this.pProfile.pUserName = sharedPreferenceManager.getUserName();
		this.pProfile.pPassword = sharedPreferenceManager.getPassUser();
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config,
					name));
		}
	}

	private void bindLayout() {
		layoutTitle = (LinearLayout) findViewById(R.id.layoutTitle);
		iconSetting = (LinearLayout) findViewById(R.id.iconSetting);
		iconBack = (LinearLayout) findViewById(R.id.iconBack);
		iconSearch = (LinearLayout) findViewById(R.id.iconSearch);
		iconVtvPlus = (ImageView) findViewById(R.id.iconVtvPlus);
		tvCategory = (TextView) findViewById(R.id.tvCategory);
		imgAds = (ImageView) findViewById(R.id.imgAds);
		iconInteract = (ImageView) findViewById(R.id.iconInteract);
		iconSetting.setOnClickListener(this);
		iconBack.setOnClickListener(this);
		iconSearch.setOnClickListener(this);
		iconVtvPlus.setOnClickListener(this);
		imgAds.setOnClickListener(this);
//		iconInteract.setOnClickListener(this);

		bindSecondScreen();

		viewBanner = findViewById(R.id.viewBanner);
		viewSearch = findViewById(R.id.viewSearch);
		imvSmallSearch = (ImageView) findViewById(R.id.imvSmallSearch);
		imvSmallSearch.setOnClickListener(this);
		edSearch = (EditText) findViewById(R.id.edSearchBanner);
		edSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
			                              KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| actionId == EditorInfo.IME_ACTION_DONE) {
					actionSearch();
					return true;
				}
				return false;
			}
		});

		btCancelSearch = (Button) findViewById(R.id.btnSearchCancel);
		btCancelSearch.setOnClickListener(this);
		viewSearch.setVisibility(View.GONE);

		gcmBundleId = "";
		gcmBundleType = "";
		scheduleId = 0;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			scheduleId = extras
					.getInt(AlarmManagerBroadcastReceiver.SCHEDULE_ID);
			try {
				gcmBundleId = extras.getString(GcmIntentService.GCMBUNDLE_ID);
				gcmBundleType = extras
						.getString(GcmIntentService.GCMBUNDLE_TYPE);
				if (gcmBundleId.equals("")) {
					;
				}
			} catch (Exception e) {
				gcmBundleId = "";
				gcmBundleType = "";
				e.printStackTrace();
			}
		}

	}

	private void inputFragment() {
		if ((BaseSlideMenuActivity.scheduleId == 0)
				&& (BaseSlideMenuActivity.gcmBundleId.equals(""))) {
			switchContent(new HomeFragment(), false);
			isStartNomal = true;
		} else if (gcmBundleType.equalsIgnoreCase("ads")) {
			switchContent(new HomeFragment(), false);
			isStartNomal = true;
		} else if (gcmBundleType.equalsIgnoreCase("vod")) {
			callDetailVod();
		} else {
			switchContent(new PlayerFragment(), false);
			isStartNomal = false;
		}
	}

	public LinearLayout getLayoutTitle() {
		return layoutTitle;
	}

	public void setLayoutTitle(LinearLayout layoutTitle) {
		this.layoutTitle = layoutTitle;
	}

	public ImageView getImageAds() {
		return imgAds;
	}

	public void setTextCategory(String text) {
		this.tvCategory.setText(text);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		mFacebookUtil.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mFacebookUtil.onStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mFacebookUtil.onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();

		imgAds.setVisibility(View.GONE);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mFacebookUtil.onStop();
	}

	@Override
	public void onDestroy() {
		try {
			GAtracker.closeTracker(tracker);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		DialogManager.closeProgressDialog();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((mContent instanceof WebviewChargeFragment)
					&& (pProfile.pUserName.length() > 0)) {
				callAccountInfoApi(callbackInfo);
			} else {
				onPressBack();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void callDetailVod() {
		ApiManager.callRelationItemVtvPlus(
				this,
				new IApiCallback() {
					@Override
					public void responseSuccess(String response) {
						// TODO Auto-generated method stub
						BaseSlideMenuActivity.gcmBundleId = "";
						BaseSlideMenuActivity.gcmBundleType = "";
						VodInfo item = (VodInfo) ParserManager
								.parserItemVtvPlusDetailsInfo(response, 1);
						if (item.isSeries()) {
							Bundle bundle = new Bundle();
							bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 2);
							bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV,
									item.getId());
							bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV,
									item.getName());
							VtvPlusFragment mVtvFragment = new VtvPlusFragment();
							mVtvFragment.setArguments(bundle);
							switchContent(mVtvFragment, false);
							isStartNomal = false;
						} else {
							switchContent(new PlayerFragment(), false);
							isStartNomal = false;
						}
					}

					@Override
					public void responseFailWithCode(int statusCode) {
						// TODO Auto-generated method stub

					}
				}, DeviceUtil.getDeviceId(this), gcmBundleId,
				HomeFragment.APP_NAME, 0,
				0, 1, null, true);
	}

	public void trackView(String screenName) {
		tracker.sendView(screenName);
		GAServiceManager.getInstance().dispatch();

	}

	public void trackEvent(String category, String action, String label) {
		tracker.sendEvent(category, action, label, (long) 0);
		GAServiceManager.getInstance().dispatch();
	}

	public void onPressBack() {
		if (imgAds.getVisibility() == View.VISIBLE) {
			imgAds.setVisibility(View.GONE);
			return;
		}
		// if (mSlideMenu.isOpen()) {
		// mSlideMenu.close(true);
		// return;
		// }

		if (getSlidingMenu().isMenuShowing()) {
			showContent();
			return;
		}

		if (isInSecondScreen) {
			closeSecondScreen();
		} else {
			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				if (sharedPreferenceManager.isRating()) {
					shutDown();
				} else {
                    shutDown();
//					new RatingDialog(this, new MyClickListener() {
//
//						@Override
//						public void onRemindLater(Dialog dialog) {
//							// TODO Auto-generated method stub
//							dialog.dismiss();
//							shutDown();
//						}
//
//						@Override
//						public void onRating(Dialog dialog) {
//							// TODO Auto-generated method stub
//							sharedPreferenceManager.setRating(true);
//							dialog.dismiss();
//							IntentUtil
//									.openAppStore(BaseSlideMenuActivity.this,
//											BaseSlideMenuActivity.this
//													.getPackageName());
//							shutDown();
//						}
//
//						@Override
//						public void onDimiss(Dialog dialog) {
//							// TODO Auto-generated method stub
//							sharedPreferenceManager.setRating(true);
//							dialog.dismiss();
//							shutDown();
//						}
//					});
				}

			} else {
				getSupportFragmentManager().popBackStack();
				removeCurrentFragment();
			}
		}
	}

	public void shutDown() {
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
		this.finish();
	}

	/**
	 * *************************************************************
	 * <p/>
	 * Process fragment
	 * <p/>
	 * **************************************************************
	 */
	public void switchContent(final Fragment fragment, boolean isAddBackStack) {
		mContent = fragment;
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.layoutContent, fragment);
		if (isAddBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();

		System.gc();
	}

	public void clearBackStackFragment() {
		FragmentManager fm = getSupportFragmentManager();
		for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
			fm.popBackStack();
		}
	}

	public void removeCurrentFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		if (mContent != null) {
			transaction.remove(mContent);
		}
		transaction.commit();
	}

	public int getCountBackStack() {
		return getSupportFragmentManager().getBackStackEntryCount();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.iconSetting:
				// if (mSlideMenu.isOpen()) {
				// mSlideMenu.close(true);
				// } else {
				// mSlideMenu.open(false, true);
				// }
				showMenu();
				break;

			case R.id.iconBack:
				onPressBack();
				break;

			case R.id.iconVtvPlus:
				actionIconVtvPlus();
				break;
			case R.id.imgAds:
				if (linkUrlAds != null && linkUrlAds.length() > 0) {
					IntentUtil.openWebView(this, linkUrlAds);
				}
				break;
			case R.id.iconSearch:
				viewBanner.setVisibility(View.GONE);
				viewSearch.setVisibility(View.VISIBLE);
				edSearch.requestFocus();
				DeviceUtil.showSoftKeyboard(this);
				break;
			case R.id.btnSearchCancel:
				viewBanner.setVisibility(View.VISIBLE);
				viewSearch.setVisibility(View.GONE);
				edSearch.setText("");
				DeviceUtil.hideKeyboard(this, edSearch);
				break;
			case R.id.imvSmallSearch:
				actionSearch();
				break;
//			case R.id.iconInteract:
//				((BaseFragment) mContent).actionInteract();
//				break;
			default:
				break;
		}
	}

	private void actionIconVtvPlus() {
		// if (mSlideMenu.isOpen()) {
		// mSlideMenu.close(true);
		// return;
		// }
		if (isInSecondScreen) {
			closeSecondScreen();
			return;
		}
		if (HomeFragment.isHomeRunning) {
			return;
		}

		int countBackStack = getSupportFragmentManager()
				.getBackStackEntryCount();
		if (countBackStack > 0) {
			int start = 0;
			for (; start < countBackStack; start++) {
				getSupportFragmentManager().popBackStack();
			}
			removeCurrentFragment();
			if (!isStartNomal) {
				switchContent(new HomeFragment(), false);
			}
		} else {
			removeCurrentFragment();
			switchContent(new HomeFragment(), false);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if (getResources().getConfiguration().orientation == 2) {
			layoutTitle.setVisibility(View.GONE);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	/**
	 * *************************************************
	 * <p/>
	 * Action Search
	 * <p/>
	 * *************************************************
	 */
	private void actionSearch() {
		DeviceUtil.hideKeyboard(this, edSearch);
		Bundle bundle = new Bundle();
		bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 3);
		bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV, edSearch.getText()
				.toString());
		bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV, "");
		VtvPlusFragment mVtvFragment = new VtvPlusFragment();
		mVtvFragment.setArguments(bundle);
		if (mContent instanceof HomeFragment) {
			switchContent(mVtvFragment, true);
		} else if (mContent instanceof VtvPlusFragment) {
			((VtvPlusFragment) mContent).clearData(edSearch.getText()
					.toString().trim());
			((VtvPlusFragment) mContent).getListChannel();
		} else {
			switchContent(mVtvFragment, false);
		}
	}

	public void closeViewSearch() {
		viewBanner.setVisibility(View.VISIBLE);
		viewSearch.setVisibility(View.GONE);
	}

	/**
	 * ************************************************
	 * <p/>
	 * Account
	 * <p/>
	 * ************************************************
	 */
	public void callLoginApi(IApiCallback callBack, String userName, String pass) {
		ApiManager.callLogin(this, callBack, userName, pass);
	}

	public void callLoginApi(IApiCallback callBack) {
		ApiManager.callLogin(this, callBack, pProfile.pUserName,
				pProfile.pPassword);
	}

		
//	public void callCheckChargeEnable() {
//		HLog.d("callCheckChargeEnable . . . ");
//		ApiManager.callCheckPaymentSetting(this, new IApiCallback() {
//			@Override
//			public void responseSuccess(String response) {
//				HLog.i("Check charge response: " + response);
//				ChargingEnableInfo enableInfo = ChargingEnableInfo.fromResponse(response);
//				if (enableInfo.code == 0) {
//					// error
//					HLog.e("Has error when checkChargeEnable. Check " + WebServiceConfig.getUrlCheckChargingEnable());
//					return;
//				}
//				boolean checked = enableInfo.mEnable == 1;
//				if (checked) {
//					mSendSmsBackground = enableInfo.mSendSmsBackground == 1;
//					callCheckUserCharged();
//				}
//			}
//
//			@Override
//			public void responseFailWithCode(int statusCode) {
//				HLog.e("get flag check charge error: " + statusCode);
//			}
//		});
//	}
//
//	private void callCheckUserCharged() {
//		HLog.i("callCheckUserCharged ...");
//		if (pProfile != null && !TextUtils.isEmpty(pProfile.pUserName) && !TextUtils.isEmpty(pProfile.pSession)) {
//			ApiManager.callCheckUserCharged(this, pProfile.pUserName, pProfile.pSession, new IApiCallback() {
//				@Override
//				public void responseSuccess(String response) {
//					HLog.i("callCheckUserCharged response: " + response);
//					ChargedUserInfo userInfo = ChargedUserInfo.fromResponse(response);
//					HLog.i("ChargedUserInfo: " + userInfo);
//					if (userInfo.error == 0 && userInfo.registered == 1) {
//						// register already, do nothing
//						return;
//					}
//					prepareToCallGetChargeSetting();
//				}
//
//				@Override
//				public void responseFailWithCode(int statusCode) {
//					HLog.e("callCheckUserCharged error: " + statusCode);
//				}
//			});
//		} else {
//			prepareToCallGetChargeSetting();
//		}
//	}
//
//	private void prepareToCallGetChargeSetting() {
//		String operatorCode = DeviceUtil.getTelcoCode(this);
//		HLog.i("Operator code: " + operatorCode);
//		if (DEBUG && TextUtils.isEmpty(operatorCode)) {
//			operatorCode = "45201";
//		}
//		if (TextUtils.isEmpty(operatorCode)) {
//			return;
//		}
//		callGetChargeSetting(DISTRIBUTOR_CHANNEL_ID, operatorCode);
//	}
//
//	private void callGetChargeSetting(final String distributorChannelId, final String operatorCode) {
//		HLog.i("callGetChargeSetting ...");
//		ApiManager.callGetChargeSetting(this, new IApiCallback() {
//			@Override
//			public void responseSuccess(String response) {
//				HLog.i("callGetChargeSetting response: " + response);
//				Charginginfo charginginfo = Charginginfo.fromResponse(response);
//				if (charginginfo.code == 0) {
//					Log.e("hieuth", "getChargeSetting code = 0, check " + WebServiceConfig.getUrlGetPaymentPopupSetting("", "") + " - with operator code: " + operatorCode);
//				}
//				doCharge(charginginfo);
//			}
//
//			@Override
//			public void responseFailWithCode(int statusCode) {
//				HLog.e("callGetChargeSetting error: " + statusCode);
//			}
//		}, distributorChannelId, operatorCode);
//	}
//
//	private void doCharge(final Charginginfo info) {
//		if (info.type != 1 || TextUtils.isEmpty(info.smsGateway) || TextUtils.isEmpty(info.smsBody)) {
//			Log.e("hieuth", "Check payment info error " + info);
//			return;
//		}
//		if (mChargingDialog == null) {
////			AlertDialog.Builder builder = new AlertDialog.Builder(this);
////			builder.setTitle(info.popupTitle);
////			builder.setMessage(info.popupBody);
////			builder.setCancelable(false);
////			builder.setPositiveButton(R.string.charging_ok, new DialogInterface.OnClickListener() {
////				@Override
////				public void onClick(DialogInterface dialog, int which) {
////					if (mSendSmsBackground) {
////						SmsUtil.sendSMS(BaseSlideMenuActivity.this, info.smsGateway, updateSmsIfNeeded(info.smsBody));
////					} else {
////						SmsUtil.sendSMSByBuildInApp(BaseSlideMenuActivity.this, info.smsGateway, updateSmsIfNeeded(info.smsBody));
////					}
////				}
////			});
////
////			builder.setNegativeButton(R.string.charging_cancel, new DialogInterface.OnClickListener() {
////				@Override
////				public void onClick(DialogInterface dialog, int which) {
////					mChargingDialog.dismiss();
////				}
////			});
////			mChargingDialog = builder.create();
//			mChargingDialog = createPolicyDialog(info);
//		}
//		mChargingDialog.show();
//	}
//
//	private Dialog createPolicyDialog(final Charginginfo info){
//		final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme);
//		View view = LayoutInflater.from(this).inflate(R.layout.policy_layout, null);
//		TextView message = (TextView) view.findViewById(R.id.dialog_message);
//		message.setText(info.popupBody);
//
//		TextView title = (TextView) view.findViewById(R.id.dialog_title);
//		title.setText(info.popupTitle);
//
////		final Dialog dialog = new Dialog(this);
////		dialog.setTitle(info.popupTitle);
////		View view = LayoutInflater.from(this).inflate(R.layout.policy_layout, null);
////		TextView text = (TextView) view.findViewById(R.id.text_view);
////		text.setText(info.popupBody);
//		View btOK = view.findViewById(R.id.button_ok);
//		View btClose = view.findViewById(R.id.button_close);
//		btOK.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				if (mSendSmsBackground) {
//					SmsUtil.sendSMS(BaseSlideMenuActivity.this, info.smsGateway, updateSmsIfNeeded(info.smsBody));
//				} else {
//					SmsUtil.sendSMSByBuildInApp(BaseSlideMenuActivity.this, info.smsGateway, updateSmsIfNeeded(info.smsBody));
//				}
//			}
//		});
//
//		btClose.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		dialog.setContentView(view);
//		dialog.setCancelable(false);
//		int height = getResources().getDisplayMetrics().heightPixels;
//		dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height/2);
////		dialog.show();
//		return dialog;
//	}
//
//	private String updateSmsIfNeeded(String smsBody) {
//		if (pProfile != null && !TextUtils.isEmpty(pProfile.pUserName)) {
//			return smsBody + " " + pProfile.pUserName;
//		}
//		return smsBody;
//	}

	public void callAccountInfoApi(IApiCallback callback) {
		ApiManager.callAccountInfo(this, callback, pProfile.pUserName,
				pProfile.pSession);
	}

	public boolean checkLogin() {
		if (pProfile.pUserName.length() > 0 && pProfile.pSession.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void logoutAccount() {
		logoutClearData();
		switchContent(new LoginFragment(), false);
	}

	public void logoutClearData() {
		sharedPreferenceManager.setUserInfo("", "");
		pProfile.clear();
		mMenuFragment.initLayout();
	}

	public boolean isMainFragment() {
		if (mContent instanceof HomeFragment
				|| mContent instanceof PlayerFragment
				|| mContent instanceof VtvPlusFragment) {
			return true;
		}
		return false;
	}

	/**
	 * *******************************************************
	 * <p/>
	 * Callback
	 * <p/>
	 * *******************************************************
	 */
	private IApiCallback callbackInfo = new IApiCallback() {

		@Override
		public void responseSuccess(String response) {
			// TODO Auto-generated method stub
			UserInfo info = ParserManager.parserUserInfo(response);
			if (info.isGetSucc()) {
				if (info.isRegister()) {
					pProfile.pTypePack = 1;
					pProfile.pIdChannel = info.getLiveChannel();
					pProfile.pNamePack = info.getCurrentService();
					pProfile.pDayPack = info.getExpiryDate();
					onPressBack();
				} else {
					pProfile.pTypePack = 2;
				}
			} else {
				DialogManager.alert(BaseSlideMenuActivity.this,
						info.getLiveChannel());
			}
		}

		@Override
		public void responseFailWithCode(int statusCode) {
			// TODO Auto-generated method stub
			DialogManager.alert(BaseSlideMenuActivity.this,
					getString(R.string.network_fail));
		}
	};

	/**
	 * ****************************************************************
	 * <p/>
	 * Second screen
	 * <p/>
	 * ****************************************************************
	 */
	public LinearLayout layoutSecondScreen;
	private WebView interactWebview;
	private ProgressBar loadingBar;
	public static boolean isInSecondScreen = false;

	private void bindSecondScreen() {
		layoutSecondScreen = (LinearLayout) findViewById(R.id.layoutSecondScreen);
		interactWebview = (WebView) findViewById(R.id.interactWebview);
		loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

		interactWebview.getSettings().setJavaScriptEnabled(true);
		interactWebview.getSettings().setSupportZoom(true);
		interactWebview.getSettings().setBuiltInZoomControls(false);
		interactWebview.getSettings().setAppCacheEnabled(false);
		interactWebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		interactWebview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		interactWebview.setWebChromeClient(new CustomWebChromeClient());
		interactWebview.setWebViewClient(new CustomWebViewClient());
	}

	public void showSecondScreen(String url) {
//		interactWebview.loadUrl(url);
//		isInSecondScreen = true;
//		layoutSecondScreen.setVisibility(View.VISIBLE);
//		layoutSecondScreen.setAnimation(AnimationUtil.SlideOutRightLeft(this));

		CookieSyncManager.createInstance(this);
		CookieManager cm = CookieManager.getInstance();
		cm.setAcceptCookie(true);
		CookieSyncManager.getInstance().sync();

		interactWebview.loadUrl(url);
		isInSecondScreen = true;
		layoutSecondScreen.setVisibility(View.VISIBLE);
		layoutSecondScreen.setAnimation(AnimationUtil.SlideOutRightLeft(this));
	}

	public void closeSecondScreen() {
		isInSecondScreen = false;
		layoutSecondScreen.setVisibility(View.GONE);
		if (mContent instanceof PlayerFragment) {
//			iconInteract.setVisibility(View.VISIBLE);
			((PlayerFragment) mContent).mVideoView.setShowMediaController(true);
		}
		Animation anim = AnimationUtil.SlideOutLeftRight(this);
		anim.setAnimationListener(this);
		layoutSecondScreen.setAnimation(anim);
	}

	private class CustomWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			view.clearCache(true);
			view.clearHistory();

			super.onPageFinished(view, url);
		}
	}

	private class CustomWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			loadingBar.setVisibility(View.VISIBLE);
			loadingBar.setProgress(newProgress);
			if (newProgress == 100) {
				loadingBar.setVisibility(View.GONE);
			}
		}
	}

	public ProfileInfo getProfile(){
		return pProfile;
	}
}
