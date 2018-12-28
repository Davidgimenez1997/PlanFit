package com.utad.david.planfit.Data.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Developer;
import com.utad.david.planfit.Model.User;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class FirebaseAdmin {

    private static String COLLECTION_USER_FIREBASE ="users";
    private static String COLLECTION_DEVELOPER_INFO_FIREBASE ="developer_info";

    public interface FirebaseAdminLoginAndRegisterListener {
        void singInWithEmailAndPassword(boolean end);
        void registerWithEmailAndPassword(boolean end);
    }

    public interface FirebaseAdminInsertAndDownloandListener {
        void insertUserDataInFirebase(boolean end);
        void downloadUserDataInFirebase(boolean end);
        void downloadInfoFirstDeveloper(boolean end);
        void downloadInfoSecondDeveloper(boolean end);
    }

    public interface FirebaseAdminUpdateAndDeleteUserListener{
        void updatePhotoInFirebase(boolean end);
        void updateEmailInFirebase(boolean end);
        void updatePasswordInFirebase(boolean end);
        void updateNickNameInFirebase(boolean end);
        void updateFullNameInFirebase(boolean end);
        void deleteUserInFirebase(boolean end);
    }

    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseAdmin.FirebaseAdminInsertAndDownloandListener firebaseAdminInsertAndDownloandListener;
    public FirebaseAdmin.FirebaseAdminLoginAndRegisterListener firebaseAdminLoginAndRegisterListener;
    public FirebaseAdmin.FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener;
    public User userDataFirebase;
    public Developer developerFirst;
    public Developer developerSecond;
    private AuthCredential credential;

    public void setFirebaseAdminInsertAndDownloandListener(FirebaseAdminInsertAndDownloandListener firebaseAdminInsertAndDownloandListener) {
        this.firebaseAdminInsertAndDownloandListener = firebaseAdminInsertAndDownloandListener;
    }

    public void setFirebaseAdminLoginAndRegisterListener(FirebaseAdminLoginAndRegisterListener firebaseAdminLoginAndRegisterListener) {
        this.firebaseAdminLoginAndRegisterListener = firebaseAdminLoginAndRegisterListener;
    }

    public void setFirebaseAdminUpdateUserListener(FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener) {
        this.firebaseAdminUpdateAndDeleteUserListener = firebaseAdminUpdateAndDeleteUserListener;
    }

    public FirebaseAdmin() {
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void registerWithEmailAndPassword(String email, String password) {
        if (firebaseAdminLoginAndRegisterListener != null) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("FirebaseAdmin", "createUserWithEmail:success");
                                currentUser = mAuth.getCurrentUser();
                                firebaseAdminLoginAndRegisterListener.registerWithEmailAndPassword(true);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FirebaseAdmin", "createUserWithEmail:failure", task.getException());
                                firebaseAdminLoginAndRegisterListener.registerWithEmailAndPassword(false);
                            }
                        }
                    });
        }

    }

    public void singInWithEmailAndPassword(String email, String password) {
        if (firebaseAdminLoginAndRegisterListener != null) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("FirebaseAdmin", "signInWithEmail:success");
                                currentUser = mAuth.getCurrentUser();
                                firebaseAdminLoginAndRegisterListener.singInWithEmailAndPassword(true);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("FirebaseAdmin", "signInWithEmail:failure", task.getException());
                                firebaseAdminLoginAndRegisterListener.singInWithEmailAndPassword(false);
                            }
                        }
                    });
        }
    }

    public void addDataUserCouldFirestore(){
        if(firebaseAdminInsertAndDownloandListener!=null){
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();
            user.put("email", SessionUser.getInstance().user.getEmail());
            user.put("password", SessionUser.getInstance().user.getPassword());
            user.put("fullName", SessionUser.getInstance().user.getFullName());
            user.put("nickName", SessionUser.getInstance().user.getNickName());
            if(SessionUser.getInstance().user.getImgUser()!=null){
                user.put("imgUser",SessionUser.getInstance().user.getImgUser());
            }else{
                user.put("imgUser","");
            }

            insertDataUserIntoFirebase(user);

        }
    }

    public void insertDataUserIntoFirebase(Map<String, Object> user){
        firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FirebaseAdmin", "DocumentSnapshot successfully written!");
                        firebaseAdminInsertAndDownloandListener.insertUserDataInFirebase(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FirebaseAdmin", "Error writing document", e);
                        firebaseAdminInsertAndDownloandListener.insertUserDataInFirebase(false);
                    }
                });
    }

    public void dowloandDataUserFirebase(){
        DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
        if(firebaseAdminInsertAndDownloandListener!=null){
            myUserRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(false);
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("FirebaseAdmin", "Current data: " + snapshot.getData());
                        User user = snapshot.toObject(User.class);
                        userDataFirebase = user;
                        firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(true);
                    } else {
                        Log.d("FirebaseAdmin", "Current data: null");
                        firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(false);
                    }
                }
            });
        }
    }

    public void updatePhotoUserInFirebase(){
        if(firebaseAdminUpdateAndDeleteUserListener!=null){
            DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put("imgUser", userDataFirebase.getImgUser());
            myUserRef.update(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully updated!");
                            firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error updating document", e);
                            firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(false);
                        }
                    });
        }
    }

    public void updateFullNameUserInFirebase(){
        if(firebaseAdminUpdateAndDeleteUserListener!=null){
            DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put("fullName", userDataFirebase.getFullName());
            myUserRef.update(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully updated!");
                            firebaseAdminUpdateAndDeleteUserListener.updateFullNameInFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error updating document", e);
                            firebaseAdminUpdateAndDeleteUserListener.updateFullNameInFirebase(false);
                        }
                    });
        }
    }

    public void updateNickNameUserInFirebase(){
        if(firebaseAdminUpdateAndDeleteUserListener!=null){
            DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
            Map<String, Object> user = new HashMap<>();
            user.put("nickName", userDataFirebase.getNickName());
            myUserRef.update(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully updated!");
                            firebaseAdminUpdateAndDeleteUserListener.updateNickNameInFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error updating document", e);
                            firebaseAdminUpdateAndDeleteUserListener.updateNickNameInFirebase(false);
                        }
                    });
        }
    }

    public void updateEmailUserInFirebase(){
        if(firebaseAdminUpdateAndDeleteUserListener!=null){
            reauthenticateUserInFirebaseWithEmailAndPassword();
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            currentUser.updateEmail(userDataFirebase.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("FirebaseAdmin", "User email address updated.");
                                DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
                                Map<String, Object> user = new HashMap<>();
                                user.put("email", userDataFirebase.getEmail());
                                myUserRef.update(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("FirebaseAdmin", "DocumentSnapshot successfully updated!");
                                                firebaseAdminUpdateAndDeleteUserListener.updateEmailInFirebase(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("FirebaseAdmin", "Error updating document", e);
                                                firebaseAdminUpdateAndDeleteUserListener.updateEmailInFirebase(false);
                                            }
                                        });
                            }
                        }
                    });
        }

    }

    public void updatePasswordUserInFirebase(){
        if(firebaseAdminUpdateAndDeleteUserListener!=null){
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String newPassword = userDataFirebase.getPassword();

            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("FirebaseAdmin", "User password updated.");
                                DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
                                Map<String, Object> user = new HashMap<>();
                                user.put("password", userDataFirebase.getPassword());
                                myUserRef.update(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("FirebaseAdmin", "DocumentSnapshot successfully updated!");
                                                firebaseAdminUpdateAndDeleteUserListener.updatePasswordInFirebase(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("FirebaseAdmin", "Error updating document", e);
                                                firebaseAdminUpdateAndDeleteUserListener.updatePasswordInFirebase(false);
                                            }
                                        });
                            }
                        }
                    });
        }
    }

    public void deleteAccountInFirebase(){
        if(firebaseAdminUpdateAndDeleteUserListener!=null){
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully deleted!");
                            reauthenticateUserInFirebaseWithEmailAndPassword();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("FirebaseAdmin", "User account deleted.");
                                                firebaseAdminUpdateAndDeleteUserListener.deleteUserInFirebase(true);

                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error deleting document", e);
                            firebaseAdminUpdateAndDeleteUserListener.deleteUserInFirebase(true);
                        }
                    });

        }
    }

    private void reauthenticateUserInFirebaseWithEmailAndPassword(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
       credential  = EmailAuthProvider
                .getCredential(userDataFirebase.getEmail(), userDataFirebase.getPassword());
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("FirebaseAdmin", "User re-authenticated.");
                    }
                });
    }

    public void dowloandDataFirstDeveloperFirebase(){
        DocumentReference myDeveloperRef = firebaseFirestore.collection(COLLECTION_DEVELOPER_INFO_FIREBASE).document("first");
        if(firebaseAdminInsertAndDownloandListener!=null){
            myDeveloperRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminInsertAndDownloandListener.downloadInfoFirstDeveloper(false);
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("FirebaseAdmin", "Current data: " + snapshot.getData());
                        Developer developer = snapshot.toObject(Developer.class);
                        developerFirst = developer;
                        firebaseAdminInsertAndDownloandListener.downloadInfoFirstDeveloper(true);
                    } else {
                        Log.d("FirebaseAdmin", "Current data: null");
                        firebaseAdminInsertAndDownloandListener.downloadInfoFirstDeveloper(false);
                    }
                }
            });
        }
    }

    public void dowloandDataSecondDeveloperFirebase(){
        DocumentReference myDeveloperRef = firebaseFirestore.collection(COLLECTION_DEVELOPER_INFO_FIREBASE).document("second");
        if(firebaseAdminInsertAndDownloandListener!=null){
            myDeveloperRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminInsertAndDownloandListener.downloadInfoSecondDeveloper(false);
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("FirebaseAdmin", "Current data: " + snapshot.getData());
                        Developer developer = snapshot.toObject(Developer.class);
                        developerSecond = developer;
                        firebaseAdminInsertAndDownloandListener.downloadInfoSecondDeveloper(true);
                    } else {
                        Log.d("FirebaseAdmin", "Current data: null");
                        firebaseAdminInsertAndDownloandListener.downloadInfoSecondDeveloper(false);
                    }
                }
            });
        }
    }
}
