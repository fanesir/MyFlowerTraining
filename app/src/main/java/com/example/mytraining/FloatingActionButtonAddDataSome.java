package com.example.mytraining;

import static android.util.Log.i;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
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
import android.view.View;
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
    ImageView EditimageView;
    String datetext;
    Uri uri;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_data_some);




        EditimageView = findViewById(R.id.addimageView);

        EditText EdedText = findViewById(R.id.addTitleEdtext);
        Button buttonfish = findViewById(R.id.buttonfish);
        Button clockimageButton = findViewById(R.id.clockimageButton);
        ProgressBar mProgressBar = findViewById(R.id.progressBar);

        StorageReference referenceStorage = FirebaseStorage.getInstance().getReference().child("data");
        DatabaseReference referenceDatabase = FirebaseDatabase.getInstance().getReference().child("data");


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
            FloatingActionButtonAddDataSome.this.startActivityForResult(intent, 1000);

        });

        buttonfish.setOnClickListener(v -> {

            if (uri != null) {
                StorageReference storageReference = referenceStorage.child(System.currentTimeMillis() + "." +
                        FloatingActionButtonAddDataSome.this.getFileExtension(uri));
                storageReference.putFile(uri)
                        .addOnSuccessListener(taskSnapshot -> {
                            Handler handler = new Handler();
                            handler.postDelayed(() -> mProgressBar.setProgress(0), 500);
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();//圖片剛上傳的資料在這

                            result.addOnSuccessListener(uri -> {
                                String imageUrll = uri.toString();
                                DataModal upload = new DataModal(EdedText.getText().toString().trim(),
                                        imageUrll, datetext);
                                referenceDatabase.child(new String(referenceDatabase.push().getKey())).setValue(upload);
                            });

                            Toast.makeText(FloatingActionButtonAddDataSome.this, "成功上傳", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(FloatingActionButtonAddDataSome.this, Mainactivity.class));
                            finish();
                        }).
                        addOnFailureListener(e -> Log.i("FireBaseErrowMessage", e.getMessage()))
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            //100.0*3932160/7842896=50.14 很像在算準確度  錯誤張數/總張數＝總準確度%
                            mProgressBar.setProgress((int) progress);

                        });
            } else {
                Toast.makeText(FloatingActionButtonAddDataSome.this, "No file selected", Toast.LENGTH_SHORT).show();
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

        if (requestCode == 1000 && resultCode == RESULT_OK & data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(EditimageView);
        }
    }

    public String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(time);
        return sf.format(d).substring(0, 10);
    }
}