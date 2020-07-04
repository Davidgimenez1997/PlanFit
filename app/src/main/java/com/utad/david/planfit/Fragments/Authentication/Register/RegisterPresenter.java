package com.utad.david.planfit.Fragments.Authentication.Register;

import android.content.Context;
import android.util.Patterns;
import com.utad.david.planfit.Data.User.SessionUser;
import com.utad.david.planfit.Utils.UtilsEncryptDecryptAES;
import com.utad.david.planfit.Utils.UtilsNetwork;
import java.util.regex.Pattern;

public class RegisterPresenter {

    private RegisterView view;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
    }

    public boolean checkInternetDevice(Context context) {
        if (UtilsNetwork.checkConnectionInternetDevice(context)) {
            return true;
        } else {
            this.view.deviceOfflineMessage();
            return false;
        }
    }

    public void setCredentials(String email, String password) {
        SessionUser.getInstance().setCredentials(email, password);
        this.view.onClickContinue();
    }

    public boolean enableButtonContinuee(Context context, String email, String password) {
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
}
