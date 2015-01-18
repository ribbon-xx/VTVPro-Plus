package mdn.vtvsport.fragment;

import java.util.ArrayList;

import mdn.vtvsport.R;
import mdn.vtvsport.adapter.RankingsListViewAdapter;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.LeagueInfo;
import mdn.vtvsport.object.RankingInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.analytics.tracking.android.Log;

public class RankingsFragment extends BaseFragment {
	private Spinner spAdapter;
	private ArrayList<LeagueInfo> arrLeagues;
	private ArrayList<String> arrStringLeagues;
	private ArrayList<RankingInfo> arrRankings;
    private RankingsListViewAdapter rankingAdapter;
    private ListView lvRankings;
    
    private boolean isOpenSlideMenu = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Bundle bundle = getArguments();
    	if (bundle != null) {
        	isOpenSlideMenu = bundle.getBoolean("isOpenSlideMenuRanking");
        }
    	
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_rankings, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		spAdapter = (Spinner) getView().findViewById(R.id.sp_adapter);
		
		getListLeagues();
		getListRankings(String.valueOf(1));
		
		lvRankings = (ListView) getView().findViewById(R.id.lv_rankings);
		if (isOpenSlideMenu) {
			baseSlideMenuActivity
			.setTextCategory(getString(R.string.slidemenu_xephang));
		}
		
	}
	
	@Override
	protected void initUiTabbar() {
		super.initUiTabbar();

//		baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
		baseSlideMenuActivity.iconSetting.setVisibility(View.GONE);
		baseSlideMenuActivity.iconBack.setVisibility(View.VISIBLE);
		baseSlideMenuActivity.iconVtvPlus.setVisibility(View.GONE);
	}
	
	private void getListLeagues() {
		arrLeagues = new ArrayList<LeagueInfo>();
		arrStringLeagues = new ArrayList<String>();
		
		ApiManager.callListLeagues(getActivity(), new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d("response: " + response);
				arrLeagues.clear();
				arrStringLeagues.clear();
				
				try {
					JSONObject obj = new JSONObject(response);
					JSONArray arrJson = obj.getJSONArray("data");
					
					for (int i = 0; i < arrJson.length(); i++) {
						JSONObject  tmp = arrJson.getJSONObject(i);
						String id = tmp.getString("id");
						String name = tmp.getString("name");
						
						LeagueInfo league = new LeagueInfo(id, name);
						arrLeagues.add(league);
						arrStringLeagues.add(name);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// bind adapter
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(),   android.R.layout.simple_spinner_item, arrStringLeagues);
				spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
				spAdapter.setAdapter(spinnerArrayAdapter);
				spAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,
		                    int pos, long id) {
						// TODO Auto-generated method stub
//						int idLeague = Integer.parseInt(spAdapter.getSelectedItem().toString());
						getListRankings(String.valueOf(pos+1));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				DialogManager.alert(baseSlideMenuActivity,
                        getString(R.string.network_fail));

                DialogManager.closeProgressDialog();
			}
			
		});
	}
	
	private void getListRankings(String idLeague) {
		arrRankings = new ArrayList<RankingInfo>();
		ApiManager.callListRankings(getActivity(), new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				Log.d("response: " + response);
				arrRankings.clear();

				try {
					JSONObject obj = new JSONObject(response);
					obj = obj.getJSONObject("data");
					obj = obj.getJSONObject("all");
					JSONArray arrJson = obj.getJSONArray("standings");
					
					Log.d("arrJson: " + arrJson.length());
					
					for (int i = 0; i < arrJson.length(); i++) {
						JSONArray arrTmp = arrJson.getJSONArray(i);
						String nameClub = arrTmp.getString(0);
						JSONObject  tmp2 = arrTmp.getJSONObject(1);
						String overall_position = tmp2.getString("overall_position");
						String points = tmp2.getString("overall_points");
						
						RankingInfo ranking = new RankingInfo(overall_position, nameClub, points);
						arrRankings.add(ranking);
					}

					Log.d("response: " + response);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (arrRankings != null) {
					rankingAdapter = new RankingsListViewAdapter(getActivity(), R.layout.fragment_rankings_item_listview, 
							arrRankings);
					
					lvRankings.setAdapter(rankingAdapter);
					rankingAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void responseFailWithCode(int statusCode) {
				DialogManager.alert(baseSlideMenuActivity,
                        getString(R.string.network_fail));

                DialogManager.closeProgressDialog();
			}
		}, idLeague);

		// return mArrMenu;
	}
}
