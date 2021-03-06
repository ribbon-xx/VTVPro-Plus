package mdn.vtvsport.util;

import mdn.vtvsport.R;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AnimationUtil {

	public static Animation SlideOutRightLeft(Context context) {
		Animation animationSlide = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_right_left);
		return animationSlide;
	}

	public static Animation SlideInRightLeft(Context context) {
		Animation animationSlide = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_right_left);
		return animationSlide;
	}

	public static Animation SlideOutLeftRight(Context context) {
		Animation animationSlide = AnimationUtils.loadAnimation(context,
				R.anim.slide_out_left_right);
		return animationSlide;
	}

	public static Animation SlideInLeftRight(Context context) {
		Animation animationSlide = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_left_right);
		return animationSlide;
	}

	public static Animation FadeIn(Context context) {
		Animation animationFadeIn = AnimationUtils.loadAnimation(context,
				R.anim.fade_in);
		return animationFadeIn;
	}

	public static Animation FadeOut(Context context) {
		Animation animationFadeOut = AnimationUtils.loadAnimation(context,
				R.anim.fade_out);
		return animationFadeOut;
	}
	
	public static Animation FadeRotate90(Context context) {
		Animation animationFadeOut = AnimationUtils.loadAnimation(context,
				R.anim.fade_rotate_90);
		return animationFadeOut;
	}
}
