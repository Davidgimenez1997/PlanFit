package com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Users;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Activitys.ChatActivity;
import com.utad.david.planfit.Adapter.Users.UsersAdapters;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

import java.util.Collections;
import java.util.List;

public class UsersFragment extends Fragment
        implements FirebaseAdmin.FirebaseAdminChatListener {

    /******************************** VARIABLES *************************************+/
     *
     */

    private RecyclerView mRecyclerView;
    private UsersAdapters mAdapter;
    private LinearLayoutManager layoutManager;
    private LinearLayout linearLayout;
    private Runnable toolbarRunnable;

    /******************************** PROGRESS DIALOG Y METODOS *************************************+/
     *
     */

    private ProgressDialog progressDialog;

    public void showLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(getContext(), R.style.TransparentProgressDialog);
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


    /******************************** SET Runnable *************************************+/
     *
     */

    public void setToolbarRunnable(Runnable toolbarRunnable) {
        this.toolbarRunnable = toolbarRunnable;
    }

    /******************************** SET CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminChatListener(this);
            SessionUser.getInstance().firebaseAdmin.donwloadAllUsers();
            Fabric.with(getContext(), new Crashlytics());
        }else{
            hideLoading();
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_users, container, false);

        if(toolbarRunnable != null) {
            toolbarRunnable.run();
        }

        mRecyclerView = view.findViewById(R.id.recycler_view_users);
        linearLayout = view.findViewById(R.id.linear_empty_users);
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    /******************************** CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void donwloadAllUsers(boolean end) {
        if(end){
            hideLoading();
            linearLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            List<User> allUsers = SessionUser.getInstance().firebaseAdmin.userList;

            Collections.sort(allUsers);

            mAdapter = new UsersAdapters(allUsers, user -> {
                openChat(user.getNickName(),user.getUid());
            });
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void openChat(String name,String uid){
        if(name.equals(SessionUser.getInstance().firebaseAdmin.userDataFirebase.getNickName())){
            Toast.makeText(getContext(),"No puedes chatear contigo mismo",Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra(Constants.ConfigureChat.EXTRA_NAME, name);
            intent.putExtra(Constants.ConfigureChat.EXTRA_UID, uid);
            startActivity(intent);
        }
    }

    @Override
    public void emptyAllUsers(boolean end) {
        if(end){
            hideLoading();
            linearLayout.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void sendMessage(boolean end) {}

    @Override
    public void donwloadAllChat(boolean end) {}

    @Override
    public void emptyAllChat(boolean end) {}
}
