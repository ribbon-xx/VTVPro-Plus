package mdn.vtvsport.fragment;

import java.util.ArrayList;

import mdn.vtvsport.R;
import mdn.vtvsport.adapter.Adapter4AllInteraction;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ImageUtility;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.ItemInteraction;
import mdn.vtvsport.util.DeviceUtil;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class AllInteractionFragment extends BaseFragment {
	private ListView lvInteraction;
	private TextView tvNoPlus;
	private ArrayList<ItemInteraction> arrInteraction;
	private Adapter4AllInteraction adapter;
	private Animation mAnimation;

	public AllInteractionFragment() {
		arrInteraction = null;
		adapter = null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.mAnimation = AnimationUtils.loadAnimation(baseSlideMenuActivity,
				R.anim.slide_in_scale);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (DeviceUtil.isTablet(baseSlideMenuActivity)) {
			baseSlideMenuActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		} else {
			baseSlideMenuActivity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		baseSlideMenuActivity.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		baseSlideMenuActivity.getLayoutTitle().setVisibility(View.VISIBLE);

		View view = inflater.inflate(R.layout.fragment_all_interaction,
				container, false);
		bindView(view);
		bindAction();
		return view;
	}

	@Override
	protected void initUiTabbar() {
		// TODO Auto-generated method stub
		super.initUiTabbar();

		baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
		baseSlideMenuActivity.iconSetting.setVisibility(View.GONE);
		baseSlideMenuActivity.iconBack.setVisibility(View.VISIBLE);
		baseSlideMenuActivity.iconVtvPlus.setVisibility(View.GONE);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (adapter != null) {
			adapter.clear();
			adapter = null;
		}
		arrInteraction = null;
	}

	private void bindView(View view) {
		lvInteraction = (ListView) view.findViewById(R.id.lvAllInteraction);
		lvInteraction.setOnItemClickListener(listener);
		tvNoPlus = (TextView) view.findViewById(R.id.tvNoPlus);
		tvNoPlus.setVisibility(View.GONE);
	}

	private void bindAction() {
		callData();
	}

	private void callData() {
		if (arrInteraction != null) {
			if (adapter == null) {
				adapter = new Adapter4AllInteraction(baseSlideMenuActivity, -1,
						arrInteraction);
			}
			lvInteraction.setAdapter(adapter);
			return;
		}

		ApiManager.callAllInteraction(baseSlideMenuActivity,
				new IApiCallback() {

					@Override
					public void responseSuccess(String response) {
						// TODO Auto-generated method stub
						ArrayList<ItemInteraction> result = ParserManager
								.parserAllInteraction(response);
						if (result != null) {
							lvInteraction.setVisibility(View.VISIBLE);
							tvNoPlus.setVisibility(View.GONE);
							if (arrInteraction != null) {
								arrInteraction.clear();
								arrInteraction.addAll(result);
							} else {
								arrInteraction = result;
							}

							if (adapter == null) {
								adapter = new Adapter4AllInteraction(
										baseSlideMenuActivity, -1,
										arrInteraction);
								lvInteraction.setAdapter(adapter);
							} else {
								adapter.notifyDataSetChanged();
							}
						} else {
							lvInteraction.setVisibility(View.GONE);
							tvNoPlus.setVisibility(View.VISIBLE);
						}

					}

					@Override
					public void responseFailWithCode(int statusCode) {
						// TODO Auto-generated method stub
						DialogManager.alert(baseSlideMenuActivity,
								getString(R.string.network_fail));
					}
				});
	}

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			ItemInteraction item = (ItemInteraction) arg0
					.getItemAtPosition(arg2);
			if (item.getType() == 0) {
				baseSlideMenuActivity.showSecondScreen(item.getUrl());
			} else if (item.getType() == 1) {
				actionInteractionImage(item.getUrl());
			} else {
				actionInteractionVideo(item.getUrl());
			}
		}
	};

	private void actionInteractionImage(String urlLink) {
		if (urlLink == null || urlLink.length() == 0) {
			return;
		}
		DialogManager.showSimpleProgressDialog(baseSlideMenuActivity);
		ImageUtility.loadBitmapFromUrl(baseSlideMenuActivity, urlLink,
				new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub
						Toast.makeText(baseSlideMenuActivity,
								getString(R.string.connect_fail),
								Toast.LENGTH_SHORT).show();
						DialogManager.closeProgressDialog();
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						baseSlideMenuActivity.getImageAds().setVisibility(
								View.VISIBLE);
						baseSlideMenuActivity.getImageAds().startAnimation(
								mAnimation);
						DialogManager.closeProgressDialog();
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						Toast.makeText(baseSlideMenuActivity,
								"Đã xảy ra lỗi. Vui lòng thử lại.",
								Toast.LENGTH_SHORT).show();
						DialogManager.closeProgressDialog();
					}
				}, baseSlideMenuActivity.getImageAds());
	}

	private void actionInteractionVideo(String urlLink) {
		if (urlLink == null || urlLink.length() == 0) {
			return;
		}
		AdsVideoFragment fr = new AdsVideoFragment();
		Bundle bundle = new Bundle();
		bundle.putString(AdsVideoFragment.XURL_VIDEO, urlLink);
		fr.setArguments(bundle);
		baseSlideMenuActivity.switchContent(fr, true);
	}
}
