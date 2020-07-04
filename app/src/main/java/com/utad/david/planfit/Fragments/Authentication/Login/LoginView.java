package com.utad.david.planfit.Fragments.Authentication.Login;

import android.app.Activity;

public interface LoginView {
    void deviceOffline();
    void navigateToMainMenu(Activity activity);
    void clickLoginButton();
    void okLogin();
    void errorLogin();
}
