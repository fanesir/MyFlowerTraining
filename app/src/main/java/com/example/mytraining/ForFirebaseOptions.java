package com.example.mytraining;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForFirebaseOptions {
    DatabaseReference mbase;

    ForFirebaseOptions() {

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

    DatabaseReference referenceDatabase(String datafrom, String DataModalIndex) {
        return FirebaseDatabase.getInstance().getReference().child(datafrom).child(DataModalIndex);
    }


}
