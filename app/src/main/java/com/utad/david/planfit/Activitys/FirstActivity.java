package com.utad.david.planfit.Activitys;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.utad.david.planfit.Fragments.LoginFragment;
import com.utad.david.planfit.Fragments.RegisterDetailsFragmet;
import com.utad.david.planfit.Fragments.RegisterFragment;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Data.SessionUser;

public class FirstActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, RegisterDetailsFragmet.OnFragmentInteractionListener {

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private RegisterDetailsFragmet registerDetailsFragmet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        if (findViewById(R.id.frameLayout_FirstActivity) != null) {
            if (savedInstanceState != null) {
                return;
            }
            loginFragment = new LoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout_FirstActivity, loginFragment).commit();
        }

    }

    @Override
    public void clickButtonLogin() {
        new LoginAsyncTask().execute();
    }

    @Override
    public void clickButtonOk() {
        new RegisterAsyncTask().execute();
    }

    @Override
    public void clickButtonRegister() {
        registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

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

    @Override
    public void clickButtonBackDetails() {
        registerFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, registerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            SessionUser.getInstance().firebaseAdmin.singInWithEmailAndPassword(SessionUser.getInstance().user.getEmail(),
                    SessionUser.getInstance().user.getPassword());
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
