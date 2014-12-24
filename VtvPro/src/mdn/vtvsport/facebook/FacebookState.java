package mdn.vtvsport.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public interface FacebookState {

	/**
	 * 
	 * @param savedInstanceState
	 */
	public void onCreate(Bundle savedInstanceState);

	/**
	 * 
	 * @param statusCallback
	 */
	public void onStart();

	/**
	 * 
	 * @param statusCallback
	 */
	public void onStop();

	/**
	 * 
	 * @param outState
	 */
	public void onSaveInstanceState(Bundle outState);

	/**
	 * 
	 * @param context
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data);

	/**
	 * Login on Activity class
	 * 
	 * @param activity
	 */
	public void login(Activity activity);

	/**
	 * Logout of facebook
	 */
	public void logout();

}
