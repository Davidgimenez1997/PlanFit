package com.utad.david.planfit.Activitys;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import com.utad.david.planfit.Base.BaseActivity;
import com.utad.david.planfit.Data.User.Login.LoginRepository;
import com.utad.david.planfit.Data.User.Register.RegisterRepository;
import com.utad.david.planfit.Fragments.Authentication.Login.LoginFragment;
import com.utad.david.planfit.Fragments.Authentication.Register.Details.RegisterDetailsFragmet;
import com.utad.david.planfit.Fragments.Authentication.Register.RegisterFragment;
import com.utad.david.planfit.Model.User.Credentials;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Data.User.SessionUser;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

    public class AuthenticationActivity extends BaseActivity
        implements LoginFragment.Callback,
        RegisterFragment.Callback,
        RegisterDetailsFragmet.Callback {

    /******************************** VARIABLES *************************************+/
     *
     */

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private RegisterDetailsFragmet registerDetailsFragmet;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Fabric.with(this, new Crashlytics());

        if (findViewById(R.id.frameLayout_FirstActivity) != null) {
            if (savedInstanceState != null) {
                return;
            }
            this.loginFragment = new LoginFragment();

            this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
            this.fragmentTransaction.add(R.id.frameLayout_FirstActivity, this.loginFragment);
            this.fragmentTransaction.commit();
        }

    }

    /******************************** LoginFragment.Callback *************************************+/
     *
     */

    @Override
    public void clickButtonLogin() {
        new LoginAsyncTask().execute();
    }

    @Override
    public void clickButtonOk() {
        new RegisterAsyncTask().execute();
    }


    /******************************** RegisterFragment.Callback *************************************+/
     *
     */

    @Override
    public void clickButtonContinue() {
        this.registerDetailsFragmet = new RegisterDetailsFragmet();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, this.registerDetailsFragmet);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void clickButtonBack() {
        this.loginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, this.loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /******************************** RegisterDetailsFragmet.Callback *************************************+/
     *
     */

    @Override
    public void clickButtonRegister() {
        this.registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, this.registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void clickButtonBackDetails() {
        this.registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, this.registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /******************************** ASYNCTASK PARA LOGEAR Y REGISTRAR *************************************+/
     *
     */

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Credentials credentials = SessionUser.getInstance().getCredentials();
            LoginRepository.getInstance().loginWithEmailAndPassword(credentials);
            return null;
        }
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Credentials credentials = SessionUser.getInstance().getCredentials();
            RegisterRepository.getInstance().registerWithEmailAndPassword(credentials);
            return null;
        }
    }


}
