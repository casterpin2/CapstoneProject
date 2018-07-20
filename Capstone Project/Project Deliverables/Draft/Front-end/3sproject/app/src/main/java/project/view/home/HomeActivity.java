package project.view.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import project.view.R;
import project.view.home.Fragment.HomeFragment;
import project.view.home.Fragment.StoreFragment;
import project.view.home.Fragment.UserFragment;
import project.view.home.adapter.ViewPagerAdapter;


public class HomeActivity extends AppCompatActivity{

    BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    StoreFragment chatFragment;
    HomeFragment callsFragment;
    UserFragment contactsFragment;
    MenuItem prevMenuItem;
    private String userJSON;
    private String storeJSON;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

        setupViewPager(viewPager,storeJSON,userJSON);
    }

    private void setupViewPager(ViewPager viewPager, String storeJSON, String userJSON) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),storeJSON,userJSON);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private void getAuthen(){
        savingPreferences();
        restoringPreferences();
    }
    private void savingPreferences(){
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("user") != null) {
            userJSON = bundle.getString("user");
            storeJSON = bundle.getString("store");
            //tạo đối tượng getSharedPreferences
            SharedPreferences pre = getSharedPreferences("authentication", Context.MODE_PRIVATE);
            //tạo đối tượng Editor để lưu thay đổi
            SharedPreferences.Editor editor = pre.edit();
            //lưu vào editor
            editor.putString("user", bundle.getString("user"));
            editor.putString("store", bundle.getString("store"));
            //chấp nhận lưu xuống file
            editor.commit();
        }
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
