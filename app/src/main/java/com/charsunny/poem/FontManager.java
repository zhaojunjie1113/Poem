package com.charsunny.poem;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by zjj on 15/11/23.
 */
public class FontManager {

    private static FontManager manager = null;
    private Context ctx;
    private Typeface type;
    static HashMap<String,String> fontsMap = new HashMap<String,String>();

    public static FontManager sharedInstance(Context ctx) {
        if(manager == null) {
            manager = new FontManager(ctx);
        }
        return manager;
    }
    protected FontManager(Context ctx) {
        fontsMap.put("汉仪全唐诗(简)","HYQuanTangShiJ.ttf");
        fontsMap.put("汉仪全唐诗(繁)","HYQuanTangShiF.ttf");
        fontsMap.put("方正宋刻本秀楷(简)", "FZ.ttf");
        fontsMap.put("方正宋刻本秀楷(繁)","fsongti.ttf");
        fontsMap.put("方正北魏楷书(简)","FZBWJ.ttf");
        fontsMap.put("方正北魏楷书(繁)", "FZBWF.ttf");
        this.ctx = ctx;


    }

    public Typeface getFont() {
        if (type != null) {
            return type;
        }
        SharedPreferences preferences = ctx.getSharedPreferences("setting", 0);
        String key = preferences.getString("font", "汉仪全唐诗(简)");
        String value = fontsMap.get(key);
        type = Typeface.createFromAsset(ctx.getAssets(), value);
        return type;
    }

    public void applyFont(TextView... views) {
        Typeface tf = this.getFont();
        for (TextView view : views) {
            view.setTypeface(tf);
        }
    }
}
