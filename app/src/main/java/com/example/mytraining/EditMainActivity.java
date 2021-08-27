package com.example.mytraining;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class EditMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_main);

        DataModal dataModal = (DataModal) getIntent().getSerializableExtra("MainactibityINFO");//DataModalIndex
        int DataModalIndex = (int) getIntent().getSerializableExtra("DataModalIndex");

        ImageView imageView = findViewById(R.id.EditimageView);
        EditText editText = findViewById(R.id.editTextTextPersonName);
        Button editdatebutton = findViewById(R.id.foreditbutton);


        Picasso.get().load(dataModal.getimgurl()).into(imageView);
        editText.setText(dataModal.getTitle()+"-"+DataModalIndex+"");
        editdatebutton.setText(dataModal.getdate());




    }
}