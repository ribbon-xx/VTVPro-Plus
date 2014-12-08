package mdn.vtvpluspro.fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import mdn.vtvplus.R;
import mdn.vtvpluspro.common.ImageUtility;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class RegisterFragment extends BaseFragment {

	private ImageView ivRegister;
	
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
}
