package com.example.xtalker.manprotest;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class SideActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Menu menus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_side, new MainFragment()).commit();

        CGlobal.user = new UserSessionManager(this);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(count <= 1){
                super.onBackPressed();
            }
            else if(NearLocation.NearLocation){
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().popBackStack();
                NearLocation.NearLocation = false;
            }
            else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side, menu);
        menus = menu;
        setMenuItem();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = null;

        //noinspection SimplifiableIfStatement
//        if (id == R.id.setting) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.content_side, new Setting()).addToBackStack(null).commit();
//        }else
        if (id == R.id.sign_in) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_side, new Login()).addToBackStack(null).commit();
        }else if (id == R.id.logout) {
            CGlobal.user.logoutUser();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            setMenuItem();
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.main) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_side, new MainFragment()).addToBackStack(null).commit();
        } else if (id == R.id.listwisata) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_side, new listwisata()).addToBackStack(null).commit();
        } else if (id == R.id.nearme) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_side, new NearLocation()).addToBackStack(null).commit();
        //} else if (id == R.id.news) {
        //    getSupportFragmentManager().beginTransaction().replace(R.id.content_side, new News()).addToBackStack(null).commit();
        } else if (id == R.id.event) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_side, new Event()).addToBackStack(null).commit();
        } else if (id == R.id.about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_side, new AboutUs()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void setMenuItem(){
        if (CGlobal.user.isUserLoggedIn()){
            MenuItem signin = menus.findItem(R.id.sign_in);
            MenuItem logout = menus.findItem(R.id.logout);
            //MenuItem setting = menus.findItem(R.id.setting);
            MenuItem username = menus.findItem(R.id.username);
            signin.setVisible(false);
            logout.setVisible(true);
            //setting.setVisible(true);
            String[] name = CGlobal.user.pref.getString(CGlobal.user.KEY_NAME, "").split(" ");
            username.setTitle(name[0]);
        }else{
            MenuItem logout = menus.findItem(R.id.logout);
            MenuItem signin = menus.findItem(R.id.sign_in);
            //MenuItem setting = menus.findItem(R.id.setting);
            MenuItem username = menus.findItem(R.id.username);
            logout.setVisible(false);
            signin.setVisible(true);
            //setting.setVisible(false);
            username.setTitle("Welcome");
        }
    }
}
