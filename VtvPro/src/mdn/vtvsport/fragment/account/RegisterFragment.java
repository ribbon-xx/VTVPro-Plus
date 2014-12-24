package mdn.vtvsport.fragment.account;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import mdn.vtvsport.R;
import mdn.vtvsport.common.DialogManager;
import mdn.vtvsport.common.ParserManager;
import mdn.vtvsport.fragment.BaseFragment;
import mdn.vtvsport.network.ApiManager;
import mdn.vtvsport.network.IApiCallback;
import mdn.vtvsport.network.WebServiceConfig;
import mdn.vtvsport.network.WebServiceParam;
import mdn.vtvsport.object.account.RegisterInfo;
import mdn.vtvsport.util.StringUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RegisterFragment extends BaseFragment implements OnClickListener {
	private EditText edUser;
	private EditText edPass;
	private EditText edRePass;
	private EditText edCaptcha;
	private ImageView imvCaptcha;
	private ProgressBar pbLoad;

	private String pChallenge;
	private String pNewChallenge;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_register, container,
				false);
		bindView(view);
		callImgCaptcha();
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
		edUser = (EditText) view.findViewById(R.id.edUserName);
		edPass = (EditText) view.findViewById(R.id.edPassword);
		edRePass = (EditText) view.findViewById(R.id.edRePassword);
		edCaptcha = (EditText) view.findViewById(R.id.edCaptcha);
		imvCaptcha = (ImageView) view.findViewById(R.id.imgCaptcha);
		Button btReg = (Button) view.findViewById(R.id.btRegis);
		btReg.setOnClickListener(this);
		ImageView btReCaptcha = (ImageView) view
				.findViewById(R.id.imvRefreshCaptcha);
		btReCaptcha.setOnClickListener(this);
		Button btLogin = (Button) view.findViewById(R.id.btLogin);
		btLogin.setOnClickListener(this);
		pbLoad = (ProgressBar) view.findViewById(R.id.progressLoad);
		pbLoad.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btRegis:
			callRegister();
			break;
		case R.id.imvRefreshCaptcha:
			callImgCaptcha();
			break;
		case R.id.btLogin:
			callToLogin();
			break;
		default:
			break;
		}
	}

	private void callImgCaptcha() {
		if (pbLoad.getVisibility() == View.VISIBLE) {
			return;
		}
		pNewChallenge = StringUtil.getCaptcha();
		new AsyncTask<String, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				// TODO Auto-generated method stub
				Bitmap img = null;
				String url = WebServiceConfig.getUrlCaptcha(params[0]);
				InputStream in = null;
				try {
					in = new URL(url).openStream();
					img = BitmapFactory.decodeStream(in);
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					in = null;
				}

				return img;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				pbLoad.setVisibility(View.GONE);
				if (result == null) {
					DialogManager.alert(baseSlideMenuActivity,
							getString(R.string.network_fail));
				} else {
					edCaptcha.setText("");
					pChallenge = pNewChallenge;
					imvCaptcha.setImageBitmap(result);
				}
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				pbLoad.setVisibility(View.VISIBLE);

			}

		}.execute(pNewChallenge);
	}

	private void callRegister() {
		if (!checkRegistry()) {
			return;
		}

		ApiManager.callRegister(
				baseSlideMenuActivity,
				new IApiCallback() {

					@Override
					public void responseSuccess(String response) {
						// TODO Auto-generated method stub
						RegisterInfo info = ParserManager
								.parserRegisterInfo(response);
						if (info.isSuccess()) {
							Toast.makeText(baseSlideMenuActivity,
									getString(R.string.regis_success),
									Toast.LENGTH_SHORT).show();

							// chuyen qua man hinh login
							Bundle bundle = new Bundle();
							bundle.putString(WebServiceParam.PARAM_USER_NAME,
									edUser.getText().toString());
							bundle.putString(WebServiceParam.PARAM_PASS, edPass
									.getText().toString());
							LoginFragment mLoginFragment = new LoginFragment();
							mLoginFragment.setArguments(bundle);
							baseSlideMenuActivity.switchContent(mLoginFragment,
									false);
						} else {
							DialogManager.alert(baseSlideMenuActivity,
									info.getMessage());
							callImgCaptcha();
						}
					}

					@Override
					public void responseFailWithCode(int statusCode) {
						// TODO Auto-generated method stub
						DialogManager.alert(baseSlideMenuActivity,
								getString(R.string.network_fail));
					}
				}, edUser.getText().toString(), edPass.getText().toString(),
				pChallenge, edCaptcha.getText().toString());
	}

	private void callToLogin() {
		baseSlideMenuActivity.switchContent(new LoginFragment(), false);
	}

	private boolean checkRegistry() {
		String strEmail = edUser.getText().toString().trim();
		String strPass = edPass.getText().toString().trim();
		String strRePass = edRePass.getText().toString().trim();
		String strCaptcha = edCaptcha.getText().toString().trim();

		if (strEmail.length() == 0 || strPass.length() == 0
				|| strRePass.length() == 0 || strCaptcha.length() == 0) {
			Toast.makeText(baseSlideMenuActivity,
					getString(R.string.full_info), Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!StringUtil.checkEmail(strEmail)) {
			Toast.makeText(baseSlideMenuActivity,
					getString(R.string.erorr_email), Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!strPass.equals(strRePass)) {
			Toast.makeText(baseSlideMenuActivity,
					getString(R.string.error_pass), Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}
}
