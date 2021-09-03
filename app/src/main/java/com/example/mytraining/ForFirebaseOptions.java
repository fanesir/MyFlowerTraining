package com.example.mytraining;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForFirebaseOptions<T> {

    DatabaseReference mbase;

    ForFirebaseOptions() {

    }

    ForFirebaseOptions(String getReference, String key, String grop) {
        mbase = FirebaseDatabase.getInstance().getReference(getReference).child(key).child(grop);

    }

    ForFirebaseOptions(String getReference) {
        mbase = FirebaseDatabase.getInstance().getReference(getReference);

    }

    public FirebaseRecyclerOptions<DataModal> Options() {
        FirebaseRecyclerOptions<DataModal> options = new FirebaseRecyclerOptions.Builder<DataModal>()
                .setQuery(mbase, DataModal.class)
                .build();
        return options;
    }

    public void deleData(String userkey) {
        if (userkey == null) {
            return;
        }
        mbase.child(userkey).removeValue();
    }


}
