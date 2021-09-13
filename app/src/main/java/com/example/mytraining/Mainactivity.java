package com.example.mytraining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class Mainactivity extends AppCompatActivity {

    static int ADDOREDIT;

    ForFirebaseOptions<DataModal> forFirebaseOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        forFirebaseOptions = new ForFirebaseOptions(LoginActivity.USER_ID);//"data"LoginActivity.USER_ID+
        AdapterForMaimActivity foreMainAdpter = new AdapterForMaimActivity(forFirebaseOptions.Options());
        recyclerView.setAdapter(foreMainAdpter);
        foreMainAdpter.startListening();


        FloatingActionButton floatingActionButton = findViewById(R.id.addDataActionButton);
        floatingActionButton.setOnClickListener(v -> {
                    ADDOREDIT = 0;
                    Mainactivity.this.startActivity(new Intent(Mainactivity.this, FloatingActionButtonAddDataSome.class));
                    finish();
                }
        );

        ImageView imageView = findViewById(R.id.sideBarButton);
        imageView.setOnClickListener(view -> {
            DrawerLayout drawer = findViewById(R.id.drawer_layoutt);
            drawer.openDrawer(GravityCompat.START);
        });

        NavigationView navigationView = findViewById(R.id.navid);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        Menu menu = navigationView.getMenu();
        MenuItem nav_camara = menu.findItem(R.id.useremail);
        nav_camara.setTitle(googleSignInAccount.getEmail() + "");

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.itemlogout) {
                FirebaseAuth.getInstance().signOut();
                Mainactivity.this.startActivity(new Intent(Mainactivity.this, LoginActivity.class));
                finish();
            }
            return false;
        });
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

            holder.imageButton.setOnClickListener(v -> {

                PopupMenu popup = new PopupMenu(holder.imageButton.getContext(), v);
                popup.getMenuInflater().inflate(R.menu.edit_menu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.item1:
                            Intent intent = new Intent(Mainactivity.this, FloatingActionButtonAddDataSome.class);
                            intent.putExtra("MainactibityINFO", model);
                            ADDOREDIT = 1;
                            Mainactivity.this.finish();
                            startActivity(intent);
                            return true;

                        case R.id.item2:
                            forFirebaseOptions.deleUser(model.getuserKey() + "");
                            return true;

                    }


                    return false;
                });
                popup.show();

            });

            holder.imageView.setOnClickListener(v -> {
                Intent intent = new Intent(Mainactivity.this, DateMainActivity.class);
                DateMainActivity.userkey = model.getuserKey();
                finish();
                startActivity(intent);
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
                imageView = itemView.findViewById(R.id.flowerimageView);
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

    public void onBackPressed() {
        System.exit(0);

    }




}


