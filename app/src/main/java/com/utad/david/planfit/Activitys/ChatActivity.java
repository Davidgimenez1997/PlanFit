package com.utad.david.planfit.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
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

    private Toolbar toolbar;

    @BindView(R.id.edit_chat_message)
    EditText etMessage;

    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView listView;
    private String loggedInUserName = "";

    private String nameUser;

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


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameUser = extras.getString(EXTRA_NAME_USER);
        }


        setUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==22){
            showLoading();
        }
    }

    /******************************** CONFIGURA LA VISTA *************************************+/
     *
     */

    private void setUI() {
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.chat_grupal)+" "+nameUser);

        Utils.showSoftKeyboard(this,etMessage);

        if(SessionUser.getInstance().firebaseAdmin.mAuth.getCurrentUser()!=null){
            showLoading();
            showAllOldMessages();
            hideLoading();
        }
    }

    private void showAllOldMessages() {
        loggedInUserName = SessionUser.getInstance().firebaseAdmin.currentUser.getUid();

        if(adapter==null){
            showLoading();
            adapter = new ChatAdapter(this, ChatMessage.class, R.layout.item_in_message,
                    FirebaseDatabase.getInstance().getReference());
            hideLoading();
        }else{
            adapter.notifyDataSetChanged();
        }

        showLoading();
        listView.setAdapter(adapter);

        listView.smoothScrollByOffset(adapter.getCount());
        hideLoading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showAllOldMessages();
        hideLoading();
    }

    @OnClick(R.id.button_chat_send)
    protected void onClickSend(){
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
    }

    public String getLoggedInUserName() {
        return loggedInUserName;
    }
}
