package project.view.home.Fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import project.view.Brand.BrandDisplayPage;
import project.view.Category.CategoryDisplayPage;
import project.view.R;
import project.view.SaleProduct.SaleProduct;
import project.view.SaleProduct.SaleProductCustomCardviewAdapter;
import project.view.home.adapter.ItemsAdapter;
import project.view.home.adapter.SliderImageViewPagerAdapter;
import project.view.home.model.Item;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewCategories,recyclerViewBrands, recyclerViewSaleProduct;
    private ItemsAdapter itemsAdapterCategories, itemsAdapterBrands;
    private SaleProductCustomCardviewAdapter saleProductCustomCardviewAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView tv_more_category,tv_more_brand;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ScrollView scroll = view.findViewById(R.id.scroll_view);

        scroll.setVerticalScrollBarEnabled(false);
        scroll.setHorizontalScrollBarEnabled(false);

        viewPager =view.findViewById(R.id.img_slider);

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


        //categorié
        recyclerViewCategories = view.findViewById(R.id.list_category);
        itemsAdapterCategories = new ItemsAdapter(getContext(), new Item().setItem());
        recyclerViewCategories.setAdapter(itemsAdapterCategories);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(linearLayoutManager1);


        //brand
        recyclerViewBrands = view.findViewById(R.id.list_brand);
        itemsAdapterBrands = new ItemsAdapter(getContext(), new Item().setItem());
        recyclerViewBrands.setAdapter(itemsAdapterBrands);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewBrands.setLayoutManager(linearLayoutManager2);

        //sale
        recyclerViewSaleProduct= view.findViewById(R.id.list_sale);

        List<SaleProduct> saleProductList;
        saleProductList = SaleProduct.setListProduct();
        saleProductCustomCardviewAdapter = new SaleProductCustomCardviewAdapter(getContext(), saleProductList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewSaleProduct.setLayoutManager(mLayoutManager);
        recyclerViewSaleProduct.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerViewSaleProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSaleProduct.setAdapter(saleProductCustomCardviewAdapter);


        swipeRefreshLayout = view.findViewById(R.id.main_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        return view;
    }

    private void refreshContent(){

             swipeRefreshLayout.setRefreshing(false);
    }
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

}


