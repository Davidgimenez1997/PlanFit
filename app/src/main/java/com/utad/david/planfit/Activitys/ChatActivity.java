package com.utad.david.planfit.Activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.utad.david.planfit.Adapter.Chat.ChatAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.ChatMessage;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity{

    /******************************** VARIABLES *************************************+/
     *
     */

    public static final String EXTRA_NAME_USER = Constants.ConfigureChat.EXTRA_NAME;
    public static final String EXTRA_UID_USER = Constants.ConfigureChat.EXTRA_UID;

    /*

    private DatabaseReference mReference;

    private ChatAdapter adapter;

    @BindView(R.id.recycler_messages)
    RecyclerView rvMessages;
    @BindView(R.id.edit_chat_message)
    EditText etMessage;

    /** Class variables **/
    private Toolbar toolbar;

    @BindView(R.id.edit_chat_message)
    EditText etMessage;

    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listView;
    private String loggedInUserName = "";

    private String nameUser;
    private String uidUser;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //SessionUser.getInstance().firebaseAdmin.setFirebaseAdminChatListener(this);
        //SessionUser.getInstance().firebaseAdmin.downloadMessage();

        ButterKnife.bind(this);
        listView = (ListView) findViewById(R.id.recycler_messages);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameUser = extras.getString(EXTRA_NAME_USER);
            uidUser = extras.getString(EXTRA_UID_USER);
        }

        toolbar = findViewById(R.id.toolbar);

        setUI();
        //setupConnection();
    }

    /******************************** CONFIGURA LA VISTA *************************************+/
     *
     */

    private void setUI() {
        setSupportActionBar(toolbar);
        setTitle("Chat Grupal: "+nameUser);

        if(SessionUser.getInstance().firebaseAdmin.mAuth.getCurrentUser()!=null){
            showAllOldMessages();
        }

        //rvMessages.setLayoutManager(new LinearLayoutManager(this));

        //adapter = new ChatAdapter();
        //rvMessages.setAdapter(adapter);
    }

    private void showAllOldMessages() {
        loggedInUserName = SessionUser.getInstance().firebaseAdmin.currentUser.getUid();
        Log.d("Main", "user id: " + loggedInUserName);

        adapter = new ChatAdapter(this, ChatMessage.class, R.layout.item_in_message,
                FirebaseDatabase.getInstance().getReference());
        listView.setAdapter(adapter);
    }

    @OnClick(R.id.button_chat_send)
    protected void onClickSend(){
        if (etMessage.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter some texts!", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .push()
                    .setValue(new ChatMessage(etMessage.getText().toString(),
                            SessionUser.getInstance().firebaseAdmin.userDataFirebase.getNickName(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid())
                    );
            etMessage.setText("");
        }
    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    /*

    private void setupConnection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mReference = database.getReference(Constants.DATABASE.DATABASE_NAME);

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                handleReturn(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), getString(R.string.chat_init_error), Toast.LENGTH_SHORT).show();
                if(UtilsNetwork.checkConnectionInternetDevice(getBaseContext())){
                    logout();
                }else{
                    Toast.makeText(getBaseContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void logout() {
        SessionUser.getInstance().firebaseAdmin.mAuth.getInstance().signOut();
        SessionUser.getInstance().removeUser();
        Intent intent =new Intent(this, FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void handleReturn(DataSnapshot dataSnapshot) {
        adapter.clearData();
        for(DataSnapshot item : dataSnapshot.getChildren()) {
            ChatMessage data = item.getValue(ChatMessage.class);
            adapter.addData(data);

        }

        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.button_chat_send)
    protected void onClickSend() {
        message = etMessage.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            if (!Utils.isEmpty(message.trim())) {
                ChatMessage data = new ChatMessage();
                data.setMessage(message.trim());
                data.setId(SessionUser.getInstance().firebaseAdmin.currentUser.getUid());
                data.setName(SessionUser.getInstance().firebaseAdmin.userDataFirebase.getNickName());
                data.setMessageTime(new Date().getTime());
                data.setDestino(uidUser);

                //mReference.child(uidUser).setValue(data);
                mReference.child(String.valueOf(new Date().getTime())).setValue(data);

                closeAndClean();
            }
            etMessage.getText().clear();
        }
    }

    private void closeAndClean() {
        Utils.closeKeyboard(this, etMessage);
        etMessage.setText("");
    }

    */
}
