package project.view.gui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import project.view.R;
import project.view.adapter.FeedbackManagementAdapter;
import project.view.model.Feedback;
import project.view.util.CustomInterface;

public class FeedbackManagement extends BasePage {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_listview);

        customView();

        Feedback feedback = new Feedback(1, 1, "Đạt chỉ hơi ngu chút thôi", 1);
        Feedback feedback1 = new Feedback(1, 1, "Hayyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy yyyyyyyyyyyyyyyyyyyyyyyyyyyyyy yyyyyyyyyyyyyyyyyyyyyyyyyyy", 2);
        List<Feedback> feedbacks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            feedbacks.add(feedback);
            feedbacks.add(feedback1);
        }
        Toast.makeText(getBaseContext(), feedbacks.size() + "", Toast.LENGTH_LONG).show();

        FeedbackManagementAdapter feedbackManagementAdapter = new FeedbackManagementAdapter(FeedbackManagement.this, R.layout.item_feedback, feedbacks);
        ListView listView = findViewById(R.id.content);

        listView.setAdapter(feedbackManagementAdapter);
    }

    private void customView(){
        CustomInterface.setStatusBarColor(this);
    }
}
