package com.utad.david.planfit.Fragments.Authentication.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.utad.david.planfit.Activitys.MainMenu.MainMenuActivity;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Fragments.Authentication.Register.RegisterFragment;
import com.utad.david.planfit.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class LoginFragment extends BaseFragment
        implements LoginView {

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
    private Context context;
    private LoginPresenter presenter;

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
            this.mListener = (Callback) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getContext(), new Crashlytics());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.fragment_login, container, false);

       this.presenter = new LoginPresenter(this);
       this.findViewById(view);
       this.onClickButtonLogin();
       this.onClickButtonRegister();
       this.configView();
       this.checkStatusUserFirebase();
       this.createLoginDialog();

       return view;
    }

    /******************************** CONFIGURA VISTA *************************************+/
     *
     */

    private void configView(){
        this.emailLogin.setText("");
        this.passwordLogin.setText("");
        this.emailLogin.addTextChangedListener(this.textWatcherLoginFragment);
        this.passwordLogin.addTextChangedListener(this.textWatcherLoginFragment);
    }

    private void findViewById(View view){
        this.emailLogin = view.findViewById(R.id.emailLogin);
        this.passwordLogin = view.findViewById(R.id.passwordLogin);
        this.buttonLogin = view.findViewById(R.id.buttonLogin);
        this.buttonRegister = view.findViewById(R.id.buttonRegister);
    }

    /******************************** CONFIGURA EL ESTADO DEL USUARIO *************************************+/
     *
     */

    @Override
    public void onStart() {
        super.onStart();
        if (this.presenter.checkInternetDevice(getContext())) {
            this.presenter.checkAuthStateListener();
        }
    }

    private void checkStatusUserFirebase(){
        this.presenter.navigateToMainMenuIsUserLoged(this);

    }

    /******************************** ONCLICK LOGIN *************************************+/
     *
     */

    private void onClickButtonLogin(){
        this.buttonLogin.setOnClickListener(v -> {
            if (this.mListener != null) {
                showLoginDialog();
                this.presenter.setCredentials(this.passwordLogin.getText().toString().trim(), this.emailLogin.getText().toString().trim());
            } else {
                dismissLoginDialog();
            }
        });
    }

    /******************************** ONCLICK REGISTER *************************************+/
     *
     */

    private void onClickButtonRegister(){
        this.buttonRegister.setOnClickListener(v -> {
            if (this.mListener != null) {
                this.mListener.clickButtonRegister();
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
            if (!presenter.emailValidate(emailUser) && presenter.passwordValidate(passwordUser)) {
                emailLogin.setError(getString(R.string.err_email));
            }
            if (!presenter.passwordValidate(passwordUser) && presenter.emailValidate(emailUser)) {
                passwordLogin.setError(getString(R.string.err_password));
            }
            if (!presenter.emailValidate(emailUser) && !presenter.passwordValidate(passwordUser)) {
                emailLogin.setError(getString(R.string.err_email));
                passwordLogin.setError(getString(R.string.err_password));
            }
            boolean validated = presenter.enableButtonLogin(getContext(), emailUser, passwordUser);
            buttonLogin.setEnabled(validated);
        }
    };

    /******************************** ERROR AL INICIAR SESION *************************************+/
     *
     */

    private void errorSingInRegister(String title){
        if (this.mListener != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
            builder.setMessage(title).setPositiveButton(R.string.info_dialog_err, (dialog, id) -> {
                this.navigateToRegisterFragment();
            });
            builder.create();
            builder.show();
        }
    }

    private void navigateToRegisterFragment() {
        RegisterFragment registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void okLogin() {
        Toast.makeText(getContext(), getString(R.string.info_login_ok), Toast.LENGTH_LONG).show();
        dismissLoginDialog();
        Intent intent = new Intent(getContext(),MainMenuActivity.class);
        startActivity(intent);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().finish();
    }

    @Override
    public void errorLogin() {
        dismissLoginDialog();
        this.errorSingInRegister(getString(R.string.err_login_fail));
    }

    @Override
    public void navigateToMainMenu(Activity activity) {
        Intent intent = new Intent(context, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void clickLoginButton() {
        this.mListener.clickButtonLogin();
    }

    @Override
    public void deviceOffline() {
        Toast.makeText(getContext(),getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }
}
