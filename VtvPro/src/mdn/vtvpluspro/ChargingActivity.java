package mdn.vtvpluspro;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import mdn.vtvplus.R;
import mdn.vtvpluspro.common.DialogManager;
import mdn.vtvpluspro.common.ParserManager;
import mdn.vtvpluspro.common.SharedPreferenceManager;
import mdn.vtvpluspro.fragment.HomeFragment;
import mdn.vtvpluspro.fragment.account.UserInfoFragment;
import mdn.vtvpluspro.network.ApiManager;
import mdn.vtvpluspro.network.IApiCallback;
import mdn.vtvpluspro.network.WebServiceConfig;
import mdn.vtvpluspro.object.ChargedUserInfo;
import mdn.vtvpluspro.object.ChargingEnableInfo;
import mdn.vtvpluspro.object.ChargingSMSInfo;
import mdn.vtvpluspro.object.Charginginfo;
import mdn.vtvpluspro.object.LoginInfo;
import mdn.vtvpluspro.object.account.UserInfo;
import mdn.vtvpluspro.util.DeviceUtil;
import mdn.vtvpluspro.util.HLog;
import mdn.vtvpluspro.util.SmsUtil;
import mdn.vtvpluspro.util.Utils;

/**
 * Created by hieuxit on 8/11/14.
 */
public class ChargingActivity extends BaseSlideMenuActivity  {

	//	public static final String DISTRIBUTOR_CHANNEL_ID = "9";
	//	private boolean mChargingEnable = true;

	public static final boolean VERSION_WITH_COVER = false;
	//	public static final boolean DK_LIB_IMPLEMENT = false;
	private static final String VIETTEL_OPERATOR = "45204";
	private static final String VINA_OPERATOR = "45202";
	private static final String MOBIL_OPERATOR = "45201";
	private static final String[] OPERATORS_APPID_ENABLE = new String[]{
			VIETTEL_OPERATOR
	};
	private static final long CHECK_APP_ID_TIMEOUT = 2 * 60 * 1000; // 2 minutes timeout
	public static final boolean DEBUG = false;
	public static final String DEBUG_OPERATOR_CODE = VINA_OPERATOR;
	public static final boolean SEND2SMSVERSION = true;
	public static final long SEND2SMS_DURATION = 5 * 1000; //5s
	private boolean mSendSmsBackground;
	private Charginginfo mChargingInfo;
	private String mAppId;

//	private Handler mHandler;
//	private CheckAppIdRunnable mCheckAppIdRunnable;
	private long mCheckAppIdStart;
	private boolean mIsFromPlayerFragment;
	private boolean mOpenSmsComposer;

	@Override
	public void onResume() {
		super.onResume();
		HLog.i("ChargingActivity onResume!!!Check autoogin here: " + mOpenSmsComposer);
//		if (mOpenSmsComposer && (!TextUtils.isEmpty(mAppId) && appIdCheck())) {
//			letCheckAppId();
//			mOpenSmsComposer = false;
//		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HLog.enableLog(DEBUG);
	}

