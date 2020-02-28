package com.utad.david.planfit.Fragments.Authentication.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.User.SessionUser;
import com.utad.david.planfit.Data.User.Login.GetLogin;
import com.utad.david.planfit.Data.User.Login.LoginRepository;
import com.utad.david.planfit.Fragments.Authentication.Register.RegisterFragment;
import com.utad.david.planfit.Utils.UtilsEncryptDecryptAES;
import com.utad.david.planfit.R;
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;
import java.util.regex.Pattern;

public class LoginFragment extends BaseFragment
        implements GetLogin {

    /******************************** VARIABLES *************************************+/
     *
     */

    private Callback mListener;
    private EditText emailLogin;
    private EditText passwordLogin;
    private Button buttonLogin;
    private Button buttonRegister;
    private String emailUser;
    private String passwordUser;
    public Context context;

    /******************************** INTERFAZ *************************************+/
     *
     */

    public interface Callback {
        void clickButtonLogin();
        void clickButtonRegister();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mListener = (Callback) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_login, container, false);

       findViewById(view);
       onClickButtonLogin();
       onClickButtonRegister();
       configView();
       checkStatusUserFirebase();
       createLoginDialog();

       return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

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

    /******************************** CONFIGURA EL ESTADO DEL USUARIO *************************************+/
     *
     */

    @Override
    public void onStart() {
        super.onStart();
        if(UtilsNetwork.checkConnectionInternetDevice(getContext())){
            LoginRepository.getInstance().firebaseAuth.addAuthStateListener(LoginRepository.getInstance().authStateListener);
            LoginRepository.getInstance().firebaseAuth.addAuthStateListener(
                    LoginRepository.getInstance().authStateListener
            );
            LoginRepository.getInstance().setGetLogin(this);
            LoginRepository.getInstance().firebaseAuth = FirebaseAuth.getInstance();
        }else{
            Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
        }
    }

    private void checkStatusUserFirebase(){
        LoginRepository.getInstance().authStateListener = firebaseAuth -> {
            LoginRepository.getInstance().currentUser = firebaseAuth.getCurrentUser();
            if (LoginRepository.getInstance().currentUser != null) {
                Activity activity = getActivity();
                if(activity!=null){
                    Intent intent = new Intent(context, MainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        };
    }

    /******************************** ONCLICK LOGIN *************************************+/
     *
     */

    private void onClickButtonLogin(){
            buttonLogin.setOnClickListener(v -> {
                if (mListener!=null){
                    showLoginDialog();
                    try {
                        String password = UtilsEncryptDecryptAES.encrypt(passwordLogin.getText().toString().trim());
                        SessionUser.getInstance().setCredentials(
                                emailLogin.getText().toString().trim(),
                                password
                        );
                        mListener.clickButtonLogin();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    dismissLoginDialog();
                }
            });
    }

    /******************************** ONCLICK REGISTER *************************************+/
     *
     */

    private void onClickButtonRegister(){
        buttonRegister.setOnClickListener(v -> {
            if(mListener!=null){
                mListener.clickButtonRegister();
            }
        });
    }

    /******************************** CONFIGURA EDITTEXT DEL LOGIN *************************************+/
     *
     */

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

    /******************************** ERROR AL INICIAR SESION *************************************+/
     *
     */

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

    /******************************** CALLBACK DE FIREBASE *************************************+/
     *
     */

    @Override
    public void loginWhitEmailAndPassword(boolean status) {
        if (status) {
            Toast.makeText(getContext(), getString(R.string.info_login_ok), Toast.LENGTH_LONG).show();
            dismissLoginDialog();
            Intent intent = new Intent(getContext(),MainMenuActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getActivity().finish();
        } else {
            dismissLoginDialog();
            errorSingInRegister(getString(R.string.err_login_fail));
        }
    }


}
