package mdn.vtvsport.fragment.account;

import mdn.vtvsport.R;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.fragment.BaseFragment;
import mdn.vtvsport.fragment.HomeFragment;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.account.UserInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserInfoFragment extends BaseFragment implements OnClickListener {
	private TextView tvUserName;
	private TextView tvCurrentService;
	private TextView tvExpiryDate;
	private Button btRegCharge;

	private TextView tvNoCharge;
	private LinearLayout layInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_user_info, container,
				false);

		bindView(view);
		initLayout();
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
		tvUserName = (TextView) view.findViewById(R.id.tvUserName);
		tvCurrentService = (TextView) view.findViewById(R.id.tvCurrentService);
		tvExpiryDate = (TextView) view.findViewById(R.id.tvExpiryDate);
		Button btLogout = (Button) view.findViewById(R.id.btLogout);
		btLogout.setOnClickListener(this);
		btRegCharge = (Button) view.findViewById(R.id.btRegCharge);
		btRegCharge.setOnClickListener(this);
		btRegCharge.setVisibility(View.GONE);
		tvNoCharge = (TextView) view.findViewById(R.id.tvNoCharge);
		tvNoCharge.setVisibility(View.GONE);
		layInfo = (LinearLayout) view.findViewById(R.id.layInfo);
	}

	private void initLayout() {
		// if (baseSlideMenuActivity.pProfile.pTypePack >= 1) {
		 updateUi();
		// } else {
//		callGetUserInfo();
		// }
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
						updateUi();
					} else {
						baseSlideMenuActivity.pProfile.pTypePack = 2;
						updateUi();
					}
				} else {
					if (info.getLiveChannel().equals("Invalid session ID")) {
						DialogManager.alert(baseSlideMenuActivity,
								baseSlideMenuActivity
										.getString(R.string.error_logout));
						baseSlideMenuActivity.logoutAccount();
						HomeFragment.IS_CHANGE_DATA = true;
					} else {
						DialogManager.alert(baseSlideMenuActivity,
								info.getLiveChannel());
					}
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btLogout:
			baseSlideMenuActivity.logoutAccount();
			HomeFragment.IS_CHANGE_DATA = true;
			break;
		case R.id.btRegCharge:
			nextFragmentChargePackage();
			break;
		default:
			break;
		}
	}

	private void updateUi() {
		if (baseSlideMenuActivity.pProfile.pTypePack == 1) {
			layInfo.setVisibility(View.VISIBLE);
			tvUserName.setText(baseSlideMenuActivity.pProfile.pUserName);
			tvCurrentService.setText(baseSlideMenuActivity.pProfile.pNamePack);
			tvExpiryDate.setText(baseSlideMenuActivity.pProfile.pDayPack);
			btRegCharge.setVisibility(View.GONE);
			tvNoCharge.setVisibility(View.GONE);
		} else if (baseSlideMenuActivity.pProfile.pTypePack == 2) {
			layInfo.setVisibility(View.GONE);
			btRegCharge.setVisibility(View.VISIBLE);
			tvNoCharge.setVisibility(View.VISIBLE);
		}
	}

	private void nextFragmentChargePackage() {
		WebviewChargeFragment fr = new WebviewChargeFragment();
		if (baseSlideMenuActivity.isMainFragment()) {
			baseSlideMenuActivity.switchContent(fr, true);
		} else {
			baseSlideMenuActivity.switchContent(fr, false);
		}
	}
}
