package com.utad.david.planfit.DialogFragment.User;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Activitys.WebViewActivity;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.Model.Developer;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;

public class InfoAboutAppDialogFragment extends BaseDialogFragment
        implements FirebaseAdmin.FirebaseAdminInsertAndDownloandListener {

    /******************************** VARIABLES *************************************+/
     *
     */

    private Button closeButton;
    private TextView textViewName;
    private Button buttonEmail;
    private Button buttonLinkedin;
    private Developer developer;


    /******************************** SET CALLBACK FIREBASE *************************************+/
     *
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            showLoading();
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertAndDownloandListener(this);
            SessionUser.getInstance().firebaseAdmin.dowloandDataDeveloperFirebase();
            Fabric.with(getContext(), new Crashlytics());
        }else{
            hideLoading();
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_info_about, container, false);
        v.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        findById(v);
        closeButton();

        return v;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findById(View v){
        closeButton = v.findViewById(R.id.close_about);
        textViewName = v.findViewById(R.id.namedeveloper);
        buttonEmail = v.findViewById(R.id.emailDeveloper);
        buttonLinkedin = v.findViewById(R.id.likedindDeveloper);
    }

    /******************************** ONCLICK EMAIL *************************************+/
     *
     */

    private void clickEmailDeveloperButton(){
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonEmail.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                String recipientList = developer.getEmailDeveloper() ;
                String[] recipients = recipientList.split(",");
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.message_email_developer)+" "+developer.getFullNameDeveloper());
                startActivity(Intent.createChooser(intent,"Elije un cliente de email"));
            });
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

    }

    /******************************** ONCLICK LINKEDIN EN WEBVIEW *************************************+/
     *
     */

    private void clickLinkedinDeveloperButton(){
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            buttonLinkedin.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_TITLE, developer.getFullNameDeveloper());
                intent.putExtra(WebViewActivity.EXTRA_URL, developer.getUrlLinkedinDeveloper());
                intent.putExtra(WebViewActivity.EXTRA_MODE, Constants.ModeWebView.MODE_LINKEDIN);
                getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
                startActivity(intent);
            });
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }

    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void closeButton(){
        closeButton.setOnClickListener(v -> dismiss());
    }


    /******************************** CALLBACK DE FIREBASE *************************************+/
     *
     */

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

    @Override
    public void insertUserDataInFirebase(boolean end) {}
    @Override
    public void downloadUserDataInFirebase(boolean end) {}

}
