package mdn.vtvpluspro.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AutoScaleImageViewer extends ImageView {

	public static int width_viewflow = 0;
	public static int height_viewflow = 0;

	public AutoScaleImageViewer(Context context) {
		super(context);
	}

	public AutoScaleImageViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoScaleImageViewer(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		if (getDrawable() != null) {
			if (width > height) {
				height = width * getDrawable().getIntrinsicHeight()
						/ getDrawable().getIntrinsicWidth();
			} else {
				width = height * getDrawable().getIntrinsicWidth()
						/ getDrawable().getIntrinsicHeight();
			}

		}

		width_viewflow = width;
		height_viewflow = height;
		setMeasuredDimension(width, height);
	}
}
