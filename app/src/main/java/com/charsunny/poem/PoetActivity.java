package com.charsunny.poem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.victor.loading.book.BookLoading;

import java.util.List;

public class PoetActivity extends AppCompatActivity {

    private ListView listView;
    private View headerView;
    private BookLoading bookLoading;
    private PoetContentAdapter contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        int aid = getIntent().getIntExtra("pos", 0);
        AuthorEntity author = AuthorEntity.getPoetById(aid);
        List<PoemEntity> poems = PoemEntity.getPoemsByAuthor(aid);
        if (author != null) {
            getSupportActionBar().setTitle(author.name);
        }
        contentAdapter = new PoetContentAdapter(poems);
        // Set the adapter
        listView = (ListView) findViewById(R.id.listview);
        bookLoading = (BookLoading) findViewById(R.id.bookloading);
        bookLoading.setVisibility(View.GONE);
        headerView = getLayoutInflater().inflate(R.layout.poet_header, listView, false);
        listView.addHeaderView(headerView);
        listView.setAdapter(contentAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    public class PoetContentAdapter extends BaseAdapter {

        private List<PoemEntity> mPoems;

        public PoetContentAdapter(List<PoemEntity> poems) {
            mPoems = poems;
        }

        @Override
        public int getCount() {
            return mPoems.size();
        }

        @Override
        public Object getItem(int position) {
            return mPoems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.poet_poem_list, parent, false);
            }
            TextView titleView = (TextView) view.findViewById(R.id.title);
            TextView descView = (TextView) view.findViewById(R.id.desc);
            final PoemEntity poem = mPoems.get(position);
            titleView.setText(poem.name);
            String content = poem.content.replace("\n", "");
            if (content.length() > 100) {
                content = content.substring(0, 99);
            }
            descView.setText(content);
            FontManager.sharedInstance(null).applyFont(titleView, descView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(PoetActivity.this, PoemActivity.class);
                    it.putExtra("pid", poem.pid);
                    startActivity(it);
                }
            });
            return view;
        }
    }

}