	public void callCheckChargeEnable(final boolean directShowPopup, boolean isFromPlayerFragment) {
		HLog.d("callCheckChargeEnable . . . ");
		mIsFromPlayerFragment = isFromPlayerFragment;
		
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
//					if (directShowPopup && mChargingInfo != null) {
//						doCharge(mChargingInfo);
//					} else {
//						callCheckUserCharged();
//					}
//				} else {
//					Log.i("hieuth", "------- server disable charging popup ------");
//				}
//			}
//
//			@Override
//			public void responseFailWithCode(int statusCode) {
//				Log.e("hieuth", "get flag check charge error: " + statusCode);
//			}
//		});
		
		ApiManager.callCheckSMSSetting(this, new IApiCallback() {
			
			@Override
			public void responseSuccess(String response) {
				try {
					JSONObject objObject = new JSONObject(response);
					String title = objObject.getString("Title");
					String body = objObject.getString("body");
					
					// check the operator
					String operatorCode = DeviceUtil.getTelcoCode(ChargingActivity.this);
					HLog.i("Operator code: " + operatorCode);
//					if ((DEBUG || VERSION_WITH_COVER) && TextUtils.isEmpty(operatorCode)) {
//						operatorCode = DEBUG_OPERATOR_CODE;
//					}
					if (TextUtils.isEmpty(operatorCode)) {
						Toast.makeText(ChargingActivity.this, R.string.charging_sim_not_found, Toast.LENGTH_SHORT).show();
						return;
					}
					
					String type = "VTL";
					if (VIETTEL_OPERATOR.equals(operatorCode)) {
						 type = "VTL";
					} else if (VINA_OPERATOR.equals(operatorCode)) {
						 type = "VMS";
					} else if (MOBIL_OPERATOR.equals(operatorCode)) {
						type = "GPC";
					}
					
					JSONObject obj = objObject.getJSONObject(type);
					String status = obj.getString("status");
					String form_sms = obj.getString("form_sms");
					String return_mt = obj.getString("return_mt");
					String message = obj.getString("message");
					String service = obj.getString("service");
					ChargingSMSInfo info = new ChargingSMSInfo(title, body,
							type, status, form_sms, return_mt, message, service);
					
					doCharge3(info);
					
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void responseFailWithCode(int statusCode) {
				  DialogManager.alert(ChargingActivity.this,
	                        getString(R.string.network_fail));

	                DialogManager.closeProgressDialog();
	                
//				Log.e("hieuth", "get flag check charge error: " + statusCode);
			}
		});
		
	}

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
//						saveChargingSession(pProfile.pSession);
//						return;
//					}
//					// Chua tra tien store not charging
//					saveNotChargingSession(pProfile.pSession);
//					prepareToCallGetChargeSetting();
//				}
//
//				@Override
//				public void responseFailWithCode(int statusCode) {
//					HLog.e("callCheckUserCharged error: " + statusCode);
//				}
//			});
//		} else {
//			if (mChargingInfo == null) {
//				prepareToCallGetChargeSetting();
//			} else {
//				doCharge(mChargingInfo);
//			}
//		}
//	}
//
//	private void prepareToCallGetChargeSetting() {
//		String operatorCode = DeviceUtil.getTelcoCode(this);
//		HLog.i("Operator code: " + operatorCode);
//		if ((DEBUG || VERSION_WITH_COVER) && TextUtils.isEmpty(operatorCode)) {
//			operatorCode = DEBUG_OPERATOR_CODE;
//		}
//		if (TextUtils.isEmpty(operatorCode)) {
//			Toast.makeText(this, R.string.charging_sim_not_found, Toast.LENGTH_SHORT).show();
//			return;
//		}
//		callGetChargeSetting(Config.DISTRIBUTOR_CHANNEL_ID, operatorCode);
//	}

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
//				mChargingInfo = charginginfo;
//				// save charging info
//				SharedPreferenceManager.getInstance(ChargingActivity.this).setChargingInfo(charginginfo);
//				doCharge(charginginfo);
////				doCharge(charginginfo);
//			}
//
//			@Override
//			public void responseFailWithCode(int statusCode) {
//				HLog.e("callGetChargeSetting error: " + statusCode);
//			}
//		}, distributorChannelId, operatorCode);
//	}

//	private void doCharge(final Charginginfo info) {
////		if (info.type != 1 || TextUtils.isEmpty(info.smsGateway) || TextUtils.isEmpty(info.smsBody)) {
////			Log.e("hieuth", "Check payment info error " + info);
////			return;
////		}
////		createPolicyDialog(info).show();
//
//		String operatorCode = DeviceUtil.getTelcoCode(this);
//		if (DEBUG && TextUtils.isEmpty(operatorCode)) {
//			operatorCode = DEBUG_OPERATOR_CODE;
//		}
//		createPolicyDialogV2(info, operatorCode).show();
//	}
	
	/**
	 * Thinhdt
	 * @param info
	 */
	private void doCharge3(final ChargingSMSInfo info) {
		
		String operatorCode = DeviceUtil.getTelcoCode(this);
		if (DEBUG && TextUtils.isEmpty(operatorCode)) {
			operatorCode = DEBUG_OPERATOR_CODE;
		}
		if (TextUtils.isEmpty(operatorCode)) {
			Toast.makeText(ChargingActivity.this, R.string.charging_sim_not_found, Toast.LENGTH_SHORT).show();
			return;
		}
		
		createPolicyDialogV3(info, operatorCode).show();
		
	}
	
	/**
	 * Thinhdt
	 */
	private Dialog createPolicyDialogV3(final ChargingSMSInfo info, final String operatorCode) {
		final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme);
		View view = LayoutInflater.from(this).inflate(R.layout.policy_layout, null);
		TextView message = (TextView) view.findViewById(R.id.dialog_message);
		message.setText(info.getBody());

		TextView title = (TextView) view.findViewById(R.id.dialog_title);
		title.setText(info.getTitle());
		View btOK = view.findViewById(R.id.button_ok);
		View btClose = view.findViewById(R.id.button_close);
		btOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (TextUtils.isEmpty(info.getService()) || TextUtils.isEmpty(info.getMessage())) {
					Toast.makeText(ChargingActivity.this, R.string.charging_operator_not_found, Toast.LENGTH_SHORT).show();
					return;
				}
				SmsUtil.sendSMSByBuildInApp(ChargingActivity.this, info.getService(), info.getMessage());
				
//				if (!SEND2SMSVERSION) {
//					
//					mOpenSmsComposer = true;
//				} else {
//					send3SMS(info, operatorCode);
//				}
			}
		});

		btClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				if (VERSION_WITH_COVER) {
//					checkAndAutoLogin();
//				}
			}
		});
		dialog.setContentView(view);
		dialog.setCancelable(false);
		int height = getResources().getDisplayMetrics().heightPixels;
		dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height / 2);
