package project.view.home.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import project.view.home.Fragment.HomeFragment;
import project.view.home.Fragment.StoreFragment;
import project.view.home.Fragment.UserFragment;

/**
 * Created by Tung
 */


public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private  String userJSON = "";
    private  String storeJSON = "";

    public ViewPagerAdapter(FragmentManager manager, String userJSON,String storeJSON) {
        super(manager);
        this.userJSON = userJSON;
        this.storeJSON = storeJSON;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0 : return new HomeFragment();
            case 1 : Bundle bundle = new Bundle();
                bundle.putString("userJSON", userJSON);
                bundle.putString("storeJSON", storeJSON);
                StoreFragment storeFragment = new StoreFragment();
                storeFragment.setArguments(bundle);
                return storeFragment;
//            if (position == 0) return mFragmentList.get(position);
//            if (mFragmentList.get(position).getArguments() == null) {
//                Bundle bundle = new Bundle();
//                bundle.putString("userJSON", userJSON);
//                bundle.putString("storeJSON", storeJSON);
//                mFragmentList.get(position).setArguments(bundle);
//            }
            //return mFragmentList.get(position);
            case 2 :
                Bundle bundle1 = new Bundle();
                bundle1.putString("userJSON", userJSON);
                bundle1.putString("storeJSON", storeJSON);
                UserFragment userFragment = new UserFragment();
                userFragment.setArguments(bundle1);
                return userFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);

    }
}