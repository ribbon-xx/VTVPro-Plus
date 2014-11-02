package mdn.vtvpluspro.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import mdn.vtvpluspro.ChargingActivity;
import mdn.vtvpluspro.common.SharedPreferenceManager;
import mdn.vtvpluspro.object.account.ProfileInfo;
import mdn.vtvpluspro.util.HLog;
import mdn.vtvpluspro.util.Utils;

/**
 * Created by hieuxit on 8/20/14.
 */
public abstract class ChargingFragment extends BaseFragment {

	private ChargingActivity mChargingActivity;
	private ProfileInfo mProfile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mChargingActivity = (ChargingActivity) getActivity();
		mProfile = getProfileInner();
		if (!needCheckCharging() || mProfile == null) {
			return;
		}
		checkChargingAndPopupIfNeeded(true);
	}

	public void checkChargingAndPopupIfNeeded(boolean isFromPlayerFragment){
		mProfile = getProfileInner();
		HLog.i("****** Check charging popup ******** "+mProfile.pSession + ", and md5 is: "+
		Utils.md5(mProfile.pSession) +", pref: "+SharedPreferenceManager.getInstance(mChargingActivity).getSessionCache()+
		"pref checked: "+ SharedPreferenceManager.getInstance(mChargingActivity).getSessionChecked());
		if (!TextUtils.isEmpty(mProfile.pSession) &&
				Utils.md5(mProfile.pSession).equals(SharedPreferenceManager.getInstance(mChargingActivity).getSessionCache())) {
			// Da dang ky roi
			return;
		}
		boolean checked = false;
		// check right session here
		String sessionChecked = SharedPreferenceManager.getInstance(mChargingActivity).getSessionChecked();
		if (!TextUtils.isEmpty(mProfile.pSession) &&
				!Utils.md5(mProfile.pSession).equals(sessionChecked)) {
			checked = true;
		}
		HLog.i("goto step 1, checked: " + checked);
		if (checked) {
			// request server for right session
			mChargingActivity.callCheckChargeEnable(false, isFromPlayerFragment);
		} else {
			// show popup
			mChargingActivity.callCheckChargeEnable(true, isFromPlayerFragment);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mChargingActivity = null;
	}

	private ProfileInfo getProfileInner() {
		return mChargingActivity == null ? null : mChargingActivity.getProfile();
	}

	public ProfileInfo getProfile(){
		return mProfile;
	}

	protected abstract boolean needCheckCharging();
}
