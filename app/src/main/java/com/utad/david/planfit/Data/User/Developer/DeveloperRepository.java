package com.utad.david.planfit.Data.User.Developer;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utad.david.planfit.Model.Developer.Developer;
import com.utad.david.planfit.Utils.Constants;

public class DeveloperRepository {

    private static DeveloperRepository instance = new DeveloperRepository();

    private FirebaseFirestore firebaseFirestore;
    private GetDeveloper getDeveloper;

    private DeveloperRepository () { this.firebaseFirestore = FirebaseFirestore.getInstance(); }

    public static DeveloperRepository getInstance() {
        if (instance == null){
            synchronized (DeveloperRepository.class){
                if (instance == null) {
                    instance = new DeveloperRepository();
                }
            }
        }
        return instance;
    }

    public void setGetDeveloper(GetDeveloper getDeveloper) {
        this.getDeveloper = getDeveloper;
    }

    // Get Developer Info

    public void getDeveloperInfo() {
        String COLLECTION_DEVELOPER_INFO_FIREBASE = Constants.CollectionsNames.DEVELOPER;
        String DOCUMENT_DEVELOPER_INFO_FIREBASE = Constants.CollectionsNames.DOCUMENT_DEVELOPER;
        DocumentReference myDeveloperRef = this.firebaseFirestore.collection(COLLECTION_DEVELOPER_INFO_FIREBASE).document(DOCUMENT_DEVELOPER_INFO_FIREBASE);
        if (this.getDeveloper != null) {
            myDeveloperRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    this.getDeveloper.getDeveloperInfo(false, null);
                }
                if (snapshot != null && snapshot.exists()) {
                    Developer developerData = snapshot.toObject(Developer.class);
                    this.getDeveloper.getDeveloperInfo(true, developerData);
                } else {
                    this.getDeveloper.getDeveloperInfo(false, null);
                }
            });
        }
    }
}
