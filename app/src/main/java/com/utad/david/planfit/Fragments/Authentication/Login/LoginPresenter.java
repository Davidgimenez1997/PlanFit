package com.utad.david.planfit.Fragments.Authentication.Login;

import android.app.Activity;
import android.content.Context;
import android.util.Patterns;
import com.google.firebase.auth.FirebaseAuth;
import com.utad.david.planfit.Base.BaseFragment;
import com.utad.david.planfit.Data.User.Login.GetLogin;
import com.utad.david.planfit.Data.User.Login.LoginRepository;
import com.utad.david.planfit.Data.User.SessionUser;
import com.utad.david.planfit.Utils.UtilsEncryptDecryptAES;
import com.utad.david.planfit.Utils.UtilsNetwork;
import java.util.regex.Pattern;

public class LoginPresenter implements GetLogin {

    private LoginView view;

    public LoginPresenter(LoginView view) {
        this.view = view;
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOffline();
            return false;
        }
    }

    public void checkAuthStateListener() {
        LoginRepository.getInstance().firebaseAuth.addAuthStateListener(LoginRepository.getInstance().authStateListener);
        LoginRepository.getInstance().firebaseAuth.addAuthStateListener(
                LoginRepository.getInstance().authStateListener
        );
        LoginRepository.getInstance().setGetLogin(this);
        LoginRepository.getInstance().firebaseAuth = FirebaseAuth.getInstance();
    }

    public void navigateToMainMenuIsUserLoged(BaseFragment fragment) {
        LoginRepository.getInstance().authStateListener = firebaseAuth -> {
            LoginRepository.getInstance().currentUser = firebaseAuth.getCurrentUser();
            if (LoginRepository.getInstance().currentUser != null) {
                Activity activity = fragment.getActivity();
                if (activity != null) {
                    this.view.navigateToMainMenu(activity);

                }
            }
        };
    }

    public void setCredentials(String password, String email) {
        try {
            String passwordEncrypt = UtilsEncryptDecryptAES.encrypt(password);
            SessionUser.getInstance().setCredentials(email, passwordEncrypt);
            this.view.clickLoginButton();
        } catch (Exception e) {
            e.printStackTrace();
            this.view.deviceOffline();
        }
    }

    public boolean enableButtonLogin(Context context, String email, String password) {
        if (this.checkInternetDevice(context)){
            return this.emailValidate(email) && this.passwordValidate(password) && !email.equals("") && !password.equals("");
        }
        return false;
    }

    public boolean emailValidate(String email) {
        if (!email.equals("")) {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(email).matches();
        }
        return true;
    }

    public boolean passwordValidate(String password) {
        if (!password.equals("")) {
            return password.length() >= 6;
        }
        return true;
    }

    @Override
    public void loginWhitEmailAndPassword(boolean status) {
        if (status) {
            this.view.okLogin();
        } else {
            this.view.errorLogin();
        }
    }


}
