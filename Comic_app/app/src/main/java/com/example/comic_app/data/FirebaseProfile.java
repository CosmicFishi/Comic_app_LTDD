//package com.example.comic_app.data;
//
//
//import com.example.comic_app.model.User;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//
//public class FirebaseProfile {
//    public static FirebaseProfile firebaseProfile;
//    private User profile;
//
//    private FirebaseProfile(){}
//
//    public User readProfile(DocumentReference dbRef, final User.ProfileCallback callback){
//        OnSuccessListener valueEventListener = new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                User p = documentSnapshot.toObject(User.class);
//                callback.onCallback(p);
//            }
//        };
//        dbRef.get().addOnSuccessListener(valueEventListener);
//        return profile;
//    }
//
//}
