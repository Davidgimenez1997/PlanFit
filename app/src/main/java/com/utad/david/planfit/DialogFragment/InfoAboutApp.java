package com.utad.david.planfit.DialogFragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Developer;
import com.utad.david.planfit.R;

public class InfoAboutApp extends DialogFragment implements FirebaseAdmin.FirebaseAdminInsertAndDownloandListener {

    //Nuestra variable communities coge el valor que se le está pasando
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertAndDownloandListener(this);
    }

    private Button closeButton;
    private TextView textViewNameFirst;
    private Button buttonEmailFirst;
    private Button buttonLinkedinFirst;
    private TextView textViewNameSecond;
    private Button buttonEmailSecond;
    private Button buttonLinkedinSecond;
    private Developer firstDeveloper;
    private Developer secondDeveloper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_info_about, container, false);
        v.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findById(v);
        closeButton();
        SessionUser.getInstance().firebaseAdmin.dowloandDataFirstDeveloperFirebase();
        SessionUser.getInstance().firebaseAdmin.dowloandDataSecondDeveloperFirebase();

        return v;
    }

    private void findById(View v){
        closeButton = v.findViewById(R.id.close_about);
        textViewNameFirst = v.findViewById(R.id.name_first_developer);
        buttonEmailFirst = v.findViewById(R.id.emailFirstDeveloper);
        buttonLinkedinFirst = v.findViewById(R.id.likedinFirstDeveloper);
        textViewNameSecond = v.findViewById(R.id.name_second_developer);
        buttonEmailSecond = v.findViewById(R.id.emailSecondDeveloper);
        buttonLinkedinSecond = v.findViewById(R.id.likedinSecondDeveloper);
    }

    private void clickEmailFirstDeveloperButton(){
        buttonEmailFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                String recipientList =firstDeveloper.getEmailDeveloper() ;
                String[] recipients = recipientList.split(",");
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.message_email_developer)+" "+firstDeveloper.getFullNameDeveloper());
                startActivity(Intent.createChooser(intent,"Elije un cliente de email"));
            }
        });
    }

    private void clickLinkedinFirstDeveloperButton(){
        buttonLinkedinFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = firstDeveloper.getUrlLinkedinDeveloper();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    private void clickEmailSecondDeveloperButton(){
        buttonEmailSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                String recipientList =secondDeveloper.getEmailDeveloper() ;
                String[] recipients = recipientList.split(",");
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.message_email_developer)+" "+secondDeveloper.getFullNameDeveloper());
                startActivity(Intent.createChooser(intent,"Elije un cliente de email"));
            }
        });
    }

    private void clickLinkedinSecondDeveloperButton(){
        buttonLinkedinSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = secondDeveloper.getUrlLinkedinDeveloper();
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
    public void downloadInfoFirstDeveloper(boolean end) {
        if(end){
            firstDeveloper = SessionUser.getInstance().firebaseAdmin.developerFirst;
            textViewNameFirst.setText(firstDeveloper.getFullNameDeveloper());
            clickEmailFirstDeveloperButton();
            clickLinkedinFirstDeveloperButton();
        }
    }

    @Override
    public void downloadInfoSecondDeveloper(boolean end) {
        if(end){
            secondDeveloper = SessionUser.getInstance().firebaseAdmin.developerSecond;
            textViewNameSecond.setText(secondDeveloper.getFullNameDeveloper());
            clickEmailSecondDeveloperButton();
            clickLinkedinSecondDeveloperButton();
        }
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
