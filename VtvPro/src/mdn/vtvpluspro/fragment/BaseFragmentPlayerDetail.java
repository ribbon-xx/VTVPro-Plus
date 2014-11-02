package mdn.vtvpluspro.fragment;

import mdn.vtvpluspro.BaseSlideMenuActivity;
import mdn.vtvpluspro.common.SharedPreferenceManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragmentPlayerDetail extends Fragment {
	protected PlayerFragment mPlayerFragment;
	protected LayoutInflater mInflater;
	protected BaseSlideMenuActivity baseSlideMenuActivity;

	protected FragmentManager mFragmentManager;
	protected SharedPreferenceManager sharedPreferenceManager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		return super.onCreateView(inflater, container, savedInstanceState);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		baseSlideMenuActivity = (BaseSlideMenuActivity) getActivity();
		mPlayerFragment = (PlayerFragment) baseSlideMenuActivity.mContent;
		mFragmentManager = getFragmentManager();
		sharedPreferenceManager = SharedPreferenceManager
				.getInstance(baseSlideMenuActivity);
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * To remove previous dialog is showed
	 * 
	 * @param tag
	 */
	protected void removePreviousDialog(String tag) {
		Fragment fragment = mFragmentManager.findFragmentByTag(tag);
		if (fragment != null) {
			FragmentTransaction transaction = mFragmentManager
					.beginTransaction();
			transaction.remove(fragment);
			transaction.commitAllowingStateLoss();
		}

	}
	
}
