package com.charsunny.poem;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.activeandroid.ActiveAndroid;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected Toolbar toolbar;
    protected FloatingActionButton fab;
    public int currentId = R.id.nav_camera;
    private RecomandFragment recomandFragment;
    private ColumnFragment columnFragment;
    private DiscorveryFragment discorveryFragment;
    private FavFragment favFragment;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        if (id == R.id.nav_camera) {
            toolbar.setTitle("诗词");
            if (recomandFragment == null) {
                recomandFragment = new RecomandFragment();
            }
            hisMenu.setVisible(true);
            searchMenu.setVisible(false);
            transaction.replace(R.id.id_content, recomandFragment);
        } else if (id == R.id.nav_gallery) {
            toolbar.setTitle("发现");
            if (discorveryFragment == null) {
                discorveryFragment = new DiscorveryFragment();
            }
            hisMenu.setVisible(false);
            searchMenu.setVisible(true);
            transaction.replace(R.id.id_content, discorveryFragment);
        } else if (id == R.id.nav_slideshow) {
            toolbar.setTitle("专栏");
            if (columnFragment == null) {
                columnFragment = ColumnFragment.newInstance(1);
            }
            hisMenu.setVisible(false);
            searchMenu.setVisible(true);
            transaction.replace(R.id.id_content, columnFragment);
        } else if (id == R.id.nav_manage) {
            toolbar.setTitle("我的");
            if (favFragment == null) {
                favFragment = new FavFragment();
            }
            hisMenu.setVisible(false);
            searchMenu.setVisible(false);
            transaction.replace(R.id.id_content, favFragment);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
