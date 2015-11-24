package com.charsunny.poem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.ImageViewBitmapInfo;
import com.koushikdutta.ion.Ion;

public class PoemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int pid = getIntent().getIntExtra("pid", 0);
        PoemEntity poem = PoemEntity.getPoemById(pid);
        setContentView(R.layout.activity_poem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleView = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setSubtitle("" + poem.aid);
        getSupportActionBar().setTitle(poem.name);
        titleView.setText(poem.name);

        TextView textView = (TextView)findViewById(R.id.content);
        textView.setText(poem.content);
        FontManager.sharedInstance(null).applyFont(textView);
        final AuthorEntity author = AuthorEntity.getPoetById(poem.aid);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (author.name != null) {
            PinyinUtil hanyu = new PinyinUtil();
            String image = "http://so.gushiwen.org/authorImg/" + hanyu.getStringPinYin(author.name) + ".jpg";
            Ion.with(this).load(image).withBitmap().asBitmap().setCallback(new FutureCallback<Bitmap>() {
                @Override
                public void onCompleted(Exception e, Bitmap result) {
                    if (result != null) {
                        Bitmap zoomBitmap = ImageUtil.zoomBitmap(result, 100, 100);
                        //获取圆角图片
                        Bitmap roundBitmap = ImageUtil.getRoundedCornerBitmap(zoomBitmap, 50.0f);
                        fab.setImageBitmap(roundBitmap);
                    } else {
                        Bitmap authorBit = ImageUtil.textAsBitmap(author.name, 40, Color.BLACK, 80);
                        fab.setImageBitmap(authorBit);
                    }
                }
            });
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(PoemActivity.this, PoetDetailActivity.class);
                it.putExtra("pid", author.pid);
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poem_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home){
            finish();
            return true ;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
