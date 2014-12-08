package mdn.vtvpluspro.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.analytics.tracking.android.Log;

import mdn.vtvplus.R;
import mdn.vtvpluspro.adapter.AdapterMenuLeft;
import mdn.vtvpluspro.fragment.account.AllWebviewFragment;
import mdn.vtvpluspro.fragment.account.LoginFragment;
import mdn.vtvpluspro.fragment.account.UserInfoFragment;
import mdn.vtvpluspro.fragment.account.WebviewChargeFragment;
import mdn.vtvpluspro.network.ApiManager;
import mdn.vtvpluspro.network.IApiCallback;
import mdn.vtvpluspro.network.WebServiceConfig;
import mdn.vtvpluspro.object.ItemInteraction;
import mdn.vtvpluspro.object.objMenu;
import android.R.array;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class ListMenuFragment extends BaseFragment implements OnClickListener, OnItemClickListener {
	private LinearLayout layLogin;
	private LinearLayout layProfile;
	private LinearLayout laySetting;
	private LinearLayout layRegister;
	private TextView layBuyPackage;
	private TextView layChargeMoney;
	private TextView layRuleCharge;
	private TextView layHelp;

	private ImageView imvProfile;
	private TextView tvProfile;
	private int posClick = -1;
	private ListView lvVideos;
	private ArrayList<objMenu> arrMenu = new ArrayList<objMenu>();
	private AdapterMenuLeft apdateMenuLeft;
	private ArrayList<objMenu> arrMenuTmp;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.layout_list_menu, null);
		bindLayout(view);
		initLayout();
		baseSlideMenuActivity.mMenuFragment = this;
		return view;
	}

	private void bindLayout(View view) {
		layLogin = (LinearLayout) view.findViewById(R.id.layMenuLogin);
		layLogin.setOnClickListener(this);
		layProfile = (LinearLayout) view.findViewById(R.id.layMenuProfile);
		layProfile.setOnClickListener(this);
		laySetting = (LinearLayout) view.findViewById(R.id.layMenuSetting);
		laySetting.setOnClickListener(this);
		layRegister = (LinearLayout) view.findViewById(R.id.ll_dangki);
		layRegister.setOnClickListener(this);
		lvVideos = (ListView) view.findViewById(R.id.lv_video);
//		layBuyPackage = (TextView) view.findViewById(R.id.tvMuagoicuoc);
//		layBuyPackage.setOnClickListener(this);
//		layChargeMoney = (TextView) view.findViewById(R.id.tvNapTien);
//		layChargeMoney.setOnClickListener(this);
//		layRuleCharge = (TextView) view.findViewById(R.id.tvChinhsach);
//		layRuleCharge.setOnClickListener(this);
//		layHelp = (TextView) view.findViewById(R.id.tvHotro);
//		layHelp.setOnClickListener(this);

		imvProfile = (ImageView) view.findViewById(R.id.imvProfile);
		tvProfile = (TextView) view.findViewById(R.id.tvNameLogin);
		
		
//		ArrayList<objMenu> tmpVideo = new ArrayList<objMenu>();
//		tmpVideo = callCategoryVideoMenuApi();
		
		// group header
		arrMenu.add(new objMenu(1, getString(R.string.slidemenu_event), R.drawable.menu_icon_sukien,true));
		arrMenu.add(new objMenu(2, getString(R.string.slidemenu_lichthidau), false));
		arrMenu.add(new objMenu(3, getString(R.string.slidemenu_livescore), false));
		arrMenu.add(new objMenu(4, getString(R.string.slidemenu_xephang), false));
		
		arrMenu.add(new objMenu(5, getString(R.string.slidemenu_video), R.drawable.menu_icon_video, true));
//		// add sub category video
//		for(int i=0; i<tmpVideo.size(); i++) {
//			arr.add(new objMenu(i, tmpVideo.get(i).name, false));
//		}
		
		arrMenu.add(new objMenu(6, getString(R.string.slidemenu_channel), true));
		arrMenu.add(new objMenu(7, getString(R.string.slidemenu_huy_dk), false));
		callCategoryVideoMenuApi();
		
		apdateMenuLeft = new AdapterMenuLeft(getActivity(), R.layout.slidemenu_item_listview_video, arrMenu);
		lvVideos.setAdapter(apdateMenuLeft);
		lvVideos.setOnItemClickListener(this);
	}

	
	public void initLayout() {
		if (baseSlideMenuActivity.pProfile.pUserName != null
				&& baseSlideMenuActivity.pProfile.pUserName.length() > 0) {
			layLogin.setVisibility(View.GONE);
			layProfile.setVisibility(View.VISIBLE);
			tvProfile.setText(baseSlideMenuActivity.pProfile.pUserName);
		} else {
			layLogin.setVisibility(View.VISIBLE);
			layProfile.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.layMenuLogin:
			posClick = -1;
			actionLoginClick();
			break;
		case R.id.layMenuProfile:
			posClick = -1;
			actionProfileClick();
			break;
		case R.id.layMenuSetting:
			posClick = -1;
			break;
		case R.id.ll_dangki:
			posClick = -1;
			actionRegisterClick();
			break;
			
//		case R.id.tvMuagoicuoc:
//			posClick = -1;
//			actionChargePackageClick();
//			break;
//		case R.id.tvNapTien:
//			break;
//		case R.id.tvChinhsach:
//			baseSlideMenuActivity.showContent();
//			if ((posClick != 2) || (!(baseSlideMenuActivity.mContent instanceof AllWebviewFragment))) {
//				posClick = 2;
//				actionSupportClick(false);
//			}
//			break;
//		case R.id.tvHotro:
//			baseSlideMenuActivity.showContent();
//			if ((posClick != 3) || (!(baseSlideMenuActivity.mContent instanceof AllWebviewFragment))) {
//				posClick = 3;
//				actionSupportClick(true);
//			}
//			break;

		default:
			break;
		}
	}

	public ImageView getImvProfile() {
		return imvProfile;
	}

	public void setImvProfile(ImageView imvProfile) {
		this.imvProfile = imvProfile;
	}

	public TextView getTvProfile() {
		return tvProfile;
	}

	public void setTvProfile(TextView tvProfile) {
		this.tvProfile = tvProfile;
	}

	private void actionLoginClick() {
		baseSlideMenuActivity.showContent();
		if (baseSlideMenuActivity.mContent instanceof LoginFragment) {
			return;
		}
		if (baseSlideMenuActivity.isMainFragment()) {
			baseSlideMenuActivity.switchContent(new LoginFragment(), true);
		} else {
			baseSlideMenuActivity.switchContent(new LoginFragment(), false);
		}
	}

	private void actionProfileClick() {
		baseSlideMenuActivity.showContent();
		if (baseSlideMenuActivity.mContent instanceof UserInfoFragment) {
			return;
		}
		if (baseSlideMenuActivity.isMainFragment()) {
			baseSlideMenuActivity.switchContent(new UserInfoFragment(), true);
		} else {
			baseSlideMenuActivity.switchContent(new UserInfoFragment(), false);
		}
	}

	private void actionRegisterClick() {
		Fragment unregister = new RegisterFragment();
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Fragment fg = fm.findFragmentById(R.id.layoutContent);
		if (fg instanceof RegisterFragment) {
			
		} else {
			baseSlideMenuActivity.switchContent(unregister, true);
		}
		baseSlideMenuActivity.getSlidingMenu().toggle();
	}
		
