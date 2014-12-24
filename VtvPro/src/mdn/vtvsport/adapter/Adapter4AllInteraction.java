package mdn.vtvsport.adapter;

import java.util.ArrayList;

import mdn.vtvsport.R;
import mdn.vtvsport.common.ImageUtility;
import mdn.vtvsport.object.ItemInteraction;
import mdn.vtvsport.util.DeviceUtil;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class Adapter4AllInteraction extends ArrayAdapter<ItemInteraction> {

	private ArrayList<ItemInteraction> arrItem;
	private Context mContext;
	private int width;

	public Adapter4AllInteraction(Context context, int resource,
			ArrayList<ItemInteraction> objects) {
		super(context, resource, objects);
		this.mContext = context;
		this.arrItem = objects;
		this.width = DeviceUtil.getWidthScreen((Activity) mContext);
	}

	@Override
	public int getCount() {
		return arrItem.size();
	}

	@Override
	public ItemInteraction getItem(int index) {
		return arrItem.get(index);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		NewsHolder helper = null;
		
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.layout_item_interaction, null);
			
			helper = new NewsHolder();
			helper.imgInteraction = (ImageView) view.findViewById(R.id.imvInteraction);
			view.setTag(helper);
		} else {
			helper = (NewsHolder)view.getTag();
		}
		
		ItemInteraction item = getItem(position);
		if (item.getThumb() != null && item.getThumb().length() > 0) {
			ImageUtility.loadBitmapFromUrl(mContext, item.getThumb(), new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					int tmpHeight = loadedImage.getHeight()*width/loadedImage.getWidth();
					view.getLayoutParams().width = width;
					view.getLayoutParams().height = tmpHeight;
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			}, helper.imgInteraction);
		}
		
		return view;
	}
	
	private class NewsHolder {
		public ImageView imgInteraction;
	}
}
