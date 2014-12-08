package mdn.vtvpluspro.fragment;

import mdn.vtvplus.R;
import mdn.vtvpluspro.common.ImageUtility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class UnregisterFragment extends BaseFragment {

	private ImageView ivUnregister;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_unregister, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		ivUnregister = (ImageView) getView().findViewById(R.id.iv_unregister);
		String imageUri = "drawable://" + R.drawable.huy;
		ImageUtility.loadBitmapFromUrl(getActivity(), imageUri, ivUnregister);
	}
}
