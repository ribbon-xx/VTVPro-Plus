/*
 * Name: $RCSfile: WebServiceConfig.java,v $
 * Version: $Revision: 1.0 $
 * Date: $Date: Oct 10, 2012 11:28:16 AM $
 *
 * Copyright (C) 2012 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvsport.network;

import android.annotation.SuppressLint;
import mdn.vtvsport.fragment.HomeFragment;

import java.net.URLEncoder;

/**
 * WebServiceConfig class contains web service settings
 *
 * @author MC
 */
@SuppressLint("DefaultLocale")
public class WebServiceConfig {

	/* ===================== WEB SERVICE CONSTANTs ===================== */
	public static int NETWORK_TIME_OUT = 60000;
	public static final String APP_API_VERSION = "1.0.0";
	public static int selectSever = 1;
//	private static final String MAIN_URL = "http://tuanda.thejupitech.com/vtvpluspro/";

	/* ===================== API URLs ===================== */

	public static String getServer() {
		if (selectSever == 0) {
//			return "http://solutions.thejupitech.com/anhhd/vtvplus/";
			return "http://tuanda.thejupitech.com/vtvpluspro/";
		} else if (selectSever == 1) {
			return "http://api.vtvsport.com.vn/index.php/";
		}
		return "";
	}

	public static String urlImageDefault = getServer()
			+ "files/uploads/images/no_image.png";

	public static String urlUpdateViewChannel() {
		return getServer() + "channel/update_view/";
	}

	public static String urlUpdateViewVod() {
		return getServer() + "vod/update_view/";
	}

	public static String urlUpdateViewEpisode() {
		return getServer() + "episode/update_view/";
	}

	public static String getItemOfCategory(String cateId, int offset, String userName) {
		// http://beta.vtvplus.vn/vtvsport/index.php/VodCate/{cate_id}/{limit}/{offset}/{platform}/
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("VodCate/").append(cateId).append("/10/").append(offset);
//		strBuilder.append("?");
//		if (userName != null && userName.length() > 0) {
//			strBuilder.append("username=").append(userName).append("&");
//		}
		strBuilder.append("/android");
		return strBuilder.toString();
	}

	// ignore
	public static String getItemOfListNew(int offset, String userName) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("category/listNew/10/").append(offset).append("/").append(HomeFragment.APP_NAME);
		strBuilder.append("?");
		if (userName != null && userName.length() > 0) {
			strBuilder.append("username=").append(userName).append("&");
		}
		strBuilder.append("platform=android");
		return strBuilder.toString();
	}
	
	public static String getItemOfChannel(int offset, String userName) {
		// http://beta.vtvplus.vn/vtvsport/index.php/ChannelList/{limit}/{offset}/{platform}/
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("ChannelList/10/").append(offset).append("/");
//		strBuilder.append("?");
//		if (userName != null && userName.length() > 0) {
//			strBuilder.append("username=").append(userName).append("&");
//		}
		strBuilder.append("android");
		return strBuilder.toString();
	}

	// ignore
	public static String getItemOfListFavoursit(int offset, String userName) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("channel/favorite/").append(userName).append("/").append(HomeFragment.APP_NAME);
		strBuilder.append("/10/").append(offset);
		strBuilder.append("?").append("platform=android");
		return strBuilder.toString();
	}

	// ignore
	public static String getItemOfListHot(int offset, String userName) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("category/listHot/10/").append(offset).append("/").append(HomeFragment.APP_NAME);
		strBuilder.append("?");
		if (userName != null && userName.length() > 0) {
			strBuilder.append("username=").append(userName).append("&");
		}
		strBuilder.append("platform=android");
		return strBuilder.toString();
	}

	public static String getEposideOfVod(String deviceId, String cateId,
	                                     int offset, String userName) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("episode/all/").append(cateId).append("/10/");
		strBuilder.append(offset).append("/").append(HomeFragment.APP_NAME);
		strBuilder.append("?");
		if (userName != null && userName.length() > 0) {
			strBuilder.append("username=").append(userName).append("&");
		}
		strBuilder.append("platform=android");
		return strBuilder.toString();
	}

	// change
	public static String getListHome(String email) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("HomeList/4/0/");
