package com.utad.david.planfit.Activitys;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import com.utad.david.planfit.Base.BaseActivity;
import com.utad.david.planfit.Data.User.Login.LoginRepository;
import com.utad.david.planfit.Fragments.FragmentsFirstActivity.LoginFragment;
import com.utad.david.planfit.Fragments.FragmentsFirstActivity.RegisterDetailsFragmet;
import com.utad.david.planfit.Fragments.FragmentsFirstActivity.RegisterFragment;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Data.SessionUser;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

    public class FirstActivity extends BaseActivity
        implements LoginFragment.Callback,
        RegisterFragment.Callback,
        RegisterDetailsFragmet.Callback {

    /******************************** VARIABLES *************************************+/
     *
     */

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private RegisterDetailsFragmet registerDetailsFragmet;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Fabric.with(this, new Crashlytics());

        if (findViewById(R.id.frameLayout_FirstActivity) != null) {
            if (savedInstanceState != null) {
                return;
            }
            loginFragment = new LoginFragment();

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameLayout_FirstActivity, loginFragment);
            fragmentTransaction.commit();
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
        registerDetailsFragmet = new RegisterDetailsFragmet();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, registerDetailsFragmet);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void clickButtonBack() {
        loginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /******************************** RegisterDetailsFragmet.Callback *************************************+/
     *
     */

    @Override
    public void clickButtonRegister() {
        registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void clickButtonBackDetails() {
        registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /******************************** ASYNCTASK PARA LOGEAR Y REGISTRAR *************************************+/
     *
     */

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            LoginRepository.getInstance().loginWithEmailAndPassword(
                    SessionUser.getInstance().user.getEmail(),
                    SessionUser.getInstance().user.getPassword()
            );
            return null;
        }
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            SessionUser.getInstance().firebaseAdmin.registerWithEmailAndPassword(SessionUser.getInstance().user.getEmail(),
                    SessionUser.getInstance().user.getPassword());
            return null;
        }
    }


}
