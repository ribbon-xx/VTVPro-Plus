package mdn.vtvpluspro.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import mdn.vtvplus.R;
import mdn.vtvpluspro.common.ExpandableHeightListView;

/**
 * Created by RibboN on 11/3/14.
 */
public class LiveScoreFragment extends BaseFragment {

    private View rootView;

    private ScrollView fragment_list_match_schedule_root;

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
}
