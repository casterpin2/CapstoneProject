package project.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import project.view.R;
import project.view.model.UserFeedback;
import project.view.util.Formater;

public class FeedbackManagementAdapter extends ArrayAdapter<UserFeedback> {

    private Context context;
    private List<UserFeedback> feedbacks;

    public FeedbackManagementAdapter(@NonNull Context context, int resource, @NonNull List<UserFeedback> feedbacks) {
        super(context, resource, feedbacks);
        this.context = context;
        this.feedbacks = feedbacks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FeedbackManagementAdapter.MyViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_feedback, parent, false);
            viewHolder = new FeedbackManagementAdapter.MyViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FeedbackManagementAdapter.MyViewHolder)convertView.getTag();
        }

        UserFeedback userFeedback = feedbacks.get(position);

        viewHolder.name.setText("Đánh giá bởi "+userFeedback.getUser().getDisplayName());
        if(userFeedback.getFeedback().getIsSatisfied() == 1){
            viewHolder.imgSatisfied.setImageResource(R.drawable.smile_checked);
        }else {
            viewHolder.imgSatisfied.setImageResource(R.drawable.sad_unchecked);
        }
        viewHolder.contentFeedback.setText(userFeedback.getFeedback().getContent());
        viewHolder.feedbackDate.setText(userFeedback.getFeedback().getRegisterLog());
        return convertView;
    }

    public static class MyViewHolder {
        public TextView name, contentFeedback,feedbackDate;
        public ImageView imgSatisfied;

        public MyViewHolder(View view) {
            name =  view.findViewById(R.id.tvName);
            contentFeedback = view.findViewById(R.id.tvContent);
            imgSatisfied = view.findViewById(R.id.imgSatisfied);
            feedbackDate = view.findViewById(R.id.tvFeedbackDate);
        }
    }
}
