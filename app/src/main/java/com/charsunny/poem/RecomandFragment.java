package com.charsunny.poem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.*;
import com.victor.loading.book.BookLoading;

/**
 * Created by zjj on 15/11/22.
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
        int rid = getActivity().getIntent().getIntExtra("rid" , 0);
        String url = "http://charsunny.ansinlee.com/rec?type=json";
        if (rid != 0 ) {
            url = "http://charsunny.ansinlee.com/rec/" + rid + "?type=json";
        }
        Ion.with(this).load(url).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                if (e != null) {
                    bookLoading.stop();
                    bookLoading.setVisibility(View.GONE);
                    return;
                }
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
        private final PinyinUtil hanyu;

        public PoemContentAdapter(JsonArray items, Activity activity) {
            mValues = items;
            hanyu = new PinyinUtil();
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
                        .inflate(R.layout.content_column_cell, parent, false);
            }
            final ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            TextView authorView = (TextView)view.findViewById(R.id.author);
            TextView titleView = (TextView)view.findViewById(R.id.title);
            TextView descView = (TextView)view.findViewById(R.id.desc);
            final JsonObject poem =  mValues.get(position).getAsJsonObject();
            FontManager.sharedInstance(null).applyFont(authorView, titleView, descView);
            if (poem instanceof  JsonObject) {
                final String poet = poem.get("Poet").getAsJsonObject().get("Name").getAsString();
                final int pid = poem.get("Poet").getAsJsonObject().get("Id").getAsInt();
                String image = "http://so.gushiwen.org/authorImg/" + hanyu.getStringPinYin(poet)+ ".jpg";
                titleView.setText(poem.get("Name").getAsString());
                authorView.setText(poet);
                String content = poem.get("RecDes").getAsString();
                content = content.replace("\n","");
                descView.setText(content);
                Ion.with(mActivity).load(image).withBitmap().placeholder(R.drawable.ic_menu_gallery).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                    @Override
                    public void onCompleted(Exception e, Bitmap result) {
                        if (result != null) {
                            Bitmap zoomBitmap = ImageUtil.zoomBitmap(result, 40, 40);
                            //获取圆角图片
                            Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(zoomBitmap, 20.0f);
                            imageView.setImageBitmap(roundBitmap);
                        } else {
                            Bitmap authorBit = ImageUtil.textAsBitmap(poet, 18, Color.BLACK, 40);
                            imageView.setImageBitmap(authorBit);
                        }
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(mActivity, PoetDetailActivity.class);
                        it.putExtra("pid", pid);
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
}
