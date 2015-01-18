package mdn.vtvsport.fragment;

import mdn.vtvsport.R;
import mdn.vtvsport.common.ImageUtility;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class RegisterFragment extends BaseFragment {

	private ImageView ivRegister;
	private int mTypeCategory = 0; //0_channel,1_vod,2_series of vod, 3_search,
	// -2 favourist; -1 listnew; -3 listhot
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_guide_register, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		ivRegister = (ImageView) getView().findViewById(R.id.iv_register);
		
//		String imageUri = "assets://image.png"; // from assets
		String imageUri = "drawable://" + R.drawable.dangky;
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.cacheInMemory()
		.cacheOnDisc()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.showImageForEmptyUri(R.drawable.no_image)
		.showImageOnFail(R.drawable.no_image)
		.showStubImage(R.drawable.no_image)
	    .imageScaleType(ImageScaleType.NONE)
	    .build();
		
		ImageUtility.loadFullBitmapFromUrl(getActivity(), imageUri, ivRegister, options);
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
