package app.rbzeta.edcimplementationtracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.rbzeta.edcimplementationtracker.R;
import app.rbzeta.edcimplementationtracker.adapter.ViewPagerHomeAdapter;
import app.rbzeta.edcimplementationtracker.application.MyApplication;
import app.rbzeta.edcimplementationtracker.fragment.EDCCompleteFragment;
import app.rbzeta.edcimplementationtracker.fragment.EDCStagingFragment;
import app.rbzeta.edcimplementationtracker.fragment.EDCVerifiedFragment;
import app.rbzeta.edcimplementationtracker.helper.SessionManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar_home)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.tab_home)
    TabLayout tabLayout;
    @BindView(R.id.view_pager_home)
    ViewPager viewPager;

    private SessionManager session;
    private ViewPagerHomeAdapter pagerAdapter;
    private SmoothActionBarDrawerToggle toggle;
    private View navHeaderView;
    private ImageView imgNavHeaderLogo;
    private TextView textNavHeaderName;
    private TextView textNavHeaderBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = MyApplication.getInstance().getSessionManager();

        //////debug before login//////
        //session.setUserLogin(true);
        //session.setUserBranchId(120);
        /////////////////////////////

        if (session.isUserLogin()){
            setContentView(R.layout.activity_home);
            ButterKnife.bind(this);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
            setSupportActionBar(toolbar);
            //getSupportActionBar().setTitle(R.string.title_activity_home);

            viewPager.setOffscreenPageLimit(3);
            //setupViewPager();
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    super.onTabSelected(tab);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    super.onTabUnselected(tab);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    super.onTabReselected(tab);
                    if (tab.getPosition() == 0){
                        Fragment f = pagerAdapter.getItem(tab.getPosition());
                        if (f != null) {
                            View fragmentView = f.getView();
                            RecyclerView mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_home);
                            if (mRecyclerView != null) {
                                mRecyclerView.scrollToPosition(0);
                            }
                        }
                    }

                    if (tab.getPosition() == 1){
                        Fragment f = pagerAdapter.getItem(tab.getPosition());
                        if (f != null) {
                            View fragmentView = f.getView();
                            RecyclerView mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_home);
                            if (mRecyclerView != null) {
                                mRecyclerView.scrollToPosition(0);
                            }
                        }
                    }

                    if (tab.getPosition() == 2){
                        Fragment f = pagerAdapter.getItem(tab.getPosition());
                        if (f != null) {
                            View fragmentView = f.getView();
                            RecyclerView mRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.recycler_home);
                            if (mRecyclerView != null) {
                                mRecyclerView.scrollToPosition(0);
                            }
                        }
                    }
                }
            });

            toggle = new SmoothActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView.setNavigationItemSelectedListener(this);
            navHeaderView = navigationView.getHeaderView(0);
            imgNavHeaderLogo = (ImageView)navHeaderView.findViewById(R.id.image_nav_header_logo);
            textNavHeaderName = (TextView)navHeaderView.findViewById(R.id.text_nav_header_user_name);
            textNavHeaderBranch = (TextView)navHeaderView.findViewById(R.id.text_nav_header_branch_name);

            loadNavigationHeaderView();

            initEDCData();

        }else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void initEDCData() {
        openEDCBrilink();
    }

    private void loadNavigationHeaderView() {
        textNavHeaderName.setText(session.getUserName());
        textNavHeaderBranch.setText(session.getUserBranchName());
    }

    private void setupViewPager() {
        pagerAdapter = new ViewPagerHomeAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EDCStagingFragment(),getString(R.string.title_tab_onprogress));
        pagerAdapter.addFragment(new EDCCompleteFragment(),getString(R.string.title_tab_completed));
        /*pagerAdapter.addFragment(new HomeNotificationFragment(),getString(R.string.title_tab_notification));
        pagerAdapter.addFragment(new HomeProfileFragment(),getString(R.string.title_tab_profile));*/
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logoutAttempt();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_brilink) {
            toggle.runWhenIdle(()->openEDCBrilink());
        } else if (id == R.id.nav_merchant) {
            toggle.runWhenIdle(()->openEDCMerchant());
        } else if (id == R.id.nav_uko) {
            toggle.runWhenIdle(()->openEDCUko());
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openEDCUko() {
        setupViewPager("UKO");
        navigationView.setCheckedItem(R.id.nav_uko);
        getSupportActionBar().setTitle(getString(R.string.toolbar_title_uko));
    }

    private void openEDCMerchant() {
        setupViewPager("MERCHANT");
        navigationView.setCheckedItem(R.id.nav_merchant);
        getSupportActionBar().setTitle(getString(R.string.toolbar_title_merchant));
    }

    private void openEDCBrilink() {
        setupViewPager("BRILINKS");
        navigationView.setCheckedItem(R.id.nav_brilink);
        getSupportActionBar().setTitle(getString(R.string.toolbar_title_brilink));
    }

    private void setupViewPager(String type) {
        pagerAdapter = new ViewPagerHomeAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();
        bundle.putString("edc_type",type);
        EDCStagingFragment stagingFragment = EDCStagingFragment.newInstance(bundle);
        EDCCompleteFragment completeFragment = EDCCompleteFragment.newInstance(bundle);
        EDCVerifiedFragment verifiedFragment = EDCVerifiedFragment.newInstance(bundle);
        pagerAdapter.addFragment(stagingFragment,getString(R.string.title_tab_onprogress));
        pagerAdapter.addFragment(completeFragment,getString(R.string.title_tab_completed));
        pagerAdapter.addFragment(verifiedFragment,getString(R.string.title_tab_verified));
        viewPager.setAdapter(pagerAdapter);
    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        private SmoothActionBarDrawerToggle(AppCompatActivity activity, DrawerLayout drawerLayout,
                                            Toolbar toolbar, int openDrawerContentDescRes,
                                            int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }
        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        private void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

    private void logoutAttempt() {
        showAlertLogoutConfirmation();
    }

    private void showAlertLogoutConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dialog_msg_logout_confirmation));

        builder.setPositiveButton(getString(R.string.action_yes),
                (dialog, which) -> logoutProcess());
        builder.setNegativeButton(getString(R.string.action_no), (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutProcess() {
        session.deleteSharedPreference();

        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof EDCStagingFragment){
                fragment.onActivityResult(requestCode,resultCode,data);
            }
        }
    }*/
}
