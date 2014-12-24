package mdn.vtvsport.adapter;

import java.util.List;

import mdn.vtvsport.R;
import mdn.vtvsport.object.MatchScheduleModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by RibboN on 11/4/14.
 */
public class LiveScoreAdapter extends BaseAdapter {

    private Context mContext;
    private List<MatchScheduleModel> mMatchScheduleModels;

    public LiveScoreAdapter(Context context, List<MatchScheduleModel> matchScheduleModels) {
        mContext = context;
        mMatchScheduleModels = matchScheduleModels;
    }

    @Override
    public int getCount() {
        return mMatchScheduleModels.size();
    }

    @Override
    public Object getItem(int i) {
        return mMatchScheduleModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mMatchScheduleModels.get(i).getID();
    }

    public static class MatchScheduleItemHolder {
        public TextView tvTime;
        public TextView tvLeftTeam;
        public TextView tvResult;
        public TextView tvRightTeam;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MatchScheduleItemHolder holder;

        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_match_schedule_item, null, false);
            holder = new MatchScheduleItemHolder();

            holder.tvTime = (TextView) view.findViewById(R.id.layout_match_schedule_item_time);
            holder.tvLeftTeam = (TextView) view.findViewById(R.id.layout_match_schedule_item_left_team);
            holder.tvResult = (TextView) view.findViewById(R.id.layout_match_schedule_item_result);
            holder.tvRightTeam = (TextView) view.findViewById(R.id.layout_match_schedule_item_right_team);

            view.setTag(holder);
        } else {
            holder = (MatchScheduleItemHolder) view.getTag();
        }

        MatchScheduleModel matchScheduleModel = mMatchScheduleModels.get(i);

        if(matchScheduleModel.getStatus().equalsIgnoreCase("vs")){
            // Ongoing
            holder.tvTime.setText(matchScheduleModel.getAdditional_info());
            holder.tvResult.setText(mContext.getResources().getString(R.string.versus));
        } else if(matchScheduleModel.getStatus().equalsIgnoreCase("FT") || matchScheduleModel.getStatus().equalsIgnoreCase("'AET'") || matchScheduleModel.getStatus().equalsIgnoreCase("'Susp'") || matchScheduleModel.getStatus().equalsIgnoreCase("'Post'")){
            // Result
            holder.tvTime.setText(matchScheduleModel.getStatus());
            holder.tvResult.setText(matchScheduleModel.getScores_and_stats());
        } else {
            // Current
            holder.tvTime.setText(matchScheduleModel.getStatus());
            holder.tvResult.setText(matchScheduleModel.getScores_and_stats());
        }

        holder.tvLeftTeam.setText(matchScheduleModel.getHomeTeam().getName());
        holder.tvRightTeam.setText(matchScheduleModel.getAwayTeam().getName());

        return view;
    }
}