package mdn.vtvpluspro.fragment;

import java.util.ArrayList;

import mdn.vtvplus.R;
import mdn.vtvpluspro.BaseSlideMenuActivity;
import mdn.vtvpluspro.network.ApiManager;
import mdn.vtvpluspro.network.IApiCallback;
import mdn.vtvpluspro.object.objMenu;
import mdn.vtvpluspro.slidingmenu.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.analytics.tracking.android.Log;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class CategoryVideoFragment extends BaseFragment {
	private View view;
	private ArrayList<objMenu> arrMenuTmp;
	private ViewPager pager;
	private TabPageIndicator indicator;
	private EventTabAdapterFragment mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_category_video, null);
		pager = (ViewPager) view.findViewById(R.id.home_pager);
		indicator = (TabPageIndicator) view
				.findViewById(R.id.home_indicator);
        indicator.setFillViewport(true);
		indicator.setVisibility(View.GONE);
		pager.setVisibility(View.INVISIBLE);
		
		getListCategoryInVideo();

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
		
		return view;
	}

	private void getListCategoryInVideo() {
		arrMenuTmp = new ArrayList<objMenu>();

		ApiManager.callCategoryVideoMenu(getActivity(), new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d("response: " + response);

				try {
					JSONArray arrJson = new JSONArray(response);
					Log.d("arrJson: " + arrJson.length());

					for (int i = 0; i < arrJson.length(); i++) {
						JSONObject obj = arrJson.getJSONObject(i);
						int id = Integer.parseInt(obj.getString("id"));
						String name = obj.getString("name");
						objMenu menu = new objMenu(id, name, false);

						arrMenuTmp.add(menu);
						// add sub category video
						Log.d("add menu: " + name);
					}

					Log.d("response: " + arrMenuTmp);
					pager.setVisibility(View.VISIBLE);
					indicator.setVisibility(View.VISIBLE);
					
					if (mAdapter == null) {
						mAdapter = new EventTabAdapterFragment(
								getActivity().getSupportFragmentManager());
					}
					pager.setAdapter(mAdapter);
					pager.setCurrentItem(0);
					pager.setOffscreenPageLimit(3);
					indicator.setViewPager(pager);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub

			}
		});

		// return mArrMenu;
	}

	class EventTabAdapterFragment extends FragmentPagerAdapter {
		public EventTabAdapterFragment(FragmentManager fm) {
			super(fm);
		}@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			FragmentTransaction trans = mFragmentManager.beginTransaction();
			for (int i = 0; i < arrMenuTmp.size(); i++) {
				Fragment f = ((BaseSlideMenuActivity)getActivity()).getSupportFragmentManager()
						.findFragmentByTag(getFragmentTag(i));
				if (f != null) {
					trans.remove(f);
				}
			}
			trans.commit();

			return null;
		}
		
		private String getFragmentTag(int pos) {
			return "android:switcher:" + R.id.home_pager + ":" + pos;
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
			super.restoreState(state, loader);
		}
		

		
		
		@Override
		public Fragment getItem(int position) {
				Bundle bundle = new Bundle();
				bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 1);
				bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV, String.valueOf(arrMenuTmp.get(position).id));
				VtvPlusFragment mVtvFragment = new VtvPlusFragment();
				mVtvFragment.setArguments(bundle);
				FragmentManager fm = getActivity().getSupportFragmentManager();
				fm.beginTransaction().replace(R.id.home_pager, mVtvFragment);
				fm.beginTransaction().addToBackStack(null);
				fm.beginTransaction().commit();
				
				return mVtvFragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return arrMenuTmp.get(position).name;
		}


		@Override
		public int getCount() {
			return arrMenuTmp.size();
		}
	}
}
