package mdn.vtvsport.adapter;

import java.util.ArrayList;

import mdn.vtvsport.BaseSlideMenuActivity;
import mdn.vtvsport.R;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

public class PlayerDetailsPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> listFragments;
	private FragmentManager mFragmentManager;
	private Context mContext;

	public PlayerDetailsPagerAdapter(Context mContext, FragmentManager fm,
			ArrayList<Fragment> listFragments) {
		super(fm);
		this.listFragments = listFragments;
		this.mFragmentManager = fm;
		this.mContext = mContext;
	}

	@Override
	public Fragment getItem(int position) {
		return listFragments.get(position);
	}

	@Override
	public int getCount() {
		return listFragments.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		FragmentTransaction trans = mFragmentManager.beginTransaction();
		for (int i = 0; i < listFragments.size(); i++) {
			Fragment f = ((BaseSlideMenuActivity)mContext).getSupportFragmentManager()
					.findFragmentByTag(getFragmentTag(i));
			if (f != null) {
				trans.remove(f);
			}
		}
		trans.commit();

		return null;
	}
	
	private String getFragmentTag(int pos) {
		return "android:switcher:" + R.id.layInfoPager + ":" + pos;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		super.restoreState(state, loader);
	}
}
