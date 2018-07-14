package project.view.OrderManagerment;

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
import android.view.MenuItem;

import project.view.OrderManagerment.fragment.DoingOrder;
import project.view.OrderManagerment.fragment.DoneOrder;
import project.view.OrderManagerment.fragment.WaitingOrder;
import project.view.R;

public class OrderManagement extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_order_management);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(((ColorDrawable) toolbar.getBackground()).getColor());
        }

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
            switch(position){
                case 0:
                    WaitingOrder waitingOrder = new WaitingOrder();
                    return waitingOrder;
                case 1:
                    DoingOrder doing = new DoingOrder();
                    return doing;
                case 2:
                    DoneOrder done = new DoneOrder();
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
