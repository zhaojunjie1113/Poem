package com.charsunny.poem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected Toolbar toolbar;
    protected FloatingActionButton fab;
    private RecomandFragment recomandFragment;//推荐
    private ColumnFragment columnFragment;//专栏
    private DiscorveryFragment discorveryFragment;//发现
    private ManageFragment manageFragment;//设置
    private DonateFragment donateFragment;//捐赠
    protected Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontManager.sharedInstance(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:charsunny@gmail.com"));
                data.putExtra(Intent.EXTRA_SUBJECT, "意见反馈");
                startActivity(data);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setDefaultFragment();
    }


    public void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        RecomandFragment rf = new RecomandFragment();
        ft.replace(R.id.id_content, rf);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        MenuItem hisMenu = this.menu.findItem(R.id.action_history);
        MenuItem searchMenu = this.menu.findItem(R.id.menu_search);
        hisMenu.setVisible(true);
        searchMenu.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_history) {
            Intent it = new Intent(this, RecommandHistoryActivity.class);
            startActivity(it);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        MenuItem hisMenu = this.menu.findItem(R.id.action_history);
        MenuItem searchMenu = this.menu.findItem(R.id.menu_search);
        int id = item.getItemId();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (id == R.id.nav_recommend) {
            toolbar.setTitle("诗词");
            if (recomandFragment == null) {
                recomandFragment = new RecomandFragment();
            }
            hisMenu.setVisible(true);
            searchMenu.setVisible(false);
            transaction.replace(R.id.id_content, recomandFragment);
        } else if (id == R.id.nav_discovery) {
            toolbar.setTitle("发现");
            if (discorveryFragment == null) {
                discorveryFragment = new DiscorveryFragment();
            }
            hisMenu.setVisible(false);
            searchMenu.setVisible(true);
            transaction.replace(R.id.id_content, discorveryFragment);
//        } else if (id == R.id.nav_slideshow) {
//            toolbar.setTitle("专栏");
//            if (columnFragment == null) {
//                columnFragment = ColumnFragment.newInstance(1);
//            }
//            hisMenu.setVisible(false);
//            searchMenu.setVisible(true);
//            transaction.replace(R.id.id_content, columnFragment);
        } else if (id == R.id.nav_settings) {
            toolbar.setTitle("设置");
            if (manageFragment == null) {
                manageFragment = new ManageFragment();
            }
            hisMenu.setVisible(false);
            searchMenu.setVisible(false);
            transaction.replace(R.id.id_content, manageFragment);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "诗词分享"); // 分享的主题
            intent.putExtra(Intent.EXTRA_TEXT, "分享内容"); // 分享的内容
            startActivity(intent);
        } else if (id == R.id.nav_donate) {
            toolbar.setTitle("捐赠");
            if (donateFragment == null) {
                donateFragment = new DonateFragment();
            }
            hisMenu.setVisible(false);
            searchMenu.setVisible(false);
            transaction.replace(R.id.id_content, donateFragment);
        }
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
