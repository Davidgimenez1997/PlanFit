package com.utad.david.planfit.Base;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import com.utad.david.planfit.R;
import butterknife.ButterKnife;

public class BaseFragment extends Fragment {

    private ProgressDialog progressDialogLogin;
    private ProgressDialog progressDialogRegisterDetails;
    private ProgressDialog progressDialog;

    /******************************** CONFIGURA EL DIALOGO LOGIN *************************************+/
     *
     */

    public void createLoginDialog(){
        progressDialogLogin = new ProgressDialog(getContext());
        progressDialogLogin.setTitle(getString(R.string.title_login));
        progressDialogLogin.setMessage(getString(R.string.message_login));
        progressDialogLogin.setCancelable(false);
        progressDialogLogin.setIndeterminate(true);
    }

    public void showLoginDialog () {
        progressDialogLogin.show();
    }

    public void dismissLoginDialog () {
        progressDialogLogin.dismiss();
    }

    /******************************** CONFIGURA EL DIALOGO REGISTER DETAILS *************************************+/
     *
     */

    public void createRegisterDetailsDialog(){
        progressDialogRegisterDetails = new ProgressDialog(getContext());
        progressDialogRegisterDetails.setTitle(getString(R.string.title_register));
        progressDialogRegisterDetails.setMessage(getString(R.string.message_register));
        progressDialogRegisterDetails.setCancelable(false);
        progressDialogRegisterDetails.setIndeterminate(true);
    }

    public void showRegisterDetailsDialog () {
        progressDialogRegisterDetails.show();
    }

    public void dismissRegisterDetailsDialog () {
        progressDialogRegisterDetails.dismiss();
    }

    /******************************** PROGRESS DIALOG Y METODOS ALL FRAGMENTS *************************************+/
     *
     */

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

}
