package com.utad.david.planfit.DialogFragment.User.InfoAboutApp;

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
import com.utad.david.planfit.Activitys.WebView.WebViewActivity;
import com.utad.david.planfit.Base.BaseDialogFragment;
import com.utad.david.planfit.Model.Developer.Developer;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Utils.Constants;
import io.fabric.sdk.android.Fabric;

public class InfoAboutAppDialogFragment extends BaseDialogFragment
        implements InfoAboutAppDialogView {

    /******************************** VARIABLES *************************************+/
     *
     */

    private Button closeButton;
    private TextView textViewName;
    private Button buttonEmail;
    private Button buttonLinkedin;
    private InfoAboutAppDialogPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_info_about, container, false);
        v.setBackgroundResource(R.drawable.corner_dialog_fragment);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        this.findById(v);
        this.closeButton();

        this.presenter = new InfoAboutAppDialogPresenter(this, getContext());

        if (this.presenter.checkInternetDevice(getContext())) {
            showLoading();
            Fabric.with(getContext(), new Crashlytics());
        }
        this.presenter.getData();

        return v;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void findById(View v){
        this.closeButton = v.findViewById(R.id.close_about);
        this.textViewName = v.findViewById(R.id.namedeveloper);
        this.buttonEmail = v.findViewById(R.id.emailDeveloper);
        this.buttonLinkedin = v.findViewById(R.id.likedindDeveloper);
    }

    /******************************** ONCLICK EMAIL *************************************+/
     *
     */

    private void clickEmailDeveloperButton(String email, String name){
        if (this.presenter.checkInternetDevice(getContext())) {
            this.buttonEmail.setOnClickListener(v -> {
                this.navigateEmail(email, name);
            });
        }
    }

    private void navigateEmail(String email, String name) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        String recipientList = email ;
        String[] recipients = recipientList.split(",");
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.message_email_developer)+" "+name);
        startActivity(Intent.createChooser(intent,"Elije un cliente de email"));
    }

    /******************************** ONCLICK LINKEDIN EN WEBVIEW *************************************+/
     *
     */

    private void clickLinkedinDeveloperButton(String name, String url){
        if (this.presenter.checkInternetDevice(getContext())) {
            this.buttonLinkedin.setOnClickListener(v -> {
                this.navigateLinkedin(name, url);
            });
        }
    }

    private void navigateLinkedin(String name, String url) {
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_TITLE, name);
        intent.putExtra(WebViewActivity.EXTRA_URL, url);
        intent.putExtra(WebViewActivity.EXTRA_MODE, Constants.ModeWebView.MODE_LINKEDIN);
        getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
        startActivity(intent);
    }

    /******************************** CIERRA LA PANTALLA *************************************+/
     *
     */

    private void closeButton(){
        this.closeButton.setOnClickListener(v -> dismiss());
    }


    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        this.hideLoading();
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void getDeveloper(Developer developer) {
        if (this.presenter == null) {
            this.presenter = new InfoAboutAppDialogPresenter(this, getContext());
        }
        this.hideLoading();
        this.textViewName.setText(developer.getFullNameDeveloper());
        this.clickEmailDeveloperButton(developer.getEmailDeveloper(), developer.getFullNameDeveloper());
        this.clickLinkedinDeveloperButton(developer.getFullNameDeveloper(), developer.getUrlLinkedinDeveloper());
    }
}