//		dialog.show();
		return dialog;
		
	}
	
//	private void doChargeV2(final Charginginfo info, String operatorCode) {
//		createPolicyDialogV2(info, operatorCode).show();
//	}
//
//	private Dialog createPolicyDialogV2(final Charginginfo info, final String operatorCode) {
//		final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme);
//		View view = LayoutInflater.from(this).inflate(R.layout.policy_layout, null);
//		TextView message = (TextView) view.findViewById(R.id.dialog_message);
//		message.setText(info.popupBody);
//
//		TextView title = (TextView) view.findViewById(R.id.dialog_title);
//		title.setText(info.popupTitle);
//		View btOK = view.findViewById(R.id.button_ok);
//		View btClose = view.findViewById(R.id.button_close);
//		btOK.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				if (TextUtils.isEmpty(info.smsGateway) || TextUtils.isEmpty(info.smsBody)) {
//					Toast.makeText(ChargingActivity.this, R.string.charging_operator_not_found, Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if (!SEND2SMSVERSION) {
//					SmsUtil.sendSMSByBuildInApp(ChargingActivity.this, info.smsGateway, updateSmsIfNeeded(info.smsBody));
//					mOpenSmsComposer = true;
//				} else {
//					send2SMS(info, operatorCode);
//				}
//			}
//		});
//
//		btClose.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
////				if (VERSION_WITH_COVER) {
////					checkAndAutoLogin();
////				}
//			}
//		});
//		dialog.setContentView(view);
//		dialog.setCancelable(false);
//		int height = getResources().getDisplayMetrics().heightPixels;
//		dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height / 2);
////		dialog.show();
//		return dialog;
//	}

