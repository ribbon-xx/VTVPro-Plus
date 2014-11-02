/*
 * Name: $RCSfile: ParserManager.java,v $
 * Version: $Revision: 1.0 $
 * Date: $Date: Oct 10, 2012 1:49:08 PM $
 *
 * Copyright (C) 2012 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvpluspro.common;

import android.content.Context;
import mdn.vtvplus.R;
import mdn.vtvpluspro.fragment.HomeFragment;
import mdn.vtvpluspro.object.*;
import mdn.vtvpluspro.object.account.LoginInfo;
import mdn.vtvpluspro.object.account.RegisterInfo;
import mdn.vtvpluspro.object.account.UserInfo;
import mdn.vtvpluspro.object.home.HomeCategory;
import mdn.vtvpluspro.object.home.HomeItemLandListview;
import mdn.vtvpluspro.object.home.HomeItemListview;
import mdn.vtvpluspro.object.home.HomeObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * ParserUtility supports to parser http response
 * 
 * @author MC
 */
public final class ParserManager {
    public static String TAG = "ParserManager";

    /**
     * Parse gcm response
     */
    public static GCMInfo parserGCMResponse(String json) {
        GCMInfo info = new GCMInfo();
        try {
            JSONObject entry = new JSONObject(json);
            String type = getStringValue(entry, "type");
            info.setType(type);
            info.setMessage(getStringValue(entry, "message"));
            info.setItemId(getStringValue(entry, "item_id"));
            info.setMessageId(getStringValue(entry, "message_id"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return info;
    }

    public static AdsInfo parserPushAds(String json) {
        try {
            JSONObject entry = new JSONObject(json);
            JSONObject dataAds = getJSONObject(entry, "data");
            return parserItemAds(dataAds);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * Parse list categories
     */
    public static ArrayList<CategoryInfo> parserListCategories(String json) {
        ArrayList<CategoryInfo> listCategories = new ArrayList<CategoryInfo>();
        try {

            JSONObject response = new JSONObject(json);
            JSONArray listCategoryEntries = response.getJSONArray("data");
            for (int i = 0; i < listCategoryEntries.length(); i++) {
                JSONObject entry = listCategoryEntries.getJSONObject(i);

                /** Parser account information */
                CategoryInfo info = new CategoryInfo();
                info.setIcon(getStringValue(entry, "image"));
                info.setName(getStringValue(entry, "name"));
                info.setId(getStringValue(entry, "id"));

                listCategories.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listCategories;
    }

    /**
     * Parse list stream
     */
    public static int parserListStreams(String json,
            ArrayList<StreamInfo> listStreams) {
        // 0: lay duoc listStream; 1: can dang nhap lai; 2: ko lay duoc
        // listStream
        listStreams.clear();
        try {
            // JSONObject response = new JSONObject(json);
            JSONObject response = new JSONObject(json);
            if (response.getInt("status") == 1) {
                JSONArray listStreamsEntries = response.getJSONArray("data");
                for (int i = 0; i < listStreamsEntries.length(); i++) {
                    JSONObject entry = listStreamsEntries.getJSONObject(i);

                    StreamInfo item = new StreamInfo();
                    // item.setId(getStringValue(entry, "rank_id"));
                    item.setUrl(getStringValue(entry, "url"));
                    listStreams.add(item);
                }
                return 0;
            } else {
                // result.setGetSucc(false);
                // update to fix @Thinhdt -> message
                // String isLogin = getStringValue(response, "is_login");
                // if (isLogin.length() > 0) {
                // if (!(isLogin.equalsIgnoreCase("1"))) {
                // return 1;
                // }
                // }

                return 1;
            }

            // boolean tmp = getBooleanValue(response, "status");
            // if (tmp) {
            // JSONArray listStreamsEntries = response.getJSONArray("data");
            // for (int i = 0; i < listStreamsEntries.length(); i++) {
            // JSONObject entry = listStreamsEntries.getJSONObject(i);
            //
            // StreamInfo item = new StreamInfo();
            // item.setId(getStringValue(entry, "rank_id"));
            // item.setUrl(getStringValue(entry, "url"));
            // listStreams.add(item);
            // }
            // return 0;
            // } else {
            // String isLogin = getStringValue(response, "is_login");
            // if (isLogin.length() > 0) {
            // if (!(isLogin.equalsIgnoreCase("1"))) {
            // return 1;
            // }
            // }
            // }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 2;
    }

    /**
	 */
    public static String isLogin(String json) {
        try {
            String login = "";
            JSONObject response = new JSONObject(json);
            login = getStringValue(response, "is_login");
            return login;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean parserCheckVersion(String json, VersionVtv pVersion) {
        boolean isCheck = false;
        try {
            JSONObject jsonObject = new JSONObject(json);
            isCheck = getBooleanValue(jsonObject, "status");
            if (isCheck) {
                pVersion.setAppUrl(getStringValue(jsonObject, "appurl"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isCheck;
    }

    public static void parserGetInfoApp(String json, VersionVtv pVersion) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            pVersion.setStatus(getBooleanValue(jsonObject, "status"));
            pVersion.setAppUrl(getStringValue(jsonObject, "appurl"));
            pVersion.setAppProUrl(getStringValue(jsonObject, "app_pro_url"));
            pVersion.setLogoApp(getStringValue(jsonObject, "logo_app"));
            pVersion.setPurchase(getStringValue(jsonObject, "purchase"));
            pVersion.setPolicy(getStringValue(jsonObject, "description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * "" Parse list channel
     */
    public static ArrayList<ItemVtvPlusInfo> parserListItemOfCategory(
            String json) {
        ArrayList<ItemVtvPlusInfo> listChannel = new ArrayList<ItemVtvPlusInfo>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray listChannelEntries = jsonObject.getJSONArray("data");
            for (int i = 0; i < listChannelEntries.length(); i++) {
                JSONObject entry = listChannelEntries.getJSONObject(i);

                ItemVtvPlusInfo info = parserItemVtvPlusInfo(entry);

                listChannel.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listChannel;
    }

    public static ArrayList<ItemVtvPlusInfo> parserListEposide(String json) {
        ArrayList<ItemVtvPlusInfo> listEposide = new ArrayList<ItemVtvPlusInfo>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray listChannelEntries = jsonObject.getJSONArray("data");
            for (int i = 0; i < listChannelEntries.length(); i++) {
                JSONObject entry = listChannelEntries.getJSONObject(i);

                EpisodeInfo info = parserItemEpisodeInfo(entry);
                listEposide.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listEposide;
    }// /cuong

    /**
     * Parse list channel relation
     */
    public static ArrayList<ItemVtvPlusInfo> parserListChannelRelation(
            String json) {
        ArrayList<ItemVtvPlusInfo> listChannel = new ArrayList<ItemVtvPlusInfo>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            // JSONArray channelEntries = jsonObject.getJSONArray("data");
            JSONObject entry = jsonObject.getJSONObject("data");

            JSONArray listChannelRelationEntries = entry
                    .getJSONArray("data_concern");
            for (int i = 0; i < listChannelRelationEntries.length(); i++) {
                JSONObject jObject = listChannelRelationEntries
                        .getJSONObject(i);
                /** Parser account information */
                ChannelInfo info = parserItemChannel(jObject);
                listChannel.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listChannel;
    }

    public static ArrayList<ItemVtvPlusInfo> parserListVodRelation(String json) {
        ArrayList<ItemVtvPlusInfo> listVod = new ArrayList<ItemVtvPlusInfo>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            // JSONArray vodEntries = jsonObject.getJSONArray("data");
            JSONObject entry = jsonObject.getJSONObject("data");
            // JSONObject entry = vodEntries.getJSONObject(0);
            JSONArray listVodRelationEntries = entry
                    .getJSONArray("data_concern");
            for (int i = 0; i < listVodRelationEntries.length(); i++) {
                JSONObject jObject = listVodRelationEntries.getJSONObject(i);
                /** Parser account information */
                VodInfo info = parserItemVodInfo(jObject);
                listVod.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listVod;
    }

    public static ArrayList<ItemVtvPlusInfo> parserListEposideRelation(
            String json) {
        ArrayList<ItemVtvPlusInfo> listEposide = new ArrayList<ItemVtvPlusInfo>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            // JSONArray episodeEntries = jsonObject.getJSONArray("data");
            // JSONObject entry = episodeEntries.getJSONObject(0);
            JSONObject entry = jsonObject.getJSONObject("data");
            JSONArray listEpisodeRelationEntries = entry
                    .getJSONArray("data_concern");
            for (int i = 0; i < listEpisodeRelationEntries.length(); i++) {
                JSONObject jObject = listEpisodeRelationEntries
                        .getJSONObject(i);

                /** Parser account information */
                EpisodeInfo info = parserItemEpisodeInfo(jObject);
                listEposide.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listEposide;
    }

    public static ArrayList<ItemVtvPlusInfo> parserListRelation(String json,
            int type) {
        if (type == 0) {
            return parserListChannelRelation(json);
        } else if (type == 1) {
            return parserListVodRelation(json);
        } else {
            return parserListEposideRelation(json);
        }
    }

    public static void parserListHome(String json,
            ArrayList<BannerInfo> listBanners,
            ArrayList<HomeObject> listHomeObjects,
            ArrayList<HomeObject> listHomeLand, Context context) {
        try {
            JSONObject jsonTotal = new JSONObject(json);
            JSONObject jsonObject = jsonTotal.getJSONObject("data");

            parserListBanner(jsonObject, listBanners);
            // parserListFavourist(jsonObject, listHomeObjects, listHomeLand,
            // context);
            // parserListNew(jsonObject, listHomeObjects, listHomeLand,
            // context);
            // parserListHosttest(jsonObject, listHomeObjects, listHomeLand,
            // context);

            parserListChannel(jsonObject, listHomeObjects, listHomeLand,
                    context);

            parserListCategoryAndItem(jsonObject, listHomeObjects, listHomeLand);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parserListBanner(JSONObject pEntry,
            ArrayList<BannerInfo> arrBanner) {
        try {
            JSONArray listBanner = pEntry.getJSONArray("banner");
            for (int i = 0; i < listBanner.length(); i++) {
                JSONObject entry = listBanner.getJSONObject(i);

                BannerInfo info = new BannerInfo();
                info.setId(getStringValue(entry, "id"));
                info.setImgLink(getStringValue(entry, "image_url"));
                info.setLinkBanner(getStringValue(entry, "link_banner"));
                arrBanner.add(info);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void parserListHosttest(JSONObject pEntry,
            ArrayList<HomeObject> arrHomeObject,
            ArrayList<HomeObject> arrHomeLand, Context context) {
        try {
            JSONArray listItem = pEntry.getJSONArray("listHot");
            if (listItem == null || listItem.length() == 0) {
                return;
            }
            HomeCategory cate = new HomeCategory();
            cate.setType(-3);
            cate.setId("");
            cate.setNameCategory(context.getString(R.string.categories_hottest));
            arrHomeObject.add(cate);
            if (arrHomeLand != null) {
                arrHomeLand.add(cate);
            }
            HomeFragment.COUNT_CATEGORY++;

            insert2ArrayListHome(listItem, arrHomeObject, arrHomeLand, false);
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void parserListFavourist(JSONObject pEntry,
            ArrayList<HomeObject> arrHomeObject,
            ArrayList<HomeObject> arrHomeLand, Context context) {
        try {
            JSONArray listItem = pEntry.getJSONArray("listfavorite");
            if (listItem == null || listItem.length() == 0) {
                return;
            }
            HomeCategory cate = new HomeCategory();
            cate.setType(-2);
            cate.setId("");
            cate.setNameCategory(context
                    .getString(R.string.categories_favorites));
            arrHomeObject.add(cate);
            if (arrHomeLand != null) {
                arrHomeLand.add(cate);
            }
            HomeFragment.COUNT_CATEGORY++;
            insert2ArrayListHome(listItem, arrHomeObject, arrHomeLand, true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void parserListChannel(JSONObject pEntry,
            ArrayList<HomeObject> arrHomeObject,
            ArrayList<HomeObject> arrHomeLand, Context context) {
        try {
            JSONArray listItem = pEntry.getJSONArray("channel");
            if (listItem == null || listItem.length() == 0) {
                return;
            }
            HomeCategory cate = new HomeCategory();
            cate.setType(0);
            cate.setId("");
            cate.setNameCategory(context
                    .getString(R.string.categories_channel));
            arrHomeObject.add(cate);
            if (arrHomeLand != null) {
                arrHomeLand.add(cate);
            }
            HomeFragment.COUNT_CATEGORY++;
            insert2ArrayListHome(listItem, arrHomeObject, arrHomeLand, true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void parserListNew(JSONObject pEntry,
            ArrayList<HomeObject> arrHomeObject,
            ArrayList<HomeObject> arrHomeLand, Context context) {
        try {
            JSONArray listItem = pEntry.getJSONArray("listNew");
            if (listItem == null || listItem.length() == 0) {
                return;
            }
            HomeCategory cate = new HomeCategory();
            cate.setType(-1);
            cate.setId("");
            cate.setNameCategory(context.getString(R.string.categories_newest));
            arrHomeObject.add(cate);
            if (arrHomeLand != null) {
                arrHomeLand.add(cate);
            }
            HomeFragment.COUNT_CATEGORY++;
            insert2ArrayListHome(listItem, arrHomeObject, arrHomeLand, false);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * list all vod
     */
    public static void parserListCategoryAndItem(JSONObject pEntry,
            ArrayList<HomeObject> arrHomeObject,
            ArrayList<HomeObject> arrHomeLand) {
        try {
            JSONArray listCate = pEntry.getJSONArray("listCategory");
            if (listCate == null || listCate.length() == 0) {
                return;
            }

            for (int i = 0; i < listCate.length(); i++) {
                JSONObject entry = listCate.getJSONObject(i);
                HomeCategory itemCate = new HomeCategory();
                itemCate.setType(1);
                itemCate.setId(getStringValue(entry, "id"));
                itemCate.setNameCategory(getStringValue(entry, "name"));
                JSONArray entryData = entry.getJSONArray("dataCategory");
                if (entryData == null || entryData.length() == 0) {
                    continue;
                }
                HomeFragment.COUNT_CATEGORY++;
                arrHomeObject.add(itemCate);
                if (arrHomeLand != null) {
                    arrHomeLand.add(itemCate);
                }

                insert2ArrayListHome(entryData, arrHomeObject, arrHomeLand,
                        false);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void insert2ArrayListHome(JSONArray listItem,
            ArrayList<HomeObject> arrHomeObject,
            ArrayList<HomeObject> arrHomeLand, boolean isFavourist) {
        int count = 0;
        HomeItemListview pItemLv = null;
        HomeItemLandListview pItemLvLand = null;
        for (int i = 0; i < listItem.length(); i++) {
            JSONObject entry = null;
            try {
                entry = listItem.getJSONObject(i);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (entry == null) {
                continue;
            }
            ItemVtvPlusInfo info = parserItemVtvPlusInfo(entry);
            if (isFavourist) {
                info.setFavorites(true);
            }

            if (count == 0) {
                pItemLv = new HomeItemListview();
                pItemLv.setItemLeft(info);
                if (arrHomeLand != null) {
                    pItemLvLand = new HomeItemLandListview();
                    pItemLvLand.setItemFirst(info);
                }
                count = 1;
            } else if (count == 1) {
                pItemLv.setItemRight(info);
                arrHomeObject.add(pItemLv);
                if (pItemLvLand != null) {
                    pItemLvLand.setItemSecond(info);
                }
                count = 2;
            } else if (count == 2) {
                pItemLv = new HomeItemListview();
                pItemLv.setItemLeft(info);
                if (pItemLvLand != null) {
                    pItemLvLand.setItemThird(info);
                }
                count = 3;
            } else if (count == 3) {
                pItemLv.setItemRight(info);
                arrHomeObject.add(pItemLv);
                if (arrHomeLand != null) {
                    pItemLvLand.setItemFourth(info);
                    arrHomeLand.add(pItemLvLand);
                }
                count = 0;
            }
        }
        if (count > 0) {
            arrHomeObject.add(pItemLv);
            if (arrHomeLand != null) {
                arrHomeLand.add(pItemLvLand);
            }
        }
    }

    public static ItemVtvPlusInfo parserItemVtvPlusInfo(JSONObject jsonOb) {
        ItemVtvPlusInfo info = null;

        String type = getStringValue(jsonOb, "type");
        if ("channel".equalsIgnoreCase(type)) {
            info = parserItemChannel(jsonOb);
        } else if ("vod".equalsIgnoreCase(type)) {
            info = parserItemVodInfo(jsonOb);
        } else if ("ads".equalsIgnoreCase(type)) {
            info = parserItemAds(jsonOb);
        } else {
            info = parserItemEpisodeInfo(jsonOb);
        }

        return info;
    }

    public static ItemVtvPlusInfo parserItemVtvPlusDetailsInfo(String response,
            int type) {
        ItemVtvPlusInfo info = null;
        try {
            JSONObject jsonOb = new JSONObject(response);
            JSONArray jArray = jsonOb.getJSONArray("data");
            JSONObject entry = jArray.getJSONObject(0);

            if (type == 0) {
                info = parserItemChannel(entry);
            } else if (type == 1) {
                info = parserItemVodInfo(entry);
            } else if (type == 2) {
                info = parserItemEpisodeInfo(entry);
            } else {
                info = parserItemAds(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    public static AdsInfo parserItemAds(JSONObject entryChannel) {
        AdsInfo info = new AdsInfo();

        info.setUrl(getStringValue(entryChannel, "url"));
        info.setId(getStringValue(entryChannel, "id"));
        info.setName(getStringValue(entryChannel, "name"));
        info.setImage(getStringValue(entryChannel, "image"));
        info.setPoster(getStringValue(entryChannel, "poster"));
        info.setLink(getStringValue(entryChannel, "link"));
        String typeAds = getStringValue(entryChannel, "type_ads");
        if (typeAds != null) {
            if (typeAds.equalsIgnoreCase("advertise")) {
                info.setTypeAds(1);
            } else if (typeAds.equalsIgnoreCase("banner")) {
                info.setTypeAds(0);
            } else if (typeAds.equalsIgnoreCase("video")) {
                info.setTypeAds(2);
            }
        }

        return info;
    }

    public static ChannelInfo parserItemChannel(JSONObject entryChannel) {
        ChannelInfo info = new ChannelInfo();

        info.setId(getStringValue(entryChannel, "id"));
        info.setName(getStringValue(entryChannel, "name"));
        info.setScreenUrl(getStringValue(entryChannel, "screen_url"));
        info.setDes(getStringValue(entryChannel, "short_description"));
        info.setIconSmall(getStringValue(entryChannel, "image"));
        info.setIcon(getStringValue(entryChannel, "capture_image"));
        String free = getStringValue(entryChannel, "is_free");
        if (free.equalsIgnoreCase("1")) {
            info.setFree(true);
        } else {
            info.setFree(false);
        }

        free = getStringValue(entryChannel, "is_audio");
        if (free.equalsIgnoreCase("0")) {
            info.setAudio(false);
        } else {
            info.setAudio(true);
        }

        info.setView(getStringValue(entryChannel, "view"));
        info.setCountLike(getStringValue(entryChannel, "like"));
        info.setStreamId(getStringValue(entryChannel, "stream_id"));
        info.setFavorites(getBooleanValue(entryChannel, "is_favorite"));
        info.setStreamUrl(getStringValue(entryChannel, "stream_url"));
        info.setLike(getBooleanValue(entryChannel, "is_like"));
        return info;
    }

    public static VodInfo parserItemVodInfo(JSONObject entryData) {
        VodInfo info = new VodInfo();

        info.setId(getStringValue(entryData, "id"));
        info.setName(getStringValue(entryData, "name"));
        info.setDes(getStringValue(entryData, "short_description"));
        info.setDuration(getStringValue(entryData, "duration"));
        info.setImage(getStringValue(entryData, "image"));
        String tmp = getStringValue(entryData, "is_serie");
        if ("1".equalsIgnoreCase(tmp)) {
            info.setSeries(true);
        } else {
            info.setSeries(false);
        }
        info.setEpisodeCount(getStringValue(entryData, "episode_count"));
        tmp = getStringValue(entryData, "is_free");
        if ("1".equalsIgnoreCase(tmp)) {
            info.setFree(true);
        } else {
            info.setFree(false);
        }
        info.setTag(getStringValue(entryData, "tags"));
        info.setPrice(getStringValue(entryData, "price"));
        info.setLanpImage(getStringValue(entryData, "land_image"));
        info.setPortImage(getStringValue(entryData, "port_image"));
        info.setView(getStringValue(entryData, "view"));
        info.setCountLike(getStringValue(entryData, "like"));
        info.setStreamId(getStringValue(entryData, "stream_id"));
        info.setFavorites(getBooleanValue(entryData, "is_favorite"));
        info.setStreamUrl(getStringValue(entryData, "stream_url"));
        info.setLike(getBooleanValue(entryData, "is_like"));

        return info;
    }

    public static EpisodeInfo parserItemEpisodeInfo(JSONObject entryData) {
        EpisodeInfo info = new EpisodeInfo();

        info.setId(getStringValue(entryData, "id"));
        info.setName(getStringValue(entryData, "name"));
        info.setDuration(getStringValue(entryData, "duration"));
        info.setImage(getStringValue(entryData, "image_url"));
        info.setEposideNumber(getStringValue(entryData, "episode_number"));
        info.setView(getStringValue(entryData, "view"));
        info.setCountLike(getStringValue(entryData, "like"));
        info.setVodId(getStringValue(entryData, "vod_id"));
        info.setStreamId(getStringValue(entryData, "stream_id"));
        info.setStreamUrl(getStringValue(entryData, "stream_url"));
        info.setFavorites(getBooleanValue(entryData, "is_favorite"));
        info.setLike(getBooleanValue(entryData, "is_like"));

        return info;
    }

    public static UserInfo parserLoginInfo(String response) {
        // LoginInfo result = new LoginInfo();
        UserInfo result = new UserInfo();
        //
        // try {
        // JSONObject entry = new JSONObject(response);
        // result.setSucess(getBooleanValue(entry, "status"));
        // if (result.isSuccess()) {
        // result.setMessage(getStringValue(entry, "session"));
        // } else {
        // result.setMessage(getStringValue(entry, "error"));
        // }
        // } catch (Exception e) {
        // // TODO: handle exception
        // }

        try {
            // check the status
            JSONObject entry = new JSONObject(response);
            if (entry.getInt("status") == 1) {
                result.setGetSucc(true);
            } else {
                result.setGetSucc(false);
            }
            // boolean tmp = getBooleanValue(entry, "status");

            if (result.isGetSucc()) {
                String strReg = getStringValue(entry, "register");
                if (strReg.equalsIgnoreCase("1")) {
                    result.setRegister(true);
                    result.setSession(getStringValue(entry, "session"));
                    // result.setCurrentService(getStringValue(entry,
                    // "current_service"));
                    result.setExpiryDate(getStringValue(entry, "expiry_date"));
                    // JSONArray arr = getJSONArray(entry, "live_channel");
                    // StringBuilder tmp = new StringBuilder(",");
                    // for (int i = 0; i < arr.length(); i++) {
                    // JSONObject job = arr.getJSONObject(i);
                    // tmp.append(getStringValue(job, "id")).append(",");
                    // }
                    // result.setLiveChannel(tmp.toString());
                } else {
                    result.setRegister(false);
                }
            } else {
                // result.setLiveChannel(getStringValue(entry, "error"));
                result.setMessage(getStringValue(entry, "message"));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

    public static UserInfo parserUserInfo(String response) {
        UserInfo result = new UserInfo();
        try {
            JSONObject entry = new JSONObject(response);
            result.setGetSucc(getBooleanValue(entry, "status"));
            if (result.isGetSucc()) {
                String strReg = getStringValue(entry, "registered");
                if (strReg.equalsIgnoreCase("1")) {
                    result.setRegister(true);
                    result.setCurrentService(getStringValue(entry,
                            "current_service"));
                    result.setExpiryDate(getStringValue(entry, "expiry_date"));
                    JSONArray arr = getJSONArray(entry, "live_channel");
                    StringBuilder tmp = new StringBuilder(",");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject job = arr.getJSONObject(i);
                        tmp.append(getStringValue(job, "id")).append(",");
                    }
                    result.setLiveChannel(tmp.toString());
                } else {
                    result.setRegister(false);
                }
            } else {
                result.setLiveChannel(getStringValue(entry, "error"));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public static RegisterInfo parserRegisterInfo(String response) {
        RegisterInfo result = new RegisterInfo();
        try {
            JSONObject entry = new JSONObject(response);
            result.setSuccess(getBooleanValue(entry, "status"));
            if (result.isSuccess()) {
                result.setMessage(getStringValue(entry, "message"));
            } else {
                result.setMessage(getStringValue(entry, "error"));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public static ArrayList<ScheduleInfo> parserScheduleInfo(String response) {
        ArrayList<ScheduleInfo> arr = new ArrayList<ScheduleInfo>();
        try {
            JSONObject entry = new JSONObject(response);
            JSONArray arrJson = getJSONArray(entry, "data");
            for (int i = 0; i < arrJson.length(); i++) {
                JSONObject pEntry = arrJson.getJSONObject(i);
                ScheduleInfo info = new ScheduleInfo();
                info.setId(getStringValue(pEntry, "id"));
                info.setTitle(getStringValue(pEntry, "category"));
                info.setTime(getStringValue(pEntry, "time"));
                info.setContent(getStringValue(pEntry, "name"));
                info.setChannelId(getStringValue(pEntry, "channel_id"));
                arr.add(info);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return arr;
    }

    public static String parserSupport(String response) {
        try {
            JSONObject entry = new JSONObject(response);
            String tmp = getStringValue(entry, "data");
            return tmp;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    public static boolean parserLike(String response) {
        boolean result = false;
        try {
            JSONObject entry = new JSONObject(response);
            result = getBooleanValue(entry, "status");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public static int parserFavourist(String response) {
        int result = 0; // 0_loi; 1_favorist; 2_un_favourist
        try {
            JSONObject entry = new JSONObject(response);
            boolean status = getBooleanValue(entry, "status");
            if (status) {
                String fv = getStringValue(entry, "favorite");
                if (fv.equalsIgnoreCase("unfavorite")) {
                    result = 2;
                } else if (fv.equalsIgnoreCase("favorite")) {
                    result = 1;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    public static ItemInteraction parserInteraction(String response) {
        boolean result = false;
        ItemInteraction info = null;
        try {
            JSONObject entry = new JSONObject(response);
            result = getBooleanValue(entry, "status");
            if (result) {
                JSONArray arrJson = getJSONArray(entry, "data");
                for (int i = 0; i < arrJson.length(); i++) {
                    JSONObject obJson = arrJson.getJSONObject(i);
                    info = parserItemInteraction(obJson);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return info;
    }

    public static ArrayList<ItemInteraction> parserAllInteraction(
            String response) {
        ArrayList<ItemInteraction> array = null;
        try {
            JSONObject entry = new JSONObject(response);
            boolean result = getBooleanValue(entry, "status");
            if (result) {
                JSONArray arrJson = getJSONArray(entry, "data");
                if (arrJson != null && arrJson.length() > 0) {
                    array = new ArrayList<ItemInteraction>();
                }
                for (int i = 0; i < arrJson.length(); i++) {
                    ItemInteraction info = null;
                    JSONObject obJson = arrJson.getJSONObject(i);
                    info = parserItemInteraction(obJson);
                    array.add(info);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return array;
    }

    public static ItemInteraction parserItemInteraction(JSONObject entry) {
        ItemInteraction info = new ItemInteraction();
        info.setThumb(getStringValue(entry, "thumb"));
        info.setUrl(getStringValue(entry, "url"));
        String type = getStringValue(entry, "type");
        if (type.equalsIgnoreCase("url")) {
            info.setType(0);
        } else if (type.equalsIgnoreCase("image")) {
            info.setType(1);
        } else if (type.equalsIgnoreCase("video")) {
            info.setType(2);
        } else if (type.equalsIgnoreCase("audio")) {
            info.setType(3);
        }
        return info;
    }

    public static String parserIpAddress(String response) {
        String result = "";
        try {
            JSONObject entry = new JSONObject(response);
            result = getStringValue(entry, "ip");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    /* ========================= CORE FUNCTIONS =========================== */

    /**
     * Get string value
     * 
     * @param obj
     * @param key
     * @return
     */
    public static String getStringValue(JSONObject obj, String key) {
        try {
            return obj.isNull(key) ? "" : obj.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    /**
     * Get long value
     * 
     * @param obj
     * @param key
     * @return
     */
    public static long getLongValue(JSONObject obj, String key) {
        try {
            return obj.isNull(key) ? 0L : obj.getLong(key);
        } catch (JSONException e) {
            return 0L;
        }
    }

    /**
     * Get int value
     * 
     * @param obj
     * @param key
     * @return
     */
    public static int getIntValue(JSONObject obj, String key) {
        try {
            return obj.isNull(key) ? 0 : obj.getInt(key);
        } catch (JSONException e) {
            return 0;
        }
    }

    public static boolean hasKey(JSONObject obj, String key) {
        return obj.isNull(key) ? false : obj.has(key);
    }

    /**
     * Get Double value
     * 
     * @param obj
     * @param key
     * @return
     */
    public static Double getDoubleValue(JSONObject obj, String key) {
        double d = 0.0;
        try {
            return obj.isNull(key) ? d : obj.getDouble(key);
        } catch (JSONException e) {
            return d;
        }
    }

    /**
     * Get boolean value
     * 
     * @param obj
     * @param key
     * @return
     */
    public static boolean getBooleanValue(JSONObject obj, String key) {
        try {
            return obj.isNull(key) ? false : obj.getBoolean(key);
        } catch (JSONException e) {
            Log.e("JSONException: " + e.toString());
            return false;
        }
    }

    /**
     * Get Json object in Json object
     * 
     * @param obj
     * @param key
     * @return
     */
    public static JSONObject getJSONObject(JSONObject obj, String key) {
        try {
            return obj.getJSONObject(key);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Get Json array in Json object
     * 
     * @param obj
     * @param key
     * @return
     */
    public static JSONArray getJSONArray(JSONObject obj, String key) {
        try {
            return obj.getJSONArray(key);
        } catch (JSONException e) {
            return null;
        }
    }

    public static void parserListMatchScheduleMenu(String json,
            List<MatchScheduleMenuObject> menuObjects) {
        try {
            JSONObject jsonTotal = new JSONObject(json);
            JSONArray jsonObject = jsonTotal.getJSONArray("data");

            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject entry = jsonObject.getJSONObject(i);

                MatchScheduleMenuObject matchScheduleMenuObject = new MatchScheduleMenuObject();
                matchScheduleMenuObject.setId(getLongValue(entry, "id"));
                matchScheduleMenuObject.setGuid(getLongValue(entry, "guid"));
                matchScheduleMenuObject.setName(getStringValue(entry, "name"));
                matchScheduleMenuObject.setCode_name(getStringValue(entry, "code_name"));

                matchScheduleMenuObject.setImage_url_large(getStringValue(entry, "image_url_large"));
                matchScheduleMenuObject.setImage_url_medium(getStringValue(entry, "image_url_medium"));
                matchScheduleMenuObject.setImage_url_small(getStringValue(entry, "image_url_small"));

                menuObjects.add(matchScheduleMenuObject);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parserListMatchScheduleData(String json, List<MatchScheduleModel> mListOfOngoing, List<MatchScheduleModel> mListOfResult) {
        try {
            JSONObject jsonTotal = new JSONObject(json);
            JSONObject jsonObject = jsonTotal.getJSONObject("data");

            JSONArray scheduleList = jsonObject.getJSONArray("lichthidau");
            JSONArray resultList = jsonObject.getJSONArray("ketqua");

            for (int i = 0; i < scheduleList.length(); i++) {
                JSONObject entry = scheduleList.getJSONObject(i);

                mListOfOngoing.add(parseJSONToObject(entry));
            }

            for (int i = 0; i < resultList.length(); i++) {
                JSONObject entry = resultList.getJSONObject(i);

                mListOfResult.add(parseJSONToObject(entry));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MatchScheduleModel parseJSONToObject(JSONObject entry) throws Exception{
        MatchScheduleModel matchScheduleModel = new MatchScheduleModel();
        matchScheduleModel.setID(getLongValue(entry, "id"));
        matchScheduleModel.setGUID(getLongValue(entry, "guid"));
        matchScheduleModel.setStat_sport_id(getLongValue(entry, "stat_sport_id"));
        matchScheduleModel.setStat_competition_id(getLongValue(entry, "stat_competition_id"));

        JSONObject homeEntry = entry.getJSONObject("home_team");
        TeamObject homeTeam = new TeamObject();
        homeTeam.setID(getLongValue(homeEntry, "id"));
        homeTeam.setGUID(getLongValue(homeEntry, "guid"));
        homeTeam.setName(getStringValue(homeEntry, "name"));
        homeTeam.setCode_Name(getStringValue(homeEntry, "code_name"));
        homeTeam.setImage_url_large(getStringValue(homeEntry, "image_url_large"));
        homeTeam.setImage_url_medium(getStringValue(homeEntry, "image_url_medium"));
        homeTeam.setImage_url_small(getStringValue(homeEntry, "image_url_small"));
        matchScheduleModel.setHomeTeam(homeTeam);

        JSONObject awayEntry = entry.getJSONObject("away_team");
        TeamObject awayTeam = new TeamObject();
        awayTeam.setID(getLongValue(awayEntry, "id"));
        awayTeam.setGUID(getLongValue(awayEntry, "guid"));
        awayTeam.setName(getStringValue(awayEntry, "name"));
        awayTeam.setCode_Name(getStringValue(awayEntry, "code_name"));
        awayTeam.setImage_url_large(getStringValue(awayEntry, "image_url_large"));
        awayTeam.setImage_url_medium(getStringValue(awayEntry, "image_url_medium"));
        awayTeam.setImage_url_small(getStringValue(awayEntry, "image_url_small"));
        matchScheduleModel.setAwayTeam(awayTeam);

        matchScheduleModel.setSeason(getLongValue(entry, "season"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(getStringValue(entry, "date"));
        matchScheduleModel.setDate(date);

        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        TimeZone utc = TimeZone.getTimeZone("UTC");
        sdf.setTimeZone(utc);
        date = sdf.parse(getStringValue(entry, "datetime_of_play"));
        matchScheduleModel.setDatetime_of_play(date);

        matchScheduleModel.setTimeZone(getStringValue(entry, "time_zone"));
        matchScheduleModel.setAvailability(getStringValue(entry, "availability"));
        matchScheduleModel.setAdditional_info(getStringValue(entry, "additional_info"));
        matchScheduleModel.setNeutral_or_not(getStringValue(entry, "neutral_or_not"));

        matchScheduleModel.setScores_and_stats(getScoreAndStat(entry));
        matchScheduleModel.setAttendance(getStringValue(entry, "attendance"));
        matchScheduleModel.setStatus(getStringValue(entry, "status"));
        matchScheduleModel.setHome_season_match_day(getLongValue(entry, "home_season_match_day"));
        matchScheduleModel.setAway_season_match_day(getLongValue(entry, "away_season_match_day"));

        return matchScheduleModel;
    }

    private static String getScoreAndStat(JSONObject entry){
        try {
            String returnValue;
            JSONObject jsonObject = entry.getJSONObject("scores_and_stats");
            JSONObject jsonObject1 = jsonObject.getJSONObject("scores");
            int homeTeamStat = getIntValue(jsonObject1, "home");
            int awayTeamStat = getIntValue(jsonObject1, "away");

            returnValue = homeTeamStat + " - " + awayTeamStat;
            return returnValue;
        } catch (Exception ex){
            return "";
        }
    }

}
