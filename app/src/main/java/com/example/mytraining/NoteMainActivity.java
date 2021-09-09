package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class NoteMainActivity extends AppCompatActivity {

    DatabaseReference referenceDatabase;
    DataModelchild dataModelchild;
    EditText notetitle, note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_main);

       // ImageView imageButtonNote = findViewById(R.id.imageButtonNote);
//        imageButtonNote.setVisibility(View.GONE);
        //FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButtonAddImage);

        notetitle = findViewById(R.id.editTextTitle);
        note = findViewById(R.id.editTextNote);


        dataModelchild = (DataModelchild) getIntent().getSerializableExtra("forNoteData");

        referenceDatabase = FirebaseDatabase.getInstance()
                .getReference().child("data").child(dataModelchild.getUserkey()).child("group")
                .child(dataModelchild.getUserdataKey()).child("note");


        referenceDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {

                    notetitle.setText(snapshot.child("notetitle").getValue().toString());
                    note.setText(snapshot.child("note").getValue().toString());

                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            Boolean aBoolean = false;
//
//            public void onClick(View view) {
//
//                if (aBoolean == false) {
//                    imageButtonNote.setVisibility(View.VISIBLE);
//                    aBoolean = true;
//
//                } else if (aBoolean == true) {
//                    imageButtonNote.setVisibility(View.GONE);
//                    aBoolean = false;
//                }
//
//            }
//        });


    }


    public void onBackPressed() {

        referenceDatabase.child("notetitle").setValue(notetitle.getText().toString());
        referenceDatabase.child("note").setValue(note.getText().toString());

        startActivity(new Intent(NoteMainActivity.this, DateMainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }


}