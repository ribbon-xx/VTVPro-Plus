package mdn.vtvpluspro.object;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hieuxit on 8/11/14.
 */
public class Charginginfo {
	public int code;
	public int type;
	public String popupTitle;
	public String popupBody;
	public String popupUrl;
	public String smsBody;
	public String smsGateway;
	public int isDeleteSmsReceiver;
	public String nonSmsBody;
	public String nonSmsGateway;
	public int isDeleteNonSmsReceiver;
	@Override
	public String toString() {
		return "[type: " + type + ", popup title: " + popupTitle +
				", popupBody: " + popupBody + ", sms: " + smsGateway + " - " + smsBody + "]";
	}

	public static Charginginfo fromResponse(String response) {
		Charginginfo info = new Charginginfo();
		try {
			JSONObject json = new JSONObject(response);
			info.code = json.optInt("code");
			if (info.code != 1) {
				return info;
			}
			JSONObject data = json.optJSONObject("data");

			info.popupTitle = data.optString("popup_title");
			info.popupBody = data.optString("popup_body");
			info.type = data.optInt("payment_type");
			if (info.type == 3) {
				info.popupUrl = data.optString("popup_api");
			} else {
				JSONObject telcoData = data.optJSONObject("telco_data");
				if (info.type == 1) {
					info.smsGateway = telcoData.optString("sms_gateway");
					info.smsBody = telcoData.optString("sms_body");
					info.isDeleteSmsReceiver = telcoData.optInt("is_delete_sms_mt");

					info.nonSmsGateway = telcoData.optString("non_sms_gateway");
					info.nonSmsBody = telcoData.optString("non_sms_body");
					info.isDeleteNonSmsReceiver = telcoData.optInt("is_delete_non_sms_mt");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}
}
