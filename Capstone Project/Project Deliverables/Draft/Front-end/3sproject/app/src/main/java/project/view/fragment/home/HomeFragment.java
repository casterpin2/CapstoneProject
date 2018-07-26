package project.view.fragment.home;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.firebase.Firebase;
import project.retrofit.APIService;
import project.view.gui.BarcodeActivity;
import project.view.model.Brand;
import project.view.gui.BrandDisplayPage;
import project.view.model.Category;
import project.view.gui.CategoryDisplayPage;
import project.view.R;
import project.view.model.SaleProduct;
import project.view.adapter.SaleProductCustomCardviewAdapter;
import project.view.gui.UserSearchProductPage;
import project.view.adapter.BrandRecycleViewAdapter;
import project.view.adapter.CategoryRecycleViewAdapter;
import project.view.adapter.SliderImageViewPagerAdapter;
import retrofit2.Call;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewCategories,recyclerViewBrands, recyclerViewSaleProduct;
    private CategoryRecycleViewAdapter categoryAdapter;
    BrandRecycleViewAdapter brandAdapter;
    private SaleProductCustomCardviewAdapter saleProductCustomCardviewAdapter;
    private APIService apiService;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManagerCategory;
    private TextView tv_more_category,tv_more_brand;
    private View view;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private LinearLayoutManager linearLayoutManagerBrand;
    private ImageButton imgBarCode;
    private CardView searchLayout;


    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // TweakUI.makeTransparent(this.getActivity());
        view = inflater.inflate(R.layout.fragment_home,container,false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        NestedScrollView scroll = view.findViewById(R.id.scrollView);

        scroll.setVerticalScrollBarEnabled(false);
        scroll.setHorizontalScrollBarEnabled(false);

        imgBarCode = view.findViewById(R.id.imgBarCode);

        viewPager =view.findViewById(R.id.img_slider);
        viewPager.requestFocus();
        searchLayout = view.findViewById(R.id.searchLayout);
        sliderDotspanel = view.findViewById(R.id.slider_dots);

        SliderImageViewPagerAdapter viewPagerAdapter = new SliderImageViewPagerAdapter(getContext());

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

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

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

       /* Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 2000, 4000);
*/

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
               startActivity(toBarCode);
           }
       });

        tv_more_category = view.findViewById(R.id.tv_more_category);
        tv_more_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CategoryDisplayPage.class);
                startActivity(i);
            }
        });

        tv_more_brand = view.findViewById(R.id.tv_more_brand);
        tv_more_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), BrandDisplayPage.class);
                startActivity(i);
            }
        });

        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<Category>> callCategory = apiService.getCategory();
        new CategoryData().execute(callCategory);
        //categorié


        //brand
        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<Brand>> callBrand = apiService.getBrands();
        new BrandData().execute(callBrand);

        //sale
        recyclerViewSaleProduct= view.findViewById(R.id.list_sale);

        apiService = APIService.retrofit.create(APIService.class);
        final Call<List<SaleProduct>> callSale = apiService.getSaleProduct();
        new SaleData().execute(callSale);




//        swipeRefreshLayout = view.findViewById(R.id.main_layout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshContent();
//            }
//        });
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

      return view;
    }



//
//    private void refreshContent(){
//
//             swipeRefreshLayout.setRefreshing(false);
//    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    private class CategoryData extends AsyncTask<Call, List<Category>, Void>{
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
            StorageReference storageReference = Firebase.getFirebase();
            List<Category> categoryList = values[0];


            recyclerViewCategories = view.findViewById(R.id.list_category);
            categoryAdapter = new CategoryRecycleViewAdapter(categoryList,getContext());
            recyclerViewCategories.setAdapter(categoryAdapter);
            linearLayoutManagerCategory  = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewCategories.setLayoutManager(linearLayoutManagerCategory);
        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<List<Category>> call = calls[0];
                Response<List<Category>> response = call.execute();
                List<Category> list = new ArrayList<>();
                for(int i =0;i< response.body().size();i++){
                    list.add(response.body().get(i));
                }
                publishProgress(list);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
    private class BrandData extends AsyncTask<Call, List<Brand>, Void>{
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
            StorageReference storageReference = Firebase.getFirebase();
            List<Brand> brandList = values[0];


            recyclerViewBrands = view.findViewById(R.id.list_brand);
            brandAdapter = new BrandRecycleViewAdapter(brandList,getContext());
            recyclerViewBrands.setAdapter(brandAdapter);
            linearLayoutManagerBrand = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewBrands.setLayoutManager(linearLayoutManagerBrand);
        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<List<Brand>> call = calls[0];
                Response<List<Brand>> response = call.execute();
                List<Brand> list = new ArrayList<>();
                for(int i =0;i< response.body().size();i++){
                    list.add(response.body().get(i));
                }
                publishProgress(list);
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

    }
    public class SaleData extends AsyncTask<Call,List<SaleProduct>,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(List<SaleProduct>... values) {
            super.onProgressUpdate(values);
            List<SaleProduct> listSale = values[0];
            saleProductCustomCardviewAdapter = new SaleProductCustomCardviewAdapter(getContext(), listSale);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
            recyclerViewSaleProduct.setLayoutManager(mLayoutManager);
            recyclerViewSaleProduct.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
            recyclerViewSaleProduct.setItemAnimator(new DefaultItemAnimator());
            recyclerViewSaleProduct.setAdapter(saleProductCustomCardviewAdapter);
        }

        @Override
        protected Void doInBackground(Call... calls) {

            try {
                Call<List<SaleProduct>> call = calls[0];
                Response<List<SaleProduct>> response = call.execute();
                List<SaleProduct> list = new ArrayList<>();
                for(int i =0 ; i< response.body().size();i++){
                    list.add(response.body().get(i));
                }
                publishProgress(list);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}


