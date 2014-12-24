package mdn.vtvsport.adapter;

import java.util.ArrayList;

import mdn.vtvsport.common.ImageUtility;
import mdn.vtvsport.fragment.HomeFragment;
import mdn.vtvsport.library.viewflow.ViewFlow;
import mdn.vtvsport.object.BannerInfo;
import mdn.vtvsport.util.DeviceUtil;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class BannerHomeAdapter extends BaseAdapter {
	private ArrayList<BannerInfo> mListBanner;
	private Context mContext;
	private OnClickListener mListener;
	public int heightLand = 0;
	public int heightPortrait = 0;
	
	public BannerHomeAdapter(Context context, ArrayList<BannerInfo> listBanner, OnClickListener listener) {
		this.mListBanner = listBanner;
		this.mListener = listener;
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListBanner.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListBanner.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		View view = convertView;
		
		if (view == null) {
			holder = new ViewHolder();
			view = new ImageView(mContext);
			((ImageView)view).setScaleType(ScaleType.FIT_XY);
			view.setLayoutParams(new ViewFlow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			holder.image = (ImageView)view;
			view.setOnClickListener(mListener);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		BannerInfo info = mListBanner.get(position);
		holder.image.setImageBitmap(null);
		if (info.getImgLink() != null && info.getImgLink().length() > 0) {
			ImageUtility.loadBitmapFromUrl(mContext, info.getImgLink(), new ImageLoadingListener() {
				
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
					if (HomeFragment.HEIGHT_BANNER == 0) {
						updateSizeImage(loadedImage);
					}
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			}, holder.image);
		}
		
		return view;
	}
	
	private class ViewHolder {
		public ImageView image;
	}
	
	private void updateSizeImage(Bitmap loaderBitmap) {
		int width = DeviceUtil.getWidthScreen((Activity) mContext);
		int height = DeviceUtil.getHeightScreen((Activity) mContext);
		
		if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			heightPortrait = width * loaderBitmap.getHeight() / loaderBitmap.getWidth();
			heightLand = height * loaderBitmap.getHeight() / loaderBitmap.getWidth();
			HomeFragment.HEIGHT_BANNER = heightPortrait;
		} else {
			heightLand = width * loaderBitmap.getHeight() / loaderBitmap.getWidth();
			heightPortrait = height * loaderBitmap.getHeight() / loaderBitmap.getWidth();
			HomeFragment.HEIGHT_BANNER = heightLand;
		}
	}
}
