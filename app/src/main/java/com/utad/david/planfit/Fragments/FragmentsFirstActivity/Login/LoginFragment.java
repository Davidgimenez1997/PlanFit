package com.utad.david.planfit.Fragments.FragmentsFirstActivity.Login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.*;
import com.utad.david.planfit.Activitys.MainMenuActivity;
import com.utad.david.planfit.Fragments.FragmentsFirstActivity.RegisterFragment;
import com.utad.david.planfit.Utils.UtilsEncryptDecryptAES;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment implements FirebaseAdmin.FirebaseAdminLoginAndRegisterListener,LoginView {

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(getContext(), new Crashlytics());
    }

    private EditText emailLogin;
    private EditText passwordLogin;
    private Button buttonLogin;
    private Button buttonRegister;
    private String emailUser;
    private String passwordUser;
    private ProgressDialog mProgress;
    public Context context;
    public LoginPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_login, container, false);

       presenter = new LoginPresenter();

       findViewById(view);
       onClickButtonLogin();
       onClickButtonRegister();
       configView();
       checkStatusUserFirebase();
       showDialog();

       return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewAttached(this);
    }





    @Override
    public void onDestroyView() {
        presenter.onViewDetached();
        super.onDestroyView();
    }

    private void checkStatusUserFirebase(){
        presenter.checkLoggedUser();
    }

    private void showDialog(){
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(getString(R.string.title_login));
        mProgress.setMessage(getString(R.string.message_login));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            SessionUser.getInstance().firebaseAdmin.mAuth.addAuthStateListener(SessionUser.getInstance().firebaseAdmin.authStateListener);
            SessionUser.getInstance().firebaseAdmin.setFirebaseAdminLoginAndRegisterListener(this);
            SessionUser.getInstance().firebaseAdmin.mAuth = FirebaseAuth.getInstance();
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }
    }

    private void configView(){
        emailLogin.setText("");
        passwordLogin.setText("");
        emailLogin.addTextChangedListener(textWatcherLoginFragment);
        passwordLogin.addTextChangedListener(textWatcherLoginFragment);
    }

    private void findViewById(View view){
        emailLogin = view.findViewById(R.id.emailLogin);
        passwordLogin = view.findViewById(R.id.passwordLogin);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        buttonRegister = view.findViewById(R.id.buttonRegister);
    }

    private void onClickButtonLogin(){
            buttonLogin.setOnClickListener(v -> {
                if (mListener!=null){
                    mProgress.show();
                    SessionUser.getInstance().user.setEmail(emailLogin.getText().toString().trim());
                    try {
                        String password = UtilsEncryptDecryptAES.encrypt(passwordLogin.getText().toString().trim());
                        SessionUser.getInstance().user.setPassword(password);
                        mListener.clickButtonLogin();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    mProgress.dismiss();
                }
            });
    }

    private void onClickButtonRegister(){
        buttonRegister.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickButtonRegister();
            }
        });
    }

    private TextWatcher textWatcherLoginFragment = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            emailUser = emailLogin.getText().toString().trim();
            passwordUser = passwordLogin.getText().toString().trim();
            buttonLogin.setEnabled(enableButton());
            if (!enableButton()) {
                if (!emailValidate(emailUser)) {
                    emailLogin.setError(getString(R.string.err_email));
                }
                if (!passValidate(emailUser)) {
                    passwordLogin.setError(getString(R.string.err_password));
                }
            }
        }
    };

    private boolean enableButton() {
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            if (emailValidate(emailUser) && passValidate(passwordUser)) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    private boolean emailValidate(String str_email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (pattern.matcher(str_email).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean passValidate(String str_password) {
        if (str_password.length() >= 6) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /******************************** FirebaseAdminLoginAndRegisterListener Callback *************************************+/
     *
     */

    @Override
    public void singInWithEmailAndPassword(boolean end) {
        if (end == true) {
            Toast.makeText(getContext(), getString(R.string.info_login_ok), Toast.LENGTH_LONG).show();
            mProgress.dismiss();
            Intent intent = new Intent(getContext(),MainMenuActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().finish();
        } else {
            mProgress.dismiss();
            errorSingInRegister(getString(R.string.err_login_fail));
        }
    }

    private void errorSingInRegister(String title){
        if(mListener!=null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
            builder.setMessage(title)
                    .setPositiveButton(R.string.info_dialog_err, (dialog, id) -> {
                        RegisterFragment registerFragment = new RegisterFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    });
            builder.create();
            builder.show();
        }
    }

    @Override
    public void registerWithEmailAndPassword(boolean end) {}

    /******************************** Login View Callback *************************************+/
     *
     */

    @Override
    public void userStatusLogged() {
        Activity activity = getActivity();
        if(activity!=null){
            Intent intent = new Intent(context, MainMenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    /******************************** Login Fragment Callback *************************************+/
     *
     */

    public interface OnFragmentInteractionListener {
        void clickButtonLogin();
        void clickButtonRegister();
    }
}
