package com.utad.david.planfit.Data.Firebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.*;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Developer;
import com.utad.david.planfit.Model.Nutrition.NutritionGainVolume;
import com.utad.david.planfit.Model.Nutrition.NutritionSlimming;
import com.utad.david.planfit.Model.Nutrition.NutritionToning;
import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;
import com.utad.david.planfit.Model.User;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseAdmin {

    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    public FirebaseAuth.AuthStateListener authStateListener;
    public FirebaseFirestore firebaseFirestore;
    public FirebaseStorage storage;
    public StorageReference storageReference;

    public FirebaseAdmin.FirebaseAdminInsertAndDownloandListener firebaseAdminInsertAndDownloandListener;
    public FirebaseAdmin.FirebaseAdminLoginAndRegisterListener firebaseAdminLoginAndRegisterListener;
    public FirebaseAdmin.FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener;
    public FirebaseAdmin.FirebaseAdminDownloandFragmentData firebaseAdminDownloandFragmentData;
    public FirebaseAdmin.FirebaseAdminInsertFavoriteSportAndNutrition firebaseAdminInsertFavoriteSportAndNutrition;

    public User userDataFirebase;
    public Developer developerInfo;
    private AuthCredential credential;

    public List<SportSlimming> sportSlimmingListSport;
    public List<SportSlimming> sportSlimmingListSportFavorite;
    public List<SportToning> sportToningListSport;
    public List<SportToning> sportToningListSportFavorite;
    public List<SportGainVolume> sportGainVolumeListSport;
    public List<SportGainVolume> sportGainVolumeListSportFavorite;

    public List<NutritionSlimming> nutritionSlimmingListNutrition;
    public List<NutritionSlimming> nutritionSlimmingListNutritionFavorite;
    public List<NutritionToning> nutritionToningsListNutrition;
    public List<NutritionToning> nutritionToningListNutritionFavorite;
    public List<NutritionGainVolume> nutritionGainVolumeListNutrition;
    public List<NutritionGainVolume> nutritionGainVolumeListNutritionFavorite;

    private static String COLLECTION_USER_FIREBASE = "users";
    private static String COLLECTION_DEVELOPER_INFO_FIREBASE = "developer_info";
    private static String COLLECTION_SPORT_SLIMMING = "deportes/adelgazar/detalles";
    private static String COLLECTION_SPORT_TONING = "deportes/tonificar/detalles";
    private static String COLLECTION_SPORT_GAIN_VOLUME = "deportes/ganarVolumen/detalles";
    private static String COLLECTION_NUTRITION_SLIMMING = "nutricion/adelgazar/detalles";
    private static String COLLECTION_NUTRITION_TONING = "nutricion/tonificar/detalles";
    private static String COLLECTION_NUTRITION_GAIN_VOLUME = "nutricion/ganarVolumen/detalles";

    private String COLLECTION_FAVORITE_SPORT;
    private String COLLECTION_FAVORITE_NUTRITION;


    public FirebaseAdmin() {
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    /*
    INTERFACES
     */

    //Login y registro

    public interface FirebaseAdminLoginAndRegisterListener {
        void singInWithEmailAndPassword(boolean end);

        void registerWithEmailAndPassword(boolean end);
    }

    //Insert and download user info and developed info

    public interface FirebaseAdminInsertAndDownloandListener {
        void insertUserDataInFirebase(boolean end);

        void downloadUserDataInFirebase(boolean end);

        void downloadInfotDeveloper(boolean end);
    }

    //Update user info and delete

    public interface FirebaseAdminUpdateAndDeleteUserListener {
        void updatePhotoInFirebase(boolean end);

        void deletePhotoInFirebase(boolean end);

        void updateEmailInFirebase(boolean end);

        void updatePasswordInFirebase(boolean end);

        void updateNickNameInFirebase(boolean end);

        void updateFullNameInFirebase(boolean end);

        void deleteUserInFirebase(boolean end);
    }

    //Download sport and nutrition data

    public interface FirebaseAdminDownloandFragmentData {
        void downloandCollectionSportSlimming(boolean end);

        void downloandCollectionSportToning(boolean end);

        void downloandCollectionSportGainVolume(boolean end);

        void downloandCollectionNutritionSlimming(boolean end);

        void downloandCollectionNutritionToning(boolean end);

        void downloandCollectionNutritionGainVolume(boolean end);
    }

    //Insert and download favorite sport and nutrition data

    public interface FirebaseAdminInsertFavoriteSportAndNutrition {
        void inserSportFavoriteFirebase(boolean end);

        void downloandCollectionSportFavorite(boolean end);

        void inserNutritionFavoriteFirebase(boolean end);

        void deleteFavoriteSport(boolean end);

        void deleteFavoriteNutrition(boolean end);

        void downloandCollectionNutritionFavorite(boolean end);
    }

    //Setters

    public void setFirebaseAdminInsertAndDownloandListener(FirebaseAdminInsertAndDownloandListener firebaseAdminInsertAndDownloandListener) {
        this.firebaseAdminInsertAndDownloandListener = firebaseAdminInsertAndDownloandListener;
    }

    public void setFirebaseAdminLoginAndRegisterListener(FirebaseAdminLoginAndRegisterListener firebaseAdminLoginAndRegisterListener) {
        this.firebaseAdminLoginAndRegisterListener = firebaseAdminLoginAndRegisterListener;
    }

    public void setFirebaseAdminUpdateUserListener(FirebaseAdminUpdateAndDeleteUserListener firebaseAdminUpdateAndDeleteUserListener) {
        this.firebaseAdminUpdateAndDeleteUserListener = firebaseAdminUpdateAndDeleteUserListener;
    }

    public void setFirebaseAdminDownloandFragmentData(FirebaseAdminDownloandFragmentData firebaseAdminDownloandFragmentData) {
        this.firebaseAdminDownloandFragmentData = firebaseAdminDownloandFragmentData;
    }

    public void setFirebaseAdminInsertFavoriteSportAndNutrition(FirebaseAdminInsertFavoriteSportAndNutrition firebaseAdminInsertFavoriteSportAndNutrition) {
        this.firebaseAdminInsertFavoriteSportAndNutrition = firebaseAdminInsertFavoriteSportAndNutrition;
    }

    //Login y registro

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

                                COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
                                COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

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

                                COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";
                                COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

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

    //Insert user info

    public void addDataUserCouldFirestore() {
        if (firebaseAdminInsertAndDownloandListener != null) {
            // Create a new user with a first and last name
            Map<String, Object> user = new HashMap<>();

            uploadImage(SessionUser.getInstance().user.getImgUser());

            user.put("email", SessionUser.getInstance().user.getEmail());
            user.put("password", SessionUser.getInstance().user.getPassword());
            user.put("fullName", SessionUser.getInstance().user.getFullName());
            user.put("nickName", SessionUser.getInstance().user.getNickName());


            if (SessionUser.getInstance().user.getImgUser() != null) {
                user.put("imgUser", SessionUser.getInstance().user.getImgUser());
            } else {
                user.put("imgUser", "");
            }



            insertDataUserIntoFirebase(user);
            //uploadImage(SessionUser.getInstance().user.getImgUser());

        }
    }

    public void insertDataUserIntoFirebase(Map<String, Object> user) {
        firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FirebaseAdmin", "DocumentSnapshot successfully written!");
                        firebaseAdminInsertAndDownloandListener.insertUserDataInFirebase(true);
                        //uploadImage(SessionUser.getInstance().user.getImgUser());
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

    public void uploadImage(final String image) {

        Uri uri = Uri.parse(image);

        if (uri != null) {
            StorageReference ref = storageReference.child("images/" + currentUser.getUid());
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("FirebaseAdmin", "Photo update successfully written!");
                            SessionUser.getInstance().user.setImgUser(image);
                            //firebaseAdminInsertAndDownloandListener.insertUserDataInFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error writing document", e);
                            //firebaseAdminInsertAndDownloandListener.insertUserDataInFirebase(false);
                        }
                    });
        }
    }

    //Download user info

    public void dowloandDataUserFirebase() {
        DocumentReference myUserRef = firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid());
        if (firebaseAdminInsertAndDownloandListener != null) {
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
                        downloadPhoto();
                        //firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(true);
                    } else {
                        Log.d("FirebaseAdmin", "Current data: null");
                        firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(false);
                    }
                }
            });
        }
    }

    public void downloadPhoto() {
        storageReference.child("images/" + currentUser.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.d("FirebaseAdmin", "URL PHOTO: " + uri.toString());
                userDataFirebase.setImgUser(uri.toString());
                firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(true);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                firebaseAdminInsertAndDownloandListener.downloadUserDataInFirebase(false);

            }
        });
    }

    //Update info user

    public void deletePhoto() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            StorageReference desertRef = storageReference.child("images/" + currentUser.getUid());

            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    firebaseAdminUpdateAndDeleteUserListener.deletePhotoInFirebase(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    firebaseAdminUpdateAndDeleteUserListener.deletePhotoInFirebase(false);
                }
            });
        }
    }

    public void updatePhotoUserInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {

            StorageReference desertRef = storageReference.child("images/" + currentUser.getUid());

            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    uploadImage(userDataFirebase.getImgUser());
                    firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(true);
                    // File deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    uploadImage(userDataFirebase.getImgUser());
                    firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(false);
                }
            });
            /*
            StorageReference desertRef = storageReference.child("images/"+ cƒdeurrentUser.getUid());

            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    uploadImage(userDataFirebase.getImgUser());
                    firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(true);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    firebaseAdminUpdateAndDeleteUserListener.updatePhotoInFirebase(false);
                }
            });
            */

        }
    }

    public void updateFullNameUserInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
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

    public void updateNickNameUserInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
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

    public void updateEmailUserInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            reauthenticateUserInFirebaseWithEmailAndPassword();
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

    public void updatePasswordUserInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            reauthenticateUserInFirebaseWithEmailAndPassword();
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

    //Delete user account

    public void deleteAccountInFirebase() {
        if (firebaseAdminUpdateAndDeleteUserListener != null) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            firebaseFirestore.collection(COLLECTION_USER_FIREBASE).document(currentUser.getUid())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully deleted!");
                            StorageReference desertRef = storageReference.child("images/" + currentUser.getUid());
                            // Delete the file
                            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // File deleted successfully
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
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    firebaseAdminUpdateAndDeleteUserListener.deleteUserInFirebase(false);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error deleting document", e);
                            firebaseAdminUpdateAndDeleteUserListener.deleteUserInFirebase(false);
                        }
                    });

        }
    }

    //Reauthenticate User

    private void reauthenticateUserInFirebaseWithEmailAndPassword() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        credential = EmailAuthProvider
                .getCredential(userDataFirebase.getEmail(), userDataFirebase.getPassword());
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("FirebaseAdmin", "User re-authenticated.");
                    }
                });
    }

    //Download developer data

    public void dowloandDataDeveloperFirebase() {
        DocumentReference myDeveloperRef = firebaseFirestore.collection(COLLECTION_DEVELOPER_INFO_FIREBASE).document("david");
        if (firebaseAdminInsertAndDownloandListener != null) {
            myDeveloperRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminInsertAndDownloandListener.downloadInfotDeveloper(false);
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d("FirebaseAdmin", "Current data: " + snapshot.getData());
                        Developer developerData = snapshot.toObject(Developer.class);
                        developerInfo = developerData;
                        firebaseAdminInsertAndDownloandListener.downloadInfotDeveloper(true);
                    } else {
                        Log.d("FirebaseAdmin", "Current data: null");
                        firebaseAdminInsertAndDownloandListener.downloadInfotDeveloper(false);
                    }
                }
            });
        }
    }

    //Download Sport data

    public void downloadSlimmingSport() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_SPORT_SLIMMING);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminDownloandFragmentData.downloandCollectionSportSlimming(false);
                    }

                    List<SportSlimming> sportSlimmingList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        sportSlimmingList.add(doc.toObject(SportSlimming.class));
                    }
                    sportSlimmingListSport = sportSlimmingList;

                    firebaseAdminDownloandFragmentData.downloandCollectionSportSlimming(true);
                }
            });
        }
    }

    public void downloadTiningSport() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_SPORT_TONING);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminDownloandFragmentData.downloandCollectionSportToning(false);
                    }

                    List<SportToning> sportToningList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        sportToningList.add(doc.toObject(SportToning.class));
                    }
                    sportToningListSport = sportToningList;

                    firebaseAdminDownloandFragmentData.downloandCollectionSportToning(true);
                }
            });
        }
    }

    public void downloadGainVolumeSport() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_SPORT_GAIN_VOLUME);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminDownloandFragmentData.downloandCollectionSportGainVolume(false);
                    }

                    List<SportGainVolume> sportGainVolumes = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        sportGainVolumes.add(doc.toObject(SportGainVolume.class));
                    }
                    sportGainVolumeListSport = sportGainVolumes;

                    firebaseAdminDownloandFragmentData.downloandCollectionSportGainVolume(true);
                }
            });
        }
    }

    //Download nutrition data

    public void downloadSlimmingNutrition() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NUTRITION_SLIMMING);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminDownloandFragmentData.downloandCollectionNutritionSlimming(false);
                    }

                    List<NutritionSlimming> nutritionSlimmingList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        nutritionSlimmingList.add(doc.toObject(NutritionSlimming.class));
                    }
                    Log.d("FirebaseAdmin", "Data: " + nutritionSlimmingList.toString());

                    nutritionSlimmingListNutrition = nutritionSlimmingList;

                    firebaseAdminDownloandFragmentData.downloandCollectionNutritionSlimming(true);
                }
            });
        }
    }

    public void downloadTiningNutrition() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NUTRITION_TONING);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminDownloandFragmentData.downloandCollectionNutritionToning(false);
                    }

                    List<NutritionToning> nutritionTonings = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        nutritionTonings.add(doc.toObject(NutritionToning.class));
                    }
                    nutritionToningsListNutrition = nutritionTonings;

                    firebaseAdminDownloandFragmentData.downloandCollectionNutritionToning(true);
                }
            });
        }
    }

    public void downloadGainVolumeNutrition() {
        if (firebaseAdminDownloandFragmentData != null) {
            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_NUTRITION_GAIN_VOLUME);
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("FirebaseAdmin", "Listen failed.", e);
                        firebaseAdminDownloandFragmentData.downloandCollectionNutritionGainVolume(false);
                    }

                    List<NutritionGainVolume> nutritionGainVolumes = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        nutritionGainVolumes.add(doc.toObject(NutritionGainVolume.class));
                    }
                    nutritionGainVolumeListNutrition = nutritionGainVolumes;

                    firebaseAdminDownloandFragmentData.downloandCollectionNutritionGainVolume(true);
                }
            });
        }
    }

    //Insert favorite Sport

    public void insertFavoriteSportSlimming(Map<String, Object> slimming) {

        COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document()
                    .set(slimming)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully written!");
                            firebaseAdminInsertFavoriteSportAndNutrition.inserSportFavoriteFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error writing document", e);
                            firebaseAdminInsertFavoriteSportAndNutrition.inserSportFavoriteFirebase(false);
                        }
                    });
        }
    }

    public void addFavoriteSportSlimmingCouldFirestore(SportSlimming sportSlimming) {
        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", sportSlimming.getName());
            slimmingMap.put("photo", sportSlimming.getPhoto());
            slimmingMap.put("video", sportSlimming.getVideo());
            slimmingMap.put("description", sportSlimming.getDescription());
            slimmingMap.put("type", "adelgazar");
            insertFavoriteSportSlimming(slimmingMap);
        }
    }

    public void insertFavoriteSportToning(Map<String, Object> toning) {

        COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document()
                    .set(toning)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully written!");
                            firebaseAdminInsertFavoriteSportAndNutrition.inserSportFavoriteFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("", "Error writing document", e);
                            firebaseAdminInsertFavoriteSportAndNutrition.inserSportFavoriteFirebase(false);
                        }
                    });
        }
    }

    public void addFavoriteSportToningCouldFirestore(SportToning sportToning) {
        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", sportToning.getName());
            slimmingMap.put("photo", sportToning.getPhoto());
            slimmingMap.put("video", sportToning.getVideo());
            slimmingMap.put("description", sportToning.getDescription());
            slimmingMap.put("type", "tonificar");
            insertFavoriteSportToning(slimmingMap);
        }
    }

    public void insertFavoriteSportGainVolume(Map<String, Object> gainVolume) {

        COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document()
                    .set(gainVolume)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully written!");
                            firebaseAdminInsertFavoriteSportAndNutrition.inserSportFavoriteFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error writing document", e);
                            firebaseAdminInsertFavoriteSportAndNutrition.inserSportFavoriteFirebase(false);
                        }
                    });
        }
    }

    public void addFavoriteSportGainVolumeCouldFirestore(SportGainVolume sportGainVolume) {
        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", sportGainVolume.getName());
            slimmingMap.put("photo", sportGainVolume.getPhoto());
            slimmingMap.put("video", sportGainVolume.getVideo());
            slimmingMap.put("description", sportGainVolume.getDescription());
            slimmingMap.put("type", "ganarVolumen");
            insertFavoriteSportGainVolume(slimmingMap);
        }
    }

    //Delete Favorite Sport

    public void deleteFavoriteSportSlimming(SportSlimming sportSlimming){

        if(firebaseAdminInsertFavoriteSportAndNutrition!=null){
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";

            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo("name",sportSlimming.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                    Log.d("BORRARFAVORITO", documentSnapshot.getId() + " - > "+ documentSnapshot.getData());
                                    String id = documentSnapshot.getId();
                                    firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("FirebaseAdmin", "Favorito borrado correctamente");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteSport(true);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("FirebaseAdmin", "Error Favorito borrado");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteSport(false);

                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }


    public void deleteFavoriteSportToning(SportToning sportToning) {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";

            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo("name", sportToning.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("BORRARFAVORITO", documentSnapshot.getId() + " - > " + documentSnapshot.getData());
                                    String id = documentSnapshot.getId();
                                    firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("FirebaseAdmin", "Favorito borrado correctamente");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteSport(true);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("FirebaseAdmin", "Error Favorito borrado");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteSport(false);

                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }

    public void deleteFavoriteSportGainVolume(SportGainVolume sportGainVolume) {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";

            firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT)
                    .whereEqualTo("name", sportGainVolume.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("BORRARFAVORITO", documentSnapshot.getId() + " - > " + documentSnapshot.getData());
                                    String id = documentSnapshot.getId();
                                    firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT).document(id)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("FirebaseAdmin", "Favorito borrado correctamente");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteSport(true);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("FirebaseAdmin", "Error Favorito borrado");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteSport(false);

                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }

    //Insert favorite Nutrition

    public void insertFavoriteNutritionnSlimming(Map<String, Object> slimming) {

        COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document()
                    .set(slimming)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully written!");
                            firebaseAdminInsertFavoriteSportAndNutrition.inserNutritionFavoriteFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error writing document", e);
                            firebaseAdminInsertFavoriteSportAndNutrition.inserNutritionFavoriteFirebase(false);
                        }
                    });
        }
    }

    public void addFavoriteNutritionCouldFirestore(NutritionSlimming nutritionSlimming) {
        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", nutritionSlimming.getName());
            slimmingMap.put("photo", nutritionSlimming.getPhoto());
            slimmingMap.put("url", nutritionSlimming.getUrl());
            slimmingMap.put("description", nutritionSlimming.getDescription());
            slimmingMap.put("type", "adelgazar");
            insertFavoriteNutritionnSlimming(slimmingMap);
        }
    }

    public void insertFavoriteNutritionToning(Map<String, Object> toning) {

        COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document()
                    .set(toning)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully written!");
                            firebaseAdminInsertFavoriteSportAndNutrition.inserNutritionFavoriteFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("", "Error writing document", e);
                            firebaseAdminInsertFavoriteSportAndNutrition.inserNutritionFavoriteFirebase(false);
                        }
                    });
        }
    }

    public void addFavoriteNutritionToningCouldFirestore(NutritionToning nutritionToning) {
        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", nutritionToning.getName());
            slimmingMap.put("photo", nutritionToning.getPhoto());
            slimmingMap.put("url", nutritionToning.getUrl());
            slimmingMap.put("description", nutritionToning.getDescription());
            slimmingMap.put("type", "tonificar");
            insertFavoriteNutritionToning(slimmingMap);
        }
    }

    public void insertFavoriteNutritionGainVolume(Map<String, Object> gainVolume) {

        COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document()
                    .set(gainVolume)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FirebaseAdmin", "DocumentSnapshot successfully written!");
                            firebaseAdminInsertFavoriteSportAndNutrition.inserSportFavoriteFirebase(true);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FirebaseAdmin", "Error writing document", e);
                            firebaseAdminInsertFavoriteSportAndNutrition.inserSportFavoriteFirebase(false);
                        }
                    });
        }
    }

    public void addFavoriteNutritionGainVolumeCouldFirestore(NutritionGainVolume nutritionGainVolume) {
        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            Map<String, Object> slimmingMap = new HashMap<>();
            slimmingMap.put("name", nutritionGainVolume.getName());
            slimmingMap.put("photo", nutritionGainVolume.getPhoto());
            slimmingMap.put("url", nutritionGainVolume.getUrl());
            slimmingMap.put("description", nutritionGainVolume.getDescription());
            slimmingMap.put("type", "ganarVolumen");
            insertFavoriteNutritionGainVolume(slimmingMap);
        }
    }

    //Delete Favorite Nutrition

    public void deleteFavoriteNutritionSlimming(NutritionSlimming nutritionSlimming){

        if(firebaseAdminInsertFavoriteSportAndNutrition!=null){
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo("name",nutritionSlimming.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                    Log.d("BORRARFAVORITO", documentSnapshot.getId() + " - > "+ documentSnapshot.getData());
                                    String id = documentSnapshot.getId();
                                    firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("FirebaseAdmin", "Favorito borrado correctamente");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteNutrition(true);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("FirebaseAdmin", "Error Favorito borrado");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteNutrition(false);

                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }


    public void deleteFavoriteNutritionToning(NutritionToning nutritionToning) {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo("name", nutritionToning.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("BORRARFAVORITO", documentSnapshot.getId() + " - > " + documentSnapshot.getData());
                                    String id = documentSnapshot.getId();
                                    firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("FirebaseAdmin", "Favorito borrado correctamente");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteNutrition(true);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("FirebaseAdmin", "Error Favorito borrado");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteNutrition(false);

                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }

    public void deleteFavoriteNutritionGainVolume(NutritionGainVolume nutritionGainVolume) {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {
            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

            firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION)
                    .whereEqualTo("name", nutritionGainVolume.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Log.d("BORRARFAVORITO", documentSnapshot.getId() + " - > " + documentSnapshot.getData());
                                    String id = documentSnapshot.getId();
                                    firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION).document(id)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("FirebaseAdmin", "Favorito borrado correctamente");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteNutrition(true);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("FirebaseAdmin", "Error Favorito borrado");
                                                    firebaseAdminInsertFavoriteSportAndNutrition.deleteFavoriteNutrition(false);

                                                }
                                            });
                                }
                            }
                        }
                    });
        }
    }

    //Download favorite Sport

    public void downloadSlimmingSportFavorite() {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {

            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";

            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.whereEqualTo("type", "adelgazar")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("FirebaseAdmin", "Listen failed.", e);
                                firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionSportFavorite(false);
                            }

                            List<SportSlimming> toningList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("type") != null) {
                                    toningList.add(doc.toObject(SportSlimming.class));
                                }
                            }
                            Log.w("FirebaseAdmin", "Data." + toningList.toString());

                            sportSlimmingListSportFavorite = toningList;

                            firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionSportFavorite(true);
                        }
                    });
        }
    }

    public void downloadToningSportFavorite() {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {

            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";

            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.whereEqualTo("type", "tonificar")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("FirebaseAdmin", "Listen failed.", e);
                                firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionSportFavorite(false);
                            }

                            List<SportToning> sportToningList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("type") != null) {
                                    sportToningList.add(doc.toObject(SportToning.class));
                                }
                            }
                            sportToningListSportFavorite = sportToningList;
                            Log.w("FirebaseAdmin", "Data." + sportToningList.toString());


                            firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionSportFavorite(true);
                        }
                    });
        }
    }

    public void downloadGainVolumeSportFavorite() {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {

            COLLECTION_FAVORITE_SPORT = "users/" + currentUser.getUid() + "/deporteFavorito";

            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_SPORT);
            collectionReference.whereEqualTo("type", "ganarVolumen")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("FirebaseAdmin", "Listen failed.", e);
                                firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionSportFavorite(false);
                            }

                            List<SportGainVolume> sportGainVolumes = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("type") != null) {
                                    sportGainVolumes.add(doc.toObject(SportGainVolume.class));
                                }
                            }
                            sportGainVolumeListSportFavorite = sportGainVolumes;
                            Log.w("FirebaseAdmin", "Data." + sportGainVolumes.toString());


                            firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionSportFavorite(true);
                        }
                    });
        }

    }

    //Download favorite Nutrition

    public void downloadSlimmingNutritionFavorite() {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {

            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";


            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.whereEqualTo("type", "adelgazar")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("FirebaseAdmin", "Listen failed.", e);
                                firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionNutritionFavorite(false);
                            }

                            List<NutritionSlimming> nutritionSlimmings = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("type") != null) {
                                    nutritionSlimmings.add(doc.toObject(NutritionSlimming.class));
                                }
                            }
                            Log.w("FirebaseAdmin", "Data." + nutritionSlimmings.toString());

                            nutritionSlimmingListNutritionFavorite = nutritionSlimmings;

                            firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionNutritionFavorite(true);
                        }
                    });
        }
    }

    public void downloadToningNutritionFavorite() {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {

            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.whereEqualTo("type", "tonificar")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("FirebaseAdmin", "Listen failed.", e);
                                firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionNutritionFavorite(false);
                            }

                            List<NutritionToning> nutritionTonings = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("type") != null) {
                                    nutritionTonings.add(doc.toObject(NutritionToning.class));
                                }
                            }
                            nutritionToningListNutritionFavorite = nutritionTonings;
                            Log.w("FirebaseAdmin", "Data." + nutritionTonings.toString());


                            firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionNutritionFavorite(true);
                        }
                    });
        }
    }

    public void downloadGainVolumeNutritionFavorite() {

        if (firebaseAdminInsertFavoriteSportAndNutrition != null) {

            COLLECTION_FAVORITE_NUTRITION = "users/" + currentUser.getUid() + "/nutricionFavorita";

            CollectionReference collectionReference = firebaseFirestore.collection(COLLECTION_FAVORITE_NUTRITION);
            collectionReference.whereEqualTo("type", "ganarVolumen")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("FirebaseAdmin", "Listen failed.", e);
                                firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionNutritionFavorite(false);
                            }

                            List<NutritionGainVolume> nutritionGainVolumes = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("type") != null) {
                                    nutritionGainVolumes.add(doc.toObject(NutritionGainVolume.class));
                                }
                            }
                            nutritionGainVolumeListNutritionFavorite = nutritionGainVolumes;
                            Log.w("FirebaseAdmin", "Data." + nutritionGainVolumes.toString());


                            firebaseAdminInsertFavoriteSportAndNutrition.downloandCollectionNutritionFavorite(true);
                        }
                    });
        }

    }

}
