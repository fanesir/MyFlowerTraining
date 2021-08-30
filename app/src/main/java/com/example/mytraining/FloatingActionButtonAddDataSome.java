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
import android.os.Build;
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

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class FloatingActionButtonAddDataSome extends AppCompatActivity {
    ImageView EditimageView;
    String datetext;
    Uri uri;
    TimePickerDialog timePickerDialog;
    String DataModalIndex = "";
    String EditImageURL = "";

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

        if (Mainactivity.ADDOREDIT == 1) {
            clockimageButton.setText("增加時間");
            EdedText.setText("");
        } else if (Mainactivity.ADDOREDIT == 0) {

            DataModal dataModal = (DataModal) getIntent().getSerializableExtra("MainactibityINFO");
            DataModalIndex = (String) getIntent().getSerializableExtra("DataModalIndexKey");
            EditImageURL = dataModal.getimgurl();

            EdedText.setText(dataModal.getTitle());
            clockimageButton.setText(dataModal.getdate());
            Picasso.get().load(dataModal.getimgurl()).into(EditimageView);

        }

        timePickerDialog = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setCallBack((timePickerView, millseconds) -> {
                    datetext = getDateToString(millseconds);
                    clockimageButton.setText(datetext);
                })
                .build();


        EditimageView.setOnClickListener(v -> {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.OFF).setAspectRatio(1, 1).setMinCropResultSize(600, 600)
                    .setRequestedSize(1000, 1000)//最後幹上去mageview的圖片畫素
                    .setCropShape(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P ? CropImageView.CropShape.RECTANGLE : CropImageView.CropShape.OVAL)
                    .start(FloatingActionButtonAddDataSome.this);
        });

        buttonfish.setOnClickListener(v -> {

            if (uri != null) {
                StorageReference storageReference = referenceStorage.child(System.currentTimeMillis() + "." + FloatingActionButtonAddDataSome.this.getFileExtension(uri));//幹上去
                storageReference.putFile(uri)//到剛上傳完的資料
                        .addOnSuccessListener(taskSnapshot -> {

                            Handler handler = new Handler();
                            handler.postDelayed(() -> mProgressBar.setProgress(0), 500);
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();//圖片剛上傳的資料在這

                            result.addOnSuccessListener(uri -> {
                                String imageUrll = uri.toString();
                                String key=referenceDatabase.push().getKey();

                                if (Mainactivity.ADDOREDIT == 1) {//新增新資料
                                    DataModal upload = new DataModal(EdedText.getText().toString().trim(),
                                            imageUrll, clockimageButton.getText().toString(),key);


                                    referenceDatabase.child(new String(key)).setValue(upload);

                                } else if (Mainactivity.ADDOREDIT == 0) {
                                    DataModal upload = new DataModal(EdedText.getText().toString().trim(),
                                            imageUrll, clockimageButton.getText().toString(),key);
                                    referenceDatabase.child(DataModalIndex).setValue(upload);
                                }

                            });

                            Toast.makeText(FloatingActionButtonAddDataSome.this, "成功上傳", Toast.LENGTH_LONG).show();
                            FloatingActionButtonAddDataSome.this.startActivity(new Intent(FloatingActionButtonAddDataSome.this, Mainactivity.class));
                            FloatingActionButtonAddDataSome.this.finish();
                        }).
                        addOnFailureListener(e -> i("FireBaseErrowMessage", e.getMessage()))
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            //100.0*3932160/7842896=50.14 很像在算準確度  錯誤張數/總張數＝總準確度%
                            mProgressBar.setProgress((int) progress);

                        });
            } else if (uri == null) {
                DataModal upload = new DataModal(EdedText.getText().toString().trim(), EditImageURL, clockimageButton.getText().toString());
                referenceDatabase.child(DataModalIndex).setValue(upload);
                Toast.makeText(FloatingActionButtonAddDataSome.this, "成功上傳", Toast.LENGTH_LONG).show();
                startActivity(new Intent(FloatingActionButtonAddDataSome.this, Mainactivity.class));
                finish();
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


    public String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(time);
        return sf.format(d).substring(0, 10);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropImage.ActivityResult result = CropImage.getActivityResult(data);

        if (resultCode == RESULT_OK) {
            uri = result.getUri();
            Picasso.get().load(uri).into(EditimageView);
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(FloatingActionButtonAddDataSome.this, Mainactivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        FloatingActionButtonAddDataSome.this.finish();
    }

}