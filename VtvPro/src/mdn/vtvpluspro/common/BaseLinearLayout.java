/*
 * Name: $RCSfile: BaseLinearLayout.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Nov 24, 2011 9:46:32 AM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvpluspro.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Base linear layout
 * 
 * @author MC
 */
public class BaseLinearLayout extends LinearLayout {
	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public BaseLinearLayout(Context context) {
		super(context);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public BaseLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param resId
	 */
	public void initLayout(Context context, int resId) {
		LayoutInflater layoutInflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflate.inflate(resId, this, true);
	}
}