//	private void send3SMS(final ChargingSMSInfo info, final String operatorCode) {
//		SmsUtil.sendSMS(this, info.getService(), updateSmsIfNeeded(info.getMessage()), this);
//	}
	
//	private void send2SMS(Charginginfo info, String operatorCode) {
//		if (VIETTEL_OPERATOR.equals(operatorCode)) {
//			SmsUtil.sendSMS(this, info.smsGateway, updateSmsIfNeeded(info.smsBody), this);
//		} else if (VINA_OPERATOR.equals(operatorCode) || MOBIL_OPERATOR.equals(operatorCode)) {
//			mAppId = DeviceUtil.getDeviceId(this);
//			if (TextUtils.isEmpty(mAppId)) {
//				HLog.e("Prepare to send mo 1(no) but device has not android id");
//				return;
//			}
//			SmsUtil.sendSMS(this, info.nonSmsGateway, updateSmsIfNeeded(info.nonSmsBody), this);
//		}
//	}

//	private Dialog createPolicyDialog(final Charginginfo info) {
//		final Dialog dialog = new Dialog(this, R.style.CustomDialogTheme);
//		View view = LayoutInflater.from(this).inflate(R.layout.policy_layout, null);
//		TextView message = (TextView) view.findViewById(R.id.dialog_message);
//		message.setText(info.popupBody);
//
//		TextView title = (TextView) view.findViewById(R.id.dialog_title);
//		title.setText(info.popupTitle);
//		View btOK = view.findViewById(R.id.button_ok);
//		View btClose = view.findViewById(R.id.button_close);
//		btOK.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//				if (VERSION_WITH_COVER) {
//					checkAndAutoLogin();
//				} else {
//					SmsUtil.sendSMS(ChargingActivity.this, info.smsGateway, updateSmsIfNeeded(info.smsBody), ChargingActivity.this);
//				}
//			}
//		});
//
//		btClose.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
////				if (VERSION_WITH_COVER) {
////					checkAndAutoLogin();
////				}
//			}
//		});
//		dialog.setContentView(view);
//		dialog.setCancelable(false);
//		int height = getResources().getDisplayMetrics().heightPixels;
//		dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, height / 2);
////		dialog.show();
//		return dialog;
//	}

//	private boolean appIdCheck() {
//		String operatorCode = DeviceUtil.getTelcoCode(this);
//		if (DEBUG && TextUtils.isEmpty(operatorCode)) {
//			operatorCode = DEBUG_OPERATOR_CODE;
//		}
//		if (TextUtils.isEmpty(operatorCode)) {
//			return false;
//		}
//		return Utils.arrayContains(operatorCode, OPERATORS_APPID_ENABLE);
//	}

//	private String updateSmsIfNeeded(String smsBody) {
//		HLog.d("AppIDCheck: " + appIdCheck());
//		if (appIdCheck() || SEND2SMSVERSION) {
//			String appId = "";
//			try {
//				appId = DeviceUtil.getDeviceId(this);
//			} catch (Exception e) {
//				HLog.e("getDeviceId error: " + e);
//			}
//			if (TextUtils.isEmpty(appId)) {
//				appId = String.valueOf(System.currentTimeMillis() + (long) (Math.random() * 1000));
//			}
//			mAppId = appId;
//			smsBody = smsBody.trim() + " " + mAppId;
//			HLog.d("sms body: " + smsBody);
//		}
//		return smsBody;
//	}

	private void saveChargingSession(String session) {
		SharedPreferenceManager.getInstance(this).setSessionCache(Utils.md5(session));
		SharedPreferenceManager.getInstance(this).setSessionChecked("");
	}

	private void saveNotChargingSession(String session) {
		SharedPreferenceManager.getInstance(this).setSessionChecked(Utils.md5(session));
	}

