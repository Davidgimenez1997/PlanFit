package com.utad.david.planfit.Activitys;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Fragments.LoginFragment;
import com.utad.david.planfit.Fragments.RegisterDetailsFragmet;
import com.utad.david.planfit.Fragments.RegisterFragment;
import com.utad.david.planfit.R;
import com.utad.david.planfit.Data.SessionUser;

public class FirstActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, FirebaseAdmin.FirebaseAdminLisener, RegisterDetailsFragmet.OnFragmentInteractionListener {

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private RegisterDetailsFragmet registerDetailsFragmet;
    private String emailUser;
    private String passwordUser;

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

        SessionUser.getInstance().firebaseAdmin.setAdminLisener(this);
    }

    @Override
    public void clickButtonLogin(String email, String password) {
        new LoginAsyncTask(email,password).execute();
    }

    @Override
    public void clickButtonOk() {
        new RegisterAsyncTask(emailUser, passwordUser).execute();
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
    public void clickButtonContinue(String emailRegister, String passwordRegister) {
        registerDetailsFragmet = new RegisterDetailsFragmet();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout_FirstActivity, registerDetailsFragmet);
        transaction.addToBackStack(null);
        transaction.commit();
        this.emailUser = emailRegister;
        this.passwordUser = passwordRegister;
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

    @Override
    public void singInWithEmailAndPassword(boolean end) {
        if (end == true) {
            Toast.makeText(this, "Login Completed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Login Fail", Toast.LENGTH_LONG).show();
        }
        Log.d("InfoUser", "Email: " + SessionUser.getInstance().user.getEmail() +
                " FullName: " + SessionUser.getInstance().user.getFullName() +
                " NickName: " + SessionUser.getInstance().user.getNickName() +
                " StringImg: " + SessionUser.getInstance().user.getImgUser());
    }

    @Override
    public void registerWithEmailAndPassword(boolean end) {
        if (end == true) {
            Toast.makeText(this, "Register Completed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Register Fail", Toast.LENGTH_LONG).show();
        }
        Log.d("InfoUser", "Email: " + SessionUser.getInstance().user.getEmail() +
                " FullName: " + SessionUser.getInstance().user.getFullName() +
                " NickName: " + SessionUser.getInstance().user.getNickName() +
                " StringImg: " + SessionUser.getInstance().user.getImgUser());
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        private String emailRegister;
        private String passwordRegister;

        public LoginAsyncTask(String emailRegister, String passwordRegister) {
            this.emailRegister = emailRegister;
            this.passwordRegister = passwordRegister;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SessionUser.getInstance().firebaseAdmin.singInWithEmailAndPassword(emailRegister, passwordRegister);
            return null;
        }
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

        private String emailRegister;
        private String passwordRegister;

        public RegisterAsyncTask(String emailRegister, String passwordRegister) {
            this.emailRegister = emailRegister;
            this.passwordRegister = passwordRegister;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SessionUser.getInstance().firebaseAdmin.registerWithEmailAndPassword(emailRegister, passwordRegister);
            return null;
        }
    }
}
