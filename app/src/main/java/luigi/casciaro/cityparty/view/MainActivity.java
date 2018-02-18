package luigi.casciaro.cityparty.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import luigi.casciaro.cityparty.AppController;
import luigi.casciaro.cityparty.R;
import luigi.casciaro.cityparty.adapter.ViewPagerAdapter;
import luigi.casciaro.cityparty.controller.UserController;
import luigi.casciaro.cityparty.model.User;
import luigi.casciaro.cityparty.util.MyDialogUtil;
import luigi.casciaro.cityparty.util.MyUtil;
import luigi.casciaro.cityparty.view.fragment.CategoryFragment;
import luigi.casciaro.cityparty.view.fragment.HomeFragment;
import luigi.casciaro.cityparty.view.fragment.ProfileFragment;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.textViewFilter)
    TextView textViewFilter;

    @OnClick(R.id.textViewFilter)
    public void clickOnFilter(){
        Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
        startActivityForResult(intent, FilterActivity.FILTER_RESULT_CODE);
        overridePendingTransition( R.anim.slide_up, R.anim.stay);
    }

    // not used Butterknife because not work with static field
    public static ViewPager viewPager;
    public static User userLogged;
    int currentFragmentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewPager = findViewById(R.id.viewpager);
        userLogged = UserController.getUserLogged();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        //RealmController.addAdsToPublisherUser();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // home
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.mainActivity = this;
        adapter.addFragment(homeFragment, getString(R.string.text_tab_home));
        // category
        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.mainActivity = this;
        adapter.addFragment(categoryFragment, getString(R.string.text_tab_categories));
        // profile
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.mainActivity = this;
        adapter.addFragment(profileFragment, getString(R.string.text_tab_profile));
        // add to adapter
        viewPager.setAdapter(adapter);
        // listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                MyUtil.print("onPageSelected() -> " + position);
                // show/hide memu item
                currentFragmentPosition = position;
                invalidateOptionsMenu();
                // home?
                if(currentFragmentPosition == 0) {
                    // yes, check filter
                    if (AppController.getInstance().isFilterByCategorySetted()) {
                        MyUtil.print("onPageSelected() -> " + AppController.getInstance().getFilterByCategory());
                        refreshFilterTextView();
                    }
                    textViewFilter.setVisibility(View.VISIBLE);
                } else{
                    textViewFilter.setVisibility(View.GONE);
                }
            }
        });
    }

    void refreshFilterTextView(){
        // get number
        int numberOfFiltersActive = AppController.getInstance().getFiltersActiveNumber();
        // check
        if(numberOfFiltersActive > 0){
            if(numberOfFiltersActive == 1)
                textViewFilter.setText(AppController.getInstance().getFiltersActiveNumber() + " filtro attivo");
            else
                textViewFilter.setText(AppController.getInstance().getFiltersActiveNumber() + " filtri attivi");
        }else{
            textViewFilter.setText(getString(R.string.text_filter_ads));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFilterTextView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
            case R.id.action_becomes_an_advertiser: {
                MyDialogUtil.showDialog(this, "Diventa inserzionista", "Richiesta inviata con successo!");
                return true;
            }
            case R.id.action_logout: {
                // logout
                AlertDialog.Builder builder;
                AlertDialog alert;
                builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle(R.string.text_logout);
                builder.setMessage(R.string.text_alert_dialog_logout);
                builder.setPositiveButton(R.string.text_yes, (dialog, id) -> UserController.logOut());
                builder.setNegativeButton(R.string.text_no, (dialog, id) -> dialog.cancel());

                alert = builder.create();
                alert.show();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // 0 Home
        // 1 Category
        // 2 Profile

        // show search in Home
        menu.findItem(R.id.action_search).setVisible(currentFragmentPosition == 0);
        // show becomes advertiser in Profile
        menu.findItem(R.id.action_becomes_an_advertiser).setVisible(currentFragmentPosition == 2);
        // show logout in Profile
        menu.findItem(R.id.action_logout).setVisible(currentFragmentPosition == 2);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        // exit
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
