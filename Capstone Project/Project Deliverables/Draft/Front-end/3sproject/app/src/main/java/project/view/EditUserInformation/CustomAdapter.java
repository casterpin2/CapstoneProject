package project.view.EditUserInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import project.view.R;

public class CustomAdapter extends BaseAdapter {

    Context context;
    int spinnerImage[];
    String[] genderName ;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] spinnerImage, String[] genderName) {
        this.context = applicationContext;
        this.spinnerImage = spinnerImage;
        this.genderName = genderName;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return spinnerImage.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(spinnerImage[i]);
        names.setText(genderName[i]);
        return view;
    }
}
