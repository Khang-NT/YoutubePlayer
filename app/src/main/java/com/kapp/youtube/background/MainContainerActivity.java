package com.kapp.youtube.background;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kapp.youtube.background.fragment.HomeFragment;
import com.kapp.youtube.background.fragment.OfflineFragment;

public class MainContainerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String HOME = "HOME";
    public static final String OFFLINE = "OFFLINE";
    public static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private NavigationView navigationView;

    private Fragment homeFragment, offlineFragment;
    private String currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        setTitle("Youtube Player");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.design_navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (savedInstanceState != null) {
            homeFragment = getSupportFragmentManager().getFragment(savedInstanceState, HOME);
            offlineFragment = getSupportFragmentManager().getFragment(savedInstanceState, OFFLINE);
            currentFragment = savedInstanceState.getString(CURRENT_FRAGMENT, HOME);
        } else {
            homeFragment = new HomeFragment();
            offlineFragment = new OfflineFragment();
            currentFragment = HOME;
        }

        if (currentFragment.equalsIgnoreCase(HOME))
            switchFragment(homeFragment, HOME);
        else
            switchFragment(offlineFragment, OFFLINE);
    }

    private void switchFragment(Fragment toFragment, String key) {
        currentFragment = key;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, toFragment, key)
                .commit();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_container, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                item.setChecked(true);
                if (!currentFragment.equalsIgnoreCase(HOME))
                    switchFragment(homeFragment, HOME);
                break;
            case R.id.nav_offline:
                item.setChecked(true);
                if (!currentFragment.equalsIgnoreCase(OFFLINE))
                    switchFragment(offlineFragment, OFFLINE);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
