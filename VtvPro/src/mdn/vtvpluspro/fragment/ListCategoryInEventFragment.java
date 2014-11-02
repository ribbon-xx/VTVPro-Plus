package mdn.vtvpluspro.fragment;

import java.util.ArrayList;

import mdn.vtvplus.R;
import mdn.vtvpluspro.fragment.CategoryVideoFragment.EventTabAdapterFragment;
import mdn.vtvpluspro.object.objMenu;
import mdn.vtvpluspro.slidingmenu.SlidingMenu;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TabPageIndicator;

public class ListCategoryInEventFragment extends BaseFragment {

	private View view;
	private ArrayList<objMenu> arrMenuTmp;
	private ViewPager pager;
	private TabPageIndicator indicator;
	private GoogleMusicAdapter mAdapter;
	private static String[] mTabName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_list_category_in_event, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mTabName = getResources().getStringArray(R.array.tab_event);
//		pager.setAdapter(mAdapter);
//		indicator.setViewPager(pager);
		
		if (mAdapter == null) {
			mAdapter = new GoogleMusicAdapter(getActivity()
					.getSupportFragmentManager());
		}
		pager = (ViewPager) getActivity().findViewById(R.id.event_pager);
		indicator = (TabPageIndicator) getActivity().findViewById(R.id.event_indicator);
		
		pager.setAdapter(mAdapter);
		pager.setCurrentItem(0);
//		pager.setOffscreenPageLimit(3);
		indicator.setViewPager(pager);
		
//		 FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
//
//	        ViewPager pager = (ViewPager)findViewById(R.id.pager);
//	        pager.setAdapter(adapter);
//
//	        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
//	        indicator.setViewPager(pager);
	        

		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					// open the slide menu
					baseSlideMenuActivity.getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;

				default:
					// close the slide menu
					baseSlideMenuActivity.getSlidingMenu().setTouchModeAbove(
							SlidingMenu.TOUCHMODE_MARGIN);
					break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

		});
	}

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return TestFragment.newInstance(mTabName[position % mTabName.length]);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTabName[position % mTabName.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return mTabName.length;
		}
	}
}