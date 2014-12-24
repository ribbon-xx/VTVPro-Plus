
package mdn.vtvsport.adapter;

import java.util.Calendar;
import java.util.Date;
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
 * Created by RibboN on 11/2/14.
 */
public class MatchScheduleAdapter extends BaseAdapter {

    private Context mContext;
    private List<MatchScheduleModel> mMatchScheduleModels;

    public MatchScheduleAdapter(Context context, List<MatchScheduleModel> matchScheduleModels) {
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

        Date dateTimeOfPlay = matchScheduleModel.getDatetime_of_play();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTimeOfPlay);
        int month = calendar.get(Calendar.MONTH) + 1;
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).length() == 1 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE)).length() == 1 ? "0" + calendar.get(Calendar.MINUTE) : String.valueOf(calendar.get(Calendar.MINUTE));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)).length() == 1 ? "0" + calendar.get(Calendar.DAY_OF_MONTH) : String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String monthValue = String.valueOf(month).length() == 1 ? "0" + month : String.valueOf(month);

        String timeOfPlay = hour + ":" + minute + " " + day + "/" + monthValue;

        holder.tvTime.setText(timeOfPlay);
        holder.tvLeftTeam.setText(matchScheduleModel.getHomeTeam().getName());
        if(!matchScheduleModel.getScores_and_stats().equals("")){
            holder.tvResult.setText(matchScheduleModel.getScores_and_stats());
        } else {
            holder.tvResult.setText(mContext.getResources().getString(R.string.versus));
        }
        holder.tvRightTeam.setText(matchScheduleModel.getAwayTeam().getName());

        return view;
    }
}
