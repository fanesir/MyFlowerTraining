package com.example.mytraining;

import static android.util.Log.i;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
    String UserDatakey="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_add_data_some);

        EditimageView = findViewById(R.id.addimageView);

        EditText EdedText = findViewById(R.id.addTitleEdtext);
        Button buttonfish = findViewById(R.id.buttonfish);
        Button clockimageButton = findViewById(R.id.clockimageButton);
        ProgressBar mProgressBar = findViewById(R.id.progressBar);

        StorageReference referenceStorage = FirebaseStorage.getInstance().getReference().child(LoginActivity.USER_ID);
        DatabaseReference referenceDatabase = FirebaseDatabase.getInstance().getReference().child(LoginActivity.USER_ID);


        clockimageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show(FloatingActionButtonAddDataSome.this.getSupportFragmentManager(), "year_month_day");
            }
        });

        if (Mainactivity.ADDOREDIT == 0) {
            clockimageButton.setText("增加時間");
            EdedText.setText("");
        } else if (Mainactivity.ADDOREDIT == 1) {

            DataModal dataModal = (DataModal) getIntent().getSerializableExtra("MainactibityINFO");
            DataModalIndex = dataModal.getuserKey();
            EditImageURL = dataModal.getimgurl();

            EdedText.setText(dataModal.getTitle());
            clockimageButton.setText(dataModal.getdate());
            Picasso.get().load(dataModal.getimgurl()).into(EditimageView);

        } else if (Mainactivity.ADDOREDIT == 3) {//檔案新增
            String dataModal = (String) getIntent().getSerializableExtra("MainactibityINFO");
            DataModalIndex = dataModal;
            EditimageView.setVisibility(View.GONE);
        } else if (Mainactivity.ADDOREDIT == 4) {//檔案編輯

            DataModelchild dataModelchild = (DataModelchild) getIntent().getSerializableExtra("DataModelchildINFO");
            DataModalIndex = dataModelchild.getUserkey();
            UserDatakey = dataModelchild.getUserdataKey();

            EditimageView.setVisibility(View.GONE);

            EdedText.setText(dataModelchild.getNotetitle());
            clockimageButton.setText(dataModelchild.getNotedate());
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
                    .setGuidelines(CropImageView.Guidelines.OFF).setAspectRatio(1, 1).setMinCropResultSize(129, 129)
                    .setRequestedSize(600, 600)//最後幹上去mageview的圖片畫素
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


                                if (Mainactivity.ADDOREDIT == 0) {//新增新資料
                                    upDataNew(referenceDatabase, referenceDatabase.push().getKey() + "", EdedText.getText().toString(), imageUrll, clockimageButton.getText().toString());

                                } else if (Mainactivity.ADDOREDIT == 1) {//編輯資料
                                    editData(referenceDatabase, DataModalIndex, EdedText.getText().toString(), imageUrll, clockimageButton.getText().toString());
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
            } else if (Mainactivity.ADDOREDIT == 1 & uri == null) {
                onlyEditTitleAndDate(referenceDatabase, DataModalIndex, EdedText.getText().toString(), clockimageButton.getText().toString());

            } else if (Mainactivity.ADDOREDIT == 3 & uri == null) {
                addThisUserDataFile(referenceDatabase, EdedText.getText().toString(), clockimageButton.getText().toString());
            } else if (Mainactivity.ADDOREDIT == 4 & uri == null) {
                editThisUserDataFile(referenceDatabase,DataModalIndex,UserDatakey, EdedText.getText().toString(), clockimageButton.getText().toString());
            } else {
                Toast.makeText(FloatingActionButtonAddDataSome.this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void upDataNew(DatabaseReference referenceDatabase, String userKey
            , String editText, String imageUrll, String clockimageButton) {

        DataModal upload = new DataModal(editText,
                imageUrll, clockimageButton, userKey);
        referenceDatabase.child(new String(userKey)).setValue(upload);
    }

    protected void editData(DatabaseReference referenceDatabase, String userKey
            , String editText, String imageUrll, String clockimageButton) {
        referenceDatabase.child(userKey).child("title").setValue(editText);
        referenceDatabase.child(userKey).child("date").setValue(clockimageButton);
        referenceDatabase.child(userKey).child("imgurl").setValue(imageUrll);
    }

    protected void onlyEditTitleAndDate(DatabaseReference referenceDatabase, String userKey, String editText, String clockimageButton) {
        referenceDatabase.child(userKey).child("title").setValue(editText);
        referenceDatabase.child(userKey).child("date").setValue(clockimageButton);
        startActivity(new Intent(FloatingActionButtonAddDataSome.this, Mainactivity.class));
        Toast.makeText(FloatingActionButtonAddDataSome.this, "成功上傳", Toast.LENGTH_LONG).show();
        finish();
    }

    protected void addThisUserDataFile(DatabaseReference referenceDatabase, String titleEditext, String clocktext) {
        String key = referenceDatabase.push().getKey();
        referenceDatabase.child(DataModalIndex).child("group").child(key).child("notetitle").setValue(titleEditext);
        referenceDatabase.child(DataModalIndex).child("group").child(key).child("notedate").setValue(clocktext);
        referenceDatabase.child(DataModalIndex).child("group").child(key).child("userkey").setValue(DataModalIndex);
        referenceDatabase.child(DataModalIndex).child("group").child(key).child("userdataKey").setValue(key);



        Toast.makeText(FloatingActionButtonAddDataSome.this, "成功上傳", Toast.LENGTH_LONG).show();
        startActivity(new Intent(FloatingActionButtonAddDataSome.this, DateMainActivity.class).putExtra("userkey", DataModalIndex));
        finish();
    }

    protected void editThisUserDataFile(DatabaseReference referenceDatabase,String DataModalIndex,String UserDatakey, String titleEditext, String clocktext) {

        referenceDatabase.child(DataModalIndex).child("group").child(UserDatakey).child("notetitle").setValue(titleEditext);
        referenceDatabase.child(DataModalIndex).child("group").child(UserDatakey).child("notedate").setValue(clocktext);
        referenceDatabase.child(DataModalIndex).child("group").child(UserDatakey).child("userdataKey").setValue(UserDatakey);
        referenceDatabase.child(DataModalIndex).child("group").child(UserDatakey).child("userkey").setValue(DataModalIndex);


        Toast.makeText(FloatingActionButtonAddDataSome.this, "成功上傳", Toast.LENGTH_LONG).show();
        DateMainActivity.userkey=DataModalIndex;
        startActivity(new Intent(FloatingActionButtonAddDataSome.this, DateMainActivity.class));
        finish();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    public String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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