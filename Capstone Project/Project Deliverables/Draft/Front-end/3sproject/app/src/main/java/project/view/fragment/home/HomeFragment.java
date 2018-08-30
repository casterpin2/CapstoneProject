package project.view.fragment.home;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.retrofit.ApiUtils;
import project.view.gui.BarcodeActivity;
import project.view.gui.HomePage;
import project.view.gui.SaleProductDisplayPage;
import project.view.model.Brand;
import project.view.gui.BrandDisplayPage;
import project.view.model.Category;
import project.view.gui.CategoryDisplayPage;
import project.view.R;
import project.view.model.Notification;
import project.view.model.NotificationDetail;
import project.view.model.Product;
import project.view.adapter.SaleProductCustomCardviewAdapter;
import project.view.gui.UserSearchProductPage;
import project.view.adapter.BrandRecycleViewAdapter;
import project.view.adapter.CategoryRecycleViewAdapter;
import project.view.adapter.SliderImageViewPagerAdapter;
import project.view.model.ResultNotification;
import project.view.model.StoreNotification;
import project.view.util.Formater;
import project.view.util.GridSpacingItemDecoration;
import project.view.util.NetworkStateReceiver;
import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements NetworkStateReceiver.NetworkStateReceiverListener {
    private RecyclerView recyclerViewCategories, recyclerViewBrands, recyclerViewSaleProduct;
    private CategoryRecycleViewAdapter categoryAdapter;
    BrandRecycleViewAdapter brandAdapter;
    private SaleProductCustomCardviewAdapter saleProductCustomCardviewAdapter;
    private APIService apiService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManagerCategory;
    private TextView tv_more_category, tv_more_brand, tv_more_sale;
    private View view;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private LinearLayoutManager linearLayoutManagerBrand;
    private ImageButton imgBarCode;
    private CardView searchLayout;
    private Toolbar toolbar;
    private NestedScrollView scroll;
    private Call<List<Category>> callCategory;
    private Call<List<Brand>> callBrand;
    private Call<List<Product>> callSale;
    private NetworkStateReceiver networkStateReceiver;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        findView();
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        scroll.setVerticalScrollBarEnabled(false);
        scroll.setHorizontalScrollBarEnabled(false);
        viewPager.requestFocus();
        apiService = APIService.retrofit.create(APIService.class);
        refreshData();
        sliderImage();

        //check network available
        networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        getContext().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toUserSearchProduct = new Intent(getContext(), UserSearchProductPage.class);
                startActivity(toUserSearchProduct);
            }
        });

        imgBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toBarCode = new Intent(getContext(), BarcodeActivity.class);
                toBarCode.putExtra("home", 3);
                startActivity(toBarCode);

            }
        });

        tv_more_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CategoryDisplayPage.class);
                startActivity(i);
            }
        });

        tv_more_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BrandDisplayPage.class);
                startActivity(i);
            }
        });


        tv_more_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toSaleProductPage = new Intent(getContext(), SaleProductDisplayPage.class);
                startActivity(toSaleProductPage);
            }
        });


        callCategory = apiService.getCategory();
        new CategoryData().execute(callCategory);

        callBrand = apiService.getBrandsTop5();
        new BrandData().execute(callBrand);

        callSale = apiService.getSaleProductTop20();
        new SaleData().execute(callSale);
        return view;
    }


    @Override
    public void networkAvailable() {
        callCategory = apiService.getCategory();
        new CategoryData().execute(callCategory);
        callBrand = apiService.getBrandsTop5();
        new BrandData().execute(callBrand);
        callSale = apiService.getSaleProductTop20();
        new SaleData().execute(callSale);

    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(getContext(), "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_LONG).show();
    }

    private void sliderImage() {
        SliderImageViewPagerAdapter viewPagerAdapter = new SliderImageViewPagerAdapter(getContext());

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void refreshData() {
        scroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(true);
                if (scroll.getScrollY() == 0) {
                    swipeRefreshLayout.setEnabled(true);
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            callCategory = apiService.getCategory();
                            new CategoryData().execute(callCategory);
                            callBrand = apiService.getBrandsTop5();
                            new BrandData().execute(callBrand);
                            callSale = apiService.getSaleProductTop20();
                            new SaleData().execute(callSale);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }, 4000);
                        }
                    });

                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    private void findView() {
        recyclerViewSaleProduct = view.findViewById(R.id.list_sale);
        recyclerViewCategories = view.findViewById(R.id.list_category);
        recyclerViewBrands = view.findViewById(R.id.list_brand);
        scroll = view.findViewById(R.id.scrollView);
        imgBarCode = view.findViewById(R.id.imgBarCode);
        viewPager = view.findViewById(R.id.img_slider);
        searchLayout = view.findViewById(R.id.searchLayout);
        sliderDotspanel = view.findViewById(R.id.slider_dots);
        tv_more_category = view.findViewById(R.id.tv_more_category);
        tv_more_brand = view.findViewById(R.id.tv_more_brand);
        tv_more_sale = view.findViewById(R.id.tv_more_sale);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefesh);

    }

    private class CategoryData extends AsyncTask<Call, List<Category>, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<Category>... values) {
            super.onProgressUpdate(values);

            List<Category> categoryList = values[0];
            if (categoryList != null) {
                if (getActivity() != null) {
                    categoryAdapter = new CategoryRecycleViewAdapter(categoryList, getContext());
                    recyclerViewCategories.setNestedScrollingEnabled(false);
                    recyclerViewCategories.setAdapter(categoryAdapter);
                    linearLayoutManagerCategory = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewCategories.setLayoutManager(linearLayoutManagerCategory);
                }
            }
        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<List<Category>> call = calls[0];
                Response<List<Category>> response = call.execute();
                publishProgress(response.body());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class BrandData extends AsyncTask<Call, List<Brand>, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<Brand>... values) {
            super.onProgressUpdate(values);
            List<Brand> brandList = values[0];
            if (brandList != null) {
                if (getActivity() != null) {
                    brandAdapter = new BrandRecycleViewAdapter(brandList, getContext());
                    recyclerViewBrands.setAdapter(brandAdapter);
                    linearLayoutManagerBrand = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewBrands.setLayoutManager(linearLayoutManagerBrand);
                }
            }
        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<List<Brand>> call = calls[0];
                Response<List<Brand>> response = call.execute();
                publishProgress(response.body());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(getContext()).clearMemory();
        networkStateReceiver.removeListener(this);
        getContext().unregisterReceiver(networkStateReceiver);

    }

    public class SaleData extends AsyncTask<Call, List<Product>, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<Product>... values) {
            super.onProgressUpdate(values);
            List<Product> listSale = values[0];
            if (listSale != null){
                if (getActivity() != null) {
                    saleProductCustomCardviewAdapter = new SaleProductCustomCardviewAdapter(getContext(), listSale);
                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
                    recyclerViewSaleProduct.setLayoutManager(mLayoutManager);
                    recyclerViewSaleProduct.setNestedScrollingEnabled(false);
                    recyclerViewSaleProduct.addItemDecoration(new GridSpacingItemDecoration(2, Formater.dpToPx(2, getResources()), true));
                    recyclerViewSaleProduct.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewSaleProduct.setAdapter(saleProductCustomCardviewAdapter);
                }
            }
        }

        @Override
        protected Void doInBackground(Call... calls) {
            try {
                Call<List<Product>> call = calls[0];
                Response<List<Product>> response = call.execute();
                publishProgress(response.body());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}


