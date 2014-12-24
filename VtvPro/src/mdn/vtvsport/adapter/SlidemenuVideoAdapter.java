package mdn.vtvsport.adapter;

import java.util.ArrayList;

import mdn.vtvsport.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
/**
 *
 * @author haint
 *
 */
public class SlidemenuVideoAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int resource;
    private ArrayList<String> list;

    public SlidemenuVideoAdapter(Context context, int resource,
            ArrayList<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resource = resource;
        this.list = objects;
    }
   
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.slidemenu_item_listview_video, null);
        }
         
        TextView txtTitle = (TextView) convertView.findViewById(R.id.tv_item);
        txtTitle.setText(list.get(position));
        
        return convertView;
	}

}
