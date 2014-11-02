/*
 * Name: $RCSfile: UserInterfaceUtility.java,v $
 * Version: $Revision: 1.0 $
 * Date: $Date: Dec 12, 2012 10:49:59 PM $
 *
 * Copyright (C) 2012 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvpluspro.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * UserInterfaceUtility
 * 
 * @author nhanhk
 */
public class UiUtil
{
    public static final float HEADER_BAR_HEIGHT = 60; // dip

    public static final int LAYOUT_TYPE_LINEAR = 0;
    public static final int LAYOUT_TYPE_RELATIVE = 1;
    public static final int LAYOUT_TYPE_GALLERY = 2;
    public static final int LAYOUT_TYPE_FRAME = 3;

    public static final int ALPHA_DIALOG = 200;

    public static final String FONT_PATH = "fonts/";
    public static final String FONT_DROID_ROBOTO_BOLD = FONT_PATH
        + "Roboto-Bold.ttf";
    public static final String FONT_DROID_ROBOTO_ITALIC = FONT_PATH
        + "Roboto-Italic.ttf";
    public static final String FONT_DROID_ROBOTO_REGULAR = FONT_PATH
        + "Roboto-Regular.ttf";
    public static final String FONT_MYRIAD_PRO_BOLD = FONT_PATH
        + "myriad_pro_bold_cond.otf";

    // ===================== FONTS =====================

