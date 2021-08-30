package com.example.mytraining;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForFirebaseOptions {
    DatabaseReference mbase;
    String getReference;

    ForFirebaseOptions() {

    }

    ForFirebaseOptions(String getReference) {
        mbase = FirebaseDatabase.getInstance().getReference(getReference);
        this.getReference = getReference;
    }

    public FirebaseRecyclerOptions<DataModal> Options() {
        FirebaseRecyclerOptions<DataModal> options = new FirebaseRecyclerOptions.Builder<DataModal>()
                .setQuery(mbase, DataModal.class)
                .build();
        return options;
    }

    public void deleData(String userkey) {
        if(userkey==null){
            return;
        }
        mbase.child(userkey).removeValue();
    }


}