//		strBuilder.append("?");
//		if (email != null && email.length() > 0) {
//			strBuilder.append("username=").append(email).append("&");
//		}
		strBuilder.append("android");
		return strBuilder.toString();
	}

	public static String checkVersionApp(String version) {
		String url = String.format(getServer() + "system/version/%s/" + HomeFragment.APP_NAME,
				version);
		return url;
	}

	public static String getInfoApp(String version) {
		String url = String.format(getServer() + "system/infoApp/%s/" + HomeFragment.APP_NAME,
				version);

		return url;
	}

	public static String getListStream(String streamId, String userName, String ip, String session, String typeNetwork, int type) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		// http://beta.vtvplus.vn/vtvsport/index.php/Channelstream/8/113.162.65.220/test/$3kb3qx978l1xy1rhp81kvnxfa92r9i$/ios/
		if (type == 0) {
			strBuilder.append("Channelstream/");
		} else if (type == 1) {
			// http://beta.vtvplus.vn/vtvsport/index.php/Vodep/4/113.162.65.220/test/$3kb3qx978l1xy1rhp81kvnxfa92r9i$/ios/
			strBuilder.append("Vodep/");
		} else if (type == 2) {
			strBuilder.append("Vodep/");
		}

		strBuilder.append(streamId).append("/").append(ip);
		if (userName != null && userName.length() > 0) {
			strBuilder.append("/").append(userName);
		}
		if (session != null && session.length() > 0) {
			strBuilder.append("/").append(session);
		}
		strBuilder.append("/android");
