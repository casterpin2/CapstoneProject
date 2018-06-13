package project.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import project.view.AddProductToStore.Item;
import project.view.AddProductToStore.SearchProductAddToStore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Item> getData = SearchProductAddToStore.addedProductList;
        TextView text = findViewById(R.id.text);
        Button button = findViewById(R.id.button);
        if(getData.isEmpty()){

        } else {
            Toast.makeText(getApplicationContext(), getData.get(0).getProductName() + "", Toast.LENGTH_SHORT).show();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData.removeAll(getData);
                Intent intent = new Intent(MainActivity.this, SearchProductAddToStore.class);
                startActivity(intent);
            }
        });
    }
}
