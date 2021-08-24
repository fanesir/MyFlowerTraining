package com.example.mytraining;

import static android.util.Log.i;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FloatingActionButtonAddDataSome extends AppCompatActivity {

    TimePickerDialog timePickerDialog;
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ImageView EditimageView;
    String datetext, imageUrl;
    ;
    DatabaseReference referenceDatabase;
    private static final int IMAGE_PICK_CODE = 1000;
    Uri uri;
    private StorageReference referenceStorage;
    private StorageTask mUploadTask;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_data_some);

        //Picasso.get().load(dataModal.getimgurl()).into(EditimageView);
        //EdedText.setText(dataModal.getTitle());

        //DataModal dataModal = (DataModal) getIntent().getSerializableExtra("MainactibityINFO");
        EditimageView = findViewById(R.id.editimageView);

        EditText EdedText = findViewById(R.id.editTitleEdtext);
        Button buttonfish = findViewById(R.id.buttonfish);
        Button clockimageButton = findViewById(R.id.clockimageButton);
        mProgressBar = findViewById(R.id.progressBar);
        clockimageButton.setBackgroundColor(Color.WHITE);

        referenceStorage = FirebaseStorage.getInstance().getReference().child("data");
        referenceDatabase = FirebaseDatabase.getInstance().getReference().child("data");


        clockimageButton.setOnClickListener(v -> timePickerDialog.show(FloatingActionButtonAddDataSome.this.getSupportFragmentManager(), "year_month_day"));

        timePickerDialog = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setCallBack((timePickerView, millseconds) -> {
                    datetext = getDateToString(millseconds);
                    clockimageButton.setText(datetext);
                })
                .build();


        EditimageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            FloatingActionButtonAddDataSome.this.startActivityForResult(intent, IMAGE_PICK_CODE);

        });

        buttonfish.setOnClickListener(v -> {

            if (uri != null) {
                StorageReference storageReference = referenceStorage.child(System.currentTimeMillis() + "." +
                        getFileExtension(uri));

                storageReference.putFile(uri)
                        .addOnSuccessListener(taskSnapshot -> {

                            Task<Uri> resuri = taskSnapshot.getStorage().getDownloadUrl();
                            resuri.addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrl = uri.toString();
                                    if (EdedText.getText().toString().trim() == null || imageUrl == null || datetext == null) {
                                        Log.i("image", imageUrl + "");
                                        Toast.makeText(FloatingActionButtonAddDataSome.this, "有欄位空白", Toast.LENGTH_LONG).show();
                                        return;
                                    } else {
                                        DataModal upload = new DataModal(EdedText.getText().toString().trim(), imageUrl, datetext);
                                        String uploadId = referenceDatabase.push().getKey();
                                        referenceDatabase.child(uploadId).setValue(upload);
                                        Toast.makeText(FloatingActionButtonAddDataSome.this, "成功上傳", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(FloatingActionButtonAddDataSome.this, Mainactivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });

                        });
            }


        });


    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK & data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(EditimageView);
        }
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d).substring(0, 10);
    }
}