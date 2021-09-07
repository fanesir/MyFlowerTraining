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
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DateMainActivity extends AppCompatActivity {


    DatabaseReference mbase;
    static String userkey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_main);
        RecyclerView recyclerView = findViewById(R.id.daterecyview);



        mbase = FirebaseDatabase.getInstance().getReference("data").child(userkey).child("group");


        FirebaseRecyclerOptions<DataModelchild> options = new FirebaseRecyclerOptions.Builder<DataModelchild>().setQuery(mbase, DataModelchild.class).build();

        AdapterForDateRecyview foreMainAdpter = new AdapterForDateRecyview(options);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foreMainAdpter);
        foreMainAdpter.startListening();

        FloatingActionButton floatingActionButton = findViewById(R.id.addnotefloatingActionButton);
        floatingActionButton.setOnClickListener(v -> {
            Mainactivity.ADDOREDIT = 3;
            Intent intent = new Intent(DateMainActivity.this, FloatingActionButtonAddDataSome.class);
            intent.putExtra("MainactibityINFO", userkey);
            startActivity(intent);
            DateMainActivity.this.finish();
        });

    }

    class AdapterForDateRecyview extends FirebaseRecyclerAdapter
            <DataModelchild, AdapterForDateRecyview.forDateRecyView> {

        public AdapterForDateRecyview(@NonNull FirebaseRecyclerOptions options) {
            super(options);
        }

        protected void onBindViewHolder(@NonNull forDateRecyView holder, int position, @NonNull DataModelchild model) {
            holder.notedate.setText(model.getNotedate());
            holder.imageButton.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(holder.imageButton.getContext(), view);
                popup.getMenuInflater().inflate(R.menu.edit_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.item1:
                            Intent intent = new Intent(DateMainActivity.this, FloatingActionButtonAddDataSome.class);
                            intent.putExtra("DataModelchildINFO", model);
                            Mainactivity.ADDOREDIT = 4;
                            startActivity(intent);
                            DateMainActivity.this.finish();
                            return true;

                        case R.id.item2:
                            ForFirebaseOptions forFirebaseOptions = new ForFirebaseOptions("data");
                            forFirebaseOptions.deleUserData(model.getUserkey() + "", model.getUserdataKey() + "");
                            return true;

                    }


                    return false;
                });
                popup.show();
            });
            holder.notetitle.setText(model.getNotetitle());

            holder.notetitle.setOnClickListener(view -> {

                DateMainActivity.this.finish();
                startActivity(new Intent(DateMainActivity.this, NoteMainActivity.class).putExtra("forNoteData", model));
            });
        }

        @NonNull
        @Override
        public forDateRecyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_recyview_design_layout, parent, false);
            return new forDateRecyView(view);
        }

        class forDateRecyView extends RecyclerView.ViewHolder {
            TextView notetitle, notedate;
            ImageButton imageButton;

            public forDateRecyView(@NonNull View itemView) {
                super(itemView);
                notetitle = itemView.findViewById(R.id.notetitletextview);
                notedate = itemView.findViewById(R.id.datetitletextview);
                imageButton = itemView.findViewById(R.id.dateimageButton);
            }
        }

    }

    public void onBackPressed() {
        startActivity(new Intent(DateMainActivity.this, Mainactivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }

}