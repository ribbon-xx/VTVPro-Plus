/*
 * Name: $RCSfile: NetworkUtility.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 3:57:18 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvsport.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * NetworkUtility supports to check available network
 * 
 * @author Hai Le
 */
public class NetworkUtility
{
    /**
     * Check network connection
     * 
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
        {
            return false;
        }
        if (!i.isConnected())
        {
            return false;
        }
        if (!i.isAvailable())
        {
            return false;
        }
        return true;
    }
    
    public static String getTypeNetwork(Context context) {
    	ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	String name = conMgr.getActiveNetworkInfo().getTypeName();
    	return name;
    }
    
    public static boolean isWifi(Context context) {
    	String strNetwork = getTypeNetwork(context);
    	boolean result = false;
    	if (strNetwork.equalsIgnoreCase("WIFI")) {
			result = true;
		}
    	return result;
    }
    
    public static String getIpAddress() {
    	try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("Get Address", ex.toString());
        }
        return "";
    }
    
}