//	private void letCheckAppId() {
////		ApiManager.callCheckAppId(this, new IApiCallback() {
////			@Override
////			public void responseSuccess(String response) {
////				HLog.i("callCheckAppId response: " + response);
////				LoginInfo loginInfo = LoginInfo.fromResponse(response);
////				if (TextUtils.isEmpty(loginInfo.mAccountName) || TextUtils.isEmpty(loginInfo.mAccountPassword)) {
////					HLog.e("Response empty for login account");
////				}
////			}
////
////			@Override
////			public void responseFailWithCode(int statusCode) {
////				HLog.e("callCheckAppId failure " + statusCode);
////			}
////		}, mAppId);
//		mCheckAppIdStart = System.currentTimeMillis();
//		if (mHandler == null) {
//			mHandler = new Handler();
//		}
//		if (mCheckAppIdRunnable == null) {
//			mCheckAppIdRunnable = new CheckAppIdRunnable();
//		}
//		mHandler.removeCallbacks(mCheckAppIdRunnable);
//		mHandler.postDelayed(mCheckAppIdRunnable, 1000);
//	}

//	@Override
//	public void onSuccess(String gateway) {
//		// onSmsSentSuccess
//		// check appId enable and mAppId not empty
////		if (!TextUtils.isEmpty(mAppId) && appIdCheck()) {
////			// let check
////			letCheckAppId();
////			return;
////		}
////		if (mChargingInfo != null) {
////			if (gateway.equals(mChargingInfo.nonSmsGateway)) {
////				if (mHandler == null) {
////					mHandler = new Handler();
////				}
////				mHandler.postDelayed(new Runnable() {
////					@Override
////					public void run() {
////						Context context = ChargingActivity.this;
////						if (context == null) {
////							HLog.e("Prepare to send sms mo, but activity is destroyed");
////							return;
////						}
////						SmsUtil.sendSMS(context, mChargingInfo.smsGateway, mChargingInfo.smsBody, ChargingActivity.this);
////					}
////				}, SEND2SMS_DURATION);
////			} else if (gateway.equals(mChargingInfo.smsGateway)) {
////				letCheckAppId();
////			}
////		}
////		HLog.i("Send sms success but sim is not viettel operator or app id is null: " + gateway);
//	}

	private void failureToWaitAccount() {
		Toast.makeText(this, "CheckAppId from server failure, timeout exception", Toast.LENGTH_LONG).show();
	}

