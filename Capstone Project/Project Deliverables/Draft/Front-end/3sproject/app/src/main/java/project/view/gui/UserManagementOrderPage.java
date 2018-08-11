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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import project.view.adapter.CartAdapter;
import project.view.adapter.UserOrderAdapter;
import project.view.fragment.manageOrder.DoingOrderUser;
import project.view.fragment.manageOrder.DoneOrderUser;
import project.view.fragment.manageOrder.WaitingOrderUser;
import project.view.R;
import project.view.model.Cart;
import project.view.model.Order;
import project.view.util.CustomInterface;

//public class UserManagementOrderPage extends AppCompatActivity {
//
//    private SectionsPagerAdapter mSectionsPagerAdapter;
//
//    private ViewPager mViewPager;
//    private Toolbar toolbar;
//    private TabLayout tabLayout;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order_management);
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(R.string.title_order_management);
//
//        CustomInterface.setStatusBarColor(this);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int colorFrom = ((ColorDrawable) toolbar.getBackground()).getColor();
//                int colorTo = getColorForTab(tab.getPosition());
//
//                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
//                colorAnimation.setDuration(500);
//                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animator) {
//                        int color = (int) animator.getAnimatedValue();
//
//                        toolbar.setBackgroundColor(color);
//                        tabLayout.setBackgroundColor(color);
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            getWindow().setStatusBarColor(color);
//                        }
//                    }
//
//                });
//                colorAnimation.start();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }
//
//
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch(position){
//                case 0:
//                    WaitingOrderUser waitingOrder = new WaitingOrderUser();
//                    return waitingOrder;
//                case 1:
//                    DoingOrderUser doing = new DoingOrderUser();
//                    return doing;
//                case 2:
//                    DoneOrderUser done = new DoneOrderUser();
//                    return done;
//            }
//            return null;
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//    }
//    public int getColorForTab(int position) {
//        if (position == 0) return ContextCompat.getColor(this, R.color.colorApplication);
//        else if (position == 1) return ContextCompat.getColor(this, R.color.colorGreen);
//        else return ContextCompat.getColor(this, R.color.colorGray);
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle arrow click here
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}

public class UserManagementOrderPage extends AppCompatActivity {

    private ExpandableListView orderListView;
    private List<Order> list = new ArrayList<>();
    private Order order;
    private RelativeLayout noOrder;
    private UserOrderAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private int userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);
        userId = getIntent().getIntExtra("userID",-1);
        getSupportActionBar().setTitle("Quản lý đơn hàng");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderListView = (ExpandableListView) findViewById(R.id.orderList);
        noOrder = (RelativeLayout) findViewById(R.id.noOrder);
        orderListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
            });
        }

    @Override
    protected void onResume() {
        super.onResume();

        if (userId != -1) {
            myRef = database.getReference().child("ordersUser").child(String.valueOf(userId));
            adapter = new UserOrderAdapter(UserManagementOrderPage.this, list,userId);
            orderListView.setAdapter(adapter);
            myRef.addValueEventListener(changeListener);
        } else {
            Toast.makeText(this, "Không có người dùng", Toast.LENGTH_LONG).show();
        }
        orderListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        myRef.removeEventListener(changeListener);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ValueEventListener changeListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            list.clear();
            //loadingBar.setVisibility(View.VISIBLE);
            if (dataSnapshot.exists()) {
                noOrder.setVisibility(View.INVISIBLE);
                for (DataSnapshot dttSnapshot2 : dataSnapshot.getChildren()) {
                    order = dttSnapshot2.getValue(Order.class);
                    Log.d("order",order.toString());
                    order.setOrderId(dttSnapshot2.getKey());
                    list.add(order);
                    for (int i = 0; i < list.size(); i++) {
                        orderListView.expandGroup(i);

                    }
                    adapter.notifyDataSetChanged();

                }
            } else {
                list.clear();
                //buyLinearLayout.setVisibility(View.INVISIBLE);
                //loadingBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                noOrder.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
