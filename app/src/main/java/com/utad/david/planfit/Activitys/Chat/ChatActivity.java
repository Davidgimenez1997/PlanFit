package com.utad.david.planfit.Activitys.Chat;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.*;
import com.utad.david.planfit.Adapter.Chat.ChatAdapter;
import com.utad.david.planfit.Base.BaseActivity;
import com.utad.david.planfit.DialogFragment.User.UserDetailChat.UserDetailChatDialogFragments;
import com.utad.david.planfit.Model.Chat.ChatMessage;
import com.utad.david.planfit.Model.User.User;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import android.support.v4.app.Fragment;

public class ChatActivity
        extends BaseActivity
        implements ChatView {

    /******************************** VARIABLES *************************************+/
     *
     */

    public static final String EXTRA_NAME_USER = Constants.ConfigChat.EXTRA_NAME;

    private Toolbar toolbar;

    @BindView(R.id.edit_chat_message)
    EditText etMessage;

    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listView;
    private String loggedInUserName = "";
    private String nameUser;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ChatPresenter chatPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);
        this.showLoading();
        this.chatPresenter = new ChatPresenter(this);

        this.listView = findViewById(R.id.recycler_messages);
        this.toolbar = findViewById(R.id.toolbar);

        this.fragmentManager = getSupportFragmentManager();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.nameUser = extras.getString(EXTRA_NAME_USER);
        }

        this.setUI();
        this.hideLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22) {
            this.showLoading();
            this.hideLoading();
        }
    }

    /******************************** CONFIGURA LA VISTA *************************************+/
     *
     */

    private void setUI() {
        this.setSupportActionBar(this.toolbar);
        this.setTitle(getString(R.string.chat_grupal)+" "+ this.nameUser);

        if (this.chatPresenter.checkInternetDevice(this)) {
            Utils.showSoftKeyboard(this, this.etMessage);
            this.chatPresenter.setUi();
        }
    }

    private void showAllOldMessages() {
        this.showLoading();
        this.loggedInUserName = this.chatPresenter.getUserUid();
        if (this.adapter == null) {
            this.adapter = new ChatAdapter(this, ChatMessage.class, R.layout.item_in_message,
                    FirebaseDatabase.getInstance().getReference(), new ChatAdapter.Callback() {
                @Override
                public void onItemClick(ChatMessage message) {
                    if (chatPresenter.checkInternetDevice(ChatActivity.this)) {
                        chatPresenter.onClickMessage(message);
                    }
                }
            });
            this.hideLoading();
        } else {
            this.adapter.notifyDataSetChanged();
        }

        this.showLoading();
        this.listView.setAdapter(this.adapter);
        this.listView.smoothScrollByOffset(this.adapter.getCount());
        this.hideLoading();
    }

    private void navigateToProfile(ChatMessage message) {
        this.chatPresenter.getUserDetailsByMessage(message);
    }

    private void deleteMessage(ChatMessage message) {
        this.chatPresenter.deleteMessageUser(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.showAllOldMessages();
        this.hideLoading();
    }

    @OnClick(R.id.button_chat_send)
    protected void onClickSend() {
        if (this.chatPresenter.checkInternetDevice(this)) {
            this.chatPresenter.onClickSend(this.etMessage.getText().toString().trim());

        }
    }

    public String getLoggedInUserName() {
        return this.chatPresenter.getUserUid();
    }

    @Override
    public void deleteMessage() {
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void getUserDetails(User userDetails) {
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment);
        }
        UserDetailChatDialogFragments.newInstance(userDetails).show(this.fragmentTransaction, Constants.TagDialogFragment.TAG);
    }

    @Override
    public void deviceOffliMessage() {
        Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void setUiLoadChat() {
        this.showLoading();
        this.showAllOldMessages();
        this.hideLoading();
    }

    @Override
    public void clickMessageUser(ChatMessage message) {
        CharSequence[] items = {getString(R.string.borrar_mensaje), getString(R.string.action_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle(R.string.opciones_chat);
        builder.setItems(items, (dialog, item) -> {
            switch (item) {
                case 0:
                    deleteMessage(message);
                    break;
                case 1:
                    dialog.dismiss();
                    break;
            }
        });
        builder.show();
    }

    @Override
    public void clickMessageOtherUser(ChatMessage message) {
        final CharSequence[] items = {getString(R.string.perfil), getString(R.string.action_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle(R.string.opciones_chat);
        builder.setItems(items, (dialog, item) -> {
            switch (item) {
                case 0:
                    navigateToProfile(message);
                    break;
                case 1:
                    dialog.dismiss();
                    break;
            }
        });
        builder.show();
    }

    @Override
    public void errorMessageEmpty() {
        Toast.makeText(this, getString(R.string.ecriba_algo), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        this.showLoading();
        FirebaseDatabase.getInstance()
                .getReference()
                .push()
                .setValue(chatMessage);
        this.etMessage.setText("");
        Utils.closeKeyboard(this, this.etMessage);
        this.hideLoading();
    }
}
