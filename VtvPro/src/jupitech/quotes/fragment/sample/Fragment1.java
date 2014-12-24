package jupitech.quotes.fragment.sample;

import java.util.ArrayList;

import jupitech.quotes.fragment.sample.sub.Sub1;
import jupitech.quotes.fragment.sample.sub.Sub2;
import jupitech.quotes.fragment.sample.sub.Sub3;
import mdn.vtvsport.BaseSlideMenuActivity;
import mdn.vtvsport.R;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment1 extends Fragment {
	private ArrayList<Fragment> listFragment = null;
	private GoogleMusicAdapter adapter = null;
	private ViewPager pager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		listFragment = new ArrayList<Fragment>();
		listFragment.add(new Sub1());
		listFragment.add(new Sub2());
		listFragment.add(new Sub3());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_one, null);
		Button btNext = (Button) view.findViewById(R.id.btNext);
		btNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nextAction();
			}
		});

		pager = (ViewPager) view.findViewById(R.id.pager);

//		TabPageIndicator indicator = (TabPageIndicator) view
//				.findViewById(R.id.indicator);
//		indicator.setViewPager(pager);

//		pager.setCurrentItem(0);

		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		pager.removeAllViewsInLayout();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		initViewPager();
		
		super.onResume();
	}
	
	private void initViewPager() {
		if (adapter == null) {
			adapter = new GoogleMusicAdapter(getFragmentManager(), listFragment);
		}
		pager.setAdapter(adapter);
	}
	
	private void nextAction() {
//		adapter.removeAll();
		((BaseSlideMenuActivity) getActivity()).switchContent(new Fragment2(), true);
	}

	public class GoogleMusicAdapter extends FragmentPagerAdapter {
		private String[] CONTENT = new String[] { "Recent",
				"Artists", "Albums" };

		private ArrayList<Fragment> list = null;
		public ArrayList<Fragment.SavedState> mSavedState = new ArrayList<Fragment.SavedState>();
		public FragmentManager mFragmetMangerSub;

		public GoogleMusicAdapter(FragmentManager fm, ArrayList<Fragment> listFm) {
			super(fm);
			this.list = listFm;
			this.mFragmetMangerSub = fm;
		}

		@Override
		public Fragment getItem(int position) {
			return list.get(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length];
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
//			Bundle state = null;
//			if (mSavedState.size() > 0) {
//				state = new Bundle();
//				Fragment.SavedState[] fss = new Fragment.SavedState[mSavedState
//						.size()];
//				mSavedState.toArray(fss);
//				state.putParcelableArray("states", fss);
//			}
//			for (int i = 0; i < list.size(); i++) {
//				Fragment f = list.get(i);
//				if (f != null) {
//					if (state == null) {
//						state = new Bundle();
//					}
//					String key = "f" + i;
//					getFragmentManager().putFragment(state, key, f);
//				}
//			}

			
			for (int i = 0; i < list.size(); i++) {
				FragmentTransaction trans = getFragmentManager().beginTransaction();
				Fragment f = mFragmetMangerSub.findFragmentByTag(getFragmentTag(i));
				if (f != null) {
					trans.remove(f);
					trans.commit();
				}
			}

			return null;
		}

		private String getFragmentTag(int pos) {
			return "android:switcher:" + R.id.pager + ":" + pos;
		}

		public void update() {
			for (int i = 0; i < list.size(); i++) {
				Fragment f = getActivity().getSupportFragmentManager()
						.findFragmentByTag(getFragmentTag(i));
				if (i == 0 && (f != null)) {
					((Sub1) f).update();
				}

				if (i == 1 && (f != null)) {
					((Sub2) f).update();
				}

				if (i == 2 && (f != null)) {
					((Sub3) f).update();
				}
			}
		}
		
		public void removeAll() {
			for (int i = 0; i < list.size(); i++) {
				FragmentTransaction trans = getFragmentManager().beginTransaction();
				Fragment f = mFragmetMangerSub.findFragmentByTag(getFragmentTag(i));
				if (f != null) {
					trans.remove(f);
				}
				trans.commit();
			}
		}
			

//		@Override
//		public void restoreState(Parcelable state, ClassLoader loader) {
//			if (state != null) {
//				Bundle bundle = (Bundle) state;
//				bundle.setClassLoader(loader);
//				Parcelable[] fss = bundle.getParcelableArray("states");
//				mSavedState.clear();
//				list.clear();
//				if (fss != null) {
//					for (int i = 0; i < fss.length; i++) {
//						mSavedState.add((Fragment.SavedState) fss[i]);
//					}
//				}
//				Iterable<String> keys = bundle.keySet();
//				for (String key : keys) {
//					if (key.startsWith("f")) {
//						int index = Integer.parseInt(key.substring(1));
//						Fragment f = getFragmentManager().getFragment(bundle,
//								key);
//						if (f != null) {
//							while (list.size() <= index) {
//								list.add(null);
//							}
//							f.setMenuVisibility(false);
//							list.set(index, f);
//						} else {
//						}
//					}
//				}
//			}
			
			
//		}
	}
}