    /**
     * get font from assets
     * 
     * @param context
     * @param fontFilePath
     * @return
     */
    public static Typeface getTypefaceFromAssets(Context context,
        String fontFilePath)
    {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
            fontFilePath);
        return tf;
    }

    /**
     * Set DroidRobotoBold font for text view
     * 
     * @param context
     * @param textView
     */
    public static void setFontDroidRobotoBold(Context context,
        final TextView textView)
    {
        if (context != null)
        {
            textView.setTypeface(UiUtil.getTypefaceFromAssets(context,
                UiUtil.FONT_DROID_ROBOTO_BOLD));
        }
    }

    /**
     * Set DroidRobotoBold font for text view
     * 
     * @param context
     * @param textView
     */
    public static void setFontDroidRobotoBold(Context context,
        final View[] views)
    {
        if (context != null)
        {
            for (View view : views)
            {
                if (view instanceof TextView)
                {
                    ((TextView) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_BOLD));
                }
                else if (view instanceof EditText)
                {
                    ((EditText) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_BOLD));
                }
                else if (view instanceof Button)
                {
                    ((Button) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_BOLD));
                }
                else if (view instanceof RadioButton)
                {
                    ((RadioButton) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_BOLD));
                }
            }
        }
    }

    /**
     * Set DroidRobotoItalic font for text view
     * 
     * @param context
     * @param textView
     */
    public static void setFontDroidRobotoItalic(Context context,
        final TextView textView)
    {
        if (context != null)
        {
            textView.setTypeface(UiUtil.getTypefaceFromAssets(context,
                UiUtil.FONT_DROID_ROBOTO_ITALIC));
        }
    }

    /**
     * Set DroidRobotoItalic font for text view
     * 
     * @param context
     * @param textView
     */
    public static void setFontDroidRobotoItalic(Context context,
        final View[] views)
    {
        if (context != null)
        {
            for (View view : views)
            {
                if (view instanceof TextView)
                {
                    ((TextView) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_ITALIC));
                }
                else if (view instanceof EditText)
                {
                    ((EditText) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_ITALIC));
                }
                else if (view instanceof Button)
                {
                    ((Button) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_ITALIC));
                }
            }
        }
    }

    /**
     * Set DroidRobotoRegular font for text view
     * 
     * @param context
     * @param textView
     */
    public static void setFontDroidRobotoRegular(Context context,
        TextView textView)
    {
        if (context != null)
        {
            Typeface tf = UiUtil.getTypefaceFromAssets(context,
                UiUtil.FONT_DROID_ROBOTO_REGULAR);
            textView.setTypeface(tf, Typeface.NORMAL);
        }
    }

    /**
     * Set DroidRobotoRegular font for text view
     * 
     * @param context
     * @param textView
     */
    public static void setFontDroidRobotoRegular(Context context,
        final View[] views)
    {
        if (context != null)
        {
            for (View view : views)
            {
                if (view instanceof TextView)
                {
                    ((TextView) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_REGULAR));
                }
                else if (view instanceof EditText)
                {
                    ((EditText) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_REGULAR));
                }
                else if (view instanceof Button)
                {
                    ((Button) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_REGULAR));
                }
                else if (view instanceof CheckBox)
                {
                    ((CheckBox) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_DROID_ROBOTO_REGULAR));
                }
            }
        }
    }

    /**
     * Set MyriadProBold font for text view
     * 
     * @param context
     * @param textView
     */
    public static void setFontMyriadProBold(Context context,
        final TextView textView)
    {
        if (context != null)
        {
            textView.setTypeface(UiUtil.getTypefaceFromAssets(context,
                UiUtil.FONT_MYRIAD_PRO_BOLD));
        }
    }

    /**
     * Set MyriadProBold font for text view
     * 
     * @param context
     * @param textView
     */
    public static void setFontMyriadProBold(Context context, final View[] views)
    {
        if (context != null)
        {
            for (View view : views)
            {
                if (view instanceof TextView)
                {
                    ((TextView) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_MYRIAD_PRO_BOLD));
                }
                else if (view instanceof EditText)
                {
                    ((EditText) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_MYRIAD_PRO_BOLD));
                }
                else if (view instanceof Button)
                {
                    ((Button) view).setTypeface(UiUtil.getTypefaceFromAssets(
                        context, UiUtil.FONT_MYRIAD_PRO_BOLD));
                }
            }
        }
    }

    // =========================================================================

    /**
     * @param view: ImageView /linear layout/Relative layout
     * @param originalBitmap
     * @param newWidth
     * @param parentLayout 0: linear /1: relative
     */
    public static void scaleImageView2DByWidth(View view,
        BitmapDrawable originalBitmap, int newWidth, int parentLayout)
    {
        int height = originalBitmap.getBitmap().getHeight();
        int width = originalBitmap.getBitmap().getWidth();
        int newHeight = (int) (((double) newWidth / (double) width) * height);
        if (parentLayout == LAYOUT_TYPE_LINEAR)
        {
            LinearLayout.LayoutParams paramsLinear = (LinearLayout.LayoutParams) view.getLayoutParams();
            paramsLinear.height = newHeight;
            paramsLinear.width = newWidth;
            view.setLayoutParams(paramsLinear);
        }
        else if (parentLayout == LAYOUT_TYPE_RELATIVE)
        {
            RelativeLayout.LayoutParams paramsRelative = (RelativeLayout.LayoutParams) view.getLayoutParams();
            paramsRelative.height = newHeight;
            paramsRelative.width = newWidth;
            view.setLayoutParams(paramsRelative);
        }
        else if (parentLayout == LAYOUT_TYPE_GALLERY)
        {
            Gallery.LayoutParams paramsGallery = (Gallery.LayoutParams) view.getLayoutParams();
            paramsGallery.height = newHeight;
            paramsGallery.width = newWidth;
            view.setLayoutParams(paramsGallery);
        }
        else if (parentLayout == LAYOUT_TYPE_FRAME)
        {
            FrameLayout.LayoutParams paramsFrame = (FrameLayout.LayoutParams) view.getLayoutParams();
            paramsFrame.height = newHeight;
            paramsFrame.width = newWidth;
            view.setLayoutParams(paramsFrame);
        }
    }

    /**
     * @param view: ImageView /linear layout/Relative layout
     * @param originalBitmap
     * @param newWidth
     * @param parentLayout 0: linear /1: relative
     */
    public static void scaleImageView2DByHeight(View view,
        BitmapDrawable originalBitmap, int newHeight, int parentLayout)
    {
        int height = originalBitmap.getBitmap().getHeight();
        int width = originalBitmap.getBitmap().getWidth();
        int newWidth = (int) (((double) newHeight / (double) height) * width);
        if (parentLayout == 0)
        {
            LinearLayout.LayoutParams paramsLinear = (LinearLayout.LayoutParams) view.getLayoutParams();
            paramsLinear.height = newHeight;
            paramsLinear.width = newWidth;
            view.setLayoutParams(paramsLinear);
        }
        else if (parentLayout == 1)
        {
            RelativeLayout.LayoutParams paramsRelative = (RelativeLayout.LayoutParams) view.getLayoutParams();
            paramsRelative.height = newHeight;
            paramsRelative.width = newWidth;
            view.setLayoutParams(paramsRelative);
        }
        else if (parentLayout == LAYOUT_TYPE_GALLERY)
        {
            Gallery.LayoutParams paramsGallery = (Gallery.LayoutParams) view.getLayoutParams();
            paramsGallery.height = newHeight;
            paramsGallery.width = newWidth;
            view.setLayoutParams(paramsGallery);
        }
        else if (parentLayout == LAYOUT_TYPE_FRAME)
        {
            FrameLayout.LayoutParams paramsFrame = (FrameLayout.LayoutParams) view.getLayoutParams();
            paramsFrame.height = newHeight;
            paramsFrame.width = newWidth;
            view.setLayoutParams(paramsFrame);
        }
    }

    /**
     * setLayoutWidth
     * 
     * @param view
     * @param newWidth
     * @param parentLayoutType: 0-linear, 1-relative
     */
    public static void setLayoutWidth(View view, int newWidth,
        int parentLayoutType)
    {
        if (parentLayoutType == LAYOUT_TYPE_LINEAR)
        {
            LinearLayout.LayoutParams paramsLinear = (LinearLayout.LayoutParams) view.getLayoutParams();
            paramsLinear.width = newWidth;
            view.setLayoutParams(paramsLinear);
        }
        else if (parentLayoutType == LAYOUT_TYPE_RELATIVE)
        {
            RelativeLayout.LayoutParams paramsRelative = (RelativeLayout.LayoutParams) view.getLayoutParams();
            paramsRelative.width = newWidth;
            view.setLayoutParams(paramsRelative);
        }
        else if (parentLayoutType == LAYOUT_TYPE_GALLERY)
        {
            Gallery.LayoutParams paramsGallery = (Gallery.LayoutParams) view.getLayoutParams();
            paramsGallery.width = newWidth;
            view.setLayoutParams(paramsGallery);
        }
        else if (parentLayoutType == LAYOUT_TYPE_FRAME)
        {
            FrameLayout.LayoutParams paramsFrame = (FrameLayout.LayoutParams) view.getLayoutParams();
            paramsFrame.width = newWidth;
            view.setLayoutParams(paramsFrame);
        }
    }

    /**
     * setLayoutHeight
     * 
     * @param view
     * @param newHeight
     * @param parentLayoutType: 0-linear, 1-relative
     */
    public static void setLayoutHeight(View view, int newHeight,
        int parentLayoutType)
    {
        if (parentLayoutType == LAYOUT_TYPE_LINEAR)
        {
            LinearLayout.LayoutParams paramsLinear = (LinearLayout.LayoutParams) view.getLayoutParams();
            paramsLinear.height = newHeight;
            view.setLayoutParams(paramsLinear);
        }
        else if (parentLayoutType == LAYOUT_TYPE_RELATIVE)
        {
            RelativeLayout.LayoutParams paramsRelative = (RelativeLayout.LayoutParams) view.getLayoutParams();
            paramsRelative.height = newHeight;
            view.setLayoutParams(paramsRelative);
        }
        else if (parentLayoutType == LAYOUT_TYPE_GALLERY)
        {
            Gallery.LayoutParams paramsGallery = (Gallery.LayoutParams) view.getLayoutParams();
            paramsGallery.height = newHeight;
            view.setLayoutParams(paramsGallery);
        }
        else if (parentLayoutType == LAYOUT_TYPE_FRAME)
        {
            FrameLayout.LayoutParams paramsFrame = (FrameLayout.LayoutParams) view.getLayoutParams();
            paramsFrame.height = newHeight;
            view.setLayoutParams(paramsFrame);
        }
    }

    /**
     * setLayoutWidthHeight
     * 
     * @param view
     * @param newHeight
     * @param parentLayoutType: 0-linear, 1-relative
     */
    public static void setLayoutWidthHeight(View view, int newWidth,
        int newHeight, int parentLayoutType)
    {
        if (parentLayoutType == LAYOUT_TYPE_LINEAR)
        {
            LinearLayout.LayoutParams paramsLinear = (LinearLayout.LayoutParams) view.getLayoutParams();
            paramsLinear.height = newHeight;
            paramsLinear.width = newWidth;
            view.setLayoutParams(paramsLinear);
        }
        else if (parentLayoutType == LAYOUT_TYPE_RELATIVE)
        {
            RelativeLayout.LayoutParams paramsRelative = (RelativeLayout.LayoutParams) view.getLayoutParams();
            paramsRelative.height = newHeight;
            paramsRelative.width = newWidth;
            view.setLayoutParams(paramsRelative);
        }
        else if (parentLayoutType == LAYOUT_TYPE_GALLERY)
        {
            Gallery.LayoutParams paramsGallery = (Gallery.LayoutParams) view.getLayoutParams();
            paramsGallery.height = newHeight;
            paramsGallery.width = newWidth;
            view.setLayoutParams(paramsGallery);
        }
        else if (parentLayoutType == LAYOUT_TYPE_FRAME)
        {
            FrameLayout.LayoutParams paramsFrame = (FrameLayout.LayoutParams) view.getLayoutParams();
            paramsFrame.height = newHeight;
            paramsFrame.width = newWidth;
            view.setLayoutParams(paramsFrame);
        }
    }

    // =======================convert Pix and Dip===================

    /**
     * px dip into
     * 
     * @param dipValue
     * @return
     */
    public static int convertDips2Pixels(Context context, float dipValue)
    {
        return (int) convertDpToPixel(context, dipValue);
    }

    /**
     * This method convets dp unit to equivalent device specific value in
     * pixels.
     * 
     * @param dp A value in dp(Device independent pixels) unit. Which we need to
     *        convert into pixels
     * @param context Context to get resources and device specific display
     *        metrics
     * @return A float value to represent Pixels equivalent to dp according to
     *         device
     */
    public static float convertDpToPixel(Context context, float dipValue)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = dipValue * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to device independent pixels.
     * 
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display
     *        metrics
     * @return A float value to represent db equivalent to px value
     */
    public static float convertPixelsToDp(Context context, float pixels)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dp = pixels / (metrics.densityDpi / 160f);
        return dp;
    }

    /**
     * set opacity for dialog
     * 
     * @param dialog
     * @param bgColor
     * @param alpha
     */
    public static void setDialogOpacity(Dialog dialog, int bgColor, int alpha)
    {
        ColorDrawable bgDrawable = new ColorDrawable(bgColor);
        bgDrawable.setAlpha(alpha);
        dialog.getWindow().setBackgroundDrawable(bgDrawable);
    }

    /**
     * get Screen Width
     * 
     * @param activity
     * @return width
     */
    public static int getScreenWidth(Activity activity)
    {
        int width = 0;
        // initialize the DisplayMetrics object
        DisplayMetrics deviceDisplayMetrics = new DisplayMetrics();

        // populate the DisplayMetrics object with the display characteristics
        activity.getWindowManager().getDefaultDisplay().getMetrics(
            deviceDisplayMetrics);

        // get the width and height
        width = deviceDisplayMetrics.widthPixels;
        return width;
    }
}