package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Mainactivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton floatingActionButton = findViewById(R.id.addDataActionButton);

        RecyclerView recyclerView = findViewById(R.id.recyview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ForFirebaseOptions ForFirebaseOptions = new ForFirebaseOptions("data");
        AdapterForMaimActivity foreMainAdpter = new AdapterForMaimActivity(ForFirebaseOptions.Options());
        recyclerView.setAdapter(foreMainAdpter);
        foreMainAdpter.startListening();

        floatingActionButton.setOnClickListener(v -> Mainactivity.this.startActivity(new Intent(Mainactivity.this, FloatingActionButtonAddDataSome.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)));
        authority();

    }

    public class AdapterForMaimActivity extends FirebaseRecyclerAdapter
            <DataModal, AdapterForMaimActivity.personsRecyclerViewViewholder> {

        public AdapterForMaimActivity(FirebaseRecyclerOptions<DataModal> options) {
            super(options);

        }

        @Override
        protected void onBindViewHolder(@NonNull personsRecyclerViewViewholder holder,
                                        @SuppressLint("RecyclerView") int position, @NonNull DataModal model) {

            holder.firstname.setText(model.getTitle());
            holder.maindate.setText(model.getdate());

            Picasso.get().load(model.getimgurl()).into(holder.imageView);

            DataModal dataModal = model;

            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Mainactivity.this, EditMainActivity.class);
                    intent.putExtra("MainactibityINFO", dataModal);
                    intent.putExtra("DataModalIndex", position);
                    startActivity(intent);
                   // finish();
                }
            });

        }


        public personsRecyclerViewViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_design, parent, false);
            return new personsRecyclerViewViewholder(view);
        }


        class personsRecyclerViewViewholder extends RecyclerView.ViewHolder {
            TextView firstname, maindate;
            ImageView imageView;
            ImageButton imageButton;

            public personsRecyclerViewViewholder(@NonNull View itemView) {
                super(itemView);
                firstname = itemView.findViewById(R.id.textView);
                imageView = itemView.findViewById(R.id.FlowerimageView);
                imageButton = itemView.findViewById(R.id.editimageView);
                maindate = itemView.findViewById(R.id.date);
            }

        }


    }

    public void authority() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)//取得權限

        {//確認使否有授權
            if (ContextCompat.checkSelfPermission(Mainactivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {//跳出來給選擇
                ActivityCompat.requestPermissions(Mainactivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            } else {

            }
        }
    }

}


