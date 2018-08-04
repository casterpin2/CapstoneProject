package com.example.chamander.test;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Firebase {

    public static StorageReference getFirebase(){
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        return storageReference.getReferenceFromUrl("gs://sproject-f71c4.appspot.com");
    }
    public static FirebaseDatabase getDatabase(){
        return FirebaseDatabase.getInstance();
    }
}
