package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DateMainActivity extends AppCompatActivity {

    DataModal dataModal;
    DatabaseReference mbase;
    String userkey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_main);
        RecyclerView recyclerView = findViewById(R.id.daterecyview);

        try {
            dataModal = (DataModal) getIntent().getSerializableExtra("MainactibityINFO");
            mbase = FirebaseDatabase.getInstance().getReference("data").child(dataModal.getuserKey()).child("group");
            userkey=dataModal.getuserKey();
        }catch (NullPointerException n){
            String userKey = (String) getIntent().getSerializableExtra("userkey");
            mbase = FirebaseDatabase.getInstance().getReference("data").child(userKey).child("group");
            userkey=userKey;
        }


        FirebaseRecyclerOptions<DataModelchild> options = new FirebaseRecyclerOptions.Builder<DataModelchild>().setQuery(mbase, DataModelchild.class).build();

        AdapterForDateRecyview foreMainAdpter = new AdapterForDateRecyview(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foreMainAdpter);
        foreMainAdpter.startListening();

        FloatingActionButton floatingActionButton = findViewById(R.id.addnotefloatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            Mainactivity.ADDOREDIT = 3;
            Intent intent = new Intent(DateMainActivity.this,FloatingActionButtonAddDataSome.class);
            intent.putExtra("MainactibityINFO", userkey);
            startActivity(intent);
        });

    }

    class AdapterForDateRecyview extends FirebaseRecyclerAdapter
            <DataModelchild, AdapterForDateRecyview.forDateRecyView> {

        public AdapterForDateRecyview(@NonNull FirebaseRecyclerOptions options) {
            super(options);
        }

        protected void onBindViewHolder(@NonNull forDateRecyView holder, int position, @NonNull DataModelchild model) {
            holder.notetitle.setText(model.getNotetitle());
            holder.notedate.setText(model.getNotedate());
        }

        @NonNull
        @Override
        public forDateRecyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_recyview_design_layout, parent, false);
            return new forDateRecyView(view);
        }

        class forDateRecyView extends RecyclerView.ViewHolder {
            TextView notetitle, notedate;

            public forDateRecyView(@NonNull View itemView) {
                super(itemView);
                notetitle = itemView.findViewById(R.id.notetitletextview);
                notedate = itemView.findViewById(R.id.datetitletextview);
            }
        }

    }


}