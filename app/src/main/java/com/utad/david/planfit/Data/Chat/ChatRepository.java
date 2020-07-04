package com.utad.david.planfit.Data.Chat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.utad.david.planfit.Model.Chat.ChatMessage;
import com.utad.david.planfit.Model.User.User;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsEncryptDecryptAES;

public class ChatRepository {

    private static ChatRepository instance = new ChatRepository();

    private FirebaseFirestore firebaseFirestore;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private GetChat getChat;
    private User userDetails;

    private ChatRepository() {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference = this.firebaseStorage.getReference();

    }

    public static ChatRepository getInstance() {
        if (instance == null){
            synchronized (ChatRepository.class){
                if (instance == null) {
                    instance = new ChatRepository();
                }
            }
        }
        return instance;
    }

    public void setGetChat(GetChat getChat) {
        this.getChat = getChat;
    }

    /**
     * Delete chat message
     * @param message for search delete
     */
    public void deleteMessageInChat(ChatMessage message) {
        if (this.getChat != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("").orderByChild(Constants.CollectionsNames.ORDER_MESSAGES).equalTo(message.getMessageTime());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot chat : dataSnapshot.getChildren()) {
                            DatabaseReference refDelete = ref.child(chat.getKey());
                            refDelete.removeValue();
                            getChat.deleteMessage(true);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    getChat.deleteMessage(false);
                }
            });

        }
    }

    /**
     * Get user details
     * @param message for search user
     */
    public void getUserDetailsByMessage(ChatMessage message) {
        DocumentReference myUserRef = this.firebaseFirestore.collection(Constants.CollectionsNames.USER).document(message.getMessageUserId());
        if (this.getChat != null) {
            myUserRef.addSnapshotListener((snapshot, e) -> {
                if (e != null) {
                    this.getChat.getUserDetails(false,null);
                }
                if (snapshot != null && snapshot.exists()) {
                    User user = snapshot.toObject(User.class);
                    this.userDetails = user;
                    try {
                        this.userDetails.setPassword(UtilsEncryptDecryptAES.decrypt(user.getPassword()));
                        this.getUserPhoto(message.getMessageUserId());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    this.getChat.getUserDetails(false,null);
                }
            });
        }
    }


    /**
     * Get user photo
     * @param uid for user
     */
    private void getUserPhoto(String uid) {
        this.storageReference.child(Constants.CollectionsNames.IMAGES + uid).getDownloadUrl().addOnSuccessListener(uri -> {
            this.userDetails.setImgUser(uri.toString());
            this.getChat.getUserDetails(true, this.userDetails);
        }).addOnFailureListener(exception -> {
            this.userDetails.setImgUser("");
            this.getChat.getUserDetails(true, this.userDetails);
        });
    }

}
