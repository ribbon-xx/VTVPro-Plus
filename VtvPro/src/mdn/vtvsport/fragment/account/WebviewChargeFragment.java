package mdn.vtvsport.fragment.account;

import mdn.vtvsport.R;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.fragment.BaseFragment;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.network.WebServiceConfig;
import mdn.vtvsport.object.account.UserInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebviewChargeFragment extends BaseFragment {
	private WebView wvContent;
	private ProgressBar loadingBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_webview_charge,
				container, false);
		bindView(view);
		initLayout();
		return view;
	}

	@Override
	protected void initUiTabbar() {
		// TODO Auto-generated method stub
		super.initUiTabbar();

		baseSlideMenuActivity.iconInteract.setVisibility(View.GONE);
		baseSlideMenuActivity.closeViewSearch();
		baseSlideMenuActivity.iconSetting.setVisibility(View.VISIBLE);
		baseSlideMenuActivity.iconBack.setVisibility(View.GONE);
		baseSlideMenuActivity.iconVtvPlus.setVisibility(View.VISIBLE);
	}

	private void bindView(View view) {
		wvContent = (WebView) view.findViewById(R.id.wvContent);
		loadingBar = (ProgressBar) view.findViewById(R.id.loadingBar);

		wvContent.getSettings().setJavaScriptEnabled(true);
		wvContent.getSettings().setSupportZoom(true);
		wvContent.getSettings().setBuiltInZoomControls(false);
		wvContent.getSettings().setAppCacheEnabled(false);
		wvContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wvContent.setWebChromeClient(new CustomWebChromeClient());
		wvContent.setWebViewClient(new CustomWebViewClient());
		wvContent.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	}

	private IApiCallback callbackLogin = new IApiCallback() {

		@Override
		public void responseSuccess(String response) {
			// TODO Auto-generated method stub
			UserInfo info = ParserManager.parserLoginInfo(response);
			if (info.isGetSucc()) {
				baseSlideMenuActivity.pProfile.pSession = info.getSession();
				callWebView(true);
			} else {
				DialogManager.alert(baseSlideMenuActivity, info.getMessage());
			}
		}

		@Override
		public void responseFailWithCode(int statusCode) {
			// TODO Auto-generated method stub
			DialogManager.alert(baseSlideMenuActivity,
					getString(R.string.network_fail));
		}
	};

	private void callWebView(boolean isLogin) {
		// check open purchase
		// purchase = 1: open
		// purchase = 0: close
		if (baseSlideMenuActivity.pVersion.getPurchase().equals("1")) {
			// set accept cookie
			CookieSyncManager.createInstance(baseSlideMenuActivity);
			CookieManager cm = CookieManager.getInstance();
			cm.setAcceptCookie(true);
			CookieSyncManager.getInstance().sync();

			StringBuilder strBuilder = new StringBuilder(
					"http://www.vtvplus.vn/index.php?view=mobi");
			if (isLogin) {
				strBuilder.append("&email=").append(
						baseSlideMenuActivity.pProfile.pUserName);
				strBuilder.append("&session=").append(
						baseSlideMenuActivity.pProfile.pSession);
			}
			wvContent.loadUrl(strBuilder.toString());
			strBuilder = null;
		} else {
			callSupport();
		}
	}

	private void callSupport() {
		ApiManager.callSupport(baseSlideMenuActivity, new IApiCallback() {

			@Override
			public void responseSuccess(String response) {
				// TODO Auto-generated method stub
				callWebview(ParserManager.parserSupport(response));
			}

			@Override
			public void responseFailWithCode(int statusCode) {
				// TODO Auto-generated method stub
				DialogManager.alert(baseSlideMenuActivity,
						getString(R.string.network_fail));
			}
		}, WebServiceConfig.getUrlSupport(false));
	}

	private void callWebview(String data) {
		StringBuilder tmp = new StringBuilder(
				"<body style=\"margin: 0; padding: 0\">");
		tmp.append(data);
		tmp.append("</body>");

		wvContent.loadDataWithBaseURL("fake://not/needed", tmp.toString(),
				"text/html", "UTF-8", "");
		tmp = null;
	}

	private void initLayout() {
		boolean isLogin = false;
		if (baseSlideMenuActivity.pProfile.pUserName != null
				&& baseSlideMenuActivity.pProfile.pUserName.length() > 0) {
			isLogin = true;
		} else {
			isLogin = false;
		}
		if (isLogin) {
			baseSlideMenuActivity.callLoginApi(callbackLogin);
		} else {
			callWebView(false);
		}
	}

	private class CustomWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			loadingBar.setVisibility(View.VISIBLE);
			loadingBar.setProgress(newProgress);
			if (newProgress == 100)
				loadingBar.setVisibility(View.GONE);
		}
	}

	private class CustomWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			view.freeMemory();
			view.removeAllViewsInLayout();
		}
	}

}
