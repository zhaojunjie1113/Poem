package com.charsunny.poem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.victor.loading.book.BookLoading;


public class RecommandHistoryActivity extends AppCompatActivity {
    private ListView listView;
    private View headerView;
    private RecommandHistoryAdapter contentAdapter;
    private BookLoading bookLoading;
    private boolean isLoadingfinsh;
    private boolean isLoading;
    private  int currentPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("历史推荐");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_recommand_history);
        listView = (ListView)findViewById(R.id.listview);
        bookLoading= (BookLoading)findViewById(R.id.bookloading);
        bookLoading.start();
        contentAdapter = new RecommandHistoryAdapter();
        listView.setAdapter(contentAdapter);
        getServerData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void getServerData() {
        if (isLoadingfinsh || isLoading) {
            return;
        }
        isLoading = true;
        Ion.with(this).load("http://charsunny.ansinlee.com/his?type=json&page=" + currentPage).asJsonObject().setCallback(new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {
                isLoading = false;
                if (e != null) {
                    return;
                }
                currentPage ++;
                bookLoading.stop();
                bookLoading.setVisibility(View.GONE);
                JsonArray array = result.getAsJsonArray();
                if (array == null) {
                    contentAdapter.addItems(array);
                    if (array.size() < 10) {
                        isLoadingfinsh = true;
                    }
                } else {
                    isLoadingfinsh = true;
                }
            }
        });
    }

    class RecommandHistoryAdapter extends BaseAdapter {

        private JsonArray mValues;

        public void addItems(JsonArray items) {
            if (mValues == null) {
                mValues = items;
            }
            mValues.addAll(items);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (mValues == null) {
                return 0;
            }
            return mValues.size();
        }

        @Override
        public Object getItem(int position) {
            if (mValues == null) {
                return null;
            }
            return mValues.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (mValues == null) {
                return null;
            }
            if (position == mValues.size() - 1) {
                RecommandHistoryActivity.this.getServerData();
            }
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recommad_history_cell, parent, false);
            }
            TextView timeView = (TextView)view.findViewById(R.id.time);
            TextView titleView = (TextView)view.findViewById(R.id.title);
            TextView descView = (TextView)view.findViewById(R.id.desc);
            final JsonObject poem =  mValues.get(position).getAsJsonObject();
            FontManager.sharedInstance(null).applyFont(timeView, titleView, descView);
            if (poem instanceof  JsonObject) {
                titleView.setText(poem.get("Title").getAsString());
                timeView.setText(poem.get("Time").getAsString());
                descView.setText(poem.get("Desc").getAsString());
            }
            return view;
        }
    }
}
