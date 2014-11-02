package mdn.vtvpluspro.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import mdn.vtvplus.R;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageUtility {
	protected static ImageLoader imageLoader;
	protected static Context context;
	// protected static ImageLoader imageLocallyLoader;
	protected static DisplayImageOptions imageOptions;
	protected static ImageLoaderConfiguration facebookConfig, locallyConfig;

	public static void initialize(Context ctx) {
		context = ctx;
		/** Configuration image loader */
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"Surface/Cache/vtvplus");
		imageLoader = ImageLoader.getInstance();
		// imageLocallyLoader = ImageLoader.getInstance();
		facebookConfig = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheExtraOptions(600, 400)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				// .denyCacheImageMultipleSizesInMemory()
				// .offOutOfMemoryHandling()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.discCache(new UnlimitedDiscCache(cacheDir))
				// You can pass your own
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				// .imageDownloader(
				// new URLConnectionImageDownloader(5 * 1000, 20 * 1000))
				// connectTimeout
				.tasksProcessingOrder(QueueProcessingType.FIFO)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.enableLogging().build();

		// locallyConfig = new ImageLoaderConfiguration.Builder(context)
		// .threadPriority(Thread.NORM_PRIORITY - 2)
		// .memoryCacheSize(2 * 1024 * 1024)
		// // 2 Mb
		// .denyCacheImageMultipleSizesInMemory()
		// .discCacheFileNameGenerator(new Md5FileNameGenerator())
		// .imageDownloader(
		// new ExtendedImageDownloader(context))
		// .build();
		imageLoader.init(facebookConfig);

		/** Loading image option */
		imageOptions = new DisplayImageOptions.Builder()
				.cacheInMemory()
				.cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showImageForEmptyUri(R.drawable.no_image)
				.showImageOnFail(R.drawable.no_image)
				.showStubImage(R.drawable.no_image).build();
	}

	/**
	 * Get bitmap file from internet and load on viewer
	 * 
	 * @param urlString
	 * @param viewer
	 */
	public static void loadBitmapFromUri(Context context, String uri,
			ImageView viewer) {
		ContentResolver cr = context.getContentResolver();
		try {
			InputStream input;
			input = cr.openInputStream(Uri.parse(uri));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
			viewer.setImageBitmap(bitmap);
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get bitmap file from internet and load on viewer
	 * 
	 * @param urlString
	 * @param viewer
	 */
	public static Bitmap loadBitmapFromUri(Context ctx, String uri,
			int maxWidth, int maxHeight) {
		Bitmap output = null;
		context = ctx;
		ContentResolver cr = context.getContentResolver();
		try {

			int scale = getImageScale(Uri.parse(uri), maxWidth, maxHeight);
			InputStream input;
			input = cr.openInputStream(Uri.parse(uri));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = scale;
			output = BitmapFactory.decodeStream(input, null, options);
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Get Image scale
	 * 
	 * @param data
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	private static int getImageScale(Uri uri, int maxWidth, int maxHeight) {
		// Decode the image size
		BitmapFactory.Options dimensionOptions = new BitmapFactory.Options();

		dimensionOptions.inJustDecodeBounds = true;

		InputStream is;
		try {
			is = context.getContentResolver().openInputStream(uri);

			BitmapFactory.decodeStream(is, null, dimensionOptions);

			is.close();
		} catch (FileNotFoundException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int scale = 1;

		double maxSize = Math.max(maxWidth, maxHeight);

		if (dimensionOptions.outWidth > maxWidth
				|| dimensionOptions.outHeight > maxHeight) {
			double imageMaxSize = Math.max(dimensionOptions.outHeight,
					dimensionOptions.outWidth);

			scale = (int) Math.round(Math.pow(
					2D,
					(double) (Math.round(Math.log(maxSize / imageMaxSize)
							/ Math.log(0.5D)))));

		}
		return scale;

	}

	/**
	 * Get bitmap file from internet and load on viewer
	 * 
	 * @param urlString
	 * @param viewer
	 */
	public static void loadBitmapFromUrl(Context context, String urlString,
			ImageView viewer) {
		if (imageLoader == null) {
			initialize(context);
		}
		imageLoader.displayImage(urlString, viewer, imageOptions);
	}

	public static void loadBitmapFromUrl(Context context, String urlString,
			ImageLoadingListener listener, ImageView viewer) {
		if (imageLoader == null) {
			initialize(context);
		}
		imageLoader.displayImage(urlString, viewer, imageOptions, listener);
		// imageLoader.displayImage(urlString, viewer, imageOptions);
	}

	/**
	 * Get bitmap file from internet and store on memory
	 * 
	 * @param context
	 * @param urlString
	 * @param listener
	 */
	public static void loadBitmapFromUrl(Context context, String urlString,
			ImageLoadingListener listener) {
		if (imageLoader == null) {
			initialize(context);
		}
		// imageLoader.loadImage(context, urlString, listener);
		imageLoader.loadImage(urlString, listener);
	}

	// public static void resizeImageView(Activity activity, ImageView image) {
	//
	// Bitmap bm = ((BitmapDrawable) image.getDrawable()).getBitmap();
	// Display display = activity.getWindowManager().getDefaultDisplay();
	// int width = display.getWidth();
	// final int height = width * bm.getHeight() / bm.getWidth();
	//
	// image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	// image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	// height));
	// }
}
