package mdn.vtvsport.fragment;

import java.util.ArrayList;

import mdn.vtvsport.R;
import mdn.vtvsport.adapter.AdapterMenuLeft;
import mdn.vtvsport.fragment.account.LoginFragment;
import mdn.vtvsport.fragment.account.UserInfoFragment;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.objMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.google.analytics.tracking.android.Log;



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
	
	private int mCurrentTab = -1;
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
		
		// group header
		arrMenu.add(new objMenu(1, getString(R.string.slidemenu_event), R.drawable.menu_icon_sukien,true));
		arrMenu.add(new objMenu(2, getString(R.string.slidemenu_lichthidau), false));
		arrMenu.add(new objMenu(3, getString(R.string.slidemenu_livescore), false));
		arrMenu.add(new objMenu(4, getString(R.string.slidemenu_xephang), false));
		
		arrMenu.add(new objMenu(5, getString(R.string.slidemenu_video), R.drawable.menu_icon_video, true));
//		// add sub category video
		
		arrMenu.add(new objMenu(6, getString(R.string.slidemenu_channel), R.drawable.menu_icon_kenhtv, true));
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
		baseSlideMenuActivity.setTextCategory(getString(R.string.slidemenu_register));
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
					apdateMenuLeft.removeItem(arrMenu.size()-1);
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
					
					arrMenu.add(new objMenu(6, getString(R.string.slidemenu_channel),  R.drawable.menu_icon_kenhtv, true));
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
				if (menu.id == (100+ arrMenuTmp.get(i).id) && mCurrentTab != (100+ arrMenuTmp.get(i).id)) {
					mCurrentTab = (100+ arrMenuTmp.get(i).id); 
//					baseSlideMenuActivity.setTextCategory(arrMenuTmp.get(i).name);
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
		
		// event
		if (menu.id == 1 && mCurrentTab !=1) {
			mCurrentTab = 1;
			Fragment event = new ListCategoryInEventFragment();
			baseSlideMenuActivity.switchContent(event, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
		
		// schedule
		if (menu.id == 2 && mCurrentTab != 2) {
			mCurrentTab = 2;
			Fragment schedule = new MatchScheduleFragment();
			Bundle bundle = new Bundle();
			bundle.putBoolean("isOpenSlideMenu", true);
			schedule.setArguments(bundle);
			
			baseSlideMenuActivity.switchContent(schedule, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
				
		// livescore
		if (menu.id == 3 && mCurrentTab !=3) {
			mCurrentTab = 3;
//			baseSlideMenuActivity.setTextCategory(getString(R.string.slidemenu_event));
			Fragment livescore = new LiveScoreFragment();
			Bundle bundle = new Bundle();
			bundle.putBoolean("isOpenSlideMenuLivescore", true);
			livescore.setArguments(bundle);
			
			baseSlideMenuActivity.switchContent(livescore, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
		
		// ranking
		if (menu.id == 4 && mCurrentTab != 4) {
			mCurrentTab = 4;
			Fragment ranking = new RankingsFragment();
			Bundle bundle = new Bundle();
			bundle.putBoolean("isOpenSlideMenuRanking", true);
			ranking.setArguments(bundle);
			
			baseSlideMenuActivity.switchContent(ranking, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
		
		// video
		if (menu.id == 5 && mCurrentTab != 5) {
			mCurrentTab = 5;
			Fragment categoryVideo = new CategoryVideoFragment();
			baseSlideMenuActivity.switchContent(categoryVideo, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}

		if (menu.id == 6 && mCurrentTab != 6) {
			// channel
			mCurrentTab = 6;
//			baseSlideMenuActivity
//					.setTextCategory(getString(R.string.slidemenu_channel));
			Bundle bundle = new Bundle();
			bundle.putInt(VtvPlusFragment.KEY_TYPE_ITEM_VTV, 0);
			bundle.putString(VtvPlusFragment.KEY_NAME_ITEM_VTV,
					getString(R.string.slidemenu_channel));
			VtvPlusFragment mVtvFragment = new VtvPlusFragment();
			mVtvFragment.setArguments(bundle);
			baseSlideMenuActivity.switchContent(mVtvFragment, true);
			baseSlideMenuActivity.getSlidingMenu().toggle();
		}
				
		// unregister
		if (menu.id == 7  && mCurrentTab !=7) {
			mCurrentTab = 7;
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
