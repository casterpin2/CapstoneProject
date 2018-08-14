package project.view.gui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import project.view.adapter.UserOrderAdapter;
import project.view.fragment.manageOrder.DoingOrderStore;
import project.view.fragment.manageOrder.DoneOrderStore;
import project.view.fragment.manageOrder.WaitingOrderStore;
import project.view.R;
import project.view.model.Order;
import project.view.util.CustomInterface;

public class StoreManagementOrderPage extends BasePage {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private int storeId;
    private Order order;
    private List<Order> list = new ArrayList<>();
    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_management);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_order_management);
        storeId = getIntent().getIntExtra("storeId" , -1);
        if (storeId == -1) {
            Toast.makeText(this, "Có lỗi xảy ra!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        CustomInterface.setStatusBarColor(this);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int colorFrom = ((ColorDrawable) toolbar.getBackground()).getColor();
                int colorTo = getColorForTab(tab.getPosition());

                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(500);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int color = (int) animator.getAnimatedValue();

                        toolbar.setBackgroundColor(color);
                        tabLayout.setBackgroundColor(color);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(color);
                        }
                    }

                });
                colorAnimation.start();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch(position){
                case 0:
                    WaitingOrderStore waitingOrder = new WaitingOrderStore();
                    bundle.putInt("storeId", storeId);
                    waitingOrder.setArguments(bundle);
                    return waitingOrder;
                case 1:
                    DoingOrderStore doing = new DoingOrderStore();
                    bundle.putInt("storeId", storeId);
                    doing.setArguments(bundle);
                    return doing;
                case 2:
                    DoneOrderStore done = new DoneOrderStore();
                    bundle.putInt("storeId", storeId);
                    done.setArguments(bundle);
                    return done;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    public int getColorForTab(int position) {
        if (position == 0) return ContextCompat.getColor(this, R.color.colorApplication);
        else if (position == 1) return ContextCompat.getColor(this, R.color.colorGreen);
        else return ContextCompat.getColor(this, R.color.colorGray);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}

