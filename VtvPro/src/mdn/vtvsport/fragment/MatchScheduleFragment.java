
package mdn.vtvsport.fragment;

import java.util.ArrayList;
import java.util.List;

import mdn.vtvsport.R;
import mdn.vtvsport.adapter.MatchScheduleAdapter;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ExpandableHeightListView;
import mdn.vtvsport.common.ImageUtility;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.object.MatchScheduleMenuObject;
import mdn.vtvsport.object.MatchScheduleModel;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by RibboN on 11/2/14.
 */
public class MatchScheduleFragment extends BaseFragment {

    private View rootView;

    private ScrollView fragment_list_match_schedule_root;

    private Spinner fragment_list_match_schedule_spinner_league;

    private ImageView fragment_list_match_schedule_league_ongoing_img;
    private TextView fragment_list_match_schedule_league_ongoing;
    private ExpandableHeightListView fragment_list_match_schedule_league_ongoing_list;

    private ImageView fragment_list_match_schedule_league_past_img;
    private TextView fragment_list_match_schedule_league_past;
    private ExpandableHeightListView fragment_list_match_schedule_league_past_list;

    private MatchScheduleAdapter lvOfOngoingAdapter;
    private MatchScheduleAdapter lvOfResultAdapter;
    private ArrayAdapter<String> menuAdapter;

    private List<MatchScheduleModel> mListOfOngoing;
    private List<MatchScheduleModel> mListOfResult;

    private List<MatchScheduleMenuObject> mListOfMenu;
    private MatchScheduleMenuObject currentLeagueObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mListOfOngoing = new ArrayList<MatchScheduleModel>();
        mListOfResult = new ArrayList<MatchScheduleModel>();

        mListOfMenu = new ArrayList<MatchScheduleMenuObject>();
        currentLeagueObject = new MatchScheduleMenuObject();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_match_schedule, null);

        fragment_list_match_schedule_root = (ScrollView) rootView.findViewById(R.id.fragment_list_match_schedule_root);

        fragment_list_match_schedule_spinner_league = (Spinner) rootView
                .findViewById(R.id.fragment_list_match_schedule_spinner_league);

        fragment_list_match_schedule_league_ongoing_img = (ImageView) rootView
                .findViewById(R.id.fragment_list_match_schedule_league_ongoing_img);
        fragment_list_match_schedule_league_ongoing = (TextView) rootView
                .findViewById(R.id.fragment_list_match_schedule_league_ongoing);
        fragment_list_match_schedule_league_ongoing_list = (ExpandableHeightListView) rootView
                .findViewById(R.id.fragment_list_match_schedule_league_ongoing_list);
        fragment_list_match_schedule_league_ongoing_list.setExpanded(true);

        fragment_list_match_schedule_league_past_img = (ImageView) rootView
                .findViewById(R.id.fragment_list_match_schedule_league_past_img);
        fragment_list_match_schedule_league_past = (TextView) rootView
                .findViewById(R.id.fragment_list_match_schedule_league_past);
        fragment_list_match_schedule_league_past_list = (ExpandableHeightListView) rootView
                .findViewById(R.id.fragment_list_match_schedule_league_past_list);
        fragment_list_match_schedule_league_past_list.setExpanded(true);

        requestMenuLeague();
        return rootView;
    }

    AdapterView.OnItemSelectedListener chooseLeague = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (null != mListOfMenu && mListOfMenu.size() > 0) {
                currentLeagueObject = mListOfMenu.get(i);
                requestData();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void requestMenuLeague() {
        ApiManager.callListMatchScheduleMenuItem(baseSlideMenuActivity, new IApiCallback() {

            @Override
            public void responseSuccess(String response) {
                // TODO Auto-generated method stub
                parserMenuData(response);
            }

            @Override
            public void responseFailWithCode(int statusCode) {
                DialogManager.alert(baseSlideMenuActivity,
                        getString(R.string.network_fail));

                DialogManager.closeProgressDialog();
            }
        });
    }

    private void updateView() {
        lvOfOngoingAdapter = new MatchScheduleAdapter(getActivity(), mListOfOngoing);
        lvOfResultAdapter = new MatchScheduleAdapter(getActivity(), mListOfResult);

        fragment_list_match_schedule_league_ongoing_list.setAdapter(lvOfOngoingAdapter);
        fragment_list_match_schedule_league_past_list.setAdapter(lvOfResultAdapter);

        lvOfOngoingAdapter.notifyDataSetChanged();
        lvOfResultAdapter.notifyDataSetChanged();

        setListViewHeightBasedOnChildren(fragment_list_match_schedule_league_ongoing_list);
        setListViewHeightBasedOnChildren(fragment_list_match_schedule_league_past_list);
    }

    private void requestData() {
        DialogManager.showSimpleProgressDialog(baseSlideMenuActivity);

        ApiManager.callListMatchSchedule(baseSlideMenuActivity, new IApiCallback() {

            @Override
            public void responseSuccess(String response) {
                // TODO Auto-generated method stub
                parserMatchScheduleData(response);
            }

            @Override
            public void responseFailWithCode(int statusCode) {
                DialogManager.alert(baseSlideMenuActivity,
                        getString(R.string.network_fail));
            }
        }, currentLeagueObject.getGuid());
    }

    private void parserMenuData(String response) {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {
                if (null == mListOfMenu) {
                    mListOfMenu = new ArrayList<MatchScheduleMenuObject>();
                }
                ParserManager.parserListMatchScheduleMenu(strings[0], mListOfMenu);
                currentLeagueObject = mListOfMenu.get(0);
                List<String> menuData = new ArrayList<String>();
                for (MatchScheduleMenuObject msmo : mListOfMenu) {
                    menuData.add(msmo.getName());
                }
                menuAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_item, menuData);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                fragment_list_match_schedule_spinner_league.setAdapter(menuAdapter);
                fragment_list_match_schedule_spinner_league.setSelection(0);
                requestData();
            }
        }.execute(response);
    }

    private void parserMatchScheduleData(String response) {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {
                if (null == mListOfOngoing) {
                    mListOfOngoing = new ArrayList<MatchScheduleModel>();
                } else {
                    mListOfOngoing.clear();
                }
                if (null == mListOfResult) {
                    mListOfResult = new ArrayList<MatchScheduleModel>();
                } else {
                    mListOfResult.clear();
                }
                ParserManager
                        .parserListMatchScheduleData(strings[0], mListOfOngoing, mListOfResult);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                fragment_list_match_schedule_league_ongoing.setText(currentLeagueObject.getName());
                fragment_list_match_schedule_league_past.setText(currentLeagueObject.getName());
                ImageUtility.loadBitmapFromUrl(getActivity(),
                        currentLeagueObject.getImage_url_small(),
                        fragment_list_match_schedule_league_ongoing_img);
                ImageUtility.loadBitmapFromUrl(getActivity(),
                        currentLeagueObject.getImage_url_small(),
                        fragment_list_match_schedule_league_past_img);
                updateView();
                DialogManager.closeProgressDialog();
                fragment_list_match_schedule_spinner_league.setOnItemSelectedListener(chooseLeague);
                fragment_list_match_schedule_root.smoothScrollTo(0, 0);
            }
        }.execute(response);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
