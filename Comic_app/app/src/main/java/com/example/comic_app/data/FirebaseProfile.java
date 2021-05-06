package com.example.comic_app.data;


import com.example.comic_app.model.Profile;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirebaseProfile {
    public static FirebaseProfile firebaseProfile;
    private Profile profile;

    private FirebaseProfile(){}

    public Profile readProfile(DocumentReference dbRef, final Profile.ProfileCallback callback){
        OnSuccessListener valueEventListener = new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Profile p = documentSnapshot.toObject(Profile.class);
                callback.onCallback(p);
            }
        };
        dbRef.get().addOnSuccessListener(valueEventListener);
        return profile;
    }

}
