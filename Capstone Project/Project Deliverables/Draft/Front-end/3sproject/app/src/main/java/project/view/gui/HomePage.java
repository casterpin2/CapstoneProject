package project.view.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import project.view.R;
import project.view.fragment.home.HomeFragment;
import project.view.fragment.home.StoreFragment;
import project.view.fragment.home.UserFragment;
import project.view.adapter.ViewPagerAdapter;


public class HomePage extends BasePage{

    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    MenuItem prevMenuItem;
    private String userJSON;
    private String storeJSON;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        boolean isIntent = getIntent().getBooleanExtra("isLogin", false);
        if (isIntent){
            startActivity(new Intent(HomePage.this,LoginPage.class));
        }
        getAuthen();
        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorApplication));
        }
        //Initializing the bottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_store:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_user:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager,userJSON,storeJSON);
    }

    private void setupViewPager(ViewPager viewPager, String userJSON, String storeJSON) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),userJSON,storeJSON);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private void getAuthen(){
        restoringPreferences();
    }


    private void restoringPreferences(){
        if (userJSON == null) {
            SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
            userJSON = pre.getString("user", "");
            storeJSON = pre.getString("store", "");
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(this).clearMemory();

    }
}