//	@Override
//	public void onFailure(String gateway) {
//		// onSmsSentFailure
//		HLog.e("Send sms failure: " + gateway);
//		Toast.makeText(this, R.string.charging_send_sms_error, Toast.LENGTH_LONG).show();
////		if (DEBUG) {
////			mAppId = "20234";
////			letCheckAppId();
////		}
//		String operatorCode = DeviceUtil.getTelcoCode(this);
//		if (DEBUG && TextUtils.isEmpty(operatorCode)) {
//			operatorCode = DEBUG_OPERATOR_CODE;
//		}
//		if (VINA_OPERATOR.equals(operatorCode) || MOBIL_OPERATOR.equals(operatorCode)) {
//			HLog.w("onFailure but send to onSuccess " + gateway);
//			onSuccess(gateway);
//		}
//	}

	private void login(final String userName, final String password) {
		callLoginApi(new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				mdn.vtvpluspro.object.account.UserInfo info = ParserManager.parserLoginInfo(response);
				if (info.isGetSucc()) {
					pProfile.pUserName = userName;
					pProfile.pPassword = password;
					pProfile.pSession = info.getSession();
					Log.i("hieuth", "login success here!!! " + pProfile.pSession + ", fromPayer: " + mIsFromPlayerFragment);
					SharedPreferenceManager.getInstance(ChargingActivity.this)
							.setUserInfo(pProfile.pUserName, pProfile.pPassword);
//					saveChargingSession(pProfile.pSession);
					if (mMenuFragment != null) {
						mMenuFragment.initLayout();
					}
//					switchContent(new UserInfoFragment(), false);
//					if (mIsFromPlayerFragment) {
//						// update user info and return to player screen
//						callGetUserInfo();
//					} else {
//						switchContent(new UserInfoFragment(), false);
//					}
					
					switchContent(new UserInfoFragment(), false);
					HomeFragment.IS_CHANGE_DATA = true;
				} else {
					DialogManager.alert(ChargingActivity.this, info.getMessage());
				}
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				DialogManager.alert(ChargingActivity.this, getString(R.string.network_fail));
			}
		}, userName, password);
	}

	private void callGetUserInfo() {
		callAccountInfoApi(new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				UserInfo info = ParserManager.parserUserInfo(response);
				if (info.isGetSucc()) {
					if (info.isRegister()) {
						pProfile.pTypePack = 1;
						pProfile.pIdChannel = info
								.getLiveChannel();
						pProfile.pNamePack = info
								.getCurrentService();
						pProfile.pDayPack = info
								.getExpiryDate();
					} else {
						pProfile.pTypePack = 2;
					}
					onPressBack();
				} else {
					DialogManager.alert(ChargingActivity.this, info.getLiveChannel());
				}
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				DialogManager.alert(ChargingActivity.this, getString(R.string.network_fail));
			}
		});
	}

	private void checkAndAutoLogin() {
		if (DEBUG) {
			mAppId = "20234";
		} else {
			mAppId = DeviceUtil.getDeviceId(this);
		}
		if (TextUtils.isEmpty(mAppId)) {
			return;
		}
//		letCheckAppId();
//		ApiManager.callCheckAppId(ChargingActivity.this, new IApiCallback() {
//			@Override
//			public void responseSuccess(String response) {
//				HLog.i("callCheckAppId response: " + response);
//				LoginInfo loginInfo = LoginInfo.fromResponse(response);
//				if (TextUtils.isEmpty(loginInfo.mAccountName) || TextUtils.isEmpty(loginInfo.mAccountPassword)) {
//					HLog.e("Response empty for login account");
//					return;
//				}
//				login(loginInfo.mAccountName, loginInfo.mAccountPassword);
//				// success login
//			}
//
//			@Override
//			public void responseFailWithCode(int statusCode) {
//				HLog.e("callCheckAppId failure " + statusCode);
//			}
//		}, mAppId);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		HLog.i("Charging activity result: " + requestCode + ", " + resultCode);
		Bundle args = data.getExtras();
		Utils.lookUp(args);
	}

//	class CheckAppIdRunnable implements Runnable {
//		@Override
//		public void run() {
//			ApiManager.callCheckAppId(ChargingActivity.this, new IApiCallback() {
//				@Override
//				public void responseSuccess(String response) {
//					HLog.i("callCheckAppId response: " + response);
//					LoginInfo loginInfo = LoginInfo.fromResponse(response);
//					if (TextUtils.isEmpty(loginInfo.mAccountName) || TextUtils.isEmpty(loginInfo.mAccountPassword)) {
//						HLog.e("Response empty for login account");
//						if (System.currentTimeMillis() - mCheckAppIdStart > CHECK_APP_ID_TIMEOUT) {
//							// failure
//							failureToWaitAccount();
//							return;
//						} else {
//							if (DEBUG && System.currentTimeMillis() - mCheckAppIdStart > 90 * 1000) {
//								mAppId = "20234";
//							}
//							mHandler.postDelayed(CheckAppIdRunnable.this, 3000);
//							return;
//						}
//					}
//					SharedPreferenceManager.getInstance(ChargingActivity.this)
//							.setUserInfo(loginInfo.mAccountName, loginInfo.mAccountPassword);
//					login(loginInfo.mAccountName, loginInfo.mAccountPassword);
//					// success login
//				}
//
//				@Override
//				public void responseFailWithCode(int statusCode) {
//					HLog.e("callCheckAppId failure " + statusCode);
//				}
//			}, mAppId);
//			HLog.i("Call check logined with app id: " + mAppId);
//		}
//	}
}
