package com.expocontacts.expomobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.expocontacts.expomobile.fragments.AgendaFragment;
import com.expocontacts.expomobile.fragments.ExhibitorListFragment;
import com.expocontacts.expomobile.fragments.FloorPlanFragment;
import com.expocontacts.expomobile.fragments.GeneralInfoFragment;
import com.expocontacts.expomobile.fragments.MyListFragment;
import com.expocontacts.expomobile.fragments.NewsFragment;
import com.expocontacts.expomobile.fragments.SpeakerListFragment;
import com.expocontacts.expomobile.fragments.UserPostsFragment;
import com.expocontacts.expomobile.model.Fair;
import com.expocontacts.expomobile.model_utils.FairUtils;
import com.expocontacts.expomobile.model_utils.SharedPreferencesUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton mFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(AppSettings.FAIR_NAME);

        mFAB = (FloatingActionButton) findViewById(R.id.fab);
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SharedPreferencesUtils.getUserEmailAddress(getApplicationContext()).equals("")) {

                    Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(i);

                } else {
                }

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navdrawTitle = (TextView) headerView.findViewById(R.id.navdrawTitle);
        navdrawTitle.setText(AppSettings.FAIR_NAME);
        TextView navdrawSubTitle = (TextView) headerView.findViewById(R.id.navdrawSubTitle);
        navdrawSubTitle.setText(AppSettings.FAIR_STRAPLINE);
        ImageView navdrawImage = (ImageView) headerView.findViewById(R.id.navdrawImage);
        navdrawImage.setImageResource(AppSettings.NAVDRAW_IMAGE);

        setMenuVisibility(navigationView);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, new UserPostsFragment()).commit();

        setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.news_feed));
    }

    private void setMenuVisibility(NavigationView navigationView) {

        Fair fair;
        fair = FairUtils.get(this).getFair(AppSettings.FAIR_DATE_ID);

        Menu menu = navigationView.getMenu();

        if (fair.getShowGeneralInfo() == 0) {
            MenuItem menuItem = menu.findItem(R.id.nav_general_info);
            menuItem.setVisible(false);
        }

        if (fair.getShowExhibitorList() == 0) {
            MenuItem menuItem = menu.findItem(R.id.nav_exhibitor_list);
            menuItem.setVisible(false);
        }

        if (fair.getShowSchedule() == 0) {
            MenuItem menuItem = menu.findItem(R.id.nav_schedule);
            menuItem.setVisible(false);
        }

        if (fair.getShowNews() == 0) {
            MenuItem menuItem = menu.findItem(R.id.nav_news);
            menuItem.setVisible(false);
        }

        if (fair.getShowSpeakers() == 0) {
            MenuItem menuItem = menu.findItem(R.id.nav_speakers);
            menuItem.setVisible(false);
        }

        if (fair.getShowFloorplan() == 0) {
            MenuItem menuItem = menu.findItem(R.id.nav_floor_plan);
            menuItem.setVisible(false);
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment;

        if (id == R.id.nav_home) {
            setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.news_feed));
            mFAB.setVisibility(View.VISIBLE);
            fragment = new UserPostsFragment();
        } else if (id == R.id.nav_general_info) {
            setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.general_info));
            fragment = new GeneralInfoFragment();
            mFAB.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_my_list) {
            setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.my_list));
            fragment = new MyListFragment();
            mFAB.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_exhibitor_list) {
            setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.exhibitors));
            fragment = new ExhibitorListFragment();
            mFAB.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_schedule) {
            setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.schedule));
            fragment = new AgendaFragment();
            mFAB.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_floor_plan) {
            setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.floor_plan));
            fragment = new FloorPlanFragment();
            mFAB.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_news) {
            setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.news));
            fragment = new NewsFragment();
            mFAB.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_speakers) {
            setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.speakers));
            fragment = new SpeakerListFragment();
            mFAB.setVisibility(View.INVISIBLE);
        } else {
            setTitle(AppSettings.FAIR_NAME + " - " + getResources().getString(R.string.news_feed));
            fragment = new UserPostsFragment();
            mFAB.setVisibility(View.VISIBLE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
