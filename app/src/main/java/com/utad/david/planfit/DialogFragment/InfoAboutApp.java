package com.utad.david.planfit.DialogFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Developer;
import com.utad.david.planfit.R;

public class InfoAboutApp extends DialogFragment implements FirebaseAdmin.FirebaseAdminInsertAndDownloandListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertAndDownloandListener(this);
    }

    private Button closeButton;
    private TextView textViewName;
    private Button buttonEmail;
    private Button buttonLinkedin;
    private Developer developer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_info_about, container, false);
        v.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        findById(v);
        closeButton();
        SessionUser.getInstance().firebaseAdmin.dowloandDataDeveloperFirebase();
        showLoading();
        return v;
    }

    private void findById(View v){
        closeButton = v.findViewById(R.id.close_about);
        textViewName = v.findViewById(R.id.namedeveloper);
        buttonEmail = v.findViewById(R.id.emailDeveloper);
        buttonLinkedin = v.findViewById(R.id.likedindDeveloper);
    }


    private void clickEmailDeveloperButton(){
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                String recipientList = developer.getEmailDeveloper() ;
                String[] recipients = recipientList.split(",");
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.message_email_developer)+" "+developer.getFullNameDeveloper());
                startActivity(Intent.createChooser(intent,"Elije un cliente de email"));
            }
        });
    }

    private void clickLinkedinDeveloperButton(){
        buttonLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = developer.getUrlLinkedinDeveloper();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void closeButton(){
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    @Override
    public void downloadInfotDeveloper(boolean end) {
        if(end){
            hideLoading();
            developer = SessionUser.getInstance().firebaseAdmin.developerInfo;
            textViewName.setText(developer.getFullNameDeveloper());
            clickEmailDeveloperButton();
            clickLinkedinDeveloperButton();
        }
    }


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

    @Override
    public void insertUserDataInFirebase(boolean end) {
        //Metodo implementado pero no se usa
    }

    @Override
    public void downloadUserDataInFirebase(boolean end) {
        //Metodo implementado pero no se usa
    }

}
