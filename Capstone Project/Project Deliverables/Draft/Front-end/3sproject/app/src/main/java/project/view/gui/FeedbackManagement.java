package project.view.gui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.objects.User;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.FeedbackManagementAdapter;
import project.view.model.Feedback;
import project.view.model.UserFeedback;
import project.view.util.CustomInterface;
import project.view.util.ProductFilter;
import retrofit2.Call;
import retrofit2.Response;

public class FeedbackManagement extends BasePage {
    private List<UserFeedback> tempList = new ArrayList<>();
    private List<UserFeedback> userFeedbackList = new ArrayList<>();
    private int storeId;
    private FeedbackManagementAdapter feedbackManagementAdapter;
    private ListView listView;
    public View footerView;
    public boolean isLoading;
    private int page;
    public Handler mHandle;
    private ProgressBar loadingBar;
    private ProductFilter filter;
    private Spinner spinner;
    private ImageView icon;
    private TextView noFeedback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_listview);
        tempList = new ArrayList<>();
        // Intent storeId
        storeId = getIntent().getIntExtra("storeId", -1);
        customView();
        findView();
        filter = new ProductFilter();
        mHandle = new MyHandle();
        feedbackManagementAdapter = new FeedbackManagementAdapter(FeedbackManagement.this, R.layout.item_feedback, tempList);

        listView.setAdapter(feedbackManagementAdapter);
        if (storeId != -1) {
            Call<List<UserFeedback>> call = ApiUtils.getAPIService().getAllFeedback(storeId, 0);
            new ManagementFeedback().execute(call);
        } else {
            Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
            return;
        }

        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = li.inflate(R.layout.footer_loading_listview_lazy_loading, null);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int count = tempList.size();
                if (view.getLastVisiblePosition() == totalItemCount - 1 && count == (page * 10) && isLoading == false && (page > 0)) {
                    isLoading = true;
                    Thread thread = new ThreadgetMoreData();
                    thread.start();
                }
            }
        });
    }

    private void customView() {
        CustomInterface.setStatusBarColor(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đánh giá về cửa hàng");
    }

    private void findView() {
        listView = findViewById(R.id.content);
        loadingBar = findViewById(R.id.loadingBar);
        spinner = findViewById(R.id.spinner);
        icon = findViewById(R.id.icon);
        noFeedback = findViewById(R.id.noFeedback);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class ManagementFeedback extends AsyncTask<Call, Void, List<UserFeedback>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<UserFeedback> result) {
            super.onPostExecute(result);

            if (result == null) {

                return;
            }
            if (result.size() == 0) {
                noFeedback.setVisibility(View.VISIBLE);
                return;
            }
            for (UserFeedback uf : result) {
                userFeedbackList.add(uf);
            }
            filter.setRateFilter(FeedbackManagement.this, spinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    tempList.clear();
                    if (adapterView.getItemAtPosition(i).toString().equals(filter.ALL_RATE)) {
                        icon.setImageResource(R.drawable.sort);
                        for (UserFeedback userFeedback : userFeedbackList) {
                            tempList.add(userFeedback);
                        }
                    } else if (adapterView.getItemAtPosition(i).toString().equals(filter.SATISFIED_RATE)) {
                        icon.setImageResource(R.drawable.smile_checked);
                        for (UserFeedback userFeedback : userFeedbackList) {
                            if (userFeedback.getFeedback().getIsSatisfied() == 1) {
                                tempList.add(userFeedback);
                            }
                        }
                    } else {
                        icon.setImageResource(R.drawable.sad_unchecked);
                        for (UserFeedback userFeedback : userFeedbackList) {
                            if (userFeedback.getFeedback().getIsSatisfied() == 0) {
                                tempList.add(userFeedback);
                            }
                        }
                    }
                    feedbackManagementAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }

            });
            feedbackManagementAdapter.notifyDataSetChanged();


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<UserFeedback> doInBackground(Call... calls) {
            try {
                Call<List<UserFeedback>> call = calls[0];
                Response<List<UserFeedback>> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class ThreadgetMoreData extends Thread {
        @Override
        public void run() {
            mHandle.sendEmptyMessage(0);
            //List<Item> addMoreList = getMoreData();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isLoading == true) {
                Message msg = mHandle.obtainMessage(1);
                mHandle.sendMessage(msg);
            }
        }
    }

    public void getMoreData() {
        Log.d("page", String.valueOf(page));
        callAPI(page);
        page++;
    }

    public class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    listView.addFooterView(footerView);
                    break;
                case 1:
                    //adapter.addListItemToAdapter((ArrayList<Item>)msg.obj);
                    listView.removeFooterView(footerView);
                    getMoreData();
                    isLoading = false;
                    break;
                default:
                    break;
            }
        }
    }

    private void callAPI(int page) {
        Call<List<UserFeedback>> call = ApiUtils.getAPIService().getAllFeedback(storeId, page);
        new ManagementFeedback().execute(call);
    }
}
