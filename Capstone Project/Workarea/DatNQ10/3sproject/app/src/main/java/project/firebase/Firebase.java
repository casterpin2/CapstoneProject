package project.firebase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Firebase {
    public static StorageReference getFirebase(){
        FirebaseStorage storageReference = FirebaseStorage.getInstance();
        return storageReference.getReferenceFromUrl("gs://sproject-f71c4.appspot.com");
    }
}
