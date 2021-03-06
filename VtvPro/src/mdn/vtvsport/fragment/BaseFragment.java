package mdn.vtvsport.fragment;

import mdn.vtvsport.BaseSlideMenuActivity;
import mdn.vtvsport.R;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.SharedPreferenceManager;
import mdn.vtvsport.util.IntentUtil;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class BaseFragment extends Fragment {
	protected LayoutInflater mInflater;
	protected BaseSlideMenuActivity baseSlideMenuActivity;
	protected SharedPreferenceManager sharedPreferenceManager;

	// fragment manager
	protected FragmentManager mFragmentManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		return super.onCreateView(inflater, container, savedInstanceState);

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		baseSlideMenuActivity.mContent = getFragmentManager().findFragmentById(R.id.layoutContent);
		initUiTabbar();
		super.onViewCreated(view, savedInstanceState);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		baseSlideMenuActivity = (BaseSlideMenuActivity) getActivity();
		mFragmentManager = getFragmentManager();
		sharedPreferenceManager = SharedPreferenceManager
				.getInstance(baseSlideMenuActivity);

		super.onCreate(savedInstanceState);
	}
	
	public void actionInteract() {
		
	}
	
	protected void initUiTabbar() {
		
	}

	protected void expandListView(ListView listView, int maxHeight) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem != null) {
				listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
				totalHeight += listItem.getMeasuredHeight();
			}
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		int actualListHeight = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		if (maxHeight == 0) {
			params.height = actualListHeight;
		} else {
			params.height = Math.min(actualListHeight, maxHeight);
		}
		listView.setLayoutParams(params);
		listView.setFocusable(false);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void showDialogLock() {
		DialogManager
				.alertWith2Status(
						baseSlideMenuActivity,
						baseSlideMenuActivity.getString(R.string.dialog_down_full_version),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								IntentUtil.openWebView(baseSlideMenuActivity, baseSlideMenuActivity.pVersion.getAppProUrl());
							}
						});
	}
	
	public void clearDataFragment() {
		
	}

}
