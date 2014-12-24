package mdn.vtvsport.adapter;

import java.util.ArrayList;

import mdn.vtvsport.R;
import mdn.vtvsport.object.RankingInfo;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RankingsListViewAdapter extends ArrayAdapter<RankingInfo> {

    private Context context;
    private ArrayList<RankingInfo> patterns;

    public RankingsListViewAdapter(Context context, int resource,
            ArrayList<RankingInfo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.patterns = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;

        if (convertView == null) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.fragment_rankings_item_listview, null);

            viewHolder = new ViewHolderItem();
            viewHolder.tvId = (TextView) convertView
                    .findViewById(R.id.tv_id);
            viewHolder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewHolder.tvPoint = (TextView) convertView
                    .findViewById(R.id.tv_point);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        RankingInfo ranking = patterns.get(position);
        if (ranking != null) {
            viewHolder.tvId.setText(ranking.getId());
            viewHolder.tvName.setText(ranking.getName());
            viewHolder.tvPoint.setText(ranking.getPoint());
        }

        return convertView;
    }

    static class ViewHolderItem {
    	TextView tvId;
        TextView tvName;
        TextView tvPoint;
    }

}
