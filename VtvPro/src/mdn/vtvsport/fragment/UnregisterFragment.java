package mdn.vtvsport.fragment;

import mdn.vtvsport.R;
import mdn.vtvsport.common.ImageUtility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class UnregisterFragment extends BaseFragment {

	private ImageView ivUnregister;
	private int mTypeCategory = 0; //0_channel,1_vod,2_series of vod, 3_search,
	// -2 favourist; -1 listnew; -3 listhot
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_unregister, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		baseSlideMenuActivity.setTextCategory(getString(R.string.slidemenu_huy_dk));
		ivUnregister = (ImageView) getView().findViewById(R.id.iv_unregister);
		String imageUri = "drawable://" + R.drawable.huy;
		ImageUtility.loadBitmapFromUrl(getActivity(), imageUri, ivUnregister);
	}
	
	@Override
	protected void initUiTabbar() {
		// TODO Auto-generated method stub
		super.initUiTabbar();

//		baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
		baseSlideMenuActivity.iconSetting.setVisibility(View.GONE);
		baseSlideMenuActivity.iconBack.setVisibility(View.VISIBLE);
		baseSlideMenuActivity.iconVtvPlus.setVisibility(View.GONE);
		if (mTypeCategory != 3) {
			baseSlideMenuActivity.closeViewSearch();
		}
	}
}
