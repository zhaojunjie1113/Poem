package com.charsunny.poem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import net.sourceforge.pinyin4j.*;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.*;
import com.victor.loading.book.BookLoading;

/**
 * Created by junjiezhao on 15/11/22.
 */
public class RecomandFragment extends Fragment {

    private ListView listView;
    private View headerView;
    private PoemContentAdapter contentAdapter;
    private BookLoading bookLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        // Set the adapter
        listView = (ListView)view.findViewById(R.id.listview);
        bookLoading= (BookLoading) view.findViewById(R.id.bookloading);
        bookLoading.start();
        headerView = inflater.inflate(R.layout.recommand_header,listView,false);
        listView.addHeaderView(headerView);
        getServerData();
        return view;
    }

    private void getServerData() {
        Ion.with(this).load("http://charsunny.ansinlee.com/rec?type=json").asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                String title = result.get("Title").getAsString();
                String desc = result.get("Desc").getAsString();
                String time = result.get("Time").getAsString();
                JsonArray poems = result.get("Poems").getAsJsonArray();
                TextView timeView = (TextView) headerView.findViewById(R.id.time);
                TextView titleView = (TextView) headerView.findViewById(R.id.title);
                TextView descView = (TextView) headerView.findViewById(R.id.desc);
                timeView.setText(time);
                titleView.setText(title);
                descView.setText(desc);
                FontManager.sharedInstance(null).applyFont(timeView, titleView, descView);
                bookLoading.stop();
                bookLoading.setVisibility(View.GONE);
                contentAdapter = new PoemContentAdapter(poems, RecomandFragment.this.getActivity());
                listView.setAdapter(contentAdapter);
            }
        });
    }

    public class PoemContentAdapter extends BaseAdapter {

        private final JsonArray mValues;
        private final Activity mActivity;
        private final Hanyu hanyu;

        public PoemContentAdapter(JsonArray items, Activity activity) {
            mValues = items;
            hanyu = new Hanyu();
            mActivity = activity;
        }
        @Override
        public int getCount() {
            return mValues.size();
        }

        @Override
        public Object getItem(int position) {
            return mValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public  int[] getItemIds() {
            int[] list = new int[mValues.size()];
            for (int i = 0; i < mValues.size(); i ++) {
                JsonObject poem =  mValues.get(i).getAsJsonObject();
                list[i] = poem.get("Id").getAsInt();
            }
            return list;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_column, parent, false);
            }
            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            TextView authorView = (TextView)view.findViewById(R.id.author);
            TextView titleView = (TextView)view.findViewById(R.id.title);
            TextView descView = (TextView)view.findViewById(R.id.desc);
            final JsonObject poem =  mValues.get(position).getAsJsonObject();
            FontManager.sharedInstance(null).applyFont(authorView, titleView, descView);
            if (poem instanceof  JsonObject) {
                String poet = poem.get("Poet").getAsJsonObject().get("Name").getAsString();
                final int pid = poem.get("Poet").getAsJsonObject().get("Id").getAsInt();
                String image = "http://so.gushiwen.org/authorImg/" + hanyu.getStringPinYin(poet)+ ".jpg";
                titleView.setText(poem.get("Name").getAsString());
                authorView.setText(poet);
                String content = poem.get("RecDes").getAsString();
                content = content.replace("\n","");
                descView.setText(content);
                Ion.with(imageView).error(R.drawable.ic_menu_gallery).load(image);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(mActivity, PoetActivity.class);
                        it.putExtra("pos", pid);
                        startActivity(it);
                    }
                });

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(mActivity, PoemActivity.class);
                        it.putExtra("pid", poem.get("Id").getAsInt());
                        startActivity(it);
                    }
                });
            }
            return view;
        }
    }
    public class Hanyu{
        private HanyuPinyinOutputFormat format = null;
        private String[] pinyin;

        public Hanyu(){
            format = new HanyuPinyinOutputFormat();
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            pinyin = null;
        }

        //转换单个字符
        public String getCharacterPinYin(char c){
            try{
                pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
            }catch(BadHanyuPinyinOutputFormatCombination e){
                e.printStackTrace();
            }
            // 如果c不是汉字，toHanyuPinyinStringArray会返回null
            if(pinyin == null) return null;
            // 只取一个发音，如果是多音字，仅取第一个发音
            return pinyin[0];

        }

        //转换一个字符串
        public String getStringPinYin(String str){
            StringBuilder sb = new StringBuilder();
            String tempPinyin = null;
            for(int i = 0; i < str.length(); ++i){
                tempPinyin =getCharacterPinYin(str.charAt(i));
                if(tempPinyin == null){
                    // 如果str.charAt(i)非汉字，则保持原样
                    sb.append(str.charAt(i));
                }else{
                    sb.append(tempPinyin);
                }
            }
            return sb.toString();
        }
    }
}
