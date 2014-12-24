package mdn.vtvsport.fragment.account;

import mdn.vtvsport.R;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.common.SharedPreferenceManager;
import mdn.vtvsport.fragment.ChargingFragment;
import mdn.vtvsport.fragment.HomeFragment;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.network.WebServiceParam;
import mdn.vtvsport.object.account.UserInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends ChargingFragment implements OnClickListener {
	private EditText edUser;
	private EditText edPass;

	private boolean isFromPlayerFragment = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		bindView(view);
		initView();
		return view;
	}

	@Override
	protected void initUiTabbar() {
		// TODO Auto-generated method stub
		super.initUiTabbar();

		baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
		baseSlideMenuActivity.closeViewSearch();
		baseSlideMenuActivity.iconSetting.setVisibility(View.VISIBLE);
		baseSlideMenuActivity.iconBack.setVisibility(View.GONE);
		baseSlideMenuActivity.iconVtvPlus.setVisibility(View.VISIBLE);
	}

	private void bindView(View view) {
		edUser = (EditText) view.findViewById(R.id.edUserName);
		edPass = (EditText) view.findViewById(R.id.edPassword);
		Button btLogin = (Button) view.findViewById(R.id.btLogin);
		btLogin.setOnClickListener(this);
		Button btReg = (Button) view.findViewById(R.id.btRegis);
		btReg.setOnClickListener(this);
	}

	private void initView() {
		Bundle bundle = getArguments();
		if (bundle != null) {
			edUser.setText(bundle.getString(WebServiceParam.PARAM_USER_NAME));
			edPass.setText(bundle.getString(WebServiceParam.PARAM_PASS));
			callLogin();

			try {
				isFromPlayerFragment = bundle.getBoolean("from_player");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btLogin:
				callLogin();
				break;
			case R.id.btRegis:
//				callReg();
				checkChargingAndPopupIfNeeded(false);
				break;

			default:
				break;
		}
	}

	private void callLogin() {
		if (!checkValue()) {
			return;
		}
		baseSlideMenuActivity.callLoginApi(new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				UserInfo info = ParserManager.parserLoginInfo(response);
				if (info.isGetSucc()) {
					baseSlideMenuActivity.pProfile.pUserName = edUser.getText()
							.toString().trim();
					baseSlideMenuActivity.pProfile.pPassword = edPass.getText()
							.toString().trim();
//					baseSlideMenuActivity.pProfile.pSession = info.getMessage();
					Log.i("hieuth", "-------- Login Fragment --------- " + baseSlideMenuActivity.pProfile.pSession);
					SharedPreferenceManager.getInstance(baseSlideMenuActivity)
							.setUserInfo(
									baseSlideMenuActivity.pProfile.pUserName,
									baseSlideMenuActivity.pProfile.pPassword);
					
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
					} else {
						baseSlideMenuActivity.pProfile.pTypePack = 2;
					}

					baseSlideMenuActivity.mMenuFragment.initLayout();

					if (isFromPlayerFragment)
					// update user info and return to player screen
					{
						baseSlideMenuActivity.onPressBack();
//						callGetUserInfo();
					} else {
						baseSlideMenuActivity.switchContent(
								new UserInfoFragment(), false);
					}
					
					HomeFragment.IS_CHANGE_DATA = true;
				}else {
					DialogManager.alert(baseSlideMenuActivity,
							info.getMessage());
				}
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				DialogManager.alert(baseSlideMenuActivity,
						getString(R.string.network_fail));
			}
		}, edUser.getText().toString(), edPass.getText().toString());
	}

	private void callReg() {
		baseSlideMenuActivity.switchContent(new RegisterFragment(), false);
	}

	private boolean checkValue() {
		String strEmail = edUser.getText().toString().trim();
		String strPass = edPass.getText().toString().trim();
		if (strEmail.length() == 0 || strPass.length() == 0) {
			Toast.makeText(baseSlideMenuActivity,
					getString(R.string.full_info), Toast.LENGTH_SHORT).show();
			return false;
		}
		
		// ignore
//		if (!StringUtil.checkEmail(strEmail)) {
//			Toast.makeText(baseSlideMenuActivity,
//					getString(R.string.erorr_email), Toast.LENGTH_SHORT).show();
//			return false;
//		}

		return true;
	}

	private void callGetUserInfo() {
		baseSlideMenuActivity.callAccountInfoApi(new IApiCallback() {

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
					} else {
						baseSlideMenuActivity.pProfile.pTypePack = 2;
					}

					baseSlideMenuActivity.onPressBack();
				} else {
					DialogManager.alert(baseSlideMenuActivity,
							info.getLiveChannel());
				}
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				DialogManager.alert(baseSlideMenuActivity,
						getString(R.string.network_fail));
			}
		});
	}

	@Override
	protected boolean needCheckCharging() {
		return false;
	}
}
