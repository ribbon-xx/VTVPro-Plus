/*
 * Name: $RCSfile: IApiCallback.java,v $
 * Version: $Revision: 1.0 $
 * Date: $Date: Nov 3, 2012 1:17:05 PM $
 *
 * Copyright (C) 2012 COMPANY_NAME, Inc. All rights reserved.
 */

package mdn.vtvpluspro.network;

/**
 * IApiCallback
 *
 * @author Quan
 */
public interface IApiCallback
{
    public void responseSuccess(String response);
    
    public void responseFailWithCode(int statusCode);
}