package com.utad.david.planfit.DialogFragment.User;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;

public class UserDetailDialogFragments extends DialogFragment {

    /******************************** VARIABLES *************************************+/
     *
     */

    public static final String EXTRA_USER = Constants.ConfigureChat.EXTRA_USER;

    private User userDetail;
    private Toolbar toolbar;

    private ImageView imageUser;
    private TextView nameUser;
    private TextView nickUser;



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

    public static UserDetailDialogFragments newInstance(User user) {
        Bundle args = new Bundle();
        UserDetailDialogFragments fragment = new UserDetailDialogFragments();
        args.putParcelable(EXTRA_USER,user);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDetail = getArguments().getParcelable(EXTRA_USER);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment_user_detail, container, false);

        toolbar = view.findViewById(R.id.toolbar);
        imageUser = view.findViewById(R.id.imageUser);
        nameUser = view.findViewById(R.id.name);
        nickUser = view.findViewById(R.id.nick);

        setUI();
        return view;

    }

    private void setUI() {
        showLoading();
        Utils.loadImage(userDetail.getImgUser(),imageUser,Utils.PLACEHOLDER_USER);
        nameUser.setText(userDetail.getFullName());
        nickUser.setText(userDetail.getNickName());
        hideLoading();
    }
}
