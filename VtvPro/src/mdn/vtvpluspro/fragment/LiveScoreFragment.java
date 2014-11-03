
package mdn.vtvpluspro.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import mdn.vtvplus.R;
import mdn.vtvpluspro.adapter.LiveScoreAdapter;
import mdn.vtvpluspro.common.DialogManager;
import mdn.vtvpluspro.common.ExpandableHeightListView;
import mdn.vtvpluspro.common.ParserManager;
import mdn.vtvpluspro.network.ApiManager;
import mdn.vtvpluspro.network.IApiCallback;
import mdn.vtvpluspro.object.MatchScheduleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RibboN on 11/3/14.
 */
public class LiveScoreFragment extends BaseFragment {

    private View rootView;

    private ScrollView fragment_list_live_score_root;

    private ExpandableHeightListView fragment_live_score_current_list;
    private ExpandableHeightListView fragment_live_score_result_list;
    private ExpandableHeightListView fragment_live_score_future_list;

    private LiveScoreAdapter lvOfCurrentAdapter;
    private LiveScoreAdapter lvOfResultAdapter;
    private LiveScoreAdapter lvOfFutureAdapter;

    private List<MatchScheduleModel> mListOfCurrent;
    private List<MatchScheduleModel> mListOfResult;
    private List<MatchScheduleModel> mListOfFuture;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mListOfCurrent = new ArrayList<MatchScheduleModel>();
        mListOfResult = new ArrayList<MatchScheduleModel>();
        mListOfFuture = new ArrayList<MatchScheduleModel>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_live_score, null);

        fragment_list_live_score_root = (ScrollView) rootView
                .findViewById(R.id.fragment_list_live_score_root);

        fragment_live_score_current_list = (ExpandableHeightListView) rootView
                .findViewById(R.id.fragment_live_score_current_list);
        fragment_live_score_result_list = (ExpandableHeightListView) rootView
                .findViewById(R.id.fragment_live_score_result_list);
        fragment_live_score_future_list = (ExpandableHeightListView) rootView
                .findViewById(R.id.fragment_live_score_future_list);

        fragment_live_score_current_list.setExpanded(true);
        fragment_live_score_result_list.setExpanded(true);
        fragment_live_score_future_list.setExpanded(true);

        requestData();
        return rootView;
    }

    private void requestData() {
        DialogManager.showSimpleProgressDialog(baseSlideMenuActivity);

        ApiManager.callListLiveScore(baseSlideMenuActivity, new IApiCallback() {

            @Override
            public void responseSuccess(String response) {
                // TODO Auto-generated method stub
                parserLiveScoreData(response);
            }

            @Override
            public void responseFailWithCode(int statusCode) {
                DialogManager.alert(baseSlideMenuActivity,
                        getString(R.string.network_fail));
                DialogManager.closeProgressDialog();
            }
        });
    }

    private void parserLiveScoreData(String response) {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... strings) {
                if (null == mListOfCurrent) {
                    mListOfCurrent = new ArrayList<MatchScheduleModel>();
                } else {
                    mListOfCurrent.clear();
                }
                if (null == mListOfResult) {
                    mListOfResult = new ArrayList<MatchScheduleModel>();
                } else {
                    mListOfResult.clear();
                }
                if (null == mListOfFuture) {
                    mListOfFuture = new ArrayList<MatchScheduleModel>();
                } else {
                    mListOfFuture.clear();
                }
                ParserManager.parserListLiveScoreData(strings[0], mListOfCurrent, mListOfResult,
                        mListOfFuture);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                updateView();
                DialogManager.closeProgressDialog();
            }
        }.execute(response);
    }

    private void updateView() {
        lvOfCurrentAdapter = new LiveScoreAdapter(getActivity(), mListOfCurrent);
        lvOfResultAdapter = new LiveScoreAdapter(getActivity(), mListOfResult);
        lvOfFutureAdapter = new LiveScoreAdapter(getActivity(), mListOfFuture);

        fragment_live_score_current_list.setAdapter(lvOfCurrentAdapter);
        fragment_live_score_result_list.setAdapter(lvOfResultAdapter);
        fragment_live_score_future_list.setAdapter(lvOfFutureAdapter);
    }
}
