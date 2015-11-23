package com.charsunny.poem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.charsunny.poem.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.*;
import com.victor.loading.book.BookLoading;

import org.json.JSONArray;
import org.json.JSONObject;

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
                Typeface type = Typeface.createFromAsset(RecomandFragment.this.getActivity().getAssets(), "FZBWJ.ttf");
                titleView.setTypeface(type);
                timeView.setTypeface(type);
                descView.setTypeface(type);
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

        public PoemContentAdapter(JsonArray items, Activity activity) {
            mValues = items;
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_column, parent, false);
            ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
            TextView authorView = (TextView)view.findViewById(R.id.author);
            TextView titleView = (TextView)view.findViewById(R.id.title);
            TextView descView = (TextView)view.findViewById(R.id.desc);
            JsonObject poem =  mValues.get(position).getAsJsonObject();
            if (poem instanceof  JsonObject) {
                titleView.setText(poem.get("Name").getAsString());
                authorView.setText(poem.get("Poet").getAsJsonObject().get("Name").getAsString());
                String content = poem.get("RecDes").getAsString();
                content = content.replace("\n","");
                descView.setText(content);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mActivity, PoemContentActivity.class);
                    startActivity(it);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(mActivity, PoemContentActivity.class);
                    startActivity(it);
                }
            });
            return view;
        }
    }
}