//		strBuilder.append("&app_name=").append(HomeFragment.APP_NAME);
		return strBuilder.toString();
	}

	public static String getDetailsItemVtvPlus(String deviceId, String itemId,
	                                           String app_name, int limit, int offset, String userName, int type) {
		StringBuilder strBuilder = new StringBuilder(getServer());

		if (type == 0) {
			//http://beta.vtvplus.vn/vtvsport/index.php/Channel/8/4/0/ios/
			strBuilder.append("Channel/");
		} else if (type == 1) {
			// http://beta.vtvplus.vn/vtvsport/index.php/Vod/2/4/0/ios/
			strBuilder.append("Vod/");
		} else {
			strBuilder.append("Vod/");
		}
		strBuilder.append(itemId)
		.append("/").append(limit).append("/").append(offset);
//		strBuilder.append("?");
//		if (userName != null && userName.length() > 0) {
//			strBuilder.append("username=").append(userName).append("&");
//		}
		strBuilder.append("/android");
		return strBuilder.toString();
	}

	public static String getUrlSearch(String keyword, int offset, String userName) {
		//http://beta.vtvplus.vn/vtvsport/index.php/search/{key}/4/0/ios/
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("search/")
		.append(URLEncoder.encode(keyword));
		strBuilder.append("/10/").append(offset);
		strBuilder.append("/android");
//		if ((userName != null) && (userName.length() > 0)) {
//			strBuilder.append("&username=").append(userName);
//		}
		return strBuilder.toString().trim();
	}

	public static String getUrlLogin(String userName, String password) {
		// http://beta.vtvplus.vn/vtvsport/index.php/Login/test/123456/
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("Login/");
		strBuilder.append(userName).append("/");
		strBuilder.append(password).append("/");
		return strBuilder.toString();
	}

	public static String getAccountInfo(String userName, String session) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("subscriber/account_info/");
		strBuilder.append(userName).append("/");
		strBuilder.append(session).append("/");
		return strBuilder.toString();
	}

	public static String getUrlCaptcha(String challenge) {
		StringBuilder strBuilder = new StringBuilder("https://api.tvplus.mobi/vtvplus/index.php/mobiApi/app/generateCaptcha?challenge=");
		strBuilder.append(challenge);
		return strBuilder.toString();
	}

	public static String getUrlRegiter() {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("subscriber/register/");
		return strBuilder.toString();
	}

	public static String getUrlSchedule(String idChannel) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("calendar_channel/list/?channel_id=").append(idChannel);
		strBuilder.append("&full_day=1");
		return strBuilder.toString();
	}

	public static String getUrlUpdateRank(String idRank) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("api/v1/channel/update_rank/").append(idRank);
		strBuilder.append("/?type_device=android");
		return strBuilder.toString();
	}

	public static String getUrlSupport(boolean isHotro) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		if (!isHotro) {
			strBuilder.append("system/page_payment/?app_name=").append(HomeFragment.APP_NAME);
		} else {
			strBuilder.append("system/page_support/?app_name=").append(HomeFragment.APP_NAME);
		}
		return strBuilder.toString();
	}

	public static String getUrlLike(int type) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		if (type == 0) {
			strBuilder.append("channel/like/");
		} else if (type == 1) {
			strBuilder.append("vod/like/");
		} else if (type == 2) {
			strBuilder.append("episode/like/");
		}
		return strBuilder.toString();
	}

	public static String getUrlFavourist(int type) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		if (type == 0) {
			strBuilder.append("channel/favorite/");
		} else if (type == 1) {
			strBuilder.append("vod/favorite/");
		} else if (type == 2) {
			strBuilder.append("episode/favorite/");
		}
		return strBuilder.toString();
	}

	public static String getUrlInteractionChannel(String id) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("api/v1/channel/interaction/").append(id);
		strBuilder.append("/?app_name=").append(HomeFragment.APP_NAME);
		return strBuilder.toString();
	}

	public static String getUrlAllInteraction() {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("api/v1/channel/all_interaction/?app_name=").append(HomeFragment.APP_NAME);
		return strBuilder.toString();
	}

	public static String getCategoryVideoMenu() {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("menuvod");
		return strBuilder.toString();
	}

	// http://api.vtvsport.com.vn/index.php/fb/menu/
	public static String getListLeagues() {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("fb/menu/");
		return strBuilder.toString();
	}

	public static String getListRankings(String idLeague) {
		// http://api.vtvsport.com.vn/index.php/fb/ranking/1/
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("fb/ranking/").append(idLeague).append("/");
		return strBuilder.toString();
	}
	
	public static String getUrlAds(String idAds) {
		StringBuilder strBuilder = new StringBuilder(getServer());
		strBuilder.append("/ads/detail/").append(idAds);
		return strBuilder.toString();
	}

	public static String getUrlLogPush(String deviceId, String messId) {
		String url = String.format(getServer() + "push/%s/android",
				deviceId, messId);
		return url;
	}

	public static String getUrlCheckChargingEnable() {
		return "http://wrappersrv.soci.vn/gom_api_v3/api/get_production_setting.php";
	}
	
	// popup payment
	public static String getUrlGetPaymentPopupSetting(String distributorChannelId, String telcoGlobalCode) {
		return new StringBuilder().append("http://wrappersrv.soci.vn/vtv_plus/api/vtvplus_payment.php")
				.append("?distributor_channel_id=")
				.append(distributorChannelId)
				.append("&telco_global_code=")
				.append(telcoGlobalCode)
				.toString();
	}

	public static String getUrlCheckCharged(String userName, String session) {
		return new StringBuilder().append("http://api.tvplus.mobi/vtvplus/index.php/mobiApi/subscriber/checkSubscriberStatus")
				.append("/username/").append(userName).append("/session/").append(session).toString();
	}
	
	// api.vtvsport.com.vn/index.php/push/{device}/{platform}/
	public static String gcmPostRegistrationId = getServer()
			+ "push/device_push/";

	public static String getUrlCheckAppId(String appId) {
		return "http://203.162.235.124/Vietnet/service.php?action=GetAppId&Appid=" + appId;
	}

	// api.vtvsport.com.vn/index.php/push/{device}/{platform}/
	public static String getUrlPushNotification(String deviceId) {
        StringBuilder strBuilder = new StringBuilder(getServer());
        strBuilder.append("push/").append(deviceId + "/android/") ;
        return strBuilder.toString();
    }
	
    public static String getListMatchSchedule(long leagueCode) {
        StringBuilder strBuilder = new StringBuilder(getServer());
        strBuilder.append("fb/home/");
        strBuilder.append(leagueCode + "/");
        return strBuilder.toString();
    }

    public static String getListMatchScheduleMenuItem() {
        StringBuilder strBuilder = new StringBuilder(getServer());
        strBuilder.append("fb/menu/");
        return strBuilder.toString();
    }

    public static String getListLiveScore() {
        StringBuilder strBuilder = new StringBuilder(getServer());
        strBuilder.append("fb/livescore/");
        return strBuilder.toString();
    }
    
    public static String getSMSSetting() {
        StringBuilder strBuilder = new StringBuilder(getServer());
        strBuilder.append("getsms/1/android/");
        return strBuilder.toString();
    }
}