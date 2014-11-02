package mdn.vtvpluspro.common;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AutoScaleImageViewerMatrix extends ImageView {

	public static int width_viewflow = 0;
	public static int height_viewflow = 0;

	public AutoScaleImageViewerMatrix(Context context) {
		super(context);
	}

	public AutoScaleImageViewerMatrix(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoScaleImageViewerMatrix(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int widthDrawable = 0;
		int heightDrawable = 0;
		int w = width;
		if (getDrawable() != null) {
			height = w * getDrawable().getIntrinsicHeight()
					/ getDrawable().getIntrinsicWidth();
			widthDrawable = getDrawable().getIntrinsicWidth();
			heightDrawable = getDrawable().getIntrinsicHeight();
			Matrix matrix = getImageMatrix();
			RectF drawableRect = new RectF(0, 0, widthDrawable, heightDrawable);
			RectF viewRect = new RectF(0, 0, w, height);
			matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
			setScaleType(ScaleType.MATRIX);
		}

		width_viewflow = width;
		height_viewflow = height;
		setMeasuredDimension(width, height);
	}
}
