package project.view.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import project.firebase.Firebase;
import project.view.R;

/**
 * Created by Tung
 */

public class SliderImageViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private StorageReference storageReference = Firebase.getFirebase();
    private Integer [] images = {R.drawable.slider1, R.drawable.cover, R.drawable.cover};

    public SliderImageViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_slider_home, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);
        Glide.with(context /* context */)
                .load(images[position])
                .into(imageView);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(position == 0){
//                    Toast.makeText(context, "Slide 1 Clicked", Toast.LENGTH_SHORT).show();
//                } else if(position == 1){
//                    Toast.makeText(context, "Slide 2 Clicked", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Slide 3 Clicked", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
