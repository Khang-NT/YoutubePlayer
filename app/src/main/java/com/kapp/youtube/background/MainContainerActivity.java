package com.kapp.youtube.background;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kapp.youtube.background.fragment.HomeFragment;
import com.kapp.youtube.background.fragment.OfflineFragment;
import com.kapp.youtube.background.util.DialogUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainContainerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String HOME = "HOME";
    public static final String OFFLINE = "OFFLINE";
    public static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";
    private static final String TAG = "MainContainerActivity";
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.design_navigation_view)
    NavigationView navigationView;

    private ActionBarDrawerToggle toggle;

    private Fragment homeFragment, offlineFragment;
    private String currentFragment;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        ButterKnife.bind(this);
        setTitle("Youtube Player");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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
            currentFragment = HOME;
        }

        if (homeFragment == null)
            homeFragment = new HomeFragment();
        if (offlineFragment == null)
            offlineFragment = new OfflineFragment();

        if (currentFragment.equalsIgnoreCase(HOME))
            switchFragment(homeFragment, HOME);
        else
            switchFragment(offlineFragment, OFFLINE);

        initNavigationView();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();
        mGoogleApiClient.connect();
    }

    private void initNavigationView() {
        View headerLayout = navigationView.getHeaderView(0);
        TextView tvUserName = (TextView) headerLayout.findViewById(R.id.tv_user_name);
        TextView tvUserEmail = (TextView) headerLayout.findViewById(R.id.tv_user_email);
        final ImageView ivLogoHeader = (ImageView) headerLayout.findViewById(R.id.iv_logo_header);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvUserName.setText(user.getDisplayName());
            tvUserEmail.setText(user.getEmail());
            Picasso.with(MainContainerActivity.this)
                    .load(user.getPhotoUrl())
                    .into(ivLogoHeader);
        }
        navigationView.getMenu().getItem(currentFragment.equalsIgnoreCase(HOME) ?
                0 : 1
        ).setChecked(true);
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
            case R.id.nav_log_out:
                DialogUtils.ConfirmDialog(this, "Log out", "Do you want to log out?",
                        DialogUtils.ButtonGroup.YES_NO,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(@NonNull Status status) {
                                        if (status.isSuccess()) {
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intent = new Intent(MainContainerActivity.this, FlashScreenActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else
                                            Toast.makeText(MainContainerActivity.this, "Log out error.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (homeFragment != null && homeFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, HOME, homeFragment);
        if (offlineFragment != null && offlineFragment.isAdded())
            getSupportFragmentManager().putFragment(outState, OFFLINE, offlineFragment);
        outState.putString(CURRENT_FRAGMENT, currentFragment);
    }
}
