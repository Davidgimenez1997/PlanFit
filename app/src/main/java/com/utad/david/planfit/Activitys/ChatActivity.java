package com.utad.david.planfit.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.utad.david.planfit.DialogFragment.User.UserDetailDialogFragments;
import com.utad.david.planfit.Model.ChatMessage;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import android.support.v4.app.Fragment;
import com.utad.david.planfit.Utils.UtilsNetwork;

public class ChatActivity extends AppCompatActivity
        implements FirebaseAdmin.FirebaseAdimChatLisetener{

    /******************************** VARIABLES *************************************+/
     *
     */

    public static final String EXTRA_NAME_USER = Constants.ConfigureChat.EXTRA_NAME;

    private Toolbar toolbar;

    @BindView(R.id.edit_chat_message)
    EditText etMessage;

    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listView;
    private String loggedInUserName = "";

    private String nameUser;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    /******************************** PROGRESS DIALOG Y METODOS *************************************+/
     *
     */

    private ProgressDialog progressDialog;

    public void showLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(this, R.style.TransparentProgressDialog);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        ImageView ivLoading = ButterKnife.findById(progressDialog, R.id.image_cards_animation);
        ivLoading.startAnimation(rotate);
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideLoading();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideLoading();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        showLoading();

        ButterKnife.bind(this);

        listView = findViewById(R.id.recycler_messages);
        toolbar = findViewById(R.id.toolbar);

        fragmentManager = getSupportFragmentManager();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameUser = extras.getString(EXTRA_NAME_USER);
        }


        setUI();
        hideLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==22){
            showLoading();
            hideLoading();
        }
    }

    /******************************** CONFIGURA LA VISTA *************************************+/
     *
     */

    private void setUI() {
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.chat_grupal)+" "+nameUser);

        if(UtilsNetwork.checkConnectionInternetDevice(this)){
            Utils.showSoftKeyboard(this,etMessage);
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdimChatLisetener(this);

            if(SessionUser.getInstance().firebaseAdmin.mAuth.getCurrentUser()!=null){
                showLoading();
                showAllOldMessages();
                hideLoading();
            }
        }else{
            Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }
    }

    private void showAllOldMessages() {
        loggedInUserName = SessionUser.getInstance().firebaseAdmin.currentUser.getUid();

        if(adapter==null){
            showLoading();
            adapter = new ChatAdapter(this, ChatMessage.class, R.layout.item_in_message,
                    FirebaseDatabase.getInstance().getReference(), new ChatAdapter.Callback() {
                @Override
                public void onItemClick(ChatMessage message) {

                    if(UtilsNetwork.checkConnectionInternetDevice(ChatActivity.this)){
                        if(message.getMessageUserId().equals(SessionUser.getInstance().firebaseAdmin.currentUser.getUid())){
                            final CharSequence[] items = {getString(R.string.borrar_mensaje), getString(R.string.action_cancel)};
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
                        }else{
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
                    }else{
                        Toast.makeText(ChatActivity.this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
                    }

                }
            });
            hideLoading();
        }else{
            adapter.notifyDataSetChanged();
        }

        showLoading();
        listView.setAdapter(adapter);

        listView.smoothScrollByOffset(adapter.getCount());
        hideLoading();
    }

    private void navigateToProfile(ChatMessage message) {
        SessionUser.getInstance().firebaseAdmin.dowloandDetailsUserFirebase(message);
    }

    private void deleteMessage(ChatMessage message) {
        SessionUser.getInstance().firebaseAdmin.deleteMessageInChat(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showAllOldMessages();
        hideLoading();
    }

    @OnClick(R.id.button_chat_send)
    protected void onClickSend(){
        if(UtilsNetwork.checkConnectionInternetDevice(this)){
            if (Utils.isEmpty(etMessage.getText().toString().trim())) {
                Toast.makeText(this, getString(R.string.ecriba_algo), Toast.LENGTH_SHORT).show();
            } else {
                showLoading();
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(etMessage.getText().toString(),
                                SessionUser.getInstance().firebaseAdmin.userDataFirebase.getNickName(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid())
                        );
                etMessage.setText("");
                Utils.closeKeyboard(this,etMessage);
                hideLoading();
            }
        }else{
            Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }

    @Override
    public void deleteMessageChat(boolean end) {
        if(end){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void donwloadUserDetails(boolean end, User userDetails) {
        if(end){
            if(userDetails!=null){
                /*
                Intent intent = new Intent(this, UserDetailDialogFragments.class);
                intent.putExtra(Constants.ConfigureChat.EXTRA_USER, userDetails);
                startActivity(intent);
                */
                fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(fragment);
                }
                UserDetailDialogFragments.newInstance(userDetails).show(fragmentTransaction,Constants.TagDialogFragment.TAG);
            }
        }
    }
}
