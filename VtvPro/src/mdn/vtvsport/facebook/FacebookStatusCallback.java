package mdn.vtvsport.facebook;

import com.facebook.Session;
import com.facebook.SessionState;

public interface FacebookStatusCallback {

	/**
	 * 
	 * @param session
	 * @param state
	 * @param exception
	 */
	public void call(Session session, SessionState state, Exception exception);
}
