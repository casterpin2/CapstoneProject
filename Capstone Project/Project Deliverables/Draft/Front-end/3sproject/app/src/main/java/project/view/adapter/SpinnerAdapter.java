package project.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import project.view.R;

public class SpinnerAdapter extends BaseAdapter {
    private List<String> categories ;
    private Activity activity;
    private LayoutInflater inflater;

    public SpinnerAdapter(List<String> categories, Activity activity, LayoutInflater inflater){
        this.categories = categories;
        this.activity = activity;
        this.inflater = inflater;

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = inflater.inflate(R.layout.custom_spinner,null);
        }
        return null;
    }
}
