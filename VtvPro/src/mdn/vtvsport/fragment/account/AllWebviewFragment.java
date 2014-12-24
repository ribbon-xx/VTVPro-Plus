package mdn.vtvsport.fragment.account;

import mdn.vtvsport.R;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.fragment.BaseFragment;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class AllWebviewFragment extends BaseFragment {
	private WebView wvContent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_all_webview, container,
				false);
		bindView(view);
		callSupport();
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

		wvContent.getSettings().setJavaScriptEnabled(true);
		wvContent.getSettings().setSupportZoom(true);
		wvContent.getSettings().setBuiltInZoomControls(true);
	}

	private void callSupport() {
		Bundle bundle = getArguments();
		String url = bundle.getString("url_hotro");
		if (url == null) {
			return;
		}
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
		}, url);
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

}