//	private void actionChargePackageClick() {
//		baseSlideMenuActivity.showContent();
//		if (baseSlideMenuActivity.mContent instanceof WebviewChargeFragment) {
//			return;
//		}
//		nextFragmentChargePackage();
//	}

//	private void actionSupportClick(boolean isHotro) {
//		baseSlideMenuActivity.showContent();
//		Bundle bundle = new Bundle();
//		bundle.putString("url_hotro", WebServiceConfig.getUrlSupport(isHotro));
//		AllWebviewFragment fr = new AllWebviewFragment();
//		fr.setArguments(bundle);
//		if (baseSlideMenuActivity.isMainFragment()) {
//			baseSlideMenuActivity.switchContent(fr, true);
//		} else {
//			baseSlideMenuActivity.switchContent(fr, false);
//		}
//	}

//	private void nextFragmentChargePackage() {
//		WebviewChargeFragment fr = new WebviewChargeFragment();
//		if (baseSlideMenuActivity.isMainFragment()) {
//			baseSlideMenuActivity.switchContent(fr, true);
//		} else {
//			baseSlideMenuActivity.switchContent(fr, false);
//		}
//	}

	private void callCategoryVideoMenuApi() {
		arrMenuTmp = new ArrayList<objMenu>();
		
		ApiManager.callCategoryVideoMenu(getActivity(), new IApiCallback() {
			
			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d("response: " + response);
				
				try {
					apdateMenuLeft.removeItem(arrMenu.size()-1);
					apdateMenuLeft.removeItem(arrMenu.size()-2);
					lvVideos.removeAllViewsInLayout();
					apdateMenuLeft.notifyDataSetChanged();
					
					JSONArray arrJson = new JSONArray(response);
					Log.d("arrJson: " + arrJson.length());
					
					for(int i=0; i < arrJson.length(); i++) {
						JSONObject obj = arrJson.getJSONObject(i);
						int id = Integer.parseInt(obj.getString("id"));
						String name = obj.getString("name");
						objMenu menu = new objMenu(id, name, false);
						
						arrMenuTmp.add(menu);
						// add sub category video
						Log.d("add menu: " + name);
						arrMenu.add(new objMenu(100+id, name, false));
					}
					
					arrMenu.add(new objMenu(6, getString(R.string.slidemenu_channel), true));
					arrMenu.add(new objMenu(7, getString(R.string.slidemenu_huy_dk), false));
					apdateMenuLeft.notifyDataSetChanged();
					
					Log.d("response: " + arrMenuTmp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		objMenu menu = arrMenu.get(position);
		
		// sub video
		if (arrMenuTmp!= null && arrMenuTmp.size() > 0) {
			for (int i=0; i<arrMenuTmp.size(); i++) {
				if (menu.id == (100+ arrMenuTmp.get(i).id)) {
					Bundle bundle = new Bundle();
					bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 1);
					bundle.putString(VtvPlusFragment.KEY_ID_ITEM_VTV, String.valueOf(arrMenuTmp.get(i).id));
					bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV, arrMenuTmp.get(i).name);
					VtvPlusFragment mVtvFragment = new VtvPlusFragment();
					mVtvFragment.setArguments(bundle);
					
					baseSlideMenuActivity.switchContent(mVtvFragment, true);
					baseSlideMenuActivity.getSlidingMenu().toggle();
				}
			}
		}
		
		// video
		if (menu.id == 5) {
			Fragment categoryVideo = new CategoryVideoFragment();
			baseSlideMenuActivity.switchContent(categoryVideo, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
		
		if (menu.id == 6) {
			// channel
			Bundle bundle = new Bundle();
			bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 0);
			bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV, getString(R.string.slidemenu_channel));
			VtvPlusFragment mVtvFragment = new VtvPlusFragment();
			mVtvFragment.setArguments(bundle);
			baseSlideMenuActivity.switchContent(mVtvFragment, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
		
		// ranking
		if (menu.id == 4) {
			Fragment ranking = new RankingsFragment();
			baseSlideMenuActivity.switchContent(ranking, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
		
		// schedule
		if (menu.id == 2) {
			Fragment schedule = new MatchScheduleFragment();
			baseSlideMenuActivity.switchContent(schedule, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
		
		// event
		if (menu.id == 1) {
			Fragment event = new ListCategoryInEventFragment();
			baseSlideMenuActivity.switchContent(event, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
		
		// Cancel to register
		if (menu.id == 7) {
			Fragment unregister = new UnregisterFragment();
			FragmentManager fm = getActivity().getSupportFragmentManager();
			Fragment fg = fm.findFragmentById(R.id.layoutContent);
			if (fg instanceof UnregisterFragment) {
				
			} else {
				baseSlideMenuActivity.switchContent(unregister, true);
			}
			
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
	}
	
}
