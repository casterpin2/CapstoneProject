package project.view.gui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import project.retrofit.ApiUtils;
import project.view.R;
import project.view.adapter.FeedbackManagementAdapter;
import project.view.model.Product;
import project.view.model.UserFeedback;
import project.view.util.CustomInterface;
import retrofit2.Call;
import retrofit2.Response;

public class FeedbackManagement extends BasePage {
    private List<UserFeedback> list;
    private int storeId;
    private FeedbackManagementAdapter feedbackManagementAdapter;
    private ListView listView;
    public View footerView;
    public boolean isLoading;
    private int page;
    public Handler mHandle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_listview);
        list = new ArrayList<>();
        // Intent storeId
        storeId = 165;
        customView();
        mHandle = new MyHandle();
        feedbackManagementAdapter = new FeedbackManagementAdapter(FeedbackManagement.this, R.layout.item_feedback, list);
        listView = findViewById(R.id.content);
        listView.setAdapter(feedbackManagementAdapter);
        if (storeId != -1){
            Call<List<UserFeedback>> call = ApiUtils.getAPIService().getAllFeedback(storeId , 0);
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
                int count = list.size();
                if (view.getLastVisiblePosition() == totalItemCount - 1 && count == (page * 10) && isLoading == false  && (page > 0)) {
                    isLoading = true;
                    Thread thread = new ThreadgetMoreData();
                    thread.start();
                }
            }
        });
    }

    private void customView(){
        CustomInterface.setStatusBarColor(this);
    }

    public class ManagementFeedback extends AsyncTask<Call,Void,List<UserFeedback>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<UserFeedback> result) {
            super.onPostExecute(result);
            if (result == null){

                return;
            }
            if (result.size() == 0){

                return;
            }
            for (UserFeedback uf : result){
                list.add(uf);
            }
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

    public void getMoreData(){
        Log.d("page",String.valueOf(page));
        callAPI(page);
        page ++;
    }

    public class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0 :
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

    private void callAPI (int page){
        Call<List<UserFeedback>> call = ApiUtils.getAPIService().getAllFeedback(storeId,page);
        new ManagementFeedback().execute(call);
    }
}
