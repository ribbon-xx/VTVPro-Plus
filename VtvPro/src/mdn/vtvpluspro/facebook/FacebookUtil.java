package mdn.vtvpluspro.facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import mdn.vtvplus.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Request.Callback;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.PickerFragment;
import com.facebook.widget.PickerFragment.OnDoneButtonClickedListener;
import com.facebook.widget.PickerFragment.OnSelectionChangedListener;
import com.facebook.widget.PlacePickerFragment;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class FacebookUtil implements FacebookState {

	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");

	private static FacebookUtil mInstance;
	private final static String TAG = "Facebook";

	private Session mSession;
	private Activity mActivity;
	@SuppressWarnings("unused")
	private boolean mCanPresentShareDialog;
	private FacebookStatusCallback mFacebookStatusCallback;
	private GraphUserCallback mGraphUserCallback;
	private GraphUser mUser;
	private GraphPlace mPlace;
	private List<GraphUser> mTags = new ArrayList<GraphUser>();

	private String mMessage = "";
	private String mImagePath = "";
	private String mName = "";
	private String mCaption = "";
	private String mDescription = "";
	private String mLink = "";
	private String mPicture = "";

	public GraphUser getUser() {
		return mUser;
	}

	public void setFacebookStatusCallback(
			FacebookStatusCallback mFacebookStatusCallback) {
		this.mFacebookStatusCallback = mFacebookStatusCallback;
	}

	public void setMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public void setImagePath(String mImagePath) {
		this.mImagePath = mImagePath;
	}

	public void setUser(GraphUser mUser) {
		this.mUser = mUser;
	}

	public void setGraphUserCallback(GraphUserCallback mGraphUserCallback) {
		this.mGraphUserCallback = mGraphUserCallback;
	}

	private PendingAction mPendingAction = PendingAction.NONE;

	private enum PendingAction {
		NONE, // do nothing
		REQUEST_USER_DATA, // request user data
		POST_PHOTO, // share bitmap image
		POST_STATUS_UPDATE, // update status
		POST_FEED, // update feed
	}

	public StatusCallback mStatusCallback = new StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			// log all permissions
			logPermissions(session);

			// log exception
			if (exception != null) {
				exception.printStackTrace();
			}

			// process session state change
			onSessionStateChange(session, state, exception);

			// process owner called back
			if (mFacebookStatusCallback != null) {
				mFacebookStatusCallback.call(session, state, exception);
			}
		}
	};

	/**
	 * Handle state challenge of session
	 * 
	 * @param session
	 * @param state
	 * @param exception
	 */
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {

		Log.d(TAG, "session state chalenge");
		if (mPendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			showAlert(mActivity, mActivity.getString(R.string.cancelled),
					mActivity.getString(R.string.permission_not_granted));
			mPendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED
				|| state == SessionState.OPENED) {
			handlePendingAction();
		}
	}

	/**
	 * private constructor
	 */
	private FacebookUtil(Activity activity) {
		this.mActivity = activity;
	}

	public static FacebookUtil newInstance(Activity activity) {
		mInstance = new FacebookUtil(activity);
		return mInstance;
	}

	/**
	 * Method is called onCreate state of activity
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// get active session
		mSession = Session.getActiveSession();

		if (mSession == null) {

			if (savedInstanceState != null) {
				// restore session with bundle saved onStateChange
				mSession = Session.restoreSession(mActivity, null,
						mStatusCallback, savedInstanceState);
			}

			if (mSession == null) {
				// create session
				mSession = new Session(mActivity);
			}

			// set current session is active
			Session.setActiveSession(mSession);
		}

		mCanPresentShareDialog = FacebookDialog.canPresentShareDialog(
				mActivity, FacebookDialog.ShareDialogFeature.SHARE_DIALOG);
	}

	/**
	 * Method is called onStart state of activity
	 */
	@Override
	public void onStart() {

		Log.d(TAG, "Facebook add status callback");
		Session.getActiveSession().addCallback(mStatusCallback);
	}

	/**
	 * Method is called onStop state of activity
	 */
	@Override
	public void onStop() {

		Log.d(TAG, "Facebook remove status callback");
		Session.getActiveSession().removeCallback(mStatusCallback);
	}

	/**
	 * Method is called onSaveInstanceState of activity
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {

		Log.d(TAG, "Facebook save session");
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	/**
	 * Method is called onActivityResult
	 */
	@Override
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {

		Log.d(TAG, "Facebook handle on activity result");
		Session.getActiveSession().onActivityResult(activity, requestCode,
				resultCode, data);
	}

	/**
	 * Call method to login Facebook on Activity
	 * 
	 * @param activity
	 *            The activity contains view to login Facebook
	 */
	@Override
	public void login(Activity activity) {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			Log.d(TAG, "login facebook on activity");
			session.openForRead(new Session.OpenRequest(activity)
					.setPermissions(FacebookConfig.READ_PERMISSIONS)
					.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK)
					.setCallback(mStatusCallback));
		} else {
			Log.d(TAG, "facebook is logined, do something");
			Session.openActiveSession(activity, true, mStatusCallback);
		}
	}

	/**
	 * Call method to logout of Facebook
	 */
	@Override
	public void logout() {

		Log.d(TAG, "logout facebook");
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
			mSession = null;
		}
	}

	/**
	 * request user data
	 * 
	 * @param session
	 * @param graphUserCallback
	 */
	public void requestUserData(GraphUserCallback graphUserCallback) {
		Log.d(TAG, "request user data");
		mGraphUserCallback = graphUserCallback;
		// we don't use share dialog then allowNoSession = false
		performAction(PendingAction.REQUEST_USER_DATA, false);
	}

	/**
	 * request post status update
	 */
	public void requestPostStatusUpdate(String message) {
		Log.d(TAG, "request post status update");
		mMessage = message;
		// we don't use share dialog then allowNoSession = false
		performAction(PendingAction.POST_STATUS_UPDATE, false);
	}

	/**
	 * request post photo
	 */
	public void requestPostPhoto(String imagePath) {
		Log.d(TAG, "request post photo");
		mImagePath = imagePath;
		// we don't use share dialog then allowNoSession = false
		performAction(PendingAction.POST_PHOTO, false);
	}

	/**
	 * 
	 * @param name
	 * @param caption
	 * @param description
	 * @param link
	 * @param picture
	 */
	public void requestPostFeed(String name, String caption,
			String description, String link, String picture) {
		Log.d(TAG, "request post feed");
		mName = name;
		mCaption = caption;
		mDescription = description;
		mLink = link;
		mPicture = picture;
		// we don't use share dialog then allowNoSession = false
		performAction(PendingAction.POST_FEED, false);
	}

	/**
	 * 
	 * @param action
	 * @param allowNoSession
	 */
	private void performAction(PendingAction action, boolean allowNoSession) {

		mPendingAction = action;

		Session session = Session.getActiveSession();

		// We only request publish actions when we joined facebook
		if (session != null && session.isOpened()) {
			Log.d(TAG, "we joined facebook, do action");
			// when we request user data, we don't need checking publish
			// permission
			if (mPendingAction == PendingAction.REQUEST_USER_DATA) {
				handlePendingAction();
				return;
			}

			if (hasPublishPermission()) {
				Log.d(TAG,
						"We have publish permission then we can do the action right away");
				handlePendingAction();
			} else {
				Log.d(TAG,
						" We need to get new permissions, then complete the action "
								+ "when we get the called back.");
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
						mActivity, PERMISSIONS).setLoginBehavior(
						SessionLoginBehavior.SUPPRESS_SSO).setCallback(
						mStatusCallback));
				return;
			}
		} else {
			Log.d(TAG, "we don't join facebook, show dialog to join");
			login(mActivity);
		}

		// TODO disable allow no session
		// if (allowNoSession) {
		// mPendingAction = action;
		// handlePendingAction();
		// }
	}

	/**
	 * handle pending action
	 */
	public void handlePendingAction() {

		PendingAction previousPendingAction = mPendingAction;
		switch (previousPendingAction) {
		case POST_PHOTO:
			// postPhoto(mImagePath);
			break;
		case POST_STATUS_UPDATE:
			postStatusUpdate(mMessage);
			break;
		case POST_FEED:
			postFeed(mName, mCaption, mDescription, mLink, mPicture);
			break;
		case REQUEST_USER_DATA:
			getUserData(mGraphUserCallback);
		default:
			break;
		}
	}

	/**
	 * 
	 * @param graphUserCallback
	 */
	public void getUserData(GraphUserCallback graphUserCallback) {
		Request request = Request.newMeRequest(Session.getActiveSession(),
				graphUserCallback);
		request.executeAsync();
	}

	/**
	 * post photo to facebook
	 * 
	 * @param imagePath
	 */
	// private void postPhoto(final String imagePath) {
	//
	// if (/* mUser != null && */hasPublishPermission()) {
	// Log.d(TAG, "post photo");
	// Bitmap bitmap = BitmapUtil.getBitmapFromPath(mActivity, 100,
	// imagePath);
	//
	// Request request = Request.newUploadPhotoRequest(
	// Session.getActiveSession(), bitmap, new Callback() {
	//
	// @Override
	// public void onCompleted(Response response) {
	// showPublishResult(mActivity,
	// mActivity.getString(R.string.photo_post),
	// response.getGraphObject(),
	// response.getError());
	// }
	// });
	// request.executeAsync();
	// } else {
	// Log.d(TAG,
	// " We need to get new permissions, then complete the action "
	// + "when we get the called back.");
	// mPendingAction = PendingAction.POST_PHOTO;
	// Session session = Session.getActiveSession();
	// session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
	// mActivity, PERMISSIONS).setLoginBehavior(
	// SessionLoginBehavior.SUPPRESS_SSO).setCallback(
	// mStatusCallback));
	// }
	// }

	/**
	 * post status update
	 * 
	 * @param message
	 */
	private void postStatusUpdate(final String message) {

		// if (mCanPresentShareDialog) {
		// // if Facebook is installed on device, show share dialog
		//
		// } else
		if (/* mUser != null && */hasPublishPermission()) {
			Log.d(TAG, "post status update");
			/*
			 * If use joined Facebook, we use graph to post status
			 */
			Request request = Request.newStatusUpdateRequest(
					Session.getActiveSession(), message, mPlace, mTags,
					new Callback() {

						@Override
						public void onCompleted(Response response) {
							mPendingAction = PendingAction.NONE;
							showPublishResult(mActivity, message,
									response.getGraphObject(),
									response.getError());
						}
					});
			request.executeAsync();
		} else {
			Log.d(TAG,
					" We need to get new permissions, then complete the action "
							+ "when we get the called back.");
			mPendingAction = PendingAction.POST_STATUS_UPDATE;
			Session session = Session.getActiveSession();
			session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
					mActivity, PERMISSIONS).setLoginBehavior(
					SessionLoginBehavior.SUPPRESS_SSO).setCallback(
					mStatusCallback));

		}
	}

	/**
	 * 
	 * @param name
	 * @param caption
	 * @param description
	 * @param link
	 * @param picture
	 */
	public void postFeed(String name, String caption, String description,
			String link, String picture) {
		Bundle postParams = new Bundle();
		postParams.putString("name", name);
		postParams.putString("caption", caption);
		postParams.putString("description", description);
		postParams.putString("link", link);
		postParams.putString("picture", picture);

		Log.d(TAG, "post feed");
		Log.d(TAG, "param: " + postParams.toString());

		// Request request = new Request(Session.getActiveSession(), "me/feed",
		// postParams, HttpMethod.POST, new Callback() {
		//
		// @Override
		// public void onCompleted(Response response) {
		// Log.d("TAG", "aa " + response.toString());
		// JSONObject graphResponse = response.getGraphObject()
		// .getInnerJSONObject();
		// @SuppressWarnings("unused")
		// String postId = null;
		// try {
		// postId = graphResponse.getString("id");
		// } catch (JSONException e) {
		// e.printStackTrace();
		// }
		// FacebookRequestError error = response.getError();
		// if (error != null) {
		// showAlert(mActivity, "Facebook",
		// error.getErrorMessage());
		// } else {
		// showAlert(mActivity, "Facebook", "Success");
		// }
		// }
		// });
		// RequestAsyncTask task = new RequestAsyncTask(request);
		// task.execute();

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(mActivity,
				Session.getActiveSession(), postParams)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						// TODO Auto-generated method stub
						if (error == null) {
							final String postId = values.getString("post_id");

							if (postId != null) {
								Toast.makeText(mActivity,
										"Posted story, id: " + postId,
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(mActivity, "Publish cancelled ",
										Toast.LENGTH_SHORT).show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							Toast.makeText(mActivity, "Publish cancelled ",

							Toast.LENGTH_SHORT).show();
						} else {

							Toast.makeText(mActivity, "Error posting story",
									Toast.LENGTH_SHORT).show();
						}
					}

				}).build();
		feedDialog.show();

	}

	public FacebookDialog.ShareDialogBuilder createShareDialogBuilder(
			String name, String description, String link) {
		return new FacebookDialog.ShareDialogBuilder(mActivity).setName(name)
				.setDescription(description).setLink(link);
	}

	/**
	 * check session has publish permission
	 * 
	 * @return
	 */
	public boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		List<String> permissions = session.getPermissions();
		return isSubsetOf(PERMISSIONS, permissions);
	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * show permission of session
	 * 
	 * @param session
	 */
	public void logPermissions(Session session) {
		List<String> permissions = session.getPermissions();
		for (String permission : permissions) {
			Log.d(TAG, "Permission: " + permission);
		}
	}

	/**
	 * show permission
	 * 
	 * @param permissions
	 */
	public void logPermissions(List<String> permissions) {
		if (permissions == null) {
			return;
		} else {
			if (permissions.size() == 0)
				return;
		}
		for (String permission : permissions) {
			Log.d(TAG, "Permission: " + permission);
		}
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param message
	 */
	private void showAlert(Context context, String title, String message) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(message)
				.setPositiveButton(R.string.ok, null).show();
	}

	/**
	 * show publish result
	 * 
	 * @param context
	 * @param message
	 * @param result
	 * @param error
	 */
	private void showPublishResult(Context context, String message,
			GraphObject result, FacebookRequestError error) {
		String title = null;
		String alertMessage = null;
		if (error == null) {
			title = context.getString(R.string.success);
			String id = result.cast(GraphObjectWithId.class).getId();
			alertMessage = context.getString(R.string.successfully_posted_post,
					message, id);
		} else {
			title = context.getString(R.string.error);
			alertMessage = error.getErrorMessage();
		}

		new AlertDialog.Builder(context).setTitle(title)
				.setMessage(alertMessage).setPositiveButton(R.string.ok, null)
				.show();
	}

	private interface GraphObjectWithId extends GraphObject {
		String getId();
	}

	/**
	 * 
	 * @param activity
	 * @param containerId
	 * @param currentLocation
	 */
	public void pickPlace(final FragmentActivity activity,
			final int containerId, final Location currentLocation) {

		// create picker fragment
		final PlacePickerFragment placePickerFragment = new PlacePickerFragment();
		placePickerFragment.setLocation(currentLocation);

		// handle done listener
		placePickerFragment
				.setOnDoneButtonClickedListener(new OnDoneButtonClickedListener() {

					@Override
					public void onDoneButtonClicked(PickerFragment<?> fragment) {
						onPlacePickerDone(activity, placePickerFragment);
					}
				});

		// handle selected listener
		placePickerFragment
				.setOnSelectionChangedListener(new OnSelectionChangedListener() {

					@Override
					public void onSelectionChanged(PickerFragment<?> fragment) {
						onPlacePickerDone(activity, placePickerFragment);
					}
				});

		// show place picker fragment
		showPickerFragment(activity, containerId, placePickerFragment);
	}

	/**
	 * 
	 * @param activity
	 * @param fragment
	 */
	private void onPlacePickerDone(FragmentActivity activity,
			PlacePickerFragment fragment) {

		// remove picker place
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.popBackStack();

		String result = "";

		GraphPlace selection = fragment.getSelection();
		if (selection != null) {
			result = selection.getName();
		} else {
			result = activity.getString(R.string.no_place_selected);
		}

		mPlace = selection;

		showAlert(activity, activity.getString(R.string.you_picked), result);
	}

	/**
	 * 
	 * @param activity
	 * @param containerId
	 * @param fragment
	 */
	private void showPickerFragment(final FragmentActivity activity,
			final int containerId, PickerFragment<?> fragment) {

		fragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
			@Override
			public void onError(PickerFragment<?> pickerFragment,
					FacebookException error) {
				String text = activity.getString(R.string.exception,
						error.getMessage());
				String title = activity.getString(R.string.error);
				showAlert(mActivity, title, text);
			}
		});

		FragmentManager fm = activity.getSupportFragmentManager();
		fm.beginTransaction().replace(containerId, fragment)
				.addToBackStack(null).commit();

		// We want the fragment fully created so we can use it immediately.
		fm.executePendingTransactions();

		fragment.loadData(false);
	}

}
