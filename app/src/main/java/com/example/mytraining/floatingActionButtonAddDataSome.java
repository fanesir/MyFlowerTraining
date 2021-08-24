package com.example.mytraining;

import static android.util.Log.i;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class floatingActionButtonAddDataSome extends AppCompatActivity {
    TimePickerDialog timePickerDialog;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String text;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_data_some);
        DataModal dataModal = (DataModal) getIntent().getSerializableExtra("MainactibityINFO");

        ImageView EditimageView = findViewById(R.id.editimageView);

        EditText EdedText = findViewById(R.id.editTitleEdtext);
        Button buttonfish = findViewById(R.id.buttonfish);
        Button clockimageButton  =findViewById(R.id.clockimageButton);
        clockimageButton.setBackgroundColor(Color.WHITE);



        reference = FirebaseDatabase.getInstance().getReference().child("data");

//        Picasso.get().load(dataModal.getimgurl()).into(EditimageView);
//        EdedText.setText(dataModal.getTitle());


        clockimageButton.setOnClickListener(v -> timePickerDialog.show(floatingActionButtonAddDataSome.this.getSupportFragmentManager(), "year_month_day"));

        timePickerDialog = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setCallBack((timePickerView, millseconds) -> {
                    text = getDateToString(millseconds);
                    clockimageButton.setText(text);
                })
                .build();

        buttonfish.setOnClickListener(v -> {
            String title = EdedText.getText().toString();
            String date = text;

            String key = reference.push().getKey();
            reference.child(key).child("Title").setValue(title);
            reference.child(key).child("date").setValue(date);

            Intent intent = new Intent(floatingActionButtonAddDataSome.this,Mainactivity.class);
            startActivity(intent);


        });


    }

    public String getDateToString(long time) {
        Log.i("time", time + "");
        Date d = new Date(time);
        return sf.format(d).substring(0, 10);
    }
}