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
    private TextView textViewName;
    private Button buttonEmail;
    private Button buttonLinkedin;
    private Developer developer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_info_about, container, false);
        v.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findById(v);
        closeButton();
        SessionUser.getInstance().firebaseAdmin.dowloandDataDeveloperFirebase();

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
            developer = SessionUser.getInstance().firebaseAdmin.developerInfo;
            textViewName.setText(developer.getFullNameDeveloper());
            clickEmailDeveloperButton();
            clickLinkedinDeveloperButton();
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
