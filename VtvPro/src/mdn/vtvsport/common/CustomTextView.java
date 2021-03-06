package mdn.vtvsport.common;

import mdn.vtvsport.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class CustomTextView extends TextView {

	public static final String TEXT_STYLE_NORMAL = "normal";
	public static final String TEXT_STYLE_BOLD = "bold";
	public static final String TEXT_STYLE_ITALIC = "italic";

	public CustomTextView(Context context) {
		super(context);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	/**
	 * 
	 * @param ctx
	 * @param attrs
	 */
	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx
				.obtainStyledAttributes(attrs, R.styleable.CustomView);
		String nameOfFont = a.getString(R.styleable.CustomView_customFont);
		String customTextStyle = a
				.getString(R.styleable.CustomView_customTextStyle);
		// set default name of font
		if (nameOfFont == null) {
			nameOfFont = "Marmellata(Jam)_demo.ttf";
		}

		// set default text style
		if (customTextStyle == null) {
			customTextStyle = TEXT_STYLE_NORMAL;
		}

		setCustomFont(ctx, nameOfFont, customTextStyle);
		a.recycle();
	}

	/**
	 * set custom font for text view
	 * 
	 * @param ctx
	 * @param nameOfFont
	 * @return
	 */
	public boolean setCustomFont(Context ctx, String nameOfFont,
			String textStyle) {
		if (textStyle.equals(TEXT_STYLE_BOLD)) {
			// bold
			if (nameOfFont.contains("Helvetica")) {
				nameOfFont = "Helvetica CE Bold.ttf";
			} else if (nameOfFont.contains("Stag")) {

			}
		} else if (textStyle.equals(TEXT_STYLE_ITALIC)) {
			// italic
		} else {
			// normal
		}

		Typeface typeface = loadFont(ctx, "fonts/" + nameOfFont);
		if (typeface == null) {
			return false;
		}

		setTypeface(typeface);
		return true;
	}

	/**
	 * Load font
	 * 
	 * @param context
	 * @param pathOfFont
	 * @return
	 */
	private Typeface loadFont(Context context, String pathOfFont) {
		Typeface tf = null;
		try {
			tf = Typeface.createFromAsset(context.getAssets(), pathOfFont);
		} catch (Exception e) {
			Log.e("Could not get typeface: ", e.getMessage());
		}
		return tf;
	}

}