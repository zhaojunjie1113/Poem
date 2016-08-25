package com.charsunny.poem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

/**
 * Created by zjj on 16/4/5.
 */
public class WeiboActivity extends Activity {
    public static final String WEIBO_KEY = "1952112499";
    public static final String REDIRECT_URL = "http://www.sina.com";
    public Oauth2AccessToken mAccessToken;
    protected Bitmap mSharedPic;
    protected SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}