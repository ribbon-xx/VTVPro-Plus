package mdn.vtvpluspro.object;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hieuxit on 8/11/14.
 */
public class ChargingEnableInfo {
	public int code;
	public int mEnable;
	public int mSendSmsBackground;

	public static ChargingEnableInfo fromResponse(String response){
		ChargingEnableInfo info = new ChargingEnableInfo();
		try {
			JSONObject json = new JSONObject(response);
			info.code = json.optInt("code");
			if(info.code == 0){
				return info;
			}
			JSONObject data = json.optJSONObject("data");
			if(data == null) return info;
			info.mEnable = data.optInt("check_charge_enable");
			info.mSendSmsBackground = data.optInt("is_background_sms");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}
}
