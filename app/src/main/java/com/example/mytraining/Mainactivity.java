package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.squareup.picasso.Picasso;

import java.io.Serializable;

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

        floatingActionButton.setOnClickListener(v -> Mainactivity.this.startActivity(new Intent(Mainactivity.this, floatingActionButtonAddDataSome.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)));


    }

    public class AdapterForMaimActivity extends FirebaseRecyclerAdapter
            <DataModal, AdapterForMaimActivity.personsRecyclerViewViewholder> {

        public AdapterForMaimActivity(FirebaseRecyclerOptions<DataModal> options) {
            super(options);

        }

        @Override
        protected void onBindViewHolder(@NonNull personsRecyclerViewViewholder holder, @SuppressLint("RecyclerView") int position, @NonNull DataModal model) {

            holder.firstname.setText(model.getTitle());
            holder.maindate.setText(model.getdate());
            Picasso.get().load(model.getimgurl()).into(holder.imageView);
            DataModal dataModal = model;

            holder.imageButton.setOnClickListener(v -> {
                Intent intent = new Intent(Mainactivity.this,floatingActionButtonAddDataSome.class);
                intent.putExtra("MainactibityINFO",dataModal);//想把的内存中的对象状态保存到一个文件中或者数据库中时候
                startActivity(intent);

            });

        }


        public personsRecyclerViewViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_design, parent, false);
            return new personsRecyclerViewViewholder(view);
        }


        class personsRecyclerViewViewholder extends RecyclerView.ViewHolder {
            TextView firstname,maindate;
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

}


