package mdn.vtvsport.fragment;

import java.util.ArrayList;

import mdn.vtvsport.BaseSlideMenuActivity;
import mdn.vtvsport.R;
import mdn.vtvsport.adapter.AdapterOfSchedule;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.ScheduleInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlayerDetailsScheduleFragment extends BaseFragmentPlayerDetail {
	private ArrayList<ScheduleInfo> arrInfo = null;
	public AdapterOfSchedule adapter = null;
	
	private ListView lvSchedule;
	private ProgressBar pbLoad;
	private TextView tvNoItem;
	
	private boolean isShow = true;
	
	public PlayerDetailsScheduleFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		isShow = true;
		View view = inflater.inflate(R.layout.fragment_player_details_schedule, container, false);
		bindView(view);
		getData();
		return view;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isShow = false;
		
		if (adapter != null) {
			adapter.clear();
			adapter = null;
		}
		arrInfo = null;
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		
		isShow = false;
	}
	
	private void bindView(View view) {
		lvSchedule = (ListView) view.findViewById(R.id.lvSchedule);
		pbLoad = (ProgressBar) view.findViewById(R.id.progressLoad);
		pbLoad.setVisibility(View.GONE);
		tvNoItem = (TextView) view.findViewById(R.id.tvNoItem);
		tvNoItem.setVisibility(View.GONE);
	}
	
	private void getData() {
		if (arrInfo != null && arrInfo.size() > 0) {
			if (adapter == null) {
				adapter = new AdapterOfSchedule(baseSlideMenuActivity, arrInfo);
			}
			lvSchedule.setAdapter(adapter);
		} else {
			arrInfo = null;
			callListSchedule();
		}
	}
	
	public void callListSchedule() {
		pbLoad.setVisibility(View.VISIBLE);
		String idChannel = null;
		if (mPlayerFragment.itemInfo != null) {
			idChannel = mPlayerFragment.itemInfo.getId();
		} else {
			idChannel = BaseSlideMenuActivity.gcmBundleId;
		}
		ApiManager.callListSchedule(baseSlideMenuActivity, new IApiCallback() {
			
			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				if (!isShow) {
					return;
				}
				if (arrInfo != null) {
					arrInfo.clear();
				}
				arrInfo = ParserManager.parserScheduleInfo(response);
				if (arrInfo.size() == 0) {
					tvNoItem.setVisibility(View.VISIBLE);
					lvSchedule.setVisibility(View.GONE);
				} else {
					tvNoItem.setVisibility(View.GONE);
					lvSchedule.setVisibility(View.VISIBLE);
				}
				if (adapter == null) {
					adapter = new AdapterOfSchedule(baseSlideMenuActivity, arrInfo);
					lvSchedule.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}
				pbLoad.setVisibility(View.GONE);
			}
			
			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				if (!isShow) {
					return;
				}
				DialogManager.alert(baseSlideMenuActivity,
						getString(R.string.network_fail));
				DialogManager.closeProgressDialog();
				pbLoad.setVisibility(View.GONE);
			}
		}, idChannel);
	}
}
