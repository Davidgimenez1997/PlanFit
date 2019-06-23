package com.utad.david.planfit.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.utad.david.planfit.Adapter.Chat.ChatAdapter;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.ChatMessage;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements FirebaseAdmin.FirebaseAdminChatListener {

    /******************************** VARIABLES *************************************+/
     *
     */

    public static final String EXTRA_NAME_USER = Constants.ConfigureChat.EXTRA_NAME;

    private Toolbar toolbar;
    private String nameUser;
    private String message;

    private ChatAdapter adapter;

    @BindView(R.id.recycler_messages)
    RecyclerView rvMessages;
    @BindView(R.id.edit_chat_message)
    EditText etMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminChatListener(this);
        SessionUser.getInstance().firebaseAdmin.downloadMessage();

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameUser = extras.getString(EXTRA_NAME_USER);
        }

        toolbar = findViewById(R.id.toolbar);

        setUI();
    }

    /******************************** CONFIGURA LA VISTA *************************************+/
     *
     */

    private void setUI() {
        setSupportActionBar(toolbar);
        setTitle(nameUser);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setItemAnimator(new DefaultItemAnimator());
        rvMessages.getItemAnimator().setChangeDuration(0);
    }

    @OnClick(R.id.button_chat_send)
    protected void onClickSend() {
        message = etMessage.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            if (!Utils.isEmpty(message.trim())) {
                SessionUser.getInstance().firebaseAdmin.addMessageUser(message);
            }
            etMessage.getText().clear();
        }
    }

    @Override
    public void sendMessage(boolean end) {
        if(end){
            ArrayList<ChatMessage> messages = new ArrayList<>();
            ChatMessage chatMessage = new ChatMessage(message,SessionUser.getInstance().firebaseAdmin.mAuth.getCurrentUser().getDisplayName());
            messages.add(chatMessage);
            if (adapter == null) {
                adapter = new ChatAdapter(messages);
                rvMessages.setAdapter(adapter);

            } else {
                adapter.addNewMessages(messages);
            }
            rvMessages.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    public void donwloadAllChat(boolean end) {
        if(end){
            if (adapter == null) {
                adapter = new ChatAdapter(SessionUser.getInstance().firebaseAdmin.messagesList);
                rvMessages.setAdapter(adapter);
            }

            rvMessages.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    public void emptyAllChat(boolean end) {

    }

    @Override
    public void donwloadAllUsers(boolean end) {}

    @Override
    public void emptyAllUsers(boolean end) {}

